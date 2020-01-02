package com.example.hadoopdemo.analyze;

import com.example.hadoopdemo.hdfs.HadoopClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.example.hadoopdemo.analyze.LogMapVersion.*;

/**
 * @author Ruison
 * on 2019/7/22 - 11:37
 */
@Component
@AllArgsConstructor
public class AnalyzeClient {
    // 默认reduce输出目录
    private static final String OUTPUT_PATH = "/output";
    private HadoopClient hadoopClient;
    private JobUtil jobUtil;

    /**
     * 开始任务
     *
     * @param jobName 任务名称
     * @param inputPath 输入文件路径
     */
    public void start(String jobName, String inputPath) {
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(inputPath)) {
            return;
        }
        // 输出目录 = output/当前Job,如果输出路径存在则删除，保证每次都是最新的
        String outputPathRequest = OUTPUT_PATH + "/" + jobName + "-request";
        String outputPathRequestStatus = OUTPUT_PATH + "/" + jobName + "-requestStatus";
        String outputPathRequestMethod = OUTPUT_PATH + "/" + jobName + "-requestMethod";
        hadoopClient.rmdir(outputPathRequest, null);
        hadoopClient.rmdir(outputPathRequestStatus, null);
        hadoopClient.rmdir(outputPathRequestMethod, null);
        jobUtil.task(jobName + "-request", inputPath, outputPathRequest, RequestMap.class, LogReduce.class);
        jobUtil.task(jobName + "-requestStatus", inputPath, outputPathRequestStatus, RequestStatusMap.class, LogReduce.class);
        jobUtil.task(jobName + "-requestMethod", inputPath, outputPathRequestMethod, RequestMethodMap.class, LogReduce.class);
    }
}
