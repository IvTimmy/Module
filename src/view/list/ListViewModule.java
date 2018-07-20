package com.baidu.tieba.module.view.list;

import java.util.ArrayList;
import java.util.List;

import com.baidu.adp.widget.ListView.BdListView;
import com.baidu.adp.widget.ListView.BdTypeListView;
import com.baidu.adp.widget.ListView.IAdapterData;
import com.baidu.adp.widget.ListView.TypeAdapter;
import com.baidu.tieba.module.view.ViewModule;

/**
 * BdListView模块
 * 
 * Created by wangzexiang on 2016/10/31.
 */
public class ListViewModule extends ViewModule {
	private BdTypeListView mListView;

	public ListViewModule(String name, BdTypeListView contentView) {
		super(name, contentView);
		mListView = contentView;
	}

	/**
	 * 只有添加操作，没有移除或者查询，因为BdTypeListView不支持
	 * 
	 * @param adapter
	 */
	public void addAdapter(ModuleTypeAdapter adapter) {
		if (adapter == null || mListView == null) {
			return;
		}
		if (!isAttached()) {
			return;
		}
		adapter.attachModule(getModuleManager(), getContext());
		mListView.addAdapter(adapter);
	}

	protected BdTypeListView getListView() {
		return mListView;
	}
}
