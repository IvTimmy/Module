package com.baidu.tieba.module.view;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.baidu.adp.lib.util.StringUtils;
import com.baidu.tbadk.core.util.ListUtils;
import com.baidu.tieba.module.Module;

import java.util.ArrayList;

/**
 * ViewGroup模块
 * 该模块的概念是这个ViewGroup中含有多个ViewGroup.ViewGroupModule和普通的ModuleGroup不一样
 * 因为可能从XML中直接inflate出来的ViewGroup也可以被添加到树上，所以在添加的时候，需要根据View
 * 是否有Parent然后才能准确的添加到固定的位置，优先根据View在这个View树上的位置添加到对应的位置
 * <p/>
 * Created by wangzexiang on 2016/8/17.
 */
public class ViewGroupModule extends ViewModule {
	private ArrayList<ViewModule> mChildModules;
	private ViewGroup mGroup;

	public ViewGroupModule(String name, ViewGroup view) {
		super(name, view);
		mChildModules = new ArrayList<ViewModule>();
		mGroup = view;
	}

	/**
	 * 添加子模块
	 *
	 * @param module
	 */
	public void addChildModule(ViewModule module) {
		addChildModule(module, -1);
	}

	public void addChildModule(ViewModule module, int index) {
		addChildModule(module, -1, null);
	}

	public void addChildModule(ViewModule module, ViewGroup.LayoutParams params) {
		addChildModule(module, -1, params);
	}

	public void addChildModule(ViewModule module, int index, ViewGroup.LayoutParams params) {
		if (module == null || module.getContentView() == null) {
			return;
		}
		View view = module.getContentView();
		// 插入到模块内部的Index
		boolean isValid = false;
		ViewParent parent = view.getParent();
		if (parent == null) {
			// 当View没有被添加的时候
			ViewGroup.LayoutParams childParams = params;
			if (childParams == null) {
				childParams = view.getLayoutParams();
				if (childParams == null) {
					childParams = generateDefaultLayoutParams();
				}
			}
			isValid = true;
			mGroup.addView(view, index, childParams);
		} else {
			// 根据ViewParent来查找是否加入的子View模块是否是在当前的ViewGroup中，按逻辑View树来进行区分
			do {
				if (parent == mGroup) {
					isValid = true;
					break;
				}
			} while ((parent = parent.getParent()) != null);
		}
		if (!isValid || index > getChildCount()) {
			return;
		}
		module.attachModule(this, getModuleManager(), getContext());
		if (mChildModules != null) {
			mChildModules.add(module);
		}
	}

	@Deprecated
	@Override
	public View getContentView() {
		return super.getContentView();
	}

	public void removeChildModule(int index) {
		ViewModule module = getChildModule(index);
		if (module != null) {
			removeChildModule(module);
		}
	}

	/**
	 * 移除子模块
	 *
	 * @param module
	 *            被移出的子模块
	 */
	public void removeChildModule(ViewModule module) {
		if (module == null || module.getContentView() == null) {
			return;
		}
		module.dettachModule(module, true);
		if (mChildModules != null) {
			mChildModules.remove(module);
		}
		View view = module.getContentView();
		if (view.getParent() == mGroup) {
			mGroup.removeView(view);
		}
	}

	/**
	 * 移除所有子模块
	 */
	public void removeAllChilds() {
		if (mChildModules != null) {
			for (ViewModule module : mChildModules) {
				if (module != null) {
					module.dettachModule(this, true);
				}
			}
			mChildModules.clear();
		}
		mGroup.removeAllViews();
	}

	@Override
	protected void onModuleDettached(Module parent, boolean isActive) {
		super.onModuleDettached(parent, isActive);
		// 当本模块被移除的时候，回调所有子View的dettach，但是不真正移除
		if (mChildModules != null) {
			for (Module child : mChildModules) {
				if (child != null) {
					child.dettachModule(this, false);
				}
			}
		}
	}

	@Override
	protected void onModuleAttached(Module parent) {
		super.onModuleAttached(parent);
		// 当本模块被添加的时候，回调所有子View的attach
		if (mChildModules != null) {
			for (Module child : mChildModules) {
				if (child != null) {
					child.attachModule(this, getModuleManager(), getContext());
				}
			}
		}
	}

	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 根据index获取对应的module
	 *
	 * @param index
	 *            子模块索引
	 * @return 返回index对应的子模块，除非index越界或者mChildModules为空
	 */
	public ViewModule getChildModule(int index) {
		if (index < 0 || index >= ListUtils.getCount(mChildModules)) {
			return null;
		}
		if (mChildModules != null) {
			return mChildModules.get(index);
		}
		return null;
	}

	public ViewGroup getViewGroup() {
		return mGroup;
	}

	public int indexOfChild(ViewModule module) {
		if (module == null) {
			return -1;
		}
		return mChildModules.indexOf(module);
	}

	/**
	 * 获取子模块
	 *
	 * @param name
	 *            通过名字获取子模块
	 * @return 返回名字对应的子模块
	 */
	public ViewModule getChildModule(String name) {
		if (StringUtils.isNull(name)) {
			return null;
		}
		if (mChildModules != null) {
			for (ViewModule child : mChildModules) {
				if (child != null && TextUtils.equals(name, child.getModuleName())) {
					return child;
				}
			}
		}
		return null;
	}

	/**
	 * 获取当前子模块的总数
	 *
	 * @return 子模块总数
	 */
	public int getChildCount() {
		return ListUtils.getCount(mChildModules);
	}

}
