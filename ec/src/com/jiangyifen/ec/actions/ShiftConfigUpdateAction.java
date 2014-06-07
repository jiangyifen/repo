package com.jiangyifen.ec.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiangyifen.ec.dao.ShiftConfig;
import com.jiangyifen.ec.fastagi.PickMobileAgent;

public class ShiftConfigUpdateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7260074770309528585L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<String> listCheck = new ArrayList<String>();

	public String execute() throws Exception {

		// 提交的数据格式为“id_enableDay”，如：1_sun,2_wed
		// 先将提交数据解析，放在commitedData中，然后根据id来update相应记录

		// key=id，value=enable days
		Map<Long, ArrayList<String>> commitedData = new HashMap<Long, ArrayList<String>>();

		// 先将数据库所有的ShiftConfig加载，放到commitedData中
		// 由于ShiftConfig数据量不会大，所以可以全部加载
		List<ShiftConfig> allSC = shiftConfigManager.getAll();
		for (ShiftConfig sc : allSC) {
			commitedData.put(sc.getId(),new ArrayList<String>());
		}

		// 解析
		for (String s : listCheck) {

			String[] item = s.split("_");
			Long id = new Long(item[0]);
			String day = item[1];

			if (!commitedData.containsKey(id)) {
				commitedData.put(id, new ArrayList<String>());
			}

			List<String> days = commitedData.get(id);
			days.add(day);

		}

		// update记录
		for (Long key : commitedData.keySet()) {
			ShiftConfig sc = shiftConfigManager.get(key);

			List<String> days = commitedData.get(key);

			updateShiftConfig(sc, days);

		}

		// 重新加载
		SimpleDateFormat dateformat = new SimpleDateFormat("EEE", Locale.US);
		String today = dateformat.format(new Date()).toLowerCase();
		PickMobileAgent.reloadMobileAgent(today);

		return SUCCESS;

	}

	private void updateShiftConfig(ShiftConfig sc, List<String> days) {
		sc.setSun(false);
		sc.setMon(false);
		sc.setTue(false);
		sc.setWed(false);
		sc.setThu(false);
		sc.setFri(false);
		sc.setSat(false);

		if (days != null) {
			for (String day : days) {
				if (day.equalsIgnoreCase("sun"))
					sc.setSun(true);
				if (day.equalsIgnoreCase("mon"))
					sc.setMon(true);
				if (day.equalsIgnoreCase("tue"))
					sc.setTue(true);
				if (day.equalsIgnoreCase("wed"))
					sc.setWed(true);
				if (day.equalsIgnoreCase("thu"))
					sc.setThu(true);
				if (day.equalsIgnoreCase("fri"))
					sc.setFri(true);
				if (day.equalsIgnoreCase("sat"))
					sc.setSat(true);

			}
		}
		shiftConfigManager.update(sc);
		logger.info("update sc: id=" + sc.getId() + ", name=" + sc.getName());

	}

	public List<String> getListCheck() {
		return listCheck;
	}

	public void setListCheck(List<String> listCheck) {
		this.listCheck = listCheck;
	}

}
