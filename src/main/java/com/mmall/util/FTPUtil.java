package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @描述：文件服务工具类
 * @作者：Stitch
 * @时间：2019/2/22 14:56
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    //读取配置文件的配置信息
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    //构造方法
    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 对外开放的方法
     *
     * @param fileList 等待上传的文件集合
     * @return
     * @throws IOException
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("--->开始连接FTP服务器........");
        boolean isSuccess = ftpUtil.uploadFile("img", fileList);
        logger.info("--->FTP服务器连接成功！等待上传.......");
        if (isSuccess) {
            logger.info("--->上传完毕！");
        }
        return isSuccess;

    }


    /**
     * 开始上传文件至FTP服务器
     *
     * @param remotePath
     * @param fileList   等待上传文件集合
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接服务器
        boolean b = connectServer(this.ip, this.port, this.user, this.pwd);
        if (b) {//已连接
            try {
                ftpClient.changeWorkingDirectory(remotePath);//上传使用的文件夹
                ftpClient.setBufferSize(1024);//缓冲区大小
                ftpClient.setControlEncoding("UTF-8");//格式
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//二进制文件
                ftpClient.enterLocalPassiveMode();//打开本地被动模式
                //开始上传
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                uploaded = false;
                logger.error("文件上传发生异常", e);
                e.printStackTrace();
            } finally {
                fis.close();//释放资源
                ftpClient.disconnect();//关闭连接
            }
        }
        return uploaded;
    }

    /**
     * 连接ftp服务器
     *
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String pwd) {

        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接ftp服务器异常！", e);
        }
        return isSuccess;
    }


    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
