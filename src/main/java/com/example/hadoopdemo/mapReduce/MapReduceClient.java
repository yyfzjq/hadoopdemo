package com.example.hadoopdemo.mapReduce;

import com.example.hadoopdemo.hdfs.HadoopClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Ruison
 * on 2019/7/6 - 15:18
 */
@Component
@AllArgsConstructor
public class MapReduceClient {

    // 默认reduce输出目录
    private static final String OUTPUT_PATH = "/output";
    private HadoopClient hadoopClient;
    private ReduceJobsUtil reduceJobsUtils;

    /**
     * 单词统计，统计某个单词出现的次数
     * @param jobName 计划名称
     * @param inputPath 文件路径，如果是文件家路径，则默认分词该文件夹下所有的文件
     */
    @SneakyThrows
    public void wordCount(String jobName, String inputPath) {
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(inputPath)) {
            return;
        }
        // 输出目录 = output/当前Job,如果输出路径存在则删除，保证每次都是最新的
        String outputPath = OUTPUT_PATH + "/" + jobName;
        hadoopClient.rmdir(outputPath, null);
        reduceJobsUtils.getWordCountJobsConf(jobName, inputPath, outputPath);
    }
}
