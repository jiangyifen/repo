package com.jiangyifen.ec.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.jiangyifen.ec.beans.BatchSmTaskStatus;
import com.jiangyifen.ec.util.Config;

public class BatchSmTaskStatusGetAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598548137596000189L;

	private int pageIndex = 1;
	private int pagesize = 25;
	private int maxPageIndex = 0;
	private List<Integer> pages = new ArrayList<Integer>();
	private int total = -1;

	private HashMap<String, BatchSmTaskStatus> batchSmTaskStatusMap = new HashMap<String, BatchSmTaskStatus>();
	private ArrayList<BatchSmTaskStatus> batchSmTaskStatusList = new ArrayList<BatchSmTaskStatus>();

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			String db_url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(db_url, db_username, db_password);

			// 总数
			String sql = "select batchNumber, count(*) from ec_sm_task group by batchNumber";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String batchNumber = rs.getString(1);
				Long totalCount = rs.getLong(2);
				if (batchNumber != null && !batchNumber.equals("")) {
					BatchSmTaskStatus batchSmTaskStatus = new BatchSmTaskStatus();
					batchSmTaskStatus.setBatchNumber(batchNumber);
					batchSmTaskStatus.setTotalCount(totalCount);

					batchSmTaskStatusMap.put(batchNumber, batchSmTaskStatus);
				}
			}

			// 等待发送数
			sql = "select batchNumber, count(*) from ec_sm_task where status='READY' group by batchNumber";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String batchNumber = rs.getString(1);
				Long readyCount = rs.getLong(2);

				BatchSmTaskStatus batchSmTaskStatus = batchSmTaskStatusMap
						.get(batchNumber);
				if (batchSmTaskStatus != null) {
					batchSmTaskStatus.setReadyCount(readyCount);
				}

			}

			// 正在发送数
			sql = "select batchNumber, count(*) from ec_sm_task where status='WAITING' group by batchNumber";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String batchNumber = rs.getString(1);
				Long waitingCount = rs.getLong(2);

				BatchSmTaskStatus batchSmTaskStatus = batchSmTaskStatusMap
						.get(batchNumber);
				if (batchSmTaskStatus != null) {
					batchSmTaskStatus.setWaitingCount(waitingCount);
				}
			}

			// 发送成功数
			sql = "select batchNumber, count(*) from ec_sm_task where status='DELIVRD' or status='0' group by batchNumber";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String batchNumber = rs.getString(1);
				Long delivrdCount = rs.getLong(2);

				BatchSmTaskStatus batchSmTaskStatus = batchSmTaskStatusMap
						.get(batchNumber);
				if (batchSmTaskStatus != null) {
					batchSmTaskStatus.setDelivrdCount(delivrdCount);
				}
			}

			// 发送失败数
			sql = "select batchNumber, count(*) from ec_sm_task where status<>'DELIVRD' and status<>'0' and status<>'WAITING' and status<>'READY' group by batchNumber";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			while (rs.next()) {
				String batchNumber = rs.getString(1);
				Long otherCount = rs.getLong(2);

				BatchSmTaskStatus batchSmTaskStatus = batchSmTaskStatusMap
						.get(batchNumber);
				if (batchSmTaskStatus != null) {
					batchSmTaskStatus.setOtherCount(otherCount);
				}
			}

			// 分页显示
			int pageIdx = 0;
			try {
				pageIdx = new Integer(pageIndex);
			} catch (NumberFormatException e) {
				pageIdx = 1;
			}
			if (pageIdx < 1)
				pageIdx = 1;

			total = batchSmTaskStatusMap.size();
			if ((total % pagesize) == 0) {
				maxPageIndex = (total / pagesize);
			} else {
				maxPageIndex = (total / pagesize) + 1;
			}

			for (int i = 0; i < maxPageIndex; i++) {
				pages.add(i + 1);
			}
			if (pageIdx > maxPageIndex)
				pageIdx = maxPageIndex;

			int fromIndex = (pageIdx - 1) * pagesize;
			int toIndex = pageIdx * pagesize;
			if (toIndex > total) {
				toIndex = total;
			}

			ArrayList<BatchSmTaskStatus> temp = new ArrayList<BatchSmTaskStatus>();
			for (BatchSmTaskStatus o : batchSmTaskStatusMap.values()) {
				temp.add(o);
			}

			batchSmTaskStatusList.clear();
			
			if (temp.size() > 0) {
				Collections.sort(temp, new MyComparator());
				for (int i = fromIndex; i < toIndex; i++) {
					batchSmTaskStatusList.add(temp.get(i));
				}
			}
			
			

			// 分页显示

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}

		return SUCCESS;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setMaxPageIndex(int maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public int getMaxPageIndex() {
		return maxPageIndex;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setBatchSmTaskStatusList(
			ArrayList<BatchSmTaskStatus> batchSmTaskStatusList) {
		this.batchSmTaskStatusList = batchSmTaskStatusList;
	}

	public ArrayList<BatchSmTaskStatus> getBatchSmTaskStatusList() {
		return batchSmTaskStatusList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}


@SuppressWarnings("rawtypes")
class MyComparator implements Comparator{
	public int compare(Object o1,Object o2) {
		BatchSmTaskStatus b1 = (BatchSmTaskStatus)o1;
		BatchSmTaskStatus b2 = (BatchSmTaskStatus)o2;
		return -b1.getBatchNumber().compareTo(b2.getBatchNumber());
	}

}