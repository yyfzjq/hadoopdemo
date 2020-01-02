package com.example.hadoopdemo.controller;

import com.example.hadoopdemo.base.BaseResponse;
import com.example.hadoopdemo.hdfs.HadoopClient;
import com.example.hadoopdemo.util.FileUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * hadoop hdfs文件操作相关接口
 *
 * @author Ruison
 * on 2019/7/4 - 14:15
 */
@RestController
@RequestMapping("file")
@AllArgsConstructor
public class FileController {

    private HadoopClient hadoopClient;

    /**
     * 上传文件
     */
    @PostMapping("upload")
    public BaseResponse upload(@RequestParam String uploadPath, MultipartFile file) {
        hadoopClient.copyFileToHDFS(false, true, FileUtil.MultipartFileToFile(file).getPath(), uploadPath);
        return BaseResponse.ok();
    }

    /**
     * 下载文件
     */
    @GetMapping("download")
    public void download(@RequestParam String path, @RequestParam String fileName, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("charset", "utf-8");
        response.setContentType("application/force-download");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        OutputStream os;
        try {
            os = response.getOutputStream();
            hadoopClient.download(path, fileName, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录
     */
    @PostMapping("mkdir")
    public BaseResponse mkdir(@RequestParam String folderPath) {
        boolean result = false;
        if (StringUtils.isNotEmpty(folderPath)) {
            result = hadoopClient.mkdir(folderPath, true);
        }
        return BaseResponse.ok(result);
    }

    /**
     * 目录信息
     */
    @GetMapping("getPathInfo")
    public BaseResponse<List<Map<String, Object>>> getPathInfo(@RequestParam String path) {
        return BaseResponse.ok(hadoopClient.getPathInfo(path));
    }

    /**
     * 获取目录下文件列表
     */
    @GetMapping("getFileList")
    public BaseResponse<List<Map<String, String>>> getFileList(@RequestParam String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        return BaseResponse.ok(hadoopClient.getFileList(path));
    }

    /**
     * 删除文件或文件夹
     */
    @PostMapping("rmdir")
    public BaseResponse rmdir(@RequestParam String path, @RequestParam(required = false) String fileName) {
        hadoopClient.rmdir(path, fileName);
        return BaseResponse.ok();
    }

    /**
     * 读取文件内容
     */
    @GetMapping("readFile")
    public BaseResponse readFile(@RequestParam String filePath) {
        return BaseResponse.ok((Object)hadoopClient.readFile(filePath));
    }

    /**
     * 文件或文件夹重命名
     */
    @PostMapping("renameFile")
    public BaseResponse renameFile(@RequestParam String oldName, @RequestParam String newName) {
        return BaseResponse.ok(hadoopClient.renameFile(oldName, newName));
    }

    /**
     * 上传本地文件
     */
    @PostMapping("uploadFileFromLocal")
    public BaseResponse uploadFileFromLocal(@RequestParam String path, @RequestParam String uploadPath) {
        hadoopClient.copyFileToHDFS(false, true, path, uploadPath);
        return BaseResponse.ok();
    }

    /**
     * 下载文件到本地
     */
    @PostMapping("downloadFileFromLocal")
    public BaseResponse downloadFileFromLocal(@RequestParam String path, @RequestParam String downloadPath) {
        hadoopClient.downloadFileFromLocal(path, downloadPath);
        return BaseResponse.ok();
    }

    /**
     * 复制文件
     */
    @PostMapping("copyFile")
    public BaseResponse copyFile(String sourcePath, String targetPath) {
        hadoopClient.copyFile(sourcePath, targetPath);
        return BaseResponse.ok();
    }
}
