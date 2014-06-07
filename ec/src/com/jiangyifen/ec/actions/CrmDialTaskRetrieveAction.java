package com.jiangyifen.ec.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.util.Config;

public class CrmDialTaskRetrieveAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6894134993995378805L;

	private Long dialTaskId;

	private String errorMsg;

	public String execute() throws Exception {

		retrieve(dialTaskId);

		logger.info("CrmDialTaskRetrieveAction： retrieve over");

		return SUCCESS;

	}

	private void retrieve(long dialTaskId) {

		// 获取每个user当前已被分派了多少个dialtask，并按此排序
		Connection con = null;
		PreparedStatement statement = null;

		try {

			String db_url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(db_url, db_username, db_password);

			String sql = "update ec_dial_task_item set OWNER=null , status='"
					+ DialTaskItem.STATUS_READY + "' where status='"
					+ DialTaskItem.STATUS_MANUAL + "' and taskid='"
					+ dialTaskId + "';";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			statement.executeUpdate();
			
			sql = "update ec_dial_task set hasassignedtoday=false where taskid='" + dialTaskId + "';";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			statement.executeUpdate();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}

	}

	public void setDialTaskId(Long dialTaskId) {
		this.dialTaskId = dialTaskId;
	}

	public Long getDialTaskId() {
		return dialTaskId;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
