package com.jiangyifen.ec.report.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractReport implements Report {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected String lastGenDate = "";

	@Override
	public void execute() {

		while (true) {

			try {
				Date today = new Date();
//				Date yesterday = new Date(today.getTime() - 1 * 24 * 3600 * 1000);

				SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
				String todayDateString = dateSdf.format(today);
//				String yesterdayDateString = dateSdf.format(yesterday);

				SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
				String todayHourString = hourSdf.format(today);
				Integer hour = Integer.valueOf(todayHourString);

				// 过了0点就可以开始处理数据了。
				if (hour != 0 ) {

					// 如果是该实例第一次运行，则需要将最后一次数据重新处理一次。
					// 因为上一次该实例的停止可能未处理完所有的数据。
					
					// 注意问题：现在每次运行report都是new一个实例出来。看ReportGeneator，每次循环都是获取一个新实例。
					// 最小修改：是否考虑将 lastGenDate 改成 static
					if (lastGenDate.equals("")) {
						String lastDateString = this.getLastDateInDB();
						logger.info("jyf lastDateInDB: " + lastDateString);
						if (lastDateString == null) {
							return;
						}
						this.genData(lastDateString);
					}

//					// 如果昨天的原始数据已经处理过了，就不再做任何操作，直接结束
//					if ((dateSdf.parse(lastGenDate).getTime() - dateSdf.parse(yesterdayDateString).getTime()) >= 0) {
					
					// 如果今天的原始数据已经处理过了，就不再做任何操作，直接结束（修改）
					if ((dateSdf.parse(lastGenDate).getTime() - dateSdf.parse(todayDateString).getTime()) >= 0) {
						logger.info("Data before " + lastGenDate
								+ " have been generated into report.");
						return;
					} else {
						// 正常处理lastGenDate下一天的数据
						String nextDateString = this.getNextDate(lastGenDate);
						logger.info(" jyf nextDateString: " + nextDateString);
						this.genData(nextDateString);
					}
				} else {
					// 超过0点就不再执行
					return;
				}

				Thread.sleep(200);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		}

	}

	protected String getNextDate(String dateString) {
		String result = "";

		try {

			SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = dateSdf.parse(dateString);
			result = dateSdf.format(new Date(d.getTime() + 24 * 3600 * 1000));

		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	protected abstract String getLastDateInDB();

	protected abstract void genData(String dateString);

}
