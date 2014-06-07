package com.jiangyifen.ec.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserDaoHibernate extends HibernateDaoSupport implements UserDao {

	public User get(String username) {
		return (User) getHibernateTemplate().get(User.class, username);
	}

	public boolean save(User user) {
		User u = get(user.getUsername());
		if (u == null) {
			getHibernateTemplate().save(user);
			return true;
		} else {
			return false;
		}
	}

	public void update(User user) {
		getHibernateTemplate().saveOrUpdate(user);
	}

	public void delete(String username) {
		getHibernateTemplate().delete(
				getHibernateTemplate().get(User.class, username));
	}

	public void delete(User user) {
		getHibernateTemplate().delete(user);
	}

	@SuppressWarnings("unchecked")
	public long getUserCount() {
		String sql = "select count(*) from User";
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public long getUserCountByCondition(String condition) {
		String sql = "select count(*) from User where username like '%"+condition+"%' or name like '%"+condition+"%' or rolename like '%"+condition+"%' or hid like '%"+condition+"%' or departmentname like '%"+condition+"%'";
		Query q = getSession().createQuery(sql);
		List<Long> l = (List<Long>) q.list();
		long count = l.get(0);
		return count;
	}

	@SuppressWarnings("rawtypes")
	public User finaUserByName(String username) {
		List ul = getHibernateTemplate().find(
				"from User au where au.username = ?", username);
		if (ul != null && ul.size() == 1) {
			return (User) ul.get(0);
		} else if (ul != null && ul.size() > 1) {
			System.out
					.println("more than 1 user have same username and password!!");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<User> findAll() {
		List<User> list = (List<User>) getHibernateTemplate().find(
				"from User order by username");
		return list;
	}

	public List<User> findByPage(int PAGESIZE, int pageIndex) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getUserCount();
			if (b > count)
				b = count;

			return findAll().subList(a, b);
		} else {
			return findAll().subList(0, PAGESIZE);
		}

	}

	public List<User> findByCondition(int PAGESIZE, int pageIndex,
			String condition) {
		if (pageIndex > 0) {
			int a = PAGESIZE * (pageIndex - 1);
			int b = PAGESIZE * pageIndex;
			int count = (int) getUserCountByCondition(condition);
			if (b > count)
				b = count;

			return findByCondition(condition).subList(a, b);
		} else {
			return findByCondition(condition).subList(0, PAGESIZE);
		}

	}

	@SuppressWarnings({ "unchecked" })
	public List<User> findByCondition(String condition) {
		List<User> ul = getHibernateTemplate().find("from User au where username like '%"+condition+"%' or rolename like '%"+condition+"%' or name like '%"+condition+"%' or hid like '%"+condition+"%' or departmentname like '%"+condition+"%'");
		return ul;
	}

	@SuppressWarnings("rawtypes")
	public User findUserByNameAndPass(String username, String password) {
		List ul = getHibernateTemplate().find(
				"from User au where au.username = ? and au.password = ?",
				new String[] { username, password });
		if (ul != null && ul.size() == 1) {
			return (User) ul.get(0);
		} else if (ul != null && ul.size() > 1) {
			System.out
					.println("more than 1 user have same username and password!!");
		}
		return null;

	}

	public List<User> findByDepartments(Set<Department> dpmts) {
		String s = "";
		for(Department d:dpmts){
			s=s+"'" + d.getDepartmentname()+"',";
		}
		s=s.substring(0, s.length()-1);
		@SuppressWarnings("unchecked")
		List<User> ul = getHibernateTemplate().find("from User au where departmentname in ("+s+")");
		return ul;
	}

	public List<User> findByDepartments(ArrayList<String> departmentNames) {
		String s = "";
		for(String departmentName:departmentNames){
			s=s+"'" + departmentName+"',";
		}
		s=s.substring(0, s.length()-1);
		logger.info("from User au where departmentname in ("+s+")");
		@SuppressWarnings("unchecked")
		List<User> ul = getHibernateTemplate().find("from User au where departmentname in ("+s+")");
		
		return ul;
	}
	
}
