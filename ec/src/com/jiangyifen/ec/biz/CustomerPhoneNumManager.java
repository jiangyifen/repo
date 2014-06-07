package com.jiangyifen.ec.biz;

import com.jiangyifen.ec.dao.CustomerPhoneNum;

public interface CustomerPhoneNumManager {

	CustomerPhoneNum get(String customerPhoneNumber) throws Exception;

}
