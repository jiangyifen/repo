package com.jiangyifen.ec.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import com.jiangyifen.ec.dao.DialTask;
import com.jiangyifen.ec.dao.DialTaskItem;
import com.jiangyifen.ec.dao.Queue;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.Const;

public class CrmDialTaskAssignAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6249245262095542945L;

	private Long dialTaskId;

	private String errorMsg;

	public String execute() throws Exception {

		//执行分派
		logger.info("CrmDialTaskAssignAction: ==== assign begin[has sm reply] ====");
		assignSmReply(dialTaskId);
		logger.info("CrmDialTaskAssignAction: ==== assign begin ====");
		assign(dialTaskId);
		logger.info("CrmDialTaskAssignAction: ==== assign over ====");
		return SUCCESS;

	}

	private String assignSmReply(long dialTaskId) {

		// 获取每个user当前已被分派了多少个dialtask，并按此排序
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			
			// 根据dialtaskid加载dialtask，并获取该dialtask对应的queueName,根据queueName加载queue对象，并getUsers()
			DialTask dialTask = dialTaskManager.get(dialTaskId);
			if (dialTask == null) {
				setErrorMsg("CrmDialTaskAssignAction: dialTask == " + dialTask);
				return INPUT;
			}
			String queueName = dialTask.getQueueName();

			if (queueName == null) {
				setErrorMsg("CrmDialTaskAssignAction: queueName == " + queueName);
				return INPUT;
			}
			Queue queue = queueManager.getQueue(queueName);
			if(queue==null){
				setErrorMsg("CrmDialTaskAssignAction: queue == null ");
				return INPUT;
			}
			Set<User> users = queue.getUsers();
			
			if(users==null){
				return SUCCESS;
			}

			String db_url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(db_url, db_username, db_password);

			//初始化treemap和备sql.ConcurrentSkipListMap是线程安全的treemap
			ConcurrentSkipListMap<String , Integer> userAndDialTaskItemCount= new ConcurrentSkipListMap<String, Integer>();
			String usernames = "";
			for (User user : users) {
				String username = user.getUsername();
				userAndDialTaskItemCount.put(username, 0);
				usernames = usernames + "'" + username + "',";
			}
			usernames = usernames.substring(0, usernames.length() - 1);

			String sql = "select owner, count(*) as c from ec_dial_task_item where owner in ("
					+ usernames + ") and status='"+DialTaskItem.STATUS_MANUAL+"' group by owner order by c";
//			logger.info("CrmDialTaskAssignAction: "+sql);
			statement = con.prepareStatement(sql);

			
			//推出无限循环的标记
			boolean shouldBreak = false;
			
			//这里用无限循环。为什么不用while(true)，是因为需要知道循环的次数，配合Const.CRM_MANUAL_DIAL_TASK_STEP算出targetCount
			for(int i=0;i<Integer.MAX_VALUE;i++){
//				Thread.sleep(1000);
				logger.info("CrmDialTaskAssignAction: ========= i="+i + " ==========");
				logger.info("CrmDialTaskAssignAction:"+dialTask.getTaskName());
				
				//更新一下每个user已分派的任务数，并将数量保存到一个treemap中
				//这个treemap是经过排序的（sql中的order by实现）				
				rs = statement.executeQuery();
				
				while (rs.next()) {
					//userAndDialTaskItemCount.put(用户名,已分派任务数量);
					userAndDialTaskItemCount.put(rs.getString("owner"),rs.getInt("c"));
				}
				
				//将已经分派到最大值的用户移出treemap
				for(String username:userAndDialTaskItemCount.keySet()){
					int assignedDialTaskItemCount = userAndDialTaskItemCount.get(username);
//					logger.info("CrmDialTaskAssignAction: username="+username+","+"assignedDialTaskItemCount="+assignedDialTaskItemCount);
					if(assignedDialTaskItemCount>=Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX){
						userAndDialTaskItemCount.remove(username);
						logger.info("CrmDialTaskAssignAction: remove "+username+":"+assignedDialTaskItemCount);
						if(userAndDialTaskItemCount.keySet()==null){
							shouldBreak = true;
							break;
						}
					}
				}
	
				
				//如果treemap里没有需要分派任务的用户，说明都达到Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX个了。这样就结束。
				if(userAndDialTaskItemCount.size()==0){
					break;
				}
	
				//将用户顺序打乱
				ArrayList<String> usernameArray = new ArrayList<String>();
				ArrayList<String> tempUsernameArray = new ArrayList<String>();
				for(String username:userAndDialTaskItemCount.keySet()){
					tempUsernameArray.add(username);
				}
				int loopTimes = tempUsernameArray.size();
				for(int size=0;size<loopTimes;size++){
					int index = (int)Math.random()*tempUsernameArray.size();
					String username = tempUsernameArray.get(index);
					tempUsernameArray.remove(index);
					usernameArray.add(username);
				}
				
				// 随机分，每轮分1个。
				// 当有短信回复的记录分完就停止分派。
				for(String username:usernameArray){

					//算一下该user当前已经分派了多少个dialtaskitem
					int assignedDialTaskItemCount = userAndDialTaskItemCount.get(username);
					
					//如果该用户当前的dialTaskItem数量未达到最大值就开始分派
					if (assignedDialTaskItemCount < Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX) {

						// 取一批hasSmReply 是true的 dialtaskitem出来
						List<DialTaskItem> dialTaskItems = dialTaskItemManager
								.findByTaskIdAndStatusHasSmReply(Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX, 1, dialTaskId,
										DialTaskItem.STATUS_READY);

						if (dialTaskItems.size() == 0) {
							shouldBreak = true;
							break;
						} else {
							DialTaskItem dialTaskItem = dialTaskItems.get(0);
							dialTaskItem.setOwner(username);
							dialTaskItem.setStatus(DialTaskItem.STATUS_MANUAL);
							dialTaskItemManager.update(dialTaskItem);
							logger.info("CrmDialTaskAssignAction: assigning:username="+username+" assignedDialTaskItemCount="+assignedDialTaskItemCount);
						}

					}else{
						//如果该用户当前的dialTaskItem数量已达到最大值就不分派
						continue;
					}

				}
				
				if(shouldBreak)
					break;
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

		return SUCCESS;

	}
	
	private String assign(long dialTaskId) {
		

		// 获取每个user当前已被分派了多少个dialtask，并按此排序
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			
			// 根据dialtaskid加载dialtask，并获取该dialtask对应的queueName,根据queueName加载queue对象，并getUsers()
			DialTask dialTask = dialTaskManager.get(dialTaskId);
			if (dialTask == null) {
				setErrorMsg("CrmDialTaskAssignAction: dialTask == " + dialTask);
				return INPUT;
			}
			String queueName = dialTask.getQueueName();

			if (queueName == null) {
				setErrorMsg("CrmDialTaskAssignAction: queueName == " + queueName);
				return INPUT;
			}
			Queue queue = queueManager.getQueue(queueName);
			Set<User> users = queue.getUsers();
			
			if(users==null){
				return SUCCESS;
			}

			String db_url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(db_url, db_username, db_password);

			//初始化treemap和备sql.ConcurrentSkipListMap是线程安全的treemap
			ConcurrentSkipListMap<String , Integer> userAndDialTaskItemCount= new ConcurrentSkipListMap<String, Integer>();
			String usernames = "";
			for (User user : users) {
				String username = user.getUsername();
				userAndDialTaskItemCount.put(username, 0);
				usernames = usernames + "'" + username + "',";
			}
			usernames = usernames.substring(0, usernames.length() - 1);

			String sql = "select owner, count(*) as c from ec_dial_task_item where owner in ("
					+ usernames + ") and status='"+DialTaskItem.STATUS_MANUAL+"' group by owner order by c";
			logger.info("CrmDialTaskAssignAction: "+sql);
			statement = con.prepareStatement(sql);

			
			//推出无限循环的标记
			boolean shouldBreak = false;
			
			//这里用无限循环。为什么不用while(true)，是因为需要知道循环的次数，配合Const.CRM_MANUAL_DIAL_TASK_STEP算出targetCount
			for(int i=0;i<Integer.MAX_VALUE;i++){
//				Thread.sleep(1000);
				logger.info("CrmDialTaskAssignAction: ========= i="+i + " ==========");
				logger.info("CrmDialTaskAssignAction:"+dialTask.getTaskName());
				
				//更新一下每个user已分派的任务数，并将数量保存到一个treemap中
				//这个treemap是经过排序的（sql中的order by实现）				
				rs = statement.executeQuery();
				
				while (rs.next()) {
					//userAndDialTaskItemCount.put(用户名,已分派任务数量);
					userAndDialTaskItemCount.put(rs.getString("owner"),rs.getInt("c"));
				}
				
				//将已经分派到最大值的用户移出treemap
				for(String username:userAndDialTaskItemCount.keySet()){
					int assignedDialTaskItemCount = userAndDialTaskItemCount.get(username);
//					logger.info("CrmDialTaskAssignAction: username="+username+","+"assignedDialTaskItemCount="+assignedDialTaskItemCount);
					if(assignedDialTaskItemCount>=Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX){
						userAndDialTaskItemCount.remove(username);
						logger.info("CrmDialTaskAssignAction: remove username="+username+","+"assignedDialTaskItemCount="+assignedDialTaskItemCount);
						if(userAndDialTaskItemCount.keySet()==null){
							shouldBreak = true;
							break;
						}
					}
				}
	
				
				//如果treemap里没有需要分派任务的用户，说明都达到Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX个了。这样就结束。
				if(userAndDialTaskItemCount.size()==0){
					break;
				}
				
				//确定此次分派目标个数，并保证目标数不大于最大值
				int targetCount = (i+1)*Const.CRM_MANUAL_DIAL_TASK_STEP;
				logger.info("CrmDialTaskAssignAction: ------------  targetCount=" + targetCount + " ------------");
				if(targetCount>Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX){
					targetCount = Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX;
				}
				
				//将 dialTaskId 对应的dialtask打上“当日分配”的标记
				dialTask.setHasAssignedToday(true);
				dialTaskManager.update(dialTask);
				
				//将dialTaskId对应的dialTask优先级调到0
				dialTaskManager.setPerorityToZero(dialTask.getId());
				
				// “砌墙法”分：从剩下最少的那个开始分，每轮分到targetCount个。
				// 当把该dialtask的dialtaskitem分完了或者全部分到Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX个，就停止分派。
				for(String username:userAndDialTaskItemCount.keySet()){

					//算一下该user当前已经分派了多少个dialtaskitem
					int assignedDialTaskItemCount = userAndDialTaskItemCount.get(username);
					
					//如果该用户当前的dialTaskItem数量未达到最大值就开始分派
					if (assignedDialTaskItemCount < Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX) {
						int shouldAssignCount = targetCount
								- assignedDialTaskItemCount;
						if (shouldAssignCount < 0) {
							shouldAssignCount = 0;
						}
						logger.info("CrmDialTaskAssignAction: assigning: username="+username+" assignedDialTaskItemCount="+assignedDialTaskItemCount+" shouldAssignCount="+shouldAssignCount);

						// 取一批dialtaskitem出来
						List<DialTaskItem> dialTaskItems = dialTaskItemManager
								.findByTaskIdAndStatus(Const.CRM_MANUAL_DIAL_TASK_COUNT_MAX, 1, dialTaskId,
										DialTaskItem.STATUS_READY);
						int couldBeAssignedCount = dialTaskItems.size();
						
						int times;
						if (couldBeAssignedCount > shouldAssignCount) {
							// 如果未分配的dialTaskItem还有很多
							times = shouldAssignCount;
						} else {
							// 如果dialTaskItem分完了
							logger.info("CrmDialTaskAssignAction: Not enough dialTaskItem.");
							times = couldBeAssignedCount;
							shouldBreak = true;
						}

						for (int j = 0; j < times; j++) {
							DialTaskItem dialTaskItem = dialTaskItems.get(j);
							dialTaskItem.setOwner(username);
							dialTaskItem.setStatus(DialTaskItem.STATUS_MANUAL);
							dialTaskItemManager.update(dialTaskItem);
						}

						if (shouldBreak)
							break;
					}else{
						//如果该用户当前的dialTaskItem数量已达到最大值就不分派
						continue;
					}
				}
				
				if(shouldBreak)
					break;
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

		return SUCCESS;

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
