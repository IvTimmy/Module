package com.baidu.tieba.module.view;

import android.view.View;
import android.view.ViewGroup;

import com.baidu.adp.base.BdPageContext;
import com.baidu.tieba.module.Module;
import com.baidu.tieba.module.manager.ModuleManager;

import tbclient.T;

/**
 * View模块，只有当父Module为ViewGroupModule的时候才会存在父View 如果直接被添加到RootModule上的话，则没有父View
 * <p/>
 * 而ViewModule的概念是一个ViewGroup或者一个View，如果是ViewGroup的话，那么这个ViewGroup中的子View
 * 中不会有ViewGroup。理论上这三个的使用和View/ViewGroup/ViewStub的概念一样。只不过拆分的粒度更加细
 *
 * @Author WangZeXiang
 */
public class ViewModule extends Module {
	// 当前模块的ContentView
	private View mContentView;
	// 当前模块的父View，
	private ViewGroup mParentView;

	public ViewModule(View contentView) {
		super();
		mContentView = contentView;
	}

	public ViewModule(String name, View contentView) {
		super(name);
		mContentView = contentView;
	}

	/**
	 * 即使是包外子类也无法调用该方法，该方法只用于ViewStub在inflate之后，设置用的
	 *
	 * @param contentView
	 * @hide
	 */

	void setContentView(View contentView) {
		mContentView = contentView;
	}

	public View getContentView() {
		return mContentView;
	}

	public final ViewGroup getParentView() {
		return mParentView;
	}

	@Override
	protected void onModuleAttached(Module parent) {
		super.onModuleAttached(parent);
		if (parent instanceof ViewGroupModule) {
			mParentView = ((ViewGroupModule) parent).getViewGroup();
		}
	}

	@Override
	protected void onModuleDettached(Module parent, boolean isPassive) {
		super.onModuleDettached(parent, isPassive);
		// 当模块被移除的时候，就会将Parent置成null
		mParentView = null;
	}

	protected void onChangeSkinType(int skinType) {

	}
}
