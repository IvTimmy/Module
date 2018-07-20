package com.baidu.tieba.module.cache.string;

import com.baidu.tieba.module.cache.ICacheDecoder;
import com.baidu.tieba.module.cache.CacheResult;

/**
 * 字符串类型缓存结果 Created by wangzexiang on 2016/8/26.
 */
public class StringReadCacheResult extends CacheResult {

	private ICacheDecoder<String> mCacheData;

	public StringReadCacheResult(String key, boolean isSuccess) {
		super(key, isSuccess);
	}

	public StringReadCacheResult(String key, boolean isSuccess, int error) {
		super(key, isSuccess, error);
	}

	public void setResult(ICacheDecoder<String> data) {
		mCacheData = data;
	}

	public final ICacheDecoder<String> getData() {
		return mCacheData;
	}
}
