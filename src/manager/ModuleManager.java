package com.baidu.tieba.module.manager;

import com.baidu.adp.BdUniqueId;
import com.baidu.adp.base.BdPageContext;
import com.baidu.tbadk.TbPageContext;
import com.baidu.tieba.module.Module;
import com.baidu.tieba.module.ModuleEvent;
import com.baidu.tieba.module.ModuleEventListener;
import com.baidu.tieba.module.ModuleGroup;

/**
 * 模块化管理器
 *
 * @Author WangZeXiang
 */
public final class ModuleManager {
	private static final String MODULE_ROOT = "module_root";
	// 模块化管理器事件中心
	private final ModuleEventCenter mEventCenter;
	// 模块化管理器拒绝策略
	private ModuleRejectPolicy mFobidPolicy;
	// 模块化管理器根模块
	private ModuleGroup<Module> mRootModule;
	private BdUniqueId mBdUniqueId;

	public ModuleManager(TbPageContext context) {
		mEventCenter = new ModuleEventCenter();
		mRootModule = new ModuleGroup<Module>(MODULE_ROOT);
		mRootModule.attachModule(null, this, context);
	}

	public Object sendModuleEvent(ModuleEvent event) {
		if (event == null) {
			return null;
		}
		return mEventCenter.sendEvent(event);
	}

	void setUniqueId(BdUniqueId uniqueId) {
		mBdUniqueId = uniqueId;
	}

	public final BdUniqueId getUniqueId() {
		return mBdUniqueId;
	}

	public void registerEventListener(ModuleEventListener listener) {
		if (listener == null) {
			return;
		}
		mEventCenter.registerEventListener(listener);
	}

	public void unregisterEventListener(ModuleEventListener listener) {
		if (listener == null) {
			return;
		}
		mEventCenter.unregisterEventListener(listener);
	}

	public void addModule(Module module) {
		mRootModule.addChildModule(module);
	}

	public void removeModule(Module module) {
		mRootModule.removeChildModule(module);
	}

	public void unregisterAllEventListener() {
		mEventCenter.unregisterAllEventListener();
	}

}
