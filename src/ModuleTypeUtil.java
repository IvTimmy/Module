package com.baidu.tieba.module;

/**
 * 模块类型转换工具
 * 
 * Created by wangzexiang on 2017/2/22.
 */
public final class ModuleTypeUtil {

	public static int getEventInt(ModuleEvent event) {
		if (event == null) {
			return -1;
		}
		if (event.getData() instanceof Integer) {
			return (Integer) event.getData();
		} else {
			return -1;
		}
	}

	public static float getEventFloat(ModuleEvent event) {
		if (event == null) {
			return -1.0f;
		}
		if (event.getData() instanceof Float) {
			return (Float) event.getData();
		} else {
			return -1.0f;
		}
	}

	public static long getEventLong(ModuleEvent event) {
		if (event == null) {
			return -1L;
		}
		if (event.getData() instanceof Long) {
			return (Long) event.getData();
		} else {
			return -1L;
		}
	}

	public static double getEventDouble(ModuleEvent event) {
		if (event == null) {
			return -1.0f;
		}
		if (event.getData() instanceof Double) {
			return (Double) event.getData();
		} else {
			return -1.0f;
		}
	}

	public static String getEventString(ModuleEvent event) {
		if (event == null) {
			return null;
		}
		if (event.getData() instanceof String) {
			return (String) event.getData();
		} else {
			return null;
		}
	}

	public static boolean getEventBoolean(ModuleEvent event) {
		if (event == null) {
			return false;
		}
		if (event.getData() instanceof Boolean) {
			return (Boolean) event.getData();
		} else {
			return false;
		}
	}

	public static int getInt(Object data) {
		if (data instanceof Integer) {
			return (Integer) data;
		} else {
			return -1;
		}
	}

	public static float getFloat(Object data) {
		if (data instanceof Float) {
			return (Float) data;
		} else {
			return -1.0f;
		}
	}

	public static long getLong(Object data) {
		if (data instanceof Long) {
			return (Long) data;
		} else {
			return -1L;
		}
	}

	public static double getDouble(Object data) {
		if (data instanceof Double) {
			return (Double) data;
		} else {
			return -1.0f;
		}
	}

	public static String getString(Object data) {
		if (data instanceof String) {
			return (String) data;
		} else {
			return null;
		}
	}

	public static boolean getBoolean(Object data) {
		if (data instanceof Boolean) {
			return (Boolean) data;
		} else {
			return false;
		}
	}
}
