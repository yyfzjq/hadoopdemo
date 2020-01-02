package com.example.hadoopdemo.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ruison
 * on 2019/7/5 - 13:54
 */
@Data
@Component
@ConfigurationProperties("hadoop")
public class HadoopProperties {
    /** fs uri地址 */
    private String nameNode = "hdfs://127.0.0.1:9000/";
    /** 默认文件夹 */
    private String directoryPath = "/";

    public String getDirectoryPath() {
        StringBuilder sb = new StringBuilder(directoryPath);
        if (!(directoryPath.indexOf("/") == directoryPath.length())) {
            sb.append("/");
        }
        return sb.toString();
    }

    public String getPath() {
        return this.nameNode + this.directoryPath;
    }
}
