package com.baidu.tieba.module;

import android.text.TextUtils;

import com.baidu.adp.base.BdPageContext;
import com.baidu.adp.lib.util.StringUtils;
import com.baidu.tbadk.TbPageContext;
import com.baidu.tbadk.core.util.ListUtils;
import com.baidu.tieba.module.manager.ModuleManager;

import java.util.ArrayList;

/**
 * ModuleGroup，该模块组内可以拥有多个模块
 *
 * @Author WangZeXiang
 */
public class ModuleGroup<T extends Module> extends MessageModule {
	private ArrayList<T> mChildModules;

	public ModuleGroup(String name) {
		super(name);
		mChildModules = new ArrayList<T>();
	}

	public ModuleGroup() {
		super();
		mChildModules = new ArrayList<T>();
	}

	/**
	 * 添加子模块
	 *
	 * @param module
	 */
	public void addChildModule(T module) {
		if (module == null) {
			return;
		}
		module.attachModule(this, getModuleManager(), getContext());
		if (mChildModules != null) {
			mChildModules.add(module);
		}
	}

	/**
	 * 移除子模块
	 *
	 * @param module
	 *            被移出的子模块
	 */
	public void removeChildModule(T module) {
		if (module == null) {
			return;
		}
		module.dettachModule(module, true);
		if (mChildModules != null) {
			mChildModules.remove(module);
		}
	}

	/**
	 * 移除所有子模块
	 */
	public void removeAllChilds() {
		if (mChildModules != null) {
			for (T module : mChildModules) {
				if (module != null) {
					module.dettachModule(this, true);
				}
			}
			mChildModules.clear();
		}
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

	/**
	 * 根据index获取对应的module
	 *
	 * @param index
	 *            子模块索引
	 * @return 返回index对应的子模块，除非index越界或者mChildModules为空
	 */
	public T getChildModule(int index) {
		if (index < 0 || index >= ListUtils.getCount(mChildModules)) {
			return null;
		}
		if (mChildModules != null) {
			return mChildModules.get(index);
		}
		return null;
	}

	/**
	 * 获取子模块
	 *
	 * @param name
	 *            通过名字获取子模块
	 * @return 返回名字对应的子模块
	 */
	public T getChildModule(String name) {
		if (StringUtils.isNull(name)) {
			return null;
		}
		if (mChildModules != null) {
			for (T child : mChildModules) {
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
