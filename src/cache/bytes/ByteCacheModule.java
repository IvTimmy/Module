package com.baidu.tieba.module.cache.bytes;

import com.baidu.adp.lib.asyncTask.BdAsyncTask;
import com.baidu.adp.lib.util.StringUtils;
import com.baidu.tieba.module.cache.CacheModule;
import com.baidu.tieba.module.cache.ICacheDecoder;
import com.baidu.tieba.module.cache.CacheResult;
import com.baidu.tieba.module.cache.ICachePolicy;

/**
 * 二进制缓存模块
 * 
 * Created by wangzexiang on 2016/8/25.
 */
public class ByteCacheModule extends CacheModule<byte[]> {
	private ICachePolicy<byte[]> mCachePolicy;

	public ByteCacheModule(String moduleName, String spaceName) {
		super(moduleName, spaceName);
		mCachePolicy = new BytesKVPolicy(spaceName);
	}

	public void setCachePolicy(ICachePolicy<byte[]> policy) {
		if (policy == null) {
			return;
		}
		mCachePolicy = policy;
	}

	public final ICachePolicy<byte[]> getCachePolicy() {
		return mCachePolicy;
	}

	@Override
	public void loadCache(String key, String uid, ICacheDecoder<byte[]> data) {
		if (StringUtils.isNull(key)) {
			return;
		}
		if (getCachePolicy() == null) {
			return;
		}
		if (!isLoading(key)) {
			ByteCacheReadTask task = new ByteCacheReadTask(data);
			// 为任务设置上Tag以及Key，确定该任务的唯一性
			task.setTag(getUniqueId());
			task.setKey(key);
			task.execute(key, uid);
		}
	}

	@Override
	public void insert(String key, byte[] value, String uid, long expiredTime) {
		if (getCachePolicy() == null) {
			return;
		}
		if (StringUtils.isNull(getCacheSpaceName())) {
			return;
		}
		if (StringUtils.isNull(key) || value == null || value.length <= 0) {
			return;
		}
		if (expiredTime <= 0) {
			return;
		}
		new InsertCacheTask(expiredTime, value).execute(key, uid);
	}

	@Override
	public void delete(String key, String uid) {
		if (getCachePolicy() == null) {
			return;
		}
		if (StringUtils.isNull(getCacheSpaceName())) {
			return;
		}
		if (StringUtils.isNull(key)) {
			return;
		}
		new DeleteCacheTask().execute(key, uid);
	}

	@Override
	public void cancelLoad(String key) {
		BdAsyncTask.removeAllTask(getUniqueId(), key);
	}

	public boolean isLoading(String key) {
		return BdAsyncTask.getTaskNum(key, getUniqueId()) > 0;
	}

	private class InsertCacheTask extends BdAsyncTask<String, Void, CacheResult> {
		private final long mExpiredTime;
		private final byte[] mCacheValue;

		public InsertCacheTask(long expiredTime, byte[] value) {
			mExpiredTime = expiredTime;
			mCacheValue = value;
		}

		@Override
		protected CacheResult doInBackground(String... params) {
			CacheResult result;
			if (params == null || params.length < 2) {
				result = new CacheResult(null, false, CacheResult.ERROR_CODE_ILLEGAL_ARGS);
				return result;
			}
			String key = params[0];
			String uid = params[1];
			ICachePolicy<byte[]> cachePolicy = getCachePolicy();
			if (cachePolicy == null) {
				result = new CacheResult(key, false, CacheResult.ERROR_CODE_NULL_POLICY);
				return result;
			}
			boolean insertResult = cachePolicy.insert(key, mCacheValue, uid, mExpiredTime);
			if (!insertResult) {
				result = new CacheResult(key, false, CacheResult.ERROR_CODE_NULL_CACHE);
				return result;
			}
			result = new CacheResult(key, true);
			return result;
		}

		@Override
		protected void onPostExecute(CacheResult result) {
			super.onPostExecute(result);
			if (result != null) {
				onCacheInserted(result);
			}
		}
	}

	private class DeleteCacheTask extends BdAsyncTask<String, Void, CacheResult> {

		@Override
		protected CacheResult doInBackground(String... params) {
			CacheResult result;
			if (params == null || params.length < 2) {
				result = new CacheResult(null, false, CacheResult.ERROR_CODE_ILLEGAL_ARGS);
				return result;
			}
			String key = params[0];
			String uid = params[1];
			ICachePolicy<byte[]> cachePolicy = getCachePolicy();
			if (cachePolicy == null) {
				result = new CacheResult(key, false, CacheResult.ERROR_CODE_NULL_POLICY);
				return result;
			}
			boolean removeResult = cachePolicy.delete(key, uid);
			if (!removeResult) {
				result = new CacheResult(key, false, CacheResult.ERROR_CODE_NULL_CACHE);
				return result;
			}
			result = new CacheResult(key, true);
			return result;
		}

		@Override
		protected void onPostExecute(CacheResult result) {
			super.onPostExecute(result);
			if (result != null) {
				onCacheDeleted(result);
			}
		}
	}

	private class ByteCacheReadTask extends BdAsyncTask<String, Void, BytesCacheReadResult> {
		private ICacheDecoder<byte[]> mByteData;

		ByteCacheReadTask(ICacheDecoder<byte[]> data) {
			mByteData = data;
		}

		@Override
		protected BytesCacheReadResult doInBackground(String... params) {
			BytesCacheReadResult result;
			if (params == null || params.length < 2) {
				result = new BytesCacheReadResult(null, false, CacheResult.ERROR_CODE_ILLEGAL_ARGS);
				return result;
			}
			String key = params[0];
			String uid = params[1];
			if (mByteData == null) {
				result = new BytesCacheReadResult(key, false, CacheResult.ERROR_CODE_NULL_DECODER);
				return result;
			}
			ICachePolicy<byte[]> cachePolicy = getCachePolicy();
			if (cachePolicy == null) {
				result = new BytesCacheReadResult(key, false, CacheResult.ERROR_CODE_NULL_POLICY);
				return result;
			}
			onBeforeCacheReadInBackground(key, uid);
			try {
                byte[] data = cachePolicy.getCache(key, uid);
                if (data == null || data.length <= 0) { // 缓存为空
                    return new BytesCacheReadResult(key, false, CacheResult.ERROR_CODE_NULL_CACHE);
                }
                mByteData.decodeInBackground(data);
            } catch (Exception e) {
				result = new BytesCacheReadResult(key, false, CacheResult.ERROR_CODE_DECODE_EXCEPTION);
				return result;
			}
			onAfterCacheReadInBackground(key, uid);
			result = new BytesCacheReadResult(key, true);
			result.setCacheData(mByteData);
			return result;
		}

		@Override
		protected void onPostExecute(BytesCacheReadResult result) {
			super.onPostExecute(result);
			onCacheLoaded(result);
		}
	}

	protected void onBeforeCacheReadInBackground(String key, String uid) {

	}

	protected void onAfterCacheReadInBackground(String key, String uid) {

	}

	protected void onCacheLoaded(BytesCacheReadResult cache) {

	}

	protected void onCacheDeleted(CacheResult result) {

	}

	protected void onCacheInserted(CacheResult result) {

	}
}
