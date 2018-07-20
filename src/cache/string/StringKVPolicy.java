package com.baidu.tieba.module.cache.string;

import com.baidu.adp.lib.cache.BdKVCache;
import com.baidu.adp.lib.util.StringUtils;
import com.baidu.tbadk.core.dbcache.DBKVCacheManager;
import com.baidu.tieba.module.cache.ICachePolicy;

/**
 * 字符串KV缓存的策略
 *
 * Created by wangzexiang on 2016/8/29.
 */
public class StringKVPolicy implements ICachePolicy<String> {
	private final String mSpaceName;

	public StringKVPolicy(String spaceName) {
		mSpaceName = spaceName;
	}

	protected BdKVCache<String> getCacheSpace(String uid) {
		return DBKVCacheManager.getInstance().getStringCacheWithSapce(mSpaceName, uid);
	}

	@Override
	public String getCache(String key, String uid) {
		BdKVCache<String> mCacheSpace = getCacheSpace(uid);
		if (mCacheSpace == null) {
			return null;
		}
		return mCacheSpace.get(key);
	}

	@Override
	public boolean insert(String key, String value, String uid, long expiredTime) {
		if (StringUtils.isNull(key)) {
			return false;
		}
		if (StringUtils.isNull(value)) {
			return false;
		}
		if (expiredTime <= 0) {
			return false;
		}
		BdKVCache<String> mCacheSpace = getCacheSpace(uid);
		if (mCacheSpace == null) {
			return false;
		}
		mCacheSpace.set(key, value, expiredTime);
		return true;
	}

	@Override
	public boolean delete(String key, String uid) {
		if (StringUtils.isNull(key)) {
			return false;
		}
		BdKVCache<String> mCacheSpace = getCacheSpace(uid);
		if (mCacheSpace == null) {
			return false;
		}
		mCacheSpace.remove(key);
		return true;
	}
}
