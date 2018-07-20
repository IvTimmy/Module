package com.baidu.tieba.module.cache.bytes;

import com.baidu.adp.lib.cache.BdKVCache;
import com.baidu.adp.lib.util.StringUtils;
import com.baidu.tbadk.core.dbcache.DBKVCacheManager;
import com.baidu.tieba.module.cache.ICachePolicy;

/**
 * 二进制KV缓存策略
 *
 * Created by wangzexiang on 2016/8/29.
 */
public class BytesKVPolicy implements ICachePolicy<byte[]> {
	private final String mSpaceName;

	public BytesKVPolicy(String spaceName) {
		mSpaceName = spaceName;
	}

	protected BdKVCache<byte[]> getCacheSpace(String uid) {
		return DBKVCacheManager.getInstance().getByteCacheWithSapce(mSpaceName, uid);
	}

	@Override
	public byte[] getCache(String key, String uid) {
		BdKVCache<byte[]> mCacheSpace = getCacheSpace(uid);
		if (mCacheSpace == null) {
			return null;
		}
		return mCacheSpace.get(key);
	}

	@Override
	public boolean insert(String key, byte[] value, String uid, long expiredTime) {
		if (StringUtils.isNull(key)) {
			return false;
		}
		if (value == null || value.length <= 0) {
			return false;
		}
		if (expiredTime <= 0) {
			return false;
		}
		BdKVCache<byte[]> mCacheSpace = getCacheSpace(uid);
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
		BdKVCache<byte[]> mCacheSpace = getCacheSpace(uid);
		if (mCacheSpace == null) {
			return false;
		}
		mCacheSpace.remove(key);
		return true;
	}

}
