package com.baidu.tieba.module.cache;

/**
 * 缓存策略接口
 * 
 * Created by wangzexiang on 2016/8/29.
 */
public interface ICachePolicy<T> {
	T getCache(String key, String uid);

	boolean insert(String key, T value, String uid, long expiredTime);

	boolean delete(String key, String uid);

}
