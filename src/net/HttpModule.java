package com.baidu.tieba.module.net;

import com.baidu.adp.framework.message.HttpResponsedMessage;
import com.baidu.adp.framework.message.Message;
import com.baidu.adp.framework.task.MessageTask;
import com.baidu.tbadk.task.TbHttpMessageTask;
import com.baidu.tieba.module.MessageModule;

/**
 * 短链接请求模块
 * 
 * Created by wangzexiang on 2016/8/24.
 */
public class HttpModule extends NetModule {
	private final String mHttpUrl;
	private Class<? extends HttpResponsedMessage> mResponsedClass;

	/**
	 * 构造函数
	 * 
	 * @param name
	 *            模块名
	 * @param cmd
	 *            CMD号
	 * @param url
	 *            完整的短链接URL
	 */
	public HttpModule(String name, int cmd, String url) {
		super(name, cmd);
		mHttpUrl = url;
	}

	public void setResponsedClass(Class<? extends HttpResponsedMessage> responsedClass) {
		mResponsedClass = responsedClass;
	}

	@Override
	protected MessageTask makeMessageTask() {
		TbHttpMessageTask task = new TbHttpMessageTask(getCmd(), mHttpUrl);
		task.setResponsedClass(mResponsedClass);
		processHttpTask(task);
		return task;
	}

	protected void processHttpTask(TbHttpMessageTask task) {

	}
}
