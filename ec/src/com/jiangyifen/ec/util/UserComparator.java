package com.jiangyifen.ec.util;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import java.util.Locale;

import com.jiangyifen.ec.dao.Department;
import com.jiangyifen.ec.dao.User;

public class UserComparator implements Comparator<User> {

	RuleBasedCollator collator = (RuleBasedCollator) Collator
			.getInstance(Locale.CHINA);

	public int compare(User o1, User o2) {
		Department d1 = o1.getDepartment();
		Department d2 = o2.getDepartment();
		String name1 = "";
		if(d1!=null){
			name1 = d1.getDepartmentname();
		}
		String name2 = "";
		if(d2!=null){
			name2 = d2.getDepartmentname();
		}
		if(name1==null){
			name1="";
		}
		if(name2==null){
			name2="";
		}
		return collator.compare(name1, name2);
	}
}