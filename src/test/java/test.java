import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class test {

    public static void main(String[] args) throws IOException {

        // 导入证书到信任存储
        disableCertificateValidation();

        String boundary = "----WebKitFormBoundaryCJEleSRxsqS0lAFv";
        URL url = new URL("https://219.138.238.250:8443/publishing/publishing/material/file/video");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setDoOutput(true);
//        String a ="------WebKitFormBoundaryCJEleSRxsqS0lAFv\nContent-Disposition: form-data; name=\"Filedata\";filename=\"1.jsp\"\n\n1\n------WebKitFormBoundaryCJEleSRxsqS0lAFv\n";
        String a = "123";
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"Filedata\"; filename=\"1.jsp\"\r\n");
        dos.writeBytes("\r\n");
//        dos.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
        dos.writeBytes(a);
//        dos.write(a.getBytes());
        dos.writeBytes("\r\n--" + boundary + "--\r\n");
        dos.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        dos.close();
        br.close();
    }

    private static void disableCertificateValidation() {
        try {
            System.getProperties().setProperty("https.proxyHost", "127.0.0.1");
            System.getProperties().setProperty("https.proxyPort", "8080");
            System.getProperties().setProperty("http.proxyHost", "127.0.0.1");
            System.getProperties().setProperty("http.proxyPort", "8080");
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
