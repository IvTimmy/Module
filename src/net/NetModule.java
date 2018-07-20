package com.baidu.tieba.module.net;

import com.baidu.adp.framework.message.Message;
import com.baidu.adp.framework.task.MessageTask;
import com.baidu.tieba.module.MessageModule;
import com.baidu.tieba.module.Module;

/**
 * 网络基础模块
 * 
 * Created by wangzexiang on 2016/8/30.
 */
public class NetModule extends MessageModule {
	private final int mCmd;
	private MessageTask mMessageTask;

	public NetModule(String name, int cmd) {
		super(name);
		mCmd = cmd;
	}

	public final int getCmd() {
		return mCmd;
	}

	public MessageTask getMessageTask() {
		if (mMessageTask == null) {
			mMessageTask = makeMessageTask();
		}
		return mMessageTask;
	}

	protected MessageTask makeMessageTask() {
		return null;
	}

	@Override
	public boolean sendMessage(Message message) {
		if (message == null) {
			return false;
		}
		if (message.getCmd() == mCmd) {
			return sendMessage(message, getMessageTask());
		}
		return super.sendMessage(message);
	}
}
