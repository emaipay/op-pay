
package com.hitler.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
* 投注记录日志
* @since 2016-10-09
*/
public class PayLog {
	private static final Logger logger = LoggerFactory.getLogger(PayLog.class);
	
	
	
	public static Logger getLogger() {
		return logger;
	}
	
}
