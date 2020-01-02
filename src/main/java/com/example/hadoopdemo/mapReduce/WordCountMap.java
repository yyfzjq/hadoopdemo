package com.example.hadoopdemo.mapReduce;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;

/**
 * 分词
 * @author Ruison
 * on 2019/7/6 - 14:53
 */
public class WordCountMap extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    /**
     * @param key 默认情况下，是MapReduce所读取到的一行文本的起始偏移量
     * @param value 默认情况下，是MapReduce所读取到的一行文本的内容，hadoop中的序列化类型为Text
     * @param context 是用户自定义逻辑处理完成后输出的KEY，在此处是单词，String
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // 防止中文乱码
        String line = new String(value.getBytes(), 0, value.getLength(), "UTF-8").trim();
        if (StringUtils.isNotEmpty(line)) {
            // 使用分词器，分隔文件行内容根据常用的短语分隔，比如我们，被分隔成 <我,1>,<们,1><我们,1>
            byte[] btValue = line.getBytes();
            InputStream inputStream = new ByteArrayInputStream(btValue);
            Reader reader = new InputStreamReader(inputStream);
            IKSegmenter ikSegmenter = new IKSegmenter(reader, true);
            Lexeme lexeme;
            while ((lexeme = ikSegmenter.next()) != null) {
                word.set(lexeme.getLexemeText());
                if (word != null) {
                    context.write(word, one);
                }
            }
        }
    }
}
