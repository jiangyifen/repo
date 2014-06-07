package com.jiangyifen.ec.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.Role;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.ShareData;

public class DepartmentDeleteAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3580571201916734564L;
	private String[] u;

	private ArrayList<String> errStrings = new ArrayList<String>();

	public String execute() throws Exception {
		if (u != null) {
			boolean err = false;
			for (String name : u) {
				Department d = departmentManager.getDepartment(name);
				
				List<User> userList = ShareData.allUsers;
				for(User user:userList){
					String dpmtName = user.getDepartment().getDepartmentname();
					if(dpmtName.equals(name)){
						err = true;
						errStrings.add("部门 " + name + "["+d.getDescription()+"] 被用户 "+user.getUsername()+" 引用: ");
					}
				}
				errStrings.add("请先在以上用户中取消对该部门的引用。 ");
				
				Set<Role> roles = d.getRoles();
				if (roles != null && roles.size() > 0) {
					err = true;
					errStrings.add("部门 " + name + "["+d.getDescription()+"] 被以下角色引用: ");
					for (Role r : roles) {
						errStrings.add("Role " + r.getRolename());
					}
					errStrings.add("请先在以上角色中取消对该部门的引用。 ");
				}
				
				Set<Department> children = d.getChildren();
				if(children!=null && children.size()>0){
					err = true;
					errStrings.add("部门 " + name + "["+d.getDescription()+"] 下还有子部门: ");
					for (Department dpmt : children) {
						errStrings.add(dpmt.getDepartmentname() + "["+dpmt.getDescription()+"]");
					}
					errStrings.add("请先删除这些子部门。");
				}
			}
			for (String s : errStrings) {
				logger.error(s);
			}

			if (err) {
				return "reference";
			} else {
				departmentManager.deleteDepartments(u);
				return SUCCESS;
			}
		} else {
			return INPUT;
		}
	}

	public void setU(String[] u) {
		this.u = u;
	}

	public String[] getU() {
		return u;
	}

	public void setErrStrings(ArrayList<String> errStrings) {
		this.errStrings = errStrings;
	}

	public ArrayList<String> getErrStrings() {
		return errStrings;
	}

}
