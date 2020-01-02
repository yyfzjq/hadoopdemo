package com.example.hadoopdemo.analyze;

import com.example.hadoopdemo.HadoopDemoApplication;
import com.example.hadoopdemo.config.HadoopConfig;
import com.example.hadoopdemo.mapReduce.WordCountMap;
import com.example.hadoopdemo.mapReduce.WordCountReduce;
import com.example.hadoopdemo.props.HadoopProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.stereotype.Component;

/**
 * 任务工具
 *
 * @author Ruison
 * on 2019/7/22 - 11:28
 */
@Component
@AllArgsConstructor
public class JobUtil {

    private HadoopProperties hadoopProperties;

    /**
     * 任务配置，并开启任务
     *
     * @param jobName 任务名称
     * @param inputPath 输入文件路径
     * @param outputPath 输出文件路径
     * @param mapClass Map处理类
     * @param reduceClass reduce处理类
     */
    @SneakyThrows
    public void task(String jobName, String inputPath, String outputPath,
                     Class<? extends Mapper> mapClass,
                     Class<? extends Reducer> reduceClass) {
        HadoopConfig hadoopConfig = new HadoopConfig();
        Job job = Job.getInstance(hadoopConfig.getConfiguration(hadoopProperties), jobName);

        job.setJarByClass(HadoopDemoApplication.class);
        job.setMapperClass(mapClass);
        job.setCombinerClass(reduceClass);
        job.setReducerClass(reduceClass);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        //提交程序  并且监控打印程序执行情况
        job.waitForCompletion(true);
    }
}
