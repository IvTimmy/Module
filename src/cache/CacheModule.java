package com.baidu.tieba.module.cache;

import com.baidu.adp.lib.cache.BdKVCacheImpl;
import com.baidu.tieba.module.Module;

/**
 * 缓存模块
 * 
 * Created by wangzexiang on 2016/8/25.
 */
public abstract class CacheModule<T> extends Module {
	private final String mCacheSpaceName;

	public CacheModule(String moduleName, String spaceName) {
		super(moduleName);
		mCacheSpaceName = spaceName;
	}

	public final String getCacheSpaceName() {
		return mCacheSpaceName;
	}

	public void loadCache(String key, ICacheDecoder<T> data) {
		loadCache(key, null, data);
	}

	public abstract void loadCache(String key, String uid, ICacheDecoder<T> data);

	public void insert(String key, T value) {
		insert(key, value, null, BdKVCacheImpl.MILLS_10Years);
	}

	public void insert(String key, T value, String uid) {
		insert(key, value, uid, BdKVCacheImpl.MILLS_10Years);
	}

	public abstract void insert(String key, T value, String uid, long expiredTime);

	public void delete(String key) {
		delete(key, null);
	}

	public abstract void delete(String key, String uid);

	public abstract void cancelLoad(String key);

	public abstract boolean isLoading(String key);
}
