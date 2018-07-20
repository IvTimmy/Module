package com.baidu.tieba.module;

/**
 * 模块事件，其中有两个Id，分别代表事件类型以及响应动作
 *
 * @Author WangZeXiang
 */
public class ModuleEvent {
	private final int mEventId;
	private final int mEventAction;
	private boolean mAbort = false;
	private Object mData;

	public ModuleEvent(int eventId) {
		this(eventId, null);
	}

	public ModuleEvent(int eventId, Object data) {
		this(eventId, 0, data);
	}

	public ModuleEvent(int eventId, int eventAction) {
		this(eventId, eventAction, null);
	}

	public ModuleEvent(int eventId, int eventAction, Object data) {
		mEventId = eventId;
		mEventAction = eventAction;
		mData = data;
	}

	public int getEventAction() {
		return mEventAction;
	}

	public int getEventId() {
		return mEventId;
	}

	public void abort() {
		mAbort = true;
	}

	public boolean isAbort() {
		return mAbort;
	}

	public Object getData() {
		return mData;
	}
}
