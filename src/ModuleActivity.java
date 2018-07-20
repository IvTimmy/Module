package com.baidu.tieba.module;

import android.os.Bundle;

import com.baidu.tbadk.BaseActivity;
import com.baidu.tieba.module.manager.ModuleManager;

/**
 * 模块化Activity
 *
 * @Author WangZeXiang
 */
public class ModuleActivity<T> extends BaseActivity<T> {
	private ModuleManager mModuleManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mModuleManager = new ModuleManager(getPageContext());
	}

	/**
	 * 添加模块
	 *
	 * @param module
	 *            需要被添加的模块
	 */
	public void addModule(Module module) {
		if (module == null) {
			return;
		}
		mModuleManager.addModule(module);
	}

	/**
	 * 移除模块
	 *
	 * @param module
	 *            被移除的模块
	 */
	public void removeModule(Module module) {
		if (module == null) {
			return;
		}
		mModuleManager.removeModule(module);
	}

	public Object sendModuleEvent(ModuleEvent event) {
		if (event == null) {
			return null;
		}
		return mModuleManager.sendModuleEvent(event);
	}

	public void registerModuleListener(ModuleEventListener listener) {
		if (listener != null) {
			mModuleManager.registerEventListener(listener);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mModuleManager.unregisterAllEventListener();
		mModuleManager = null;// help GC
	}
}
