package com.baidu.tieba.module.manager;

import android.util.SparseArray;

import com.baidu.adp.lib.util.BdLog;
import com.baidu.tbadk.core.util.ListUtils;
import com.baidu.tieba.module.ModuleEvent;
import com.baidu.tieba.module.ModuleEventListener;

import java.util.ArrayList;

/**
 * 模块事件分发中心
 * 
 * @Author WangZeXiang
 */
public final class ModuleEventCenter {
	public static final int MAX_PRIORITY = 10;
	public static final int NORMAL_PRIORITY = 5;
	public static final int MIN_PRIORITY = 1;
	private final SparseArray<ArrayList<ModuleEventListener>> mListeners;

	public ModuleEventCenter() {
		mListeners = new SparseArray<ArrayList<ModuleEventListener>>();
	}

	/**
	 * 注册Listener
	 * 
	 * @param listener
	 *            待注册的Listener
	 */
	public void registerEventListener(ModuleEventListener listener) {
		if (listener == null) {
			return;
		}
		int listenerId = listener.getEventTypeId();
		ArrayList<ModuleEventListener> listenerList = mListeners.get(listenerId);
		if (listenerList == null) {
			listenerList = new ArrayList<ModuleEventListener>();
			mListeners.put(listenerId, listenerList);
		}
		if (listenerList.contains(listener)) {
			return;
		}
		insertListener(listenerList, listener);
	}

	/**
	 * 发送事件
	 * 
	 * @param event
	 *            待发送事件
	 * @return 事件处理结果
	 */
	public Object sendEvent(ModuleEvent event) {
		if (event == null) {
			return null;
		}
		int eventTypeId = event.getEventId();
		ArrayList<ModuleEventListener> listenerList = mListeners.get(eventTypeId);
		if (ListUtils.isEmpty(listenerList)) {
			return null;
		}
		Object eventResult;
		for (ModuleEventListener listener : listenerList) {
			if (listener == null) {
				continue;
			}
			BdLog.e("Listener:" + listener + "  Priority:" + listener.getPriority() + " EventId:"
					+ listener.getEventTypeId());
			if (listener.getEventTypeId() == eventTypeId) {
				// 分发事件
				eventResult = listener.onEvent(event);
				// 判断事件处理结果是否为空，不为空则返回
				if (eventResult != null) {
					return eventResult;
				}
				// 判断事件传递是否要继续，如果被中断，则不继续
				if (event.isAbort()) {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 插入Listener
	 * 
	 * @param listenerList
	 *            原有的Listener列表
	 * @param listener
	 *            待插入Listener
	 */
	private void insertListener(ArrayList<ModuleEventListener> listenerList, ModuleEventListener listener) {
		if (listenerList == null || listener == null) {
			return;
		}
		int priority = listener.getPriority();
		int insertIndex = 0;
		int length = listenerList.size();
		ModuleEventListener l;
		for (int i = 0; i < length; i++) {
			l = listenerList.get(i);
			if (l == null) {
				continue;
			}
			if (priority < l.getPriority()) {
				insertIndex = i;
				break;
			}
		}
		if (insertIndex >= 0) {
			listenerList.add(insertIndex, listener);
		}
	}

	public void unregisterEventListener(ModuleEventListener listener) {
		if (listener == null) {
			return;
		}
		int listenerId = listener.getEventTypeId();
		ArrayList<ModuleEventListener> listenerList = mListeners.get(listenerId);
		if (listenerList == null) {
			return;
		}
		listenerList.remove(listener);
	}

	public void unregisterAllEventListener() {
		mListeners.clear();
	}
}
