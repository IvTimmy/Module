package com.baidu.tieba.module.cache;

/**
 * 读缓存结果结构体
 * 
 * Created by wangzexiang on 2016/8/26.
 */
public class CacheResult {
	// 错误码，参数错误
	public static final int ERROR_CODE_ILLEGAL_ARGS = 1;
	// 错误码，数据解析体为空
	public static final int ERROR_CODE_NULL_DECODER = 2;
	// 错误码，缓存为空
	public static final int ERROR_CODE_NULL_CACHE = 3;
	// 错误码，解析缓存异常
	public static final int ERROR_CODE_DECODE_EXCEPTION = 4;
	// 错误码，缓存策略为空
	public static final int ERROR_CODE_NULL_POLICY = 5;
	private final boolean mSuccess;
	private int mErrorCode;
	private final String mCacheKey;

	public CacheResult(String key, boolean isSuccess) {
		mCacheKey = key;
		mSuccess = isSuccess;
	}

	public CacheResult(String key, boolean isSuccess, int error) {
		mCacheKey = key;
		mSuccess = isSuccess;
		mErrorCode = error;
	}

	public final boolean isSuccess() {
		return mSuccess;
	}

	public void setErrorCode(int error) {
		mErrorCode = error;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	public final String getCacheKey() {
		return mCacheKey;
	}

}
