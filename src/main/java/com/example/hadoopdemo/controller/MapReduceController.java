package com.example.hadoopdemo.controller;

import com.example.hadoopdemo.base.BaseResponse;
import com.example.hadoopdemo.mapReduce.MapReduceClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MapReduce操作相关接口
 * @author Ruison
 * on 2019/7/6 - 15:21
 */
@RestController
@RequestMapping("reduce")
@AllArgsConstructor
public class MapReduceController {

    private final MapReduceClient mapReduceClient;

    /**dsfdfs
     * 单词统计(统计指定key单词的出现次数)
     *
     * @param jobName 计划名称
     * @param inputPath 文件路径，如果是文件家路径，则默认分词该文件夹下所有的文件
     * @return 响应内容
     */
    @PostMapping("wordCount")
    public BaseResponse wordCount(@RequestParam("jobName") String jobName,
                                  @RequestParam("inputPath") String inputPath) {
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(inputPath)) {
            return BaseResponse.error("请求参数为空");
        }
        mapReduceClient.wordCount(jobName, inputPath);
        return BaseResponse.ok("单词统计成功");
    }
}
