import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.Notice;
import com.jiangyifen.ec.dao.NoticeItem;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.User;

//import com.jiangyifen.ec.dao.User;

public class HibernateTest {

	Configuration config;
	SessionFactory sessionFactory;

	public HibernateTest() {
		config = new Configuration().configure();
		sessionFactory = config.buildSessionFactory();
	}

	/** */
	/**
	 * 对象持久化测试(Insert方法)
	 */
	public void testInsert() {

		Transaction tran = null;
		Session session = null;
		DialTaskItem dti = null;
		try {

			for (Long i = 1l; i <= 100000; i++) {
				session = sessionFactory.openSession();
				dti = new DialTaskItem();
				dti.setPhoneNumber(i + "");
				dti.setStatus(DialTaskItem.STATUS_READY);
				dti.setTaskid(i);

				tran = session.beginTransaction();
				// 插入
				session.merge(dti);
				session.flush();

				// tran.rollback();
				tran.commit();

				session.close();
			}

		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
					session.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void testDelete() {
		Transaction tran = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();

			Role role = new Role();
			role.setRolename("usettest");

			User u = new User();
			u.setUsername("test");
			u.setPassword("test");
			u.setRole(role);

			tran = session.beginTransaction();
			// 插入user信息
			session.delete(u);

			session.flush();
			tran.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void testDeleteDepartment() {
		Transaction tran = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			// 删除department以及中间表相应记录
			Department d = (Department) session.get(Department.class, "dpmt2");

			Set<Role> roles = d.getRoles();
			for (Role r : roles) {
				System.out.println(r.getRolename());
				r.getDepartments().remove(d);
			}

			tran = session.beginTransaction();
			session.delete(d);
			session.flush();
			tran.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void testDeleteRole() {
		Transaction tran = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			// 删除Role以及中间表相应记录
			Role d = (Role) session.get(Role.class, "r1");
			Set<Department> Departments = d.getDepartments();
			for (Department r : Departments) {
				System.out.println(r.getDepartmentname());
				r.getRoles().remove(d);
			}

			tran = session.beginTransaction();
			session.delete(d);
			session.flush();
			tran.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void testInsertRoleDepartment() {
		Transaction tran = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();

			Role role = new Role();
			role.setRolename("role2");

			Department d1 = new Department();
			d1.setDepartmentname("dpmt1");
			// d1.setDescription("description1");
			Department d2 = new Department();
			d2.setDepartmentname("dpmt2");
			d2.setDescription("description2");

			role.getDepartments().add(d1);
			role.getDepartments().add(d2);

			tran = session.beginTransaction();
			// 插入user信息
			session.saveOrUpdate(role);

			session.flush();
			tran.commit();

			// 删除role以及中间表相应记录
			// session.delete(role);
			// session.flush();
			// tran.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/** */
	/**
	 * 对象读取测试(Select方法)
	 */

	public void testSelect() {
		String hql;
		Session session = null;
		try {

			session = sessionFactory.openSession();

			hql = "from Department where departmentname='销售1部'";

			@SuppressWarnings("unchecked")
			List<Department> l = (List<Department>) session.createQuery(hql)
					.list();
			Department dpmt = l.get(0);

			System.out.println("--------------------------------------");
			Set<Department> allChildren = new HashSet<Department>();
			Department.getAllChildren(dpmt, allChildren);

			System.out.println("-----------------"+allChildren.size());
			for (Department d : allChildren) {
				System.out.println(d.getDepartmentname());
			}

			System.out.println("--------------------------------------");

			ArrayList<ArrayList<Department>> dpmtTree = new ArrayList<ArrayList<Department>>();
			Department.getAllChildrenTree(dpmt, dpmtTree,0);

			for (ArrayList<Department> d : dpmtTree) {
				System.out.println("-----------------"+d.size());
				for (Department dd : d) {
					System.out.println(dd.getDepartmentname() + " " + dd.getLevel());
				}
			}

			System.out.println("--------------------------------------");
//			for(int i=0;i<dpmtTree.size()-1;i++){
//				for(Department d: dpmtTree.get(i)){
//					Set<Department> children = d.getChildren();
//					if(children!=null &&children.size()>0){
//						continue;
//					}else{
//						dpmtTree.get(i+1).add(d);
//					}
//				}
//			}
			Department.fillupChildrenTree(dpmtTree);
			for (ArrayList<Department> d : dpmtTree) {
				System.out.println("-----------------"+d.size());
				for (Department dd : d) {
					System.out.println(dd.getDepartmentname());
				}
			}
			
			
			
			
			
			
			
			
			
			
			
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testBatchDel(){
		Transaction tran = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tran = session.beginTransaction();
			
			session.createQuery( "delete from Notice where id=2" ).executeUpdate();	
			
			session.flush();
			tran.commit();
			session.close(); 
			
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		
	}
	
	public void testInsertNoticeAndNoticeItem() {
		Transaction tran = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();

			Notice notice = new Notice();
			notice.setTitle("t1");
			notice.setContent("ccccccc");
			notice.setDate(new Date());
			
			NoticeItem ni1 = new NoticeItem();
			ni1.setRead(true);
			ni1.setUsername("u1");
			ni1.setNotice(notice);
			
			NoticeItem ni2 = new NoticeItem();
			ni2.setRead(false);
			ni2.setUsername("u2");
			ni2.setNotice(notice);
			
			notice.getNoticeItems().add(ni1);
			notice.getNoticeItems().add(ni2);
			

			tran = session.beginTransaction();
			// 插入user信息
			session.saveOrUpdate(notice);

			session.flush();
			tran.commit();

			// 删除role以及中间表相应记录
			// session.delete(role);
			// session.flush();
			// tran.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public void testSelectNotice() {
		String hql;
		Session session = null;
		try {

			session = sessionFactory.openSession();

			hql = "from Notice where id=1";

			@SuppressWarnings("unchecked")
			List<Notice> l = (List<Notice>) session.createQuery(hql).list();
			Notice notice = l.get(0);
			System.out.println(notice.getTitle());
			for(NoticeItem o:notice.getNoticeItems()){
				System.out.println(o.getNotice().getId() + o.getUsername() + o.isRead());
			}
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testDeleteNotice() {
		Transaction tran = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();

			String hql = "from Notice where id=2";

			@SuppressWarnings("unchecked")
			List<Notice> l = (List<Notice>) session.createQuery(hql).list();
			Notice notice = l.get(0);

			tran = session.beginTransaction();
			session.delete(notice);
			session.flush();
			tran.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tran != null) {
				try {
					tran.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		HibernateTest t = new HibernateTest();
//		t.testInsertNoticeAndNoticeItem();
//		t.testDeleteNotice();
		t.testSelect();

	}
}