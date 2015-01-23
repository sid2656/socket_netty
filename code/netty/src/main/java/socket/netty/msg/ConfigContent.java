/**
 * Project Name:main
 * File Name:ConfigContent.java
 * Package Name:com.hdsx.taxi.driver.cq.content
 * Date:2014年3月14日下午8:07:52
 * Copyright (c) 2014, Kevinyarm All Rights Reserved.
 * Blog http://www.cnblogs.com/Kevinyarm/
 *
 */
/**
 * Project Name:main
 * File Name:ConfigContent.java
 * Package Name:com.hdsx.taxi.driver.cq.content
 * Date:2014年3月14日下午8:07:52
 * Copyright (c) 2014, KevinYarm All Rights Reserved.
 *
 */

package socket.netty.msg;

import java.io.File;

public class ConfigContent {
	public ConfigContent() {
	}

	public static String PROJECT_ABSOLUTE_PATH = System.getProperty("user.dir");
	/* Linux */
	/*
	 * public static String CONFIG_FILE_PATH = PROJECT_ABSOLUTE_PATH +
	 * "/target/classes/conf";
	 */
	/* Windows */
	public static String CONFIG_FILE_PATH = PROJECT_ABSOLUTE_PATH
			+ "\\target\\classes\\conf\\config.properties";
	public static String DATABASE_CONFIG_FILE_PATH = PROJECT_ABSOLUTE_PATH
			+ "\\target\\classes\\conf\\config.properties";
	/* 成功失败返回指令 */
	public static String SUCCESS = "success";
	public static String FAIL = "fail";
	/** Mybatis config file path */
	public static String MYBATIS_CONF_FILE = "mybatis/Configuration.xml";


	public static final String RESOURCES_DIR = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources";
	
	public static final int TCP_HEADER_LENGTH = 29;
	
	public static final int TCP_GRABSUCCESS_LENGTH = 78;
	
	public static final byte TCP_HEADER_STARTFLAG = '@';
	
	public static void main(String[] args) {
		System.err.println(CONFIG_FILE_PATH);
	}
}
