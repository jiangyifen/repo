package com.jiangyifen.ec.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserLoginRecordDaoHibernate extends HibernateDaoSupport implements
		UserLoginRecordDao {

	public void save(UserLoginRecord alr) {
		getHibernateTemplate().saveOrUpdate(alr);
	}

	@SuppressWarnings("unchecked")
	public UserLoginRecord findLastULRByUsername(String username) {
		List<UserLoginRecord> result = getHibernateTemplate().find(
				"from UserLoginRecord t where t.username = ? order by id desc",
				new String[] { username });
		if (result != null && result.size() >= 1) {
			return result.get(0);
		}

		return null;
	}

}
