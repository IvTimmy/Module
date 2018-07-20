
package com.baidu.tieba.module.net;

import com.baidu.adp.framework.message.Message;
import com.baidu.adp.framework.message.NetMessage;
import com.baidu.tieba.module.ModuleGroup;

/**
 * 网络模块组
 * 
 * Created by wangzexiang on 2016/8/24.
 */
public class NetGroupModule extends ModuleGroup<NetModule> {
	public NetGroupModule(String name) {
		super(name);
	}

	/**
	 * 发送NetMessage
	 * 
	 * @param message
	 *            待发送的NetMessage
	 */
	public void sendMessage(NetMessage message) {
		if (message == null) {
			return;
		}
		NetMessage.NetType netType = message.getNetType();
		if (netType == NetMessage.NetType.AUTO) {
			if (!sendMessage(message.getSocketMessage())) {
				sendMessage(message.getHttpMessage());
			}
		} else if (netType == NetMessage.NetType.SOCKET) {
			sendMessage(message.getSocketMessage());
		} else if (netType == NetMessage.NetType.HTTP) {
			sendMessage(message.getHttpMessage());
		}
	}

	@Override
	public boolean sendMessage(Message message) {
		if (message == null) {
			return false;
		}
		int childCount = getChildCount();
		int cmd = message.getCmd();
		if (childCount > 0) {
			NetModule module;
			for (int i = 0; i < childCount; i++) {
				module = getChildModule(i);
				if (module != null && module.getCmd() == cmd) {
					return sendMessage(message, module.getMessageTask());
				}
			}
		}
		return super.sendMessage(message);
	}

}
