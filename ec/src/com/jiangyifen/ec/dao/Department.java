package com.jiangyifen.ec.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class Department implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7947770861312607096L;

	private static final Log logger = LogFactory.getLog(Department.class);

	private String departmentname;
	private String description;
	private Set<Role> roles = new HashSet<Role>();

	private Department parent;
	private Set<Department> children;
	private int level = 0;

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Department getParent() {
		return parent;
	}

	public void setChildren(Set<Department> children) {
		this.children = children;
	}

	public Set<Department> getChildren() {
		return children;
	}

	public static void getAllChildren(Department dpmt,
			Set<Department> allChildren) {

		if (dpmt == null) {
			logger.error("dpmt is null");
			return;
		}
		if (allChildren == null) {
			logger.error("allChildren is null");
			return;
		}

		Set<Department> children = dpmt.getChildren();

		if (children != null && children.size() > 0) {
			for (Department child : children) {
				if (child.getDepartmentname().equals("root")) {
					continue;
				}
				allChildren.add(child);
				getAllChildren(child, allChildren);
			}
		} else {
			return;
		}

	}

	public static void getAllChildrenTree(Department dpmt,
			ArrayList<ArrayList<Department>> dpmtTree, int level) {

		if (dpmt == null) {
			logger.error("dpmt is null");
			return;
		}
		if (dpmtTree == null) {
			logger.error("dpmtTree is null");
			return;
		}

		Set<Department> children = dpmt.getChildren();

		if (children != null && children.size() > 0) {

			Set<Department> chilerenWithoutRoot = new HashSet<Department>();
			for (Department d : children) {
				if (!d.getDepartmentname().equals("root")) {
					chilerenWithoutRoot.add(d);
				}
			}
			ArrayList<Department> thisLevelChildren = new ArrayList<Department>(
					chilerenWithoutRoot);

			if (dpmtTree.size() == level + 1) {
				dpmtTree.get(level).addAll(thisLevelChildren);
			} else {
				dpmtTree.add(level, thisLevelChildren);
			}

			for (Department d : thisLevelChildren) {
				d.setLevel(level);
			}

			for (Department d : thisLevelChildren) {
				getAllChildrenTree(d, dpmtTree, d.getLevel() + 1);
			}

		}
		return;

	}
	
	public static void fillupChildrenTree(ArrayList<ArrayList<Department>> dpmtTree){
		for(int i=0;i<dpmtTree.size()-1;i++){
			for(Department d: dpmtTree.get(i)){
				Set<Department> children = d.getChildren();
				if(children!=null &&children.size()>0){
					continue;
				}else{
					dpmtTree.get(i+1).add(d);
				}
			}
		}
	}

	public boolean isRoot() {
		return departmentname.equals("root");
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

}
