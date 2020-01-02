package com.example.hadoopdemo.mapReduce;

import com.example.hadoopdemo.HadoopDemoApplication;
import com.example.hadoopdemo.config.HadoopConfig;
import com.example.hadoopdemo.props.HadoopProperties;
import lombok.AllArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapred.lib.CombineTextInputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Ruison
 * on 2019/7/6 - 15:11
 */
@Component
@AllArgsConstructor
public class ReduceJobsUtil {

    private HadoopProperties hadoopProperties;

    /**
     * 获取单词统计的配置信息
     *
     * @param jobName 名称
     * @param inputPath 分词地址
     * @param outputPath 输出地址
     * @throws Exception 异常
     */
    public void getWordCountJobsConf(String jobName, String inputPath, String outputPath)
            throws Exception {
        HadoopConfig hadoopConfig = new HadoopConfig();
        Job job = Job.getInstance(hadoopConfig.getConfiguration(hadoopProperties), jobName);
        job.setJarByClass(HadoopDemoApplication.class);
        job.setMapperClass(WordCountMap.class);
        job.setCombinerClass(WordCountReduce.class);
        job.setReducerClass(WordCountReduce.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 小文件合并设置
//        job.setInputFormatClass(CombineTextInputFormat.class);
//        // 最大分片
//        CombineTextInputFormat.setMaxInputSplitSize(job, 4 * 1024 * 1024);
//        // 最小分片
//        CombineTextInputFormat.setMinInputSplitSize(job, 2 * 1024 * 1024);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        //提交程序  并且监控打印程序执行情况
        job.waitForCompletion(true);
    }

}
