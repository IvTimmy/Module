package com.baidu.tieba.module;

import com.baidu.adp.framework.MessageManager;
import com.baidu.adp.framework.listener.MessageListener;
import com.baidu.adp.framework.message.Message;
import com.baidu.adp.framework.message.ResponsedMessage;
import com.baidu.adp.framework.task.MessageTask;

/**
 * 可以发消息的Module
 * 
 * Created by wangzexiang on 2016/8/30.
 */
public class MessageModule extends Module {
	public MessageModule(String name) {
		super(name);
	}

	public MessageModule() {
		super();
	}

	public boolean sendMessage(Message message) {
		return sendMessage(message, null);
	}

	public boolean sendMessage(Message message, MessageTask task) {
		if (message == null) {
			return false;
		}
		if (message.getTag() == null) {
			message.setTag(getUniqueId());
		}
		return MessageManager.getInstance().sendMessage(message, task);
	}

	public void registerMessageListener(MessageListener listener) {
		if (listener == null) {
			return;
		}
		if (listener.getTag() == null) {
			listener.setTag(getUniqueId());
		}
		MessageManager.getInstance().registerListener(listener);
	}

	public void unregisterMessageListener(MessageListener listener) {
		if (listener != null) {
			MessageManager.getInstance().unRegisterListener(listener);
		}
	}

	public void dispatchResponseMessage(ResponsedMessage responsedMessage) {
		if (responsedMessage != null) {
			MessageManager.getInstance().dispatchResponsedMessage(responsedMessage);
		}
	}
}
