package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @描述：
 * @作者：Stitch
 * @时间：2019/2/22 14:36
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        //获取上传文件名
        String filename = file.getOriginalFilename();
        //获取文件格式（扩展名.jpg）
        String fileExtensionName = filename.substring(filename.lastIndexOf(".") + 1);
        //生成保存文件的名称
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件---上传文件名称：{}，上传路径：{}，新文件名称：{}", filename, path, uploadFileName);

        //根据路径创建好文件夹
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();//自动一级一级创建下去
        }
        //创建目标文件
        File targetFile = new File(path, uploadFileName);

        //上传信息
        try {
            file.transferTo(targetFile);
            //将该上传的文件上传到ftp文件服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //文件上传至FTP成功后，删除当前服务器上的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("文件上传出现异常！", e);
            return null;
        }
        return targetFile.getName();
    }
}
