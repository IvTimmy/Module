package com.baidu.tieba.module;

import com.baidu.adp.BdUniqueId;
import com.baidu.adp.base.BdPageContext;
import com.baidu.adp.framework.MessageManager;
import com.baidu.adp.framework.listener.MessageListener;
import com.baidu.adp.framework.message.Message;
import com.baidu.adp.framework.message.NetMessage;
import com.baidu.adp.framework.task.MessageTask;
import com.baidu.adp.lib.util.BdLog;
import com.baidu.tbadk.TbPageContext;
import com.baidu.tieba.module.manager.ModuleManager;

import tbclient.T;

/**
 * 基础模块
 *
 * @Author WangZeXiang
 */
public class Module {
	private final String mModuleName;
	private ModuleManager mModuleManager;
	// 该模块的父模块
	private Module mParent;
	private BdUniqueId mUniqueId;
	private TbPageContext mPageContext;

	public Module() {
		mModuleName = getClass().getSimpleName();
	}

	public Module(String name) {
		mModuleName = name;
	}

	protected final ModuleManager getModuleManager() {
		return mModuleManager;
	}

	public final String getModuleName() {
		return mModuleName;
	}

	public void setUniqueId(BdUniqueId uniqueId) {
		mUniqueId = uniqueId;
	}

	public final BdUniqueId getUniqueId() {
		if (mUniqueId != null) {
			return mUniqueId;
		}
		if (mModuleManager != null) {
			return mModuleManager.getUniqueId();
		}
		return null;
	}

	public void registerModuleListener(ModuleEventListener listener) {
		if (mModuleManager != null) {
			mModuleManager.registerEventListener(listener);
		}
	}

	public void unregisterModuleListener(ModuleEventListener listener) {
		if (mModuleManager != null) {
			mModuleManager.unregisterEventListener(listener);
		}
	}

	/**
	 * 传递模块间的事件
	 *
	 * @param event
	 *            事件
	 * @return 返回事件处理后的结果，如果只需要过程，则返回null
	 */
	public Object sendModuleEvent(ModuleEvent event) {
		if (mModuleManager != null) {
			return mModuleManager.sendModuleEvent(event);
		}
		return null;
	}

	/**
	 * 当前模块是否被添加到ModuleManager
	 *
	 * @return true：已经被添加 flase：还未被添加
	 */
	public final boolean isAttached() {
		return mParent != null && mModuleManager != null;
	}

	/**
	 * 添加到模块
	 *
	 * @param parent
	 *            父模块
	 * @param moduleManager
	 *            模块化管理器
	 */
	public final void attachModule(Module parent, ModuleManager moduleManager, TbPageContext context) {
		mParent = parent;
		mModuleManager = moduleManager;
		mPageContext = context;
		onModuleAttached(parent);
	}

	/**
	 * 移除模块
	 *
	 * @param parent
	 *            被移除的父模块
	 * @param isActive
	 *            是否是主动移除
	 */
	public final void dettachModule(Module parent, boolean isActive) {
		if (parent != mParent) {
			BdLog.e("Remove Failed Reason:" + parent + " is Not Match:" + mParent);
			return;
		}
		onModuleDettached(parent, isActive);
		mParent = null;
		mPageContext = null;
		mModuleManager = null;
	}

	/**
	 * 获取父模块
	 *
	 * @return 父模块
	 */
	public Module getParent() {
		return mParent;
	}

	/**
	 * 子类重写函数，当模块被添加上的时候，会被调用
	 *
	 * @param parent
	 *            父模块
	 */
	protected void onModuleAttached(Module parent) {

	}

	/**
	 * 当模块被移除的时候，会被调用
	 *
	 * @param parent
	 *            父模块
	 * @param isPassive
	 *            是否是主动移除
	 */
	protected void onModuleDettached(Module parent, boolean isPassive) {
	}

	/**
	 * 获取当前模块的上下文环境
	 *
	 * @return 返回当前模块上下文环境，返回null的时候说明当前模块还未被添加上
	 */
	protected final TbPageContext getContext() {
		return mPageContext;
	}
}
