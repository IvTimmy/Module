package com.baidu.tieba.module.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

/**
 * 与系统ViewStub功能类似
 * <p/>
 * Created by wangzexiang on 2016/8/2.
 */
public class ViewStubModule extends ViewModule {
	private final int mLayoutId;
	private final ViewStub mViewStub;

	public ViewStubModule(String name, int layoutId) {
		super(name, null);
		mLayoutId = layoutId;
		mViewStub = null;
	}

	public ViewStubModule(ViewStub viewstub) {
		super(null, null);
		mLayoutId = -1;
		mViewStub = viewstub;
	}

	public View inflate() {
		if (isAttached() && mLayoutId > 0 && getContentView() == null) {
			return inflateInternal();
		}
		return null;
	}

	/**
	 * 是否已经inflate过
	 * 
	 * @return true:已经inflate过
	 */
	public boolean isInflated() {
		return getContentView() != null;
	}

	private View inflateInternal() {
		Context context = getContext().getPageActivity();
		View view = null;
		if (mLayoutId > 0) {
			view = LayoutInflater.from(context).inflate(mLayoutId, getParentView(), false);
		} else if (mViewStub != null) {
			view = mViewStub.inflate();
		}
		ViewGroupModule parent = (ViewGroupModule) getParent();
		final int index = parent.indexOfChild(this);
		ViewGroup parentViewGroup = parent.getViewGroup();
		parentViewGroup.addView(view, index);
		setContentView(view);
		onInflated(view);
		return view;
	}

	protected void onInflated(View view) {

	}

}
