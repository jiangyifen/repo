package com.jiangyifen.ec.biz;

import java.util.List;

import com.jiangyifen.ec.dao.ExtenAndOutline;
import com.jiangyifen.ec.dao.ExtenAndOutlineDao;

public class ExtenAndOutlineManagerImpl implements ExtenAndOutlineManager {

	private ExtenAndOutlineDao extenAndOutlineDao;

	public void setExtenAndOutlineDao(ExtenAndOutlineDao extenAndOutlineDao) {
		this.extenAndOutlineDao = extenAndOutlineDao;
	}

	public void save(ExtenAndOutline o) throws Exception {
		extenAndOutlineDao.save(o);
	}

	public void delete(String exten) throws Exception {
		extenAndOutlineDao.delete(exten);

	}

	public void delete(String[] exten) throws Exception {
		for (String s : exten) {
			delete(s);
		}

	}

	public void update(ExtenAndOutline o) throws Exception {
		extenAndOutlineDao.update(o);
	}

	public ExtenAndOutline get(String exten) throws Exception {
		return extenAndOutlineDao.get(exten);
	}

	public int getCount() throws Exception {
		return (int) extenAndOutlineDao.getExtenAndOutlineCount();
	}

	public List<ExtenAndOutline> getAll() throws Exception {
		return extenAndOutlineDao.findAll();
	}

	public List<ExtenAndOutline> getByPage(int pageSize, int pageIndex)
			throws Exception {
		return extenAndOutlineDao.findByPage(pageSize, pageIndex);
	}
}
