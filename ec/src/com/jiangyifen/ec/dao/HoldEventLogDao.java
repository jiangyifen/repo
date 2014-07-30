package com.jiangyifen.ec.dao;

public interface HoldEventLogDao {

	void save(HoldEventLog log);
	void update(HoldEventLog log);

	HoldEventLog findLastLogByUniqueid(String uniqueid);

}
