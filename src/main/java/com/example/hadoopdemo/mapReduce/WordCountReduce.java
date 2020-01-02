package com.example.hadoopdemo.mapReduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 处理Map传入的内容
 * @author Ruison
 * on 2019/7/6 - 15:09
 */
public class WordCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    private IntWritable result = new IntWritable();

    /**
     * @param key
     * 第一个Text: 是传入的单词名称，是Mapper中传入的
     * @param values
     * 第二个：LongWritable 是该单词出现了多少次，这个是MapReduce计算出来的，比如 hello出现了11次
     * @param context
     * 第三个Text: 是输出单词的名称 ，这里是要输出到文本中的内容
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        result.set(sum);
        if (key != null && result != null) {
            context.write(key, result);
            String keyStr = key.toString();
            // 使用分词器，内容已经被统计好了，直接输出即可
            System.out.println("============ " + keyStr + " 统计分词为: " + sum + " ============");
        }
    }
}
