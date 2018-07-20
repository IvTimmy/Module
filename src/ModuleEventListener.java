package com.baidu.tieba.module;

import com.baidu.tieba.module.manager.ModuleEventCenter;

/**
 * ModuleEvent监听器
 *
 * @Author WangZeXiang
 */
public abstract class ModuleEventListener {
    private final int mEventListenerId;
    private int mPriority = ModuleEventCenter.NORMAL_PRIORITY;

    public ModuleEventListener(int eventTypeId) {
        mEventListenerId = eventTypeId;
    }

    public final int getEventTypeId() {
        return mEventListenerId;
    }

    public void setPriority(int priority) {
        if (mPriority < ModuleEventCenter.MIN_PRIORITY) {
            mPriority = ModuleEventCenter.MIN_PRIORITY;
        } else if (mPriority > ModuleEventCenter.MAX_PRIORITY) {
            mPriority = ModuleEventCenter.MAX_PRIORITY;
        } else {
            mPriority = priority;
        }
    }

    public int getPriority() {
        return mPriority;
    }

    public abstract Object onEvent(ModuleEvent event);
}
