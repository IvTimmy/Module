package com.baidu.tieba.module.net;

import com.baidu.adp.framework.message.Message;
import com.baidu.adp.framework.message.SocketResponsedMessage;
import com.baidu.adp.framework.task.MessageTask;
import com.baidu.tbadk.task.TbSocketMessageTask;
import com.baidu.tieba.module.MessageModule;
import com.baidu.tieba.module.Module;

/**
 * 长连接模块
 * 
 * Created by wangzexiang on 2016/8/24.
 */
public class SocketModule extends NetModule {
	private Class<? extends SocketResponsedMessage> mResponsedClass;

	public SocketModule(String name, int cmd) {
		super(name, cmd);
	}

	public void setResponsedClass(Class<? extends SocketResponsedMessage> responsedClass) {
		mResponsedClass = responsedClass;
	}

	@Override
	protected MessageTask makeMessageTask() {
		TbSocketMessageTask mSocketTask = new TbSocketMessageTask(getCmd());
		mSocketTask.setResponsedClass(mResponsedClass);
		processSocketTask(mSocketTask);
		return mSocketTask;
	}

	protected void processSocketTask(TbSocketMessageTask task) {

	}

}
