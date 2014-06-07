package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.CustomerSatisfactionInvestigationLog;
import com.jiangyifen.ec.dao.CustomerSatisfactionInvestigationLogDao;

public class CustomerSatisfactionInvestigationLogManagerImpl implements CustomerSatisfactionInvestigationLogManager {
	private CustomerSatisfactionInvestigationLogDao customerSatisfactionInvestigationLogDao;

	public void setCustomerSatisfactionInvestigationLogDao(CustomerSatisfactionInvestigationLogDao customerSatisfactionInvestigationLogDao) {
		this.customerSatisfactionInvestigationLogDao = customerSatisfactionInvestigationLogDao;
	}

	public void save(CustomerSatisfactionInvestigationLog o) {
		customerSatisfactionInvestigationLogDao.save(o);
	}

}
