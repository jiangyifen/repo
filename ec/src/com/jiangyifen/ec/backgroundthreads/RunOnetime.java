package com.jiangyifen.ec.backgroundthreads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.ShareData;

public class RunOnetime extends Thread {


	private final Log logger = LogFactory.getLog(getClass());

	public RunOnetime() {

		this.setDaemon(true);
		this.setName("RunOnetime");
		this.start();
	}

	private void loadDataExtenAndOutline() {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			String url = Config.props.getProperty("pg_url");
			String db_username = Config.props.getProperty("pg_username");
			String db_password = Config.props.getProperty("pg_password");

			Class.forName("org.postgresql.Driver");

			con = DriverManager.getConnection(url, db_username, db_password);

			String sql = "select exten,outline from ec_exten_and_outline";
			
			//分机外线对应关系			
			statement = con.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {
				String exten = rs.getString(1);
				String outline = rs.getString(2);
				while(ShareData.extenAndOutline==null){
					sleep(500);
				}
				ShareData.extenAndOutline.put(exten, outline);
				logger.info("put " + exten+":"+outline);
			}
			logger.info(ShareData.extenAndOutline.size());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	public void run() {
		
			try {
				
				loadDataExtenAndOutline();
				logger.info(ShareData.extenAndOutline.size());
				for(String key:ShareData.extenAndOutline.keySet()){
					logger.info(this.getClass() + " " + key + ":" + ShareData.extenAndOutline.get(key));
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}


}
