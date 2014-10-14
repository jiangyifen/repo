public class ProjectInit {

	public static void printSipExten(int start, int end) {
		for (int i = start; i <= end; i++) {
			System.out.println("[" + i + "](mysip)");
		}
	}

	public static void printSipTrunk(int start, int end) {

		for (int i = start; i <= end; i++) {
			System.out.println("[" + i + "](myTrunk)");
			System.out.println("username=" + i);
			System.out.println("fromuser=" + i);
			System.out.println("secret=214011");
			System.out.println("host=210.13.73.109");
			System.out.println("fromdomain=210.13.73.109");
			System.out.println();
		}

	}

	public static void printExtenAndOutline(int start, int end,String outline) {
		
		for (int i = start; i <= end; i++) {

			System.out.println("INSERT INTO ec_exten_and_outline VALUES ('" + i
					+ "','" + outline + "');");
		}
	}

	public static void printSipInsert(int start, int end) {
		for (int i = start; i <= end; i++) {

			System.out
					.println("INSERT INTO sip_conf (name, host, nat, type, accountcode, amaflags, \"call-limit\", callgroup, callerid, cancallforward, canreinvite, context, defaultip, dtmfmode, fromuser, fromdomain, insecure, language, mailbox, md5secret, permit, deny, mask, musiconhold, pickupgroup, qualify, regexten, restrictcid, rtptimeout, rtpholdtimeout, secret, setvar, disallow, allow, fullcontact, ipaddr, port, regserver, regseconds, lastms, username, defaultuser, subscribecontext, dpmt, loginusername)VALUES ('"
							+ i
							+ "', 'dynamic', 'no', 'friend', NULL, NULL, 9999, NULL, NULL, 'yes', 'no', 'outgoing', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'no', '', NULL, NULL, NULL, '', NULL, 'all', 'all', '', '192.168.2.6', '5060', '', 1314673740, 0, '0', '', '', '', '0');");

		}
	}

	public static void printDefaultQueueINSERT(int start, int end) {
		System.out
				.println("INSERT INTO queue_table (name, musiconhold, announce, context, timeout, monitor_join, monitor_format, queue_youarenext, queue_thereare, queue_callswaiting, queue_holdtime, queue_minutes, queue_seconds, queue_lessthan, queue_thankyou, queue_reporthold, announce_frequency, announce_round_seconds, announce_holdtime, retry, wrapuptime, maxlen, servicelevel, strategy, joinempty, leavewhenempty, eventmemberstatus, eventwhencalled, reportholdtime, memberdelay, weight, timeoutrestart, setinterfacevar, autopause, description, queue_periodic_announce, dynamicmember) "
						+ "VALUES ('default', 'default', NULL, NULL, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 5, NULL, 'rrmemory', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, True, 'no', 'default', NULL, False);");

		for (int i = start; i <= end; i++) {
			System.out
					.println("insert into queue_member_table values ('default','SIP/"
							+ i + "',5);");
		}
	}
	
	public static void printQueueINSERT(int start, int end) {
		for (int i = start; i <= end; i++) {

			System.out
					.println("INSERT INTO queue_table (name, musiconhold, announce, context, timeout, monitor_join, monitor_format, queue_youarenext, queue_thereare, queue_callswaiting, queue_holdtime, queue_minutes, queue_seconds, queue_lessthan, queue_thankyou, queue_reporthold, announce_frequency, announce_round_seconds, announce_holdtime, retry, wrapuptime, maxlen, servicelevel, strategy, joinempty, leavewhenempty, eventmemberstatus, eventwhencalled, reportholdtime, memberdelay, weight, timeoutrestart, setinterfacevar, autopause, description, queue_periodic_announce, dynamicmember) "
							+ "VALUES ('"
							+ i
							+ "', 'default', NULL, NULL, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 5, NULL, 'rrmemory', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, True, 'no', '"
							+ i + "', NULL, False);");

		}

		for (int i = start; i <= end; i++) {
			System.out.println("insert into queue_member_table values ('" + i
					+ "','SIP/" + i + "',5);");
		}
	}

	public static void printQueueMemberINSERT(String queuename, int p, int start, int end) {
		for (int i = start; i <= end; i++) {

			System.out
					.println("insert into queue_member_table values ('"+queuename+"','SIP/"
							+ i + "',"+p+");");

		}
	}

	public static void userINSERT(int start, int end) {
		for (int i = start; i <= end; i++) {

			System.out
					.println("INSERT INTO ec_user (username, password, email, name, hid, rolename, departmentname) VALUES ('"
							+ i
							+ "', '"
							+ i
							+ "', '', '"
							+ i
							+ "', '"
							+ i
							+ "', 'agent', 'default');");

		}
	}

	public static void printExtention(int start, int end) {
		for (int i = start; i <= end; i++) {
			int p = 60558364 + i;
			System.out.println("exten => " + p + ",1,Answer()");
			System.out.println("exten => " + p
					+ ",n,Agi(agi://localhost/blacklist.agi?direction=in)");
			System.out.println("exten => " + p
					+ ",n,Set(CDR(userfield)=NO ANSWER)");
			System.out.println("exten => " + p + ",n,Background(silence/2)");
			System.out.println("exten => " + p + ",n,SetMusicOnHold(mymoh)");
			System.out.println("exten => " + p + ",n,Set(TIMEOUT(absolute)=0)");
			System.out
					.println("exten => " + p + ",n,Set(TIMEOUT(response)=60)");
			System.out
					.println("exten => "
							+ p
							+ ",n,Set(recFileName=${STRFTIME(${EPOCH},,%Y%m%d)}-${UNIQUEID})");
			System.out.println("exten => " + p
					+ ",n,Agi(agi://localhost/outlineStorage.agi)");
			System.out.println("exten => " + p + ",n,Set(mydst=" + i + ")");
			System.out.println("exten => " + p + ",n,Goto(${CALLERID(NUM)},1)");
			System.out.println("exten => " + p + ",n,Hangup()");

			System.out.println();
		}
	}
	
	public static void printCtiserver(String ip){
		System.out.println("update tb_ctiserver SET ctiserver_hostname ='"+ip+"';");
		System.out.println("update tb_ctiserver SET pgsqlhost ='"+ip+"';");
		System.out.println("update tb_ctiserver SET ftphost ='"+ip+"';");
		System.out.println("update tb_ctiserver SET echost ='"+ip+"';");
		System.out.println("update tb_ctiserver SET loginurl ='http://"+ip+":8080/';");
		System.out.println("update tb_ctiserver SET popurl ='http://"+ip+":8080/';");
	}

	public static void main(String[] args) {
		
//		printSipExten(8001, 8006);
		userINSERT(1201,1210);
//		printDefaultQueueINSERT(8001,8006);

//		printSipInsert(8201,8210);

//		printSipInsert(8001,8008);

//		printCtiserver("192.168.1.157");

//		printQueueINSERT(1,1);
//		printQueueMemberINSERT("vip",10,8086,8095);
		
		printExtenAndOutline(8500,8547,"31268888");
//		printExtenAndOutline(8877,8877,31268888);

		

	}
}