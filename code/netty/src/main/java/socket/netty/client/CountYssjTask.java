package com.hdsx.taxi.dcs.dcsserver.socket;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.dcs.dcsservice.OperationService;
import com.hdsx.taxi.dcs.module.GuiceUtil;
import com.hdsx.taxi.dcs.upamsg.bean.DayOperation;
import com.hdsx.taxi.dcs.upamsg.bean.MounthOperation;
import com.hdsx.taxi.dcs.upamsg.msg.UP_SUM_MSG_CAR_MONTH_REQ;
import com.hdsx.taxi.dcs.upamsg.msg.UP_SUM_MSG_CITY_DAY_REQ;
import com.hdsx.taxi.dcs.upamsg.util.DataTypeUtil;
import com.hdsx.taxi.dcs.utils.ConstantUtils;
import com.hdsx.taxi.dcs.utils.DateFormateUtil;

public class CountYssjTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(CountYssjTask.class);
	
	
	public CountYssjTask() {
		
	}


	@Override
	public void run() {
		sendOperationMsg();
	}

	/**
	 * 发送营收消息
	 */
	private void sendOperationMsg() {
		OperationService service = GuiceUtil.getInstance().getInstance(OperationService.class);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String year = DateFormateUtil.getFormateDay(calendar.getTime(), "yyyy");
		String month = DateFormateUtil.getFormateDay(calendar.getTime(), "MM");
		String day = DateFormateUtil.getFormateDay(calendar.getTime(), "dd");
		
		UP_SUM_MSG_CITY_DAY_REQ sumDay = new UP_SUM_MSG_CITY_DAY_REQ(); 
		DayOperation operation  = service.getDayOperation(year, month, day);
		sumDay.setDayOperation(operation);
		UpaClient.getInstance().send(sumDay);
		logger.info("定时统计日营运信息发送给upa");

		UP_SUM_MSG_CAR_MONTH_REQ sumMonth = new UP_SUM_MSG_CAR_MONTH_REQ(); 
		MounthOperation m  = service.getMounthOperation(year, month);
		sumMonth.setMounthOperation(m);
		UpaClient.getInstance().send(sumMonth);
		logger.info("定时统计月营运信息发送给upa");
	}

	/**
	 * 根据日期来进行统计
	 */
	private static void countDay(){
		try {
			OperationService service = GuiceUtil.getInstance().getInstance(OperationService.class);

			Calendar calendar = Calendar.getInstance();
			long end = calendar.getTimeInMillis();
			String dateStr = ConstantUtils.OP_DAY;
			if (!DataTypeUtil.isNotEmpty(dateStr)) {

				Date startDate = DateFormateUtil.dateString2JavaUtilDate(dateStr);
				calendar.setTime(startDate);
				long startDay = calendar.getTimeInMillis();
				long startMonth = calendar.getTimeInMillis();
				
				while (startDay<end) {
					String year = DateFormateUtil.getFormateDay(calendar.getTime(), "yyyy");
					String month = DateFormateUtil.getFormateDay(calendar.getTime(), "MM");
					String day = DateFormateUtil.getFormateDay(calendar.getTime(), "dd");
					UP_SUM_MSG_CITY_DAY_REQ sumDay = new UP_SUM_MSG_CITY_DAY_REQ(); 
					DayOperation operation  = service.getDayOperation(year, month, day);
					sumDay.setDayOperation(operation);
					UpaClient.getInstance().send(sumDay);
					calendar.add(Calendar.DAY_OF_YEAR, 1);
					startDay = calendar.getTimeInMillis();
					logger.debug("统计"+year+month+day+"日营运信息发送给upa");
				}

				calendar.setTime(startDate);
				while (startMonth<end) {
					String year = DateFormateUtil.getFormateDay(calendar.getTime(), "yyyy");
					String month = DateFormateUtil.getFormateDay(calendar.getTime(), "MM");
					UP_SUM_MSG_CAR_MONTH_REQ sumMonth = new UP_SUM_MSG_CAR_MONTH_REQ(); 
					MounthOperation m  = service.getMounthOperation(year, month);
					sumMonth.setMounthOperation(m);
					UpaClient.getInstance().send(sumMonth);
					calendar.add(Calendar.MONTH, 1);
					startMonth = calendar.getTimeInMillis();
					logger.debug("统计"+year+month+"月营运信息发送给upa");
				}
			}

			logger.info("统计营运信息发送完毕");
		} catch (Exception e) {
			logger.info("统计营运信息异常");
			e.printStackTrace();
		}

	}
	
	/**
	 * 初始化工作
	 */
	public static void init(){
		//根据输入的营收时间来进行处理
		countDay();
	}
	
}
