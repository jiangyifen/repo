package com.jiangyifen.ec.backgroundthreads;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.asteriskjava.manager.event.CdrEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.Cdr;
import com.jiangyifen.ec.dao.User;
import com.jiangyifen.ec.manager.MyManager;
import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.ShareData;

public class CdrUpdateThread extends Thread {

	private static final int period = 1000; // 5 seconds

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String recUrlPerfix = Config.props
			.getProperty("rec_url_perfix");
	
	private final String crmCdrNotify = Config.props
			.getProperty("crm_cdr_notify");
	
	private final String crmServerUrl = Config.props
			.getProperty("crm_cdr_notify_url");



	public static ConcurrentLinkedQueue<CdrEvent> cdrEvents = new ConcurrentLinkedQueue<CdrEvent>();


	
	public CdrUpdateThread() {

		this.setDaemon(true);
		this.setName("CdrUpdateThread" + System.currentTimeMillis());
		this.start();

	}

	private void doNotify(Cdr cdr) {
		
		
		
		if (crmCdrNotify.equalsIgnoreCase("true")) {
			try {
				String urlString = crmServerUrl + "?";

				String type = "2";
				String calldate = cdr.getCalldate().getTime() + "";
				String dcontext = cdr.getDcontext();
				String disposition = cdr.getDisposition();
				String src = cdr.getSrc();
				String dst = cdr.getDst();
				String duration = cdr.getDuration() + "";
				String uniqueid = cdr.getUniqueid();
				String u = cdr.getUrl();
				String dstChannel = cdr.getDstchannel();
				String lastdata = cdr.getLastdata();
				String username = cdr.getUsername();

				disposition = URLEncoder.encode(disposition, "utf-8");

				urlString = urlString + "type=" + type + "&calldate="
						+ calldate + "&dcontext=" + dcontext + "&disposition="
						+ disposition + "&src=" + src + "&dst=" + dst
						+ "&duration=" + duration + "&uniqueid=" + uniqueid
						+ "&url=" + u + "&dstChannel=" + dstChannel
						+ "&lastdata=" + lastdata+ "&username=" + username;

				logger.info("CDR Notify: " + cdr.toString());
				logger.info(urlString);
				
				URL url = new URL(urlString);
				url.openStream();
			} catch (MalformedURLException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void doUpdate(CdrEvent event) {
		
		
		
		try {
			// 根据分机号从sip_conf中获取此时登录该分机的username
			String sipName = null;
			if (sipName == null) {
				if(ShareData.allSipName.contains(event.getSrc())){
					sipName = event.getSrc();
				}
			}
			if (sipName == null) {
				if(ShareData.allSipName.contains(event.getDestination())){
					sipName = event.getDestination();
				}
			}
			if (sipName == null) {
				String userfield = event.getUserField();
				if (userfield != null && userfield.split(",").length > 1) {
					if(ShareData.allSipName.contains(event.getUserField()
							.split(",")[1])){
						sipName = event.getUserField()
								.split(",")[1];
					}
				}
				// logger.info("CDR UPDATE: SIP USERFIELD="+s);
			}

			
			
			
			
			String username = null;
			String name = null;
			String hid = null;
			String dpmt = "";
			if (sipName != null) {
//				
				username = ShareData.sipAndLoginusername.get(sipName);
				if(username==null){
					username="0";
				}
				// logger.info("CDR UPDATE: username="+username);
			}

			if (username == null) {
				username = "0";
				name = "0";
				hid = "0";
				dpmt = "UNKNOW";
				// logger.info("CDR UPDATE: username="+username);
			} else {
				User user = MyManager.userManager.getUser(username);
				if (user != null) {
					name = user.getName();
					hid = user.getHid();
					if (name == null || name.equals("")) {
						name = "0";
					}
					if (hid == null || name.equals("")) {
						hid = "0";
					}
					if (user.getDepartment() == null) {
						dpmt = "UNKNOW";
					} else {
						dpmt = user.getDepartment().getDepartmentname();
					}
					// logger.info("CDR UPDATE: name="+name);
				}
			}

			
			
			// 录音文件url
			String url = "";
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String d = df.format(event.getStartTimeAsDate());
			url = recUrlPerfix + d + "/" + d + "-" + event.getUniqueId()
					+ ".wav";

			// 根据uniqueid来update这条cdr的agent和dst字段
			// cdr表中的agent字段是51value需求中的员工工号，对应ec_user中的用户名

			// 如果是呼入队列的情况，dst字段会是dialplan匹配数字而不是分机号。
			// 此时就将userfield中记录的分机号拿过来替换原来的dst字段的内容
			String userfield = event.getUserField();
			String[] userfields = null;
			if (userfield != null) {
				userfields = userfield.split(",");
			}
			String dst = event.getDestination();
			String fenji = "";

			if (userfields != null && userfields.length > 1) {
				fenji = userfields[1];
			} else if (ShareData.allSipName.contains(dst)) {
				fenji = dst;
			}

			if (userfields != null && userfields.length > 1) {

				if (dst.length() > 3 && dst.substring(0, 3).equals("999")) {
					dst = userfields[1];
				}

				if (dst.length() == 1) {
					dst = userfields[1];
				}

			}

			String disposition = event.getDisposition();
			if (disposition.equals("ANSWERED")) {
				disposition = "ANSWER";
			}
			if (userfields != null && userfields.length >= 1) {
				if (userfields[0].equals("")) {
					disposition = "TRANSFER";
				} else {
					disposition = userfields[0];
				}
			}

			// 更新dcontext
			String dcontext = event.getDestinationContext();
			if (dcontext != null && dcontext.contains("ivr")) {
				dcontext = "incoming";
			}

			
			
			// 等1秒，确保CDR已经插入数据库
			Cdr cdr = null;
			for (int i = 0; i < 30; i++) {
				cdr = MyManager.cdrManager
						.getCdrByUniqueId(event.getUniqueId());
				
				if (cdr == null) {
					Thread.sleep(200);
					logger.info("CDR UPDATE:  "+this.getName()+ ": sleep 200 "+i);
				} else {
					// 更新数据库中的CDR记录
					// MyManager.cdrManager.rewriteCDR(event.getUniqueId(),
					// username,
					// name, hid, dpmt, dst, fenji, disposition, url, dcontext);

					cdr.setUsername(username);
					cdr.setName(name);
					cdr.setHid(hid);
					cdr.setDst(dst);
					cdr.setDpmt(dpmt);
					cdr.setFenji(fenji);
					cdr.setDisposition(disposition);
					cdr.setUrl(url);
					cdr.setDcontext(dcontext);
					
					MyManager.cdrManager.updateCdr(cdr);

					logger.info("CDR UPDATE:  "+this.getName()+" username=" + username + " name="
							+ name + " hid=" + hid + " dst=" + dst + " dpmt="
							+ dpmt + " fenji=" + fenji + " disposition="
							+ disposition + " url=" + url + " dcontext="
							+ dcontext);
					
					doNotify(cdr);
					
					break;
				}
			}


			
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

//	private void doUpdate(CdrEvent event) {
//
//		try {
//			// 根据分机号从sip_conf中获取此时登录该分机的username
//			Sip s = null;
//			if (s == null) {
//				s = MyManager.sipManager.findSipByName(event.getSrc());
//
//				// logger.info("CDR UPDATE: SIP SRC="+s);
//			}
//			if (s == null) {
//				s = MyManager.sipManager.findSipByName(event.getDestination());
//				// logger.info("CDR UPDATE: SIP DST="+s);
//			}
//			if (s == null) {
//				String userfield = event.getUserField();
//				if (userfield != null && userfield.split(",").length > 1) {
//					s = MyManager.sipManager.findSipByName(event.getUserField()
//							.split(",")[1]);
//				}
//				// logger.info("CDR UPDATE: SIP USERFIELD="+s);
//			}
//			// logger.info("CDR event get sip =" + s);
//			String username = null;
//			String name = null;
//			String hid = null;
//			String dpmt = "";
//			if (s != null) {
//				username = s.getLoginusername();
//				// logger.info("CDR UPDATE: username="+username);
//			}
//
//			if (username == null) {
//				username = "0";
//				name = "0";
//				hid = "0";
//				dpmt = "UNKNOW";
//				// logger.info("CDR UPDATE: username="+username);
//			} else {
//				User user = MyManager.userManager.getUser(username);
//				if (user != null) {
//					name = user.getName();
//					hid = user.getHid();
//					if (name == null || name.equals("")) {
//						name = "0";
//					}
//					if (hid == null || name.equals("")) {
//						hid = "0";
//					}
//					if (user.getDepartment() == null) {
//						dpmt = "UNKNOW";
//					} else {
//						dpmt = user.getDepartment().getDepartmentname();
//					}
//					// logger.info("CDR UPDATE: name="+name);
//				}
//			}
//
//			// 录音文件url
//			String url = "";
//			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
//			String d = df.format(event.getStartTimeAsDate());
//			url = recUrlPerfix + d + "/" + d + "-" + event.getUniqueId()
//					+ ".wav";
//
//			// 根据uniqueid来update这条cdr的agent和dst字段
//			// cdr表中的agent字段是51value需求中的员工工号，对应ec_user中的用户名
//
//			// 如果是呼入队列的情况，dst字段会是dialplan匹配数字而不是分机号。
//			// 此时就将userfield中记录的分机号拿过来替换原来的dst字段的内容
//			String userfield = event.getUserField();
//			String[] userfields = null;
//			if (userfield != null) {
//				userfields = userfield.split(",");
//			}
//			String dst = event.getDestination();
//			String fenji = "";
//
//			if (userfields != null && userfields.length > 1) {
//				fenji = userfields[1];
//			} else if (ShareData.allSipName.contains(dst)) {
//				fenji = dst;
//			}
//
//			if (userfields != null && userfields.length > 1) {
//
//				if (dst.length() > 3 && dst.substring(0, 3).equals("999")) {
//					dst = userfields[1];
//				}
//
//				if (dst.length() == 1) {
//					dst = userfields[1];
//				}
//
//			}
//
//			String disposition = event.getDisposition();
//			if (disposition.equals("ANSWERED")) {
//				disposition = "ANSWER";
//			}
//			if (userfields != null && userfields.length >= 1) {
//				if (userfields[0].equals("")) {
//					disposition = "TRANSFER";
//				} else {
//					disposition = userfields[0];
//				}
//			}
//
//			// 更新dcontext
//			String dcontext = event.getDestinationContext();
//			if (dcontext != null && dcontext.contains("ivr")) {
//				dcontext = "incoming";
//			}
//
//			// 等1秒，确保CDR已经插入数据库
//			Cdr cdr = null;
//			for (int i = 0; i < 3; i++) {
//				cdr = MyManager.cdrManager
//						.getCdrByUniqueId(event.getUniqueId());
//				if (cdr == null) {
//					Thread.sleep(1000);
//				} else {
//					// 更新数据库中的CDR记录
//					// MyManager.cdrManager.rewriteCDR(event.getUniqueId(),
//					// username,
//					// name, hid, dpmt, dst, fenji, disposition, url, dcontext);
//
//					cdr.setUsername(username);
//					cdr.setName(name);
//					cdr.setHid(hid);
//					cdr.setDst(dst);
//					cdr.setDpmt(dpmt);
//					cdr.setFenji(fenji);
//					cdr.setDisposition(disposition);
//					cdr.setUrl(url);
//					cdr.setDcontext(dcontext);
//
//					MyManager.cdrManager.updateCdr(cdr);
//
//					logger.info("CDR UPDATE: username=" + username + " name="
//							+ name + " hid=" + hid + " dst=" + dst + " dpmt="
//							+ dpmt + " fenji=" + fenji + " disposition="
//							+ disposition + " url=" + url + " dcontext="
//							+ dcontext);
//
//					doNotify(cdr);
//
//					break;
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
	
	public void run() {

		while (true) {
			try {

				CdrEvent event = cdrEvents.poll();
				if (event != null) {
					doUpdate(event);
					logger.info("CDR UPDATE: "+this.getName()+" cdrEvents.size()="+cdrEvents.size());
				} else {
					sleep(period);
//					logger.info("CDR UPDATE: sleep"+period);
				}

			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}


}
