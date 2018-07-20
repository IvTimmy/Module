package com.baidu.tieba.module.cache.string;

import com.baidu.adp.lib.asyncTask.BdAsyncTask;
import com.baidu.adp.lib.util.StringUtils;
import com.baidu.tieba.module.cache.CacheModule;
import com.baidu.tieba.module.cache.CacheResult;
import com.baidu.tieba.module.cache.ICacheDecoder;
import com.baidu.tieba.module.cache.ICachePolicy;

/**
 * 
 * 字符串缓存模块
 * 
 * Created by wangzexiang on 2016/8/25.
 */
public class StringCacheModule extends CacheModule<String> {
	private ICachePolicy<String> mCachePolicy;

	public StringCacheModule(String moduleName, String spaceName) {
		super(moduleName, spaceName);
		mCachePolicy = new StringKVPolicy(spaceName);
	}

	public void setCachePolicy(ICachePolicy<String> policy) {
		if (policy == null) {
			return;
		}
		mCachePolicy = policy;
	}

	public final ICachePolicy<String> getCachePolicy() {
		return mCachePolicy;
	}

	@Override
	public void loadCache(String key, String uid, ICacheDecoder<String> data) {
		if (StringUtils.isNull(key) || data == null) {
			return;
		}
		if (!isLoading(key)) {
			StringCacheReadTask task = new StringCacheReadTask(data);
			// 为任务设置上Tag以及Key，确定该任务的唯一性
			task.setTag(getUniqueId());
			task.setKey(key);
			task.execute(key, uid);
		}
	}

	@Override
	public void cancelLoad(String key) {
		BdAsyncTask.removeAllTask(getUniqueId(), key);
	}

	@Override
	public boolean isLoading(String key) {
		return BdAsyncTask.getTaskNum(key, getUniqueId()) > 0;
	}

	@Override
	public void insert(String key, String value, String uid, long expiredTime) {
		if (StringUtils.isNull(getCacheSpaceName())) {
			return;
		}
		if (StringUtils.isNull(key) || StringUtils.isNull(value)) {
			return;
		}
		if (expiredTime <= 0) {
			return;
		}
		new InsertCacheTask(expiredTime).execute(key, value, uid);
	}

	@Override
	public void delete(String key, String uid) {
		if (StringUtils.isNull(getCacheSpaceName())) {
			return;
		}
		if (StringUtils.isNull(key)) {
			return;
		}
		new DeleteCacheTask().execute(key, uid);
	}

	private class InsertCacheTask extends BdAsyncTask<String, Void, CacheResult> {
		private final long mExpiredTime;

		public InsertCacheTask(long expiredTime) {
			mExpiredTime = expiredTime;
		}

		@Override
		protected CacheResult doInBackground(String... params) {
			CacheResult result;
			if (params == null || params.length < 3) {
				result = new CacheResult(null, false, CacheResult.ERROR_CODE_ILLEGAL_ARGS);
				return result;
			}
			String key = params[0];
			String value = params[1];
			String uid = params[2];
			ICachePolicy<String> cachePolicy = getCachePolicy();
			if (cachePolicy == null) {
				result = new CacheResult(key, false, CacheResult.ERROR_CODE_NULL_POLICY);
				return result;
			}
			boolean insertResult = cachePolicy.insert(key, value, uid, mExpiredTime);
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
			if (result == null) {
				return;
			}
			onCacheInserted(result);
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
			ICachePolicy<String> cachePolicy = getCachePolicy();
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

	private class StringCacheReadTask extends BdAsyncTask<String, Void, StringReadCacheResult> {
		private final ICacheDecoder<String> mStringData;

		StringCacheReadTask(ICacheDecoder<String> data) {
			mStringData = data;
		}

		@Override
		protected StringReadCacheResult doInBackground(String... params) {
			StringReadCacheResult result;
			if (params == null || params.length < 2) {
				result = new StringReadCacheResult(null, false, CacheResult.ERROR_CODE_ILLEGAL_ARGS);
				return result;
			}
			String key = params[0];
			String uid = params[1];
			if (mStringData == null) {
				result = new StringReadCacheResult(key, false, CacheResult.ERROR_CODE_NULL_DECODER);
				return result;
			}
			ICachePolicy<String> cachePolicy = getCachePolicy();
			if (cachePolicy == null) {
				result = new StringReadCacheResult(key, false, CacheResult.ERROR_CODE_NULL_POLICY);
				return result;
			}
			onBeforeCacheReadInBackground(key, uid);
			try {
				mStringData.decodeInBackground(cachePolicy.getCache(key, uid));
			} catch (Exception e) {
				result = new StringReadCacheResult(key, false, CacheResult.ERROR_CODE_DECODE_EXCEPTION);
				return result;
			}
			onAfterCacheReadInBackground(key, uid);
			result = new StringReadCacheResult(key, true);
			result.setResult(mStringData);
			return result;
		}

		@Override
		protected void onPostExecute(StringReadCacheResult result) {
			super.onPostExecute(result);
			if (result != null) {
				onCacheLoaded(result);
			}
		}
	}

	protected void onCacheLoaded(StringReadCacheResult cache) {

	}

	protected void onBeforeCacheReadInBackground(String key, String uid) {

	}

	protected void onAfterCacheReadInBackground(String key, String uid) {

	}

	protected void onCacheDeleted(CacheResult result) {

	}

	protected void onCacheInserted(CacheResult result) {

	}
}