package com.baidu.tieba.module.view.list;

import com.baidu.adp.BdUniqueId;
import com.baidu.adp.widget.ListView.AbsDelegateAdapter;
import com.baidu.adp.widget.ListView.IAdapterData;
import com.baidu.adp.widget.ListView.TypeAdapter;
import com.baidu.tbadk.TbPageContext;
import com.baidu.tieba.module.ModuleEvent;
import com.baidu.tieba.module.ModuleEventListener;
import com.baidu.tieba.module.manager.ModuleManager;

import android.content.Context;

/**
 * 模块化列表页Adapter
 * 
 * Created by wangzexiang on 2016/10/13.
 */
public abstract class ModuleTypeAdapter extends AbsDelegateAdapter<IAdapterData, TypeAdapter.ViewHolder> {
	private ModuleManager mModuleManager;
	private TbPageContext mPageContext;

	protected ModuleTypeAdapter(Context mContext, BdUniqueId mType) {
		super(mContext, mType);
	}

	void attachModule(ModuleManager manager, TbPageContext context) {
		mModuleManager = manager;
		mPageContext = context;
		onAttachedModule();
	}

	void detachModule() {
		onDettachedModule();
		mModuleManager = null;
		mPageContext = null;
	}

	protected final ModuleManager getModuleManager() {
		return mModuleManager;
	}

	protected final TbPageContext getPageContext() {
		return mPageContext;
	}

	protected void onAttachedModule() {

	}

	protected void onDettachedModule() {

	}

	public final boolean isAttached() {
		return mModuleManager != null;
	}

	public void sendEvent(ModuleEvent event) {
		if (event == null && !isAttached()) {
			return;
		}
		mModuleManager.sendModuleEvent(event);
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

}
