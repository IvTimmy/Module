package com.baidu.tieba.module.cache.bytes;

import com.baidu.tieba.module.cache.ICacheDecoder;
import com.baidu.tieba.module.cache.CacheResult;

/**
 * 二进制缓存读结果
 * 
 * Created by wangzexiang on 2016/8/26.
 */
public class BytesCacheReadResult extends CacheResult {
	private ICacheDecoder<byte[]> mCacheData;

	public BytesCacheReadResult(String key, boolean isSuccess) {
		super(key, isSuccess);
	}

	public BytesCacheReadResult(String key, boolean isSuccess, int error) {
		super(key, isSuccess, error);
	}

	public void setCacheData(ICacheDecoder data) {
		mCacheData = data;
	}

	public final ICacheDecoder getCacheData() {
		return mCacheData;
	}
}
