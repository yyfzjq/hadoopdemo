package com.example.hadoopdemo;

import lombok.AllArgsConstructor;
import org.apache.hadoop.fs.FileStatus;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hadoop.fs.FsShell;

@SpringBootApplication
@AllArgsConstructor
public class HadoopDemoApplication implements CommandLineRunner {

    private final FsShell fsShell;

    public static void main(String[] args) {
        //System.setProperty("hadoop.home.dir", "D:\\IDEA\\hadoop-3.1.2");
        System.setProperty("hadoop.home.dir", "/usr/local/opt/hadoop");
        SpringApplication.run(HadoopDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("========= 获取FS文件目录信息 ============");
        for (FileStatus fileStatus : fsShell.lsr("/")) {
            System.out.println(">" + fileStatus.getPath());
        }
        System.out.println("=========== 获取完毕 ===========");
    }
}
