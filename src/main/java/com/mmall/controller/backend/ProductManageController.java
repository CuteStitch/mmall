package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述：商品模块
 * @作者：Stitch
 * @时间：2019/2/21 15:07
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 新增产品
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "product_save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iProductService.savaOrUpdateProduct(product);

    }

    /**
     * 商品上下架
     *
     * @param session
     * @param productId 商品id
     * @param status    上/下架
     * @return
     */
    @RequestMapping(value = "set_product_status.do")
    @ResponseBody
    public ServerResponse setProductSellStatus(HttpSession session, Integer productId, Integer status) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iProductService.setProductSellStatus(productId, status);
    }

    /**
     * 根据产品id获取该产品信息
     *
     * @param session
     * @param productId 产品id
     * @return
     */
    @RequestMapping(value = "get_detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iProductService.getDetail(productId);
    }

    /**
     * 查询商品
     *
     * @param session
     * @param pageNum  当前页
     * @param pageSize 每页显示数量
     * @return
     */
    @RequestMapping(value = "get_detail_list.do")
    @ResponseBody
    public ServerResponse getDetaiList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iProductService.getDetailList(pageNum, pageSize);
    }

    /**
     * 商品搜索功能
     *
     * @param session
     * @param producName 模糊搜索名称
     * @param productId  id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search_list.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String producName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        return iProductService.searchProduct(producName, productId, pageNum, pageSize);
    }

    /**
     * 文件上传至FTP服务器
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            return ServerResponse.createByErrorMessage("该用户不是管理员");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");//获取该文件夹路径
        String targetFileName = iFileService.upload(file, path);//上传文件
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;//生成文件地址返回

        //封装成map集合返回
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * 利用富文本编辑器上传图片
     *
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();//新建map
        //验证登陆
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登陆");
            return resultMap;
        }
        //验证是否是管理员
        if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
            resultMap.put("success", false);
            resultMap.put("msg", "该用户不是管理员");
            return resultMap;
        }
        String path = request.getSession().getServletContext().getRealPath("upload");//获取该文件夹路径
        String targetFileName = iFileService.upload(file, path);//上传文件
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;//生成文件地址返回
        //封装成map集合返回
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");//修改响应头
        return resultMap;
    }


}
