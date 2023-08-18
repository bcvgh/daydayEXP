import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.payloads.VulPocTemplateImp;
import com.bcvgh.util.FileUtil;
import com.bcvgh.util.JsonUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class dsadas {
    @Test
    public void add() throws ExecutionException, InterruptedException {
        System.out.println("----程序开始运行----");
        Date date1 = new Date();

        // 线程池数量
        int taskSize = 5;
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(taskSize);
        // 创建多个有返回值的任务
        List<Future<Object>> list = new ArrayList<>();
        for (int i = 0; i < taskSize; i++) {
            Callable c = new MyCallable(i + " ");
            // 执行任务并获取Future对象
            Future<Object> f = executorService.submit(c);
            System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        executorService.shutdown();

        // 获取所有并发任务的运行结果
        for (Future<Object> f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>" + f.get().toString());
        }

        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【" + (date2.getTime() - date1.getTime()) + "毫秒】");
    }

}

class MyCallable implements Callable<Object> {
    private String taskNum;

    MyCallable(String taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public Object call() throws Exception {
        System.out.println(">>>" + taskNum + "任务启动");
        Date dateTmp1 = new Date();
        Thread.sleep(1000);
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        System.out.println(">>>" + taskNum + "任务终止");
        return taskNum + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
    }
}
