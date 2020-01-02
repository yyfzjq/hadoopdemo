package com.example.hadoopdemo.analyze;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author Ruison
 * on 2019/7/22 - 11:17
 */
public class LogMapVersion {

    public static class RequestMap extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            Kpi kpi = Kpi.filterPVs(value.toString());
            if (kpi != null && kpi.isValid()) {
                word.set(kpi.getRequestUrl());
                context.write(word, one);
            }
        }
    }

    public static class RequestStatusMap extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            Kpi kpi = Kpi.filterPVs(value.toString());
            if (kpi != null && kpi.isValid()) {
                word.set(kpi.getStatus());
                context.write(word, one);
            }
        }
    }

    public static class RequestMethodMap extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            Kpi kpi = Kpi.filterPVs(value.toString());
            if (kpi != null && kpi.isValid()) {
                word.set(kpi.getRequestMethod());
                context.write(word, one);
            }
        }
    }
}
