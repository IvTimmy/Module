package com.baidu.tieba.module.cache;

/**
 * 缓存数据解析
 * 
 * Created by wangzexiang on 2016/8/25.
 */
public interface ICacheDecoder<T> {
	void decodeInBackground(T data);
}
