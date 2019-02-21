package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ICategoryService {

    /**
     * 新增分类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse addCategory(String categoryName, Integer parentId);

    /**
     * 更新分类名称
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 获取平级的分类
     *
     * @param categoryId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    /**
     * 递归获取分类id
     *
     * @param categoryId
     * @return
     */
    ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId);

    /**
     * 递归方法
     *
     * @param categorySets 去重集合
     * @param catagoryId   传入分类id
     * @return
     */
    Set<Category> findChildCategory(HashSet<Category> categorySets, Integer catagoryId);
}
