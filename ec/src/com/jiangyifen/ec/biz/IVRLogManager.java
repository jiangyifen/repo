package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.IVRLog;

public interface IVRLogManager {
	
	void save(IVRLog o);
	
	long getCountByNode(String node);
}
