package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @描述：
 * @作者：Stitch
 * @时间：2019/2/22 14:36
 */
public interface IFileService {

    /**
     * 文件上传的方法
     *
     * @param file 文件
     * @param path 路径
     * @return
     */
    String upload(MultipartFile file, String path);
}
