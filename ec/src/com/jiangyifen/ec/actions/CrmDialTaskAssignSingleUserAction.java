package com.jiangyifen.ec.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.Const;

public class CrmDialTaskAssignSingleUserAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4847002573896228572L;

	private String username;

	private String errorMsg;

	public String execute() throws Exception {
		assign(username);
		return SUCCESS;

	}

	private void assign(String username) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			User user = userManager.getUser(username);
			if(user==null){
				return;
			}
			Set<Queue> queues = user.getQueues();
			String queuesString = "";
			for (Queue queue : queues) {
				String queueName = queue.getName();
				queuesString = queuesString + "'" + queueName + "',";
			}
			queuesString = queuesString.substring(0, queuesString.length() - 1);

			String db_url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(db_url, db_username, db_password);

			// 如果该用户名下已经有足够的dialtaskitem，则不再进行分配
			String sql = "select count(*) from ec_dial_task_item where owner='"
					+ username + "' and status='" + DialTaskItem.STATUS_MANUAL
					+ "'";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			if (count > Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX) {
				return;
			}

			// 分配
			sql = "select id from ec_dial_task where queuename in ("
					+ queuesString + ") order by perority;";
			logger.info(sql);
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();
			List<DialTaskItem> dialTaskItems = null;
			while (rs.next()) {
				long dialtaskId = rs.getInt(1);
				List<DialTaskItem> tempDialTaskItems = dialTaskItemManager
						.findByTaskIdAndStatus(
								Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX, 1,
								dialtaskId, DialTaskItem.STATUS_READY);
				int couldBeAssignedCount = tempDialTaskItems.size();
				if (couldBeAssignedCount > 0) {
					dialTaskItems = tempDialTaskItems;
					break;
				}
			}
			if (dialTaskItems == null) {
				return;
			} else {
				for (DialTaskItem dti : dialTaskItems) {
					dti.setOwner(username);
					dti.setStatus(DialTaskItem.STATUS_MANUAL);
					dialTaskItemManager.update(dti);
				}
			}

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

	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
