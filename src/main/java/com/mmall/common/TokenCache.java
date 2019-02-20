package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @描述：GUAVA本地缓存
 * @作者：Stitch
 * @时间：2019/2/20 17:03
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //创建缓存：初始化缓存大小为1000，当超过该大小时，自动根据LRU算法进行缓存扩张，再设置缓存过期时间为12小时，且当根据key值查询不到对应值时自动执行其他方法
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(
            new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值时，如果key没有对应的值就调用该方法进行加载
                @Override
                public String load(String key) throws Exception {
                    return "null";
                }
            });

    /**
     * 保存数据至缓存
     * @param key
     * @param value
     */
    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    /**
     * 根据key获取value值
     * @param key
     * @return
     */
    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("localCache get error", e);
        }
        return null;
    }
}
