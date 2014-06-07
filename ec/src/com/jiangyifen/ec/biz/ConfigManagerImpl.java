package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.ConfigDao;

public class ConfigManagerImpl implements ConfigManager {
	private ConfigDao configDao;

	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

	public ConfigDao getConfigDao() {
		return configDao;
	}

}