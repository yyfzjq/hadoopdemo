package com.example.hadoopdemo.controller;

import com.example.hadoopdemo.analyze.AnalyzeClient;
import com.example.hadoopdemo.base.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ruison
 * on 2019/7/22 - 11:44
 */
@RestController
@RequestMapping("analyze")
@AllArgsConstructor
public class AnalyzeController {

    private final AnalyzeClient analyzeClient;

    /**
     * 日志分析
     *
     * @param jobName 计划名称
     * @param inputPath 文件路径，如果是文件家路径，则默认分词该文件夹下所有的文件
     * @return 响应内容
     */
    @PostMapping("log")
    public BaseResponse analyzeLog(@RequestParam("jobName") String jobName,
                                   @RequestParam("inputPath") String inputPath) {
        analyzeClient.start(jobName, inputPath);
        return BaseResponse.ok();
    }
}
