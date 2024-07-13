package com.onmobile.apps.ivm.daemons.missedcalls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.util.concurrent.RateLimiter;
import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.IDaemonConsts;
import com.onmobile.apps.ivm.daemons.common.ListHashTable;
import com.onmobile.apps.ivm.daemons.common.ProductDetails;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.config.xmlParsing.MCAXMLParser;
import com.onmobile.apps.ivm.daemons.db.model.MissedCall;
import com.onmobile.apps.ivm.daemons.db.model.Sms;
import com.onmobile.apps.ivm.daemons.db.model.Subscriber;
import com.onmobile.apps.ivm.daemons.dto.McaMissedcallDto;
import com.onmobile.apps.ivm.daemons.dto.ProductDto;
import com.onmobile.apps.ivm.daemons.dto.SubscriberDto;
import com.onmobile.apps.ivm.daemons.expiry.VMExpiryDaemon;
import com.onmobile.common.debug.DebugManager;

/**
 * @author rupesh.kumar
 * This thread is started by VMMTDispatcher thread
 * takes B type products from vm_missedcalls_mca table, manipulates it and dumps created sms into vm_sms table
 */

public class VMMCAMTMissedCallProcessor extends VMMCAMTThreadProcessor implements IDaemonConsts,Runnable {
	
	protected String m_reasons_supported = "'" + MC_REASON_UNREACHABLE + "'"; // need to provide the correct value
	protected static String _class = "VMMCAMTMissedCallProcessor";
		
	public static long blackOutStartTimeInMillis;
	public static long currentTimeInMillis;
	public static long blackOutEndTimeInMillis;
	public static long timeIntervalInMillis;	
	private static long messageDuration;
	
	protected String NUM_TIMES = "%NUM_TIMES%";
	protected String MESSAGE_DURATION = "%MESSAGE_DURATION%";
	private Vector<String> vMCASubscribers;	
	
	public static String blackoutStart = "";
	public static String blackoutEnd = "";		
	long fromTime = 0L;
	long toTime = 0L;
	public static boolean isServerStartUpFirstTime = false;
	public static long serverStartUpTime;
	public static Connection bulkPromoCon;
	public static boolean isThreadRunning = false;
	public static List <Long> backlogtimeList = new ArrayList<Long>();// backlogs toTime list
	public static List <Long> serverStartuptoTimeList = new ArrayList<Long>();// total time list

	private int mcaSmscCapacity = 0;
	private int runEveryXSeconds = 60;
	RateLimiter limiter = RateLimiter.create(1.0 / runEveryXSeconds);
	
	private static Vector<Sms> smsThrottleList = new Vector<Sms>();

	public static List <Long> timeListForAfterBlackoutPeriod = new ArrayList<Long>();

	VMMCAMTMissedCallProcessor vMMCAMTMissedCallProcessor = this;

	public VMMCAMTMissedCallProcessor(int threadID, String mcProcessingType,
			boolean silentSMS) {
		super(threadID, mcProcessingType, silentSMS);
		// TODO Auto-generated constructor stub
	}

	public VMMCAMTMissedCallProcessor() {
        super();
    }

	public VMMCAMTMissedCallProcessor(String reasonsSupported) {
		m_reasons_supported = reasonsSupported;
	}

	private boolean m_boolStop = false;

	private String condition = " AND subscriber like ";

	private int noofrecords;
	private boolean serverStartUpInsideBlackoutPeriod;
	public static boolean scheduletimerstartedfirsttime = false;
	
	public void stop(boolean stop) {
		m_boolStop  = stop;
	}

	public void run(){
		
		DebugManager.status(_module, _class, "run","method called  ..", Thread.currentThread().getName(), null);
						
		blackoutStart = ConfigDTO.getMcaDTO().getBlackoutStart();

	    blackoutEnd = ConfigDTO.getMcaDTO().getBlackoutEnd();

		String runIntervalInMinutes = ConfigDTO.getMcaDTO().getRunIntervalInMinutes();
		
		DebugManager.status(_module, _class, "run","blackoutStart : "+blackoutStart+" blackoutEnd : "+blackoutEnd+" timeInterval : "+runIntervalInMinutes, Thread.currentThread().getName(), null);
		
		timeIntervalInMillis = Integer.parseInt(runIntervalInMinutes)*DaemonUtils.oneMinuteInMillis;
			
		fromTime = System.currentTimeMillis() - timeIntervalInMillis;
		
		initializeBlackoutStartEndTime();

		startThread(System.currentTimeMillis(), null); // at this time there is no timer
		
		Timer timer = new Timer(); 
		TimerT1 t1 = new TimerT1(vMMCAMTMissedCallProcessor);
		long delay = getDelayForTimerT1();

		timer.scheduleAtFixedRate(t1, delay, 5*DaemonUtils.oneMinuteInMillis);
	}	
	
	/**
	 * This method tries for 3 times to process the missed calls if it fails due to any reason
	 * 
	 * @param toTime
	 *           time till which missed calls has o be processed
	 * @param timer
	 *           current timer, will be used to cancel it
	 */
	public void runHelper(long toTime, Timer timer){
		int count = 0;
		while (count < 3) {
			if (processMissedcallHelper(toTime, timer)) {
				break;
			}else{
				count++;
				if (count == 3) {
					DebugManager.fatalError(_module, _class, "runHelper", " Tried 3 times unable to process MCA missedcalls ", Thread.currentThread().getName(), null);
				}
			}
		}
	}
	
	/**
	 * This method gets the connection from connection pool pass to the method and releases it
	 * for it timer call new connection is taken from connection pool
	 * 
	 * @param toTime
	 *           time till which missed calls has been processed
	 * @param timer
	 *           current timer, will be used to cancel it
	 */
	public boolean processMissedcallHelper(long toTime, Timer timer) {
		DebugManager.status(_module, _class, "processMissedcallHelper"," Start ", Thread.currentThread().getName(), null);
		boolean result = false;
		Connection con = null;

		try {
			con = VMNotificationDaemon.getConFactory().getConnection();
			processMissedcallTask(con, toTime, timer);
			result = true;
		}catch(Exception e){
			DebugManager.exception(_module, _class, "processMissedcallHelper", e, Thread.currentThread().getName(),null);
		}finally{
			VMNotificationDaemon.getConFactory().releaseConnection(con);
		}
		
	return result;
	}
	
	/**
	 * This method contains the code to get the blackout and in period
	 * 
	 * @param con
	 *  		 DB connection object from connection pool
	 * @param toTime
	 *           time till which missed calls has o be processed
	 * @param timer
	 *           current timer, will be used to cancel it
	 */
	public void processMissedcallTask(Connection con, long toTime, Timer timer){
				
		if(isServerStartUpFirstTime){
			DebugManager.status(_module, _class, "processMissedcallTask","Server started first time ", Thread.currentThread().getName(), null);
		}
				
		long curntTime = System.currentTimeMillis();
		initializeBlackoutStartEndTime();
		DebugManager.status(_module, _class, "processMissedcallTask","blackOutStartTimeInMillis "+DaemonUtils.getCompleteDate(blackOutStartTimeInMillis)+" " +
				                      "blackOutEndTimeInMillis : "+DaemonUtils.getCompleteDate(blackOutEndTimeInMillis)+" curntTime "+DaemonUtils.getCompleteDate(curntTime), Thread.currentThread().getName(), null);          
        
        // inside blackout period, cancel the timer
        if((curntTime >= blackOutStartTimeInMillis) && (curntTime <= blackOutEndTimeInMillis)){
			DebugManager.status(_module, _class, "processMissedcallTask","inside blackout period ", Thread.currentThread().getName(), null);
        	if(isServerStartUpFirstTime){
        		serverStartUpInsideBlackoutPeriod = true;
        	}
        	
			DebugManager.status(_module, _class, "processMissedcallTask","MCAXMLParser.blackOutSleepDurationInMillis "+MCAXMLParser.blackOutSleepDurationInMillis+" VMMCAMTMissedCallProcessor.timeIntervalInMillis "+VMMCAMTMissedCallProcessor.timeIntervalInMillis+" timer "+timer, Thread.currentThread().getName(), null);

			// if blackOutSleepDuration is less than timeInterval, the only one timer should be created and it should not be destroyed
			if(!(MCAXMLParser.blackOutSleepDurationInMillis < VMMCAMTMissedCallProcessor.timeIntervalInMillis)){
            	if(timer != null){
        			DebugManager.status(_module, _class, "processMissedcallTask","canceling timer "+timer.getClass().getName(), Thread.currentThread().getName(), null);
            		timer.cancel();
            		TimerT1.schedulTaskTimer = timer = null;
            	}
        	}
        }else{
        	processMissedcallAtRunInterval(con, toTime, timer);
        	
			DebugManager.status(_module, _class, "processMissedcallTask","outside blackout period backlogtimeList "+backlogtimeList.size()+" "+
			                                 backlogtimeList, Thread.currentThread().getName(), null);
        }                
	}
	
	/**
	 * This method contains the code to check if thread is running and the process missed calls
	 * process the pending backlogs
	 * handles the case at server startup time and first run after blackout period 
	 * 
	 * @param con
	 *  		 DB connection object from connection pool
	 * @param toTime
	 *           time till which missed calls has o be processed
	 */
	public void processMissedcallAtRunInterval(Connection con, long toTime, Timer timer){
		DebugManager.status(_module, _class, "processMissedcallAtRunInterval"," timeListForAfterBlackoutPeriod "+timeListForAfterBlackoutPeriod+" isThreadRunning "+isThreadRunning+" backlogtimeList "+backlogtimeList+" TimerT1.firstRunAfterBlackout "+TimerT1.firstRunAfterBlackout+" isServerStartUpFirstTime "+isServerStartUpFirstTime+" scheduletimerstartedfirsttime "+scheduletimerstartedfirsttime, Thread.currentThread().getName(), null);

		try{
			if (!isServerStartUpFirstTime) {

				if ((!isThreadRunning) && (backlogtimeList.size() == 0)) {
					if (TimerT1.firstRunAfterBlackout) {
						if (timeListForAfterBlackoutPeriod.size() > 0) {
							fromTime = timeListForAfterBlackoutPeriod.get(timeListForAfterBlackoutPeriod.size() - 1);
							TimerT1.firstRunAfterBlackout = false;
							timeListForAfterBlackoutPeriod.clear();
						} else {
							fromTime = blackOutStartTimeInMillis - timeIntervalInMillis;
						}
						
						// if first schedule task run is cancelled (b/c entered inside blackout period)
						scheduletimerstartedfirsttime = false;
						
					} else {
						if (!scheduletimerstartedfirsttime) {
							fromTime = toTime - timeIntervalInMillis;
						} else {
							if ((toTime - timeIntervalInMillis) < serverStartUpTime) {
								fromTime = toTime - timeIntervalInMillis;
							} else {
								fromTime = serverStartUpTime;
							}
							scheduletimerstartedfirsttime = false;
						}
					}
					processMissedcallUtil(con, fromTime, toTime, timer);
				}
				DebugManager.status(_module, _class, "processMissedcallAtRunInterval isThreadRunning "+isThreadRunning,"going to process backlogs "+
				                         backlogtimeList.size()+" "+backlogtimeList, Thread.currentThread().getName(), null);
				
				while ((!isThreadRunning) && (backlogtimeList.size() > 0)) {
					scheduletimerstartedfirsttime = false;
					DebugManager.status(_module, _class, "processMissedcallAtRunInterval","processing backlogs "+backlogtimeList.size()+" "+backlogtimeList, 
							                         Thread.currentThread().getName(), null);

					fromTime = backlogtimeList.get(0) - timeIntervalInMillis;
					toTime = backlogtimeList.get(backlogtimeList.size() - 1);
					backlogtimeList.clear();
					processMissedcallUtil(con, fromTime, toTime, timer);
				}
			}else{
				/*if(serverStartUpInsideBlackoutPeriod){
					if (!isSameDay()) {						
						fromTime = blackOutStartTimeInMillis; // b/c this fromTime is already calculated
					}else{
						long currTime = System.currentTimeMillis();
						 
						  * if end time is on previous day but after comming out of blackout current time falls on next day
						  * end time [23:59:59] after blackout current time will be 00:03:00
						  
						if((DaemonUtils.getTodayDateInMillis(currTime)) > (DaemonUtils.getTodayDateInMillis(serverStartUpTime))){
							fromTime = blackOutStartTimeInMillis - DaemonUtils.oneDayInMillis;
						}else{							
							fromTime = blackOutStartTimeInMillis;
						}
					}
				}else{
					fromTime = toTime - timeIntervalInMillis;	
				}*/
				
				long oldestTime = ConfigDTO.getDaoDTO().getMissedCallDao().getOldestRecordCalledtimeFromMCATable(con);
				
				long tempFromTime = System.currentTimeMillis() - (VMExpiryDaemon.longMissedCallLifeTime - 15*DaemonUtils.oneMinuteInMillis);
				
				DebugManager.status(_module, _class, "processMissedcallAtRunInterval ","oldestTime = "+DaemonUtils.getCompleteDate(oldestTime)+" tempFromTime = "+DaemonUtils.getCompleteDate(tempFromTime), Thread.currentThread().getName(), null);
				
				if(oldestTime > tempFromTime){
					fromTime = oldestTime;
				}else{
					fromTime = tempFromTime;
				}
				
				// this line has been added only for testing remove it
				// fromTime = System.currentTimeMillis() - timeIntervalInMillis;
				
				serverStartUpInsideBlackoutPeriod = false;
				isServerStartUpFirstTime = false;
				TimerT1.firstRunAfterBlackout = false; // otherwise in next call it will create problem
				processMissedcallUtil(con, fromTime, toTime, timer);
				
				DebugManager.status(_module, _class, "Inside blackout period processMissedcallAtRunInterval isThreadRunning "+isThreadRunning,"going to process backlogs "+
                        backlogtimeList.size()+" "+backlogtimeList, Thread.currentThread().getName(), null);

				while ((!isThreadRunning) && (backlogtimeList.size() > 0)) {
					scheduletimerstartedfirsttime = false;
					DebugManager.status(_module, _class, "processMissedcallAtRunInterval","processing backlogs "+backlogtimeList.size()+" "+backlogtimeList, 
							                         Thread.currentThread().getName(), null);
				
					fromTime = backlogtimeList.get(0) - timeIntervalInMillis;
					toTime = backlogtimeList.get(backlogtimeList.size() - 1);
					backlogtimeList.clear();
					processMissedcallUtil(con, fromTime, toTime, timer);
				}
			}
		}catch(Exception e){
			DebugManager.exception(_module, _class, "processMissedcallAtRunInterval", e, Thread.currentThread().getName(), null);
		}		
		
	}
	
	/**
	 * This method calls the api to process missed calls
	 * maintains a list of toTime 
	 * notifies if thread has completed its task
	 * 
	 * @param con
	 *  		 DB connection object from connection pool
	 * @param fromTime
	 *           time from which missed calls has to be processed
	 * @param toTime
	 *           time till which missed calls has to be processed
	 */
	public void processMissedcallUtil(Connection con,long fromTime, long toTime, Timer timer){
		DebugManager.status(_module, _class, "processMissedcallUtil","backlogtimeList "+backlogtimeList+" ,timeListForAfterBlackoutPeriod "
	                                   +timeListForAfterBlackoutPeriod+" fromTime "+fromTime+" "+DaemonUtils.getCompleteDate(fromTime), Thread.currentThread().getName(), null);
		isThreadRunning = true;		
		boolean processSuccessfully = processAllMissedcalls(con,m_reasons_supported,fromTime,toTime, timer);
		
		if(processSuccessfully){		
			timeListForAfterBlackoutPeriod.add(toTime);
		}else{
			timeListForAfterBlackoutPeriod.add(fromTime);
			backlogtimeList.clear();
		}

		isThreadRunning = false;	
	}
	
	/**
	 * This method returns the delay time for TimerT1
	 * maintains a list of all the boot up times
	 * 
	 * @return : delay time
	 */
	public long getDelayForTimerT1(){

		DebugManager.status(_module, _class, "getDelayForTimerT1 : current time  "+DaemonUtils.getCompleteDate(System.currentTimeMillis()), "", "", "");
		
		long currTime = System.currentTimeMillis();
		long reminder = currTime % (5*DaemonUtils.oneMinuteInMillis);
		long delay = (5*DaemonUtils.oneMinuteInMillis - reminder);
		long bootuptime = 0;
		
		if(currTime < blackOutEndTimeInMillis){
			bootuptime = blackOutEndTimeInMillis - DaemonUtils.oneDayInMillis;		
		}else{
			bootuptime = blackOutEndTimeInMillis;		
		}
		
		long blackoutEndLimit = bootuptime + DaemonUtils.oneDayInMillis;
		
		while (bootuptime <= blackoutEndLimit) {
			serverStartuptoTimeList.add(bootuptime);
			bootuptime = bootuptime + timeIntervalInMillis;
		}
		
		DebugManager.status(_module, _class, "getDelayForTimerT1","delay "+delay+" ,"+DaemonUtils.getTimeInHHMMSSFormat(delay)+" serverStartuptoTimeList size "+
		                                 serverStartuptoTimeList.size(), Thread.currentThread().getName(), null);
		
		return delay;
	}
	
	/**
	 * This method starts a new thread when no thread is running and no backlogs is there
	 * inside blackout period it cancels the timer and outside add to the backlog list
	 * 
	 * @param toTime
	 *           time till which missed calls has o be processed
	 * @param timer
	 *           current timer, will be used to cancel it
	 */
	public void startThread(long toTime, Timer timer){

		DebugManager.status(_module, _class, "startThread","toTime "+DaemonUtils.getCompleteDate(toTime)+" isThreadRunning "+isThreadRunning, Thread.currentThread().getName(), null);

		initializeBlackoutStartEndTime();
		
		// if any backlog time is there then new thread should not be created
		if((!isThreadRunning) && (backlogtimeList.size() == 0)){
			ProcessMissedCallsThread  pmcThread = new ProcessMissedCallsThread(vMMCAMTMissedCallProcessor,toTime,timer);
			Thread thread = new Thread(pmcThread);
			thread.start();
		}else{
			long curntTime = System.currentTimeMillis();
			
			// don't add toTime in this list while inside blackout period, cancel timer
	        if((curntTime > blackOutStartTimeInMillis) && (curntTime < blackOutEndTimeInMillis)){
				DebugManager.status(_module, _class, "startThread","inside blackout period dont add toTime to backlog list, and cancel timer",
						                       Thread.currentThread().getName(), null);

				// if blackOutSleepDuration is less than timeInterval, the only one timer should be created and it should not be destroyed
				if(!(MCAXMLParser.blackOutSleepDurationInMillis < VMMCAMTMissedCallProcessor.timeIntervalInMillis)){
	            	if(timer != null){
	            		timer.cancel();
	            		TimerT1.schedulTaskTimer = timer = null;
	            	}
	        	}
	        }else{
	        	backlogtimeList.add(toTime);	        	
	        	DebugManager.status(_module, _class, "startThread","isThreadRunning "+isThreadRunning+" added to backlogtimeList, size "+backlogtimeList.size()+
	        			                       "  "+backlogtimeList, Thread.currentThread().getName(), null);
	        }
		}		
	}
	
	/**
	 * This method initializes the blackout start and end time based on they falls on same day or different day
	 */
	public static void initializeBlackoutStartEndTime(){

		// if blackout start and end are on different day [22:00 PM - 07:00 AM]
        if (!isSameDay()) {
        	
        	blackOutStartTimeInMillis = DaemonUtils.getTimeInMillis(DaemonUtils.getTodayDate(System.currentTimeMillis())+" "+blackoutStart);
        	blackOutEndTimeInMillis = DaemonUtils.getTimeInMillis(DaemonUtils.getTodayDate(System.currentTimeMillis())+" "+blackoutEnd) + DaemonUtils.oneDayInMillis;
    	
        	long currTime = System.currentTimeMillis();
        	if (currTime < (blackOutEndTimeInMillis + 5*DaemonUtils.oneMinuteInMillis - DaemonUtils.oneDayInMillis)) {
        		DebugManager.status(_module, _class, "initializeBlackoutStartEndTime"," subtracting one day ", Thread.currentThread().getName(), null);
				// if server starts between  7 AM - 22 PM
				blackOutEndTimeInMillis = blackOutEndTimeInMillis - DaemonUtils.oneDayInMillis;
				blackOutStartTimeInMillis = blackOutStartTimeInMillis - DaemonUtils.oneDayInMillis;
			}
		
        // if blackout start and end time is on same day  [7:00 AM - 17:00 PM]
		}else{
			blackOutStartTimeInMillis = DaemonUtils.getTimeInMillis(DaemonUtils.getTodayDate(System.currentTimeMillis())+" "+blackoutStart);
        	blackOutEndTimeInMillis = DaemonUtils.getTimeInMillis(DaemonUtils.getTodayDate(System.currentTimeMillis())+" "+blackoutEnd);        	
		}
        
		DebugManager.status(_module, _class, "initializeBlackoutStartEndTime","blackOutStartTimeInMillis "+DaemonUtils.getCompleteDate(blackOutStartTimeInMillis) 
				                         +" blackOutEndTimeInMillis "+DaemonUtils.getCompleteDate(blackOutEndTimeInMillis), Thread.currentThread().getName(), null);
	}

	/**
	 * This method gets the total counts of missed calls from vm_missedcalls_mca table 
	 * based on its counts it breaks it into smaller chunks and process those many records at a time 
	 * 
	 * @param con
	 *  		 DB connection object from connection pool
	 * @param  m_reasons_supported
	 *           reson supported
	 * @param fromTime
	 *           time from which missed calls has to be processed
	 * @param toTime
	 *           time till which missed calls has to be processed
	 */
	public boolean processAllMissedcalls(Connection con, String m_reasons_supported, long fromTime, long toTime, Timer timer){
		
		DebugManager.status(_module, _class, "run","processAllMissedcalls ...... begin", Thread.currentThread().getName(), null);
		
		boolean processedSuccessfully = true;
		
		fromTime = fromTime - 5*DaemonUtils.oneMinuteInMillis;

		long startTimeTotal = System.currentTimeMillis();
		
		String whereCondition = "";
		noofrecords = ConfigDTO.getDaoDTO().getMissedCallDao().getNoOfRecords(con,m_reasons_supported,fromTime,toTime,whereCondition);
				
		DebugManager.status(_module, _class, "processAllMissedcalls noofrecords : "+noofrecords, " fromTime "+fromTime +" " +DaemonUtils.getCompleteDate(fromTime)+" toTime  "
		                                 +DaemonUtils.getCompleteDate(toTime), "", "");

		if(noofrecords>5000){
			
			for(int i = 0;i<10;i++){
								
			    whereCondition = condition+"'%"+i+"'";

			    for (int j = 0; j < 10; j++) {
			    	
			    	initializeBlackoutStartEndTime();
					
					long curntTime = System.currentTimeMillis();

			        // inside blackout period, cancel the timer
			        if((curntTime > blackOutStartTimeInMillis) && (curntTime < blackOutEndTimeInMillis)){

						// if blackOutSleepDuration is less than timeInterval, the only one timer should be created and it should not be destroyed
						if(!(MCAXMLParser.blackOutSleepDurationInMillis < VMMCAMTMissedCallProcessor.timeIntervalInMillis)){
			            	if(timer != null){
			        			DebugManager.status(_module, _class, "processAllMissedcalls","canceling timer "+timer.getClass().getName(), Thread.currentThread().getName(), null);
			            		timer.cancel();
			            		TimerT1.schedulTaskTimer = timer = null;
			            	}
			        	}
						DebugManager.status(_module, _class, "processAllMissedcalls Exiting the while loop! ", " fromTime "+DaemonUtils.getCompleteDate(fromTime)+" toTime  "
                                +DaemonUtils.getCompleteDate(toTime), "", "");
						
						processedSuccessfully = false;
						
						break;
			        }
			    	
					whereCondition = condition+"'%" + j + i + "'";	
					
					long startTime = System.currentTimeMillis();
						
		            processMcaMissedcalls(con, fromTime, toTime,whereCondition);
		            		            
					DebugManager.status(_module, _class, "Execution of processAllMissedcalls 1, i : "+i+" j : "+j+" Start time : "+
					               DaemonUtils.getCompleteDate(startTime)+" End Time : "+DaemonUtils.getCompleteDate(System.currentTimeMillis()), " ", "", "");
				}
			    
			    if(!processedSuccessfully){
			    	break;
			    }
			}
			if(smsThrottleList!=null && smsThrottleList.size() > 0){
				DebugManager.status(_module, _class, "processAllMissedcalls 1","calling saveSmsWithThrottlingHelper clearing sms size:"+smsThrottleList.size(), Thread.currentThread().getName(), null);
				saveSmsWithThrottlingHelper(con, smsThrottleList, limiter);
				smsThrottleList.clear();
			}
		}else{
			
			if(noofrecords>0){
				 processMcaMissedcalls(con, fromTime, toTime,whereCondition);
				 DebugManager.status(_module, _class, "Execution of processAllMissedcalls 2, Start time : "+DaemonUtils.getCompleteDate(startTimeTotal)+
						 " End Time : "+DaemonUtils.getCompleteDate(System.currentTimeMillis()), "", "", "");
				 
				if(smsThrottleList!=null && smsThrottleList.size() > 0){
					DebugManager.status(_module, _class, "processAllMissedcalls 2","calling saveSmsWithThrottlingHelper clearing sms size:"+smsThrottleList.size(), Thread.currentThread().getName(), null);
					saveSmsWithThrottlingHelper(con, smsThrottleList, limiter);
					smsThrottleList.clear();
				}
			}          
		}
		
		return processedSuccessfully;
	}
	
	/**
	 * This method gets the total counts of missed calls from vm_missedcalls_mca table 
	 * based on its counts it breaks it into smaller chunks and process those many records at a time 
	 * 
	 * @param con
	 *  		 DB connection object from connection pool
	 * @param fromTime
	 *           time from which missed calls has to be processed
	 * @param toTime
	 *           time till which missed calls has to be processed
	 * @param  whereCondition
	 *           whereCondition, where condition to filter the subscribers based on last digits
	 */
	public void processMcaMissedcalls(Connection con, long fromTime, long toTime, String whereCondition){

		// In case of huge entries we may have entries older than say 23 hours, we should not process that and leave for expire daemon 
		if((System.currentTimeMillis() - fromTime) > (VMExpiryDaemon.longMissedCallLifeTime - 15*DaemonUtils.oneMinuteInMillis)){
			if((VMExpiryDaemon.longMissedCallLifeTime - 15*DaemonUtils.oneMinuteInMillis) > 0){
				fromTime = System.currentTimeMillis() - (VMExpiryDaemon.longMissedCallLifeTime - 15*DaemonUtils.oneMinuteInMillis);
			}
			DebugManager.fatalError(_module, _class, "processMcaMissedcalls", "leaving the older records unprocessed and processing from "+DaemonUtils.getCompleteDate(fromTime), Thread.currentThread().getName(), null);
		}
		
		messageDuration = System.currentTimeMillis() - fromTime + 5*DaemonUtils.oneMinuteInMillis; 

		ListHashTable<String, McaMissedcallDto> listHashTable =  ConfigDTO.getDaoDTO().getMissedCallDao().getUnProcessedMissedcallsFromMcaTable(con,
				m_reasons_supported,fromTime,toTime,whereCondition);
		
		ConcurrentHashMap<String, String> conHmSubsWhoGotMC = getUniqueSubsWhoGotMC(listHashTable.getList());
		
		Vector<String> vMCASubscribers = getSubscribers(conHmSubsWhoGotMC);
		
		Hashtable<String, McaMissedcallDto> htMcaMissedcallDto = listHashTable.getHtable();
		
		Hashtable<String, SubscriberDto> htSubscriberProvisioning = getSubscriberProvisioning(con, vMCASubscribers);
				
		processMissedCalls(con,htMcaMissedcallDto, htSubscriberProvisioning, fromTime, toTime, whereCondition);
	}
	
	/**
	 * This method gets the unique subscribers from the list of missed calls
	 * 
	 * @param listMC
	 *           vector of subscribers
	 */
	public ConcurrentHashMap<String, String> getUniqueSubsWhoGotMC(Vector<String> listMC){
		ConcurrentHashMap<String, String> conHm = new ConcurrentHashMap<String, String>();
		if(listMC != null){
			for(String sub:listMC){
				String strSubscriber = sub;
				conHm.put(strSubscriber, strSubscriber);
			}
		}
		DebugManager.status(_module, _class, "getUniqueSubsWhoGotMC", "size of conHm = "+conHm.size(), Thread.currentThread().getName(), null);
		return conHm;
	}
	
	/**
	 * method to get the blackout sleep duration in milli seconds
	 * 
	 * @param blackOutStart
	 *         blackout start time after which missed calls should not be processed
	 * @param blackOutEnd
	 * 		   blackout end time before which missed calls should not be processed
	 * @return
	 *         returns the blackout duration in milli second
	 */
	public long getBlackOutSleepDurationInMillis(String blackOutStart, String blackOutEnd){
		
		DebugManager.status(_module, _class, "getBlackOutSleepDurationInMillis","blackOutStart : "+blackOutStart+" blackOutEnd : "+blackOutEnd, 
				Thread.currentThread().getName(), null);

		if(isSameDay()){
			MCAXMLParser.blackOutSleepDurationInMillis = MCAXMLParser.blackOutEndInMillis - MCAXMLParser.blackOutStartInMillis ;
		}else{
			MCAXMLParser.blackOutSleepDurationInMillis = (24*DaemonUtils.oneHourInMillis - MCAXMLParser.blackOutStartInMillis)+ MCAXMLParser.blackOutEndInMillis;
		}
		
		return MCAXMLParser.blackOutSleepDurationInMillis;
	}
	
	/**
	 * method to check if blackout start and end falls on same day or different day
	 * @return
	 *      true if blackout start and end time falls on same day else false
	 */
	public static boolean isSameDay() {
		
		return DaemonUtils.getTodayDateHHMMSSInMillis(blackoutStart)<=DaemonUtils.getTodayDateHHMMSSInMillis(blackoutEnd);
	}
	
	/**
	 * This method gets the B type products from vm_missedcalls_mca table creat's sms and saves into vm_sms table
	 * 
	 * @param con
	 *  		 DB connection object from connection pool
	 * @param htMcaMissedcallDto
	 *           hash table having subscriber as key and McaMissedcallDto object as value 
	 * @param htSubscriberProvisioning
	 *           hash table having subscriber as key and SubscriberDto object as value 
	 * @param fromTime
	 *           time from which missed calls has to be processed
	 * @param toTime
	 *           time till which missed calls has to be processed
	 */
	public void processMissedCalls(Connection con,
			Hashtable<String, McaMissedcallDto> htMcaMissedcallDto,
			Hashtable<String, SubscriberDto> htSubscriberProvisioning, long fromTime, long toTime, String whereCondition) {
		
		DebugManager.status(_module, _class, "processMissedCalls","htMcaMissedcallDto size : "+htMcaMissedcallDto.size(), Thread.currentThread().getName(), null);
		
	    Vector<Sms> vecSms = getMCASmsForSubscribers(htMcaMissedcallDto,htSubscriberProvisioning);
	   
		DebugManager.status(_module, _class, "processMissedCalls","before vecSms size "+vecSms.size(), Thread.currentThread().getName(), null);

		// save in vm_sms table with throttling according to the configured SMSC capacity
		saveSmsWithThrottling(con, vecSms);
		
		DebugManager.status(_module, _class, "processMissedCalls","before deletion "+vecSms.size(), Thread.currentThread().getName(), null);

		// select from mca table and insert into mac_get table then delete
		String [] mcaINfo = {""+fromTime, ""+toTime, whereCondition};
		
		try {
			MCAGetDumpingThread.mcaRecordInfo.put(mcaINfo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	    //ConfigDTO.getDaoDTO().getMissedCallDao().deleteFomVmMissedcallsMcaAfterProcessing(con,fromTime,toTime,whereCondition);		

		DebugManager.status(_module, _class, "processMissedCalls","after deletion ", Thread.currentThread().getName(), null);	 
	}
	
	/**
	 * This method is to throttle the SMS saving in DB. According to the MCA
	 * SMSC capacity, SMS are gathered and saved in DB per minute. Google
	 * guava's RateLimiter is used for throttling process.
	 * 
	 * @param con
	 *            DB connection object from connection pool
	 * @param vecSms
	 *            vector with all the SMS to be saved in vm_sms table
	 */
	private void saveSmsWithThrottling(Connection con, Vector<Sms> vecSms) {
		try {
			if (mcaSmscCapacity <= 0) {
				mcaSmscCapacity = 100;
				DebugManager.information(_module, _class, "saveSmsWithThrottling", "MCA SMSC capacity is set to default value:" + mcaSmscCapacity,
						Thread.currentThread().getName(), null);
			}
			DebugManager.status(_module, _class, "saveSmsWithThrottling","saving total '"+vecSms.size()+"' sms", Thread.currentThread().getName(), null);
			
			if(smsThrottleList!=null && vecSms!=null && vecSms.size()>0){
				smsThrottleList.addAll(vecSms);
				DebugManager.status(_module, _class, "saveSmsWithThrottling","adding to existing list. now there are '"+smsThrottleList.size()+"' sms", Thread.currentThread().getName(), null);
			}
			
			int taken = 0, endRange = mcaSmscCapacity * runEveryXSeconds;
			while (smsThrottleList.size() >= mcaSmscCapacity * runEveryXSeconds) {
				Vector<Sms> subList = new Vector<Sms>(smsThrottleList.subList(taken, endRange));
				DebugManager.status(_module, _class, "saveSmsWithThrottling","subList size  '"+subList.size()+"' sms", Thread.currentThread().getName(), null);
				saveSmsWithThrottlingHelper(con, subList, limiter);
				smsThrottleList.removeAll(subList);
			}
		} catch (Exception e) {
			DebugManager.exception(_module, _class, "saveSmsWithThrottling", e, Thread.currentThread().getName(), null);
		}
	}

	/**
	 * This helper method is created to acquire an lock, which is needed for
	 * throttling
	 * 
	 * @param con
	 *            DB connection object from connection pool
	 * @param vecSms
	 *            vector with the SMS (as per the SMSC capacity) to be saved in
	 *            vm_sms table
	 * @param limiter
	 *            guava API to acquire a lock for throttling
	 */
	private void saveSmsWithThrottlingHelper(Connection con, Vector<Sms> vecSms, RateLimiter limiter) {
		try {
			limiter.acquire();
			DebugManager.status(_module, _class, "saveSmsWithThrottlingHelper","saving '"+vecSms.size()+"' sms", Thread.currentThread().getName(), null);
			ConfigDTO.getDaoDTO().getSmsDao().saveSms(con, vecSms);
		} catch (Exception e) {
			DebugManager.exception(_module, _class, "saveSmsWithThrottlingHelper", e, Thread.currentThread().getName(), null);
		}
	}
	
	/**
	 * @param htMcaMissedcallDto
	 *           hash table having subscriber as key and McaMissedcallDto object as value 
	 * @param htSubscriberProvisioning
	 *           hash table having subscriber as key and SubscriberDto object as value 
	 * @return
	 *           vector of sms objects for provisioned subscribers
	 */
	private Vector<Sms> getMCASmsForSubscribers(Hashtable<String, McaMissedcallDto> htMcaMissedcallDto, Hashtable<String, SubscriberDto> htSubscriberProvisioning) {

		DebugManager.status(_module, _class, "getMCASmsForSubscribers","", Thread.currentThread().getName(), null);
		
		Vector<Sms> vecSms = new Vector<Sms>();
		String smsText = null;
		String replaceSmsText = null;
		String[] arrStrSmsText = null;
				
		Enumeration<String> enStrMcDto = htMcaMissedcallDto.keys();
		
		DebugManager.trace(_module, _class, "getMCASmsForSubscribers","enStrMcDto size : "+htMcaMissedcallDto.size(), Thread.currentThread().getName(), null);
		
		while(enStrMcDto.hasMoreElements())
		{
			String subscriber = enStrMcDto.nextElement();
			
			DebugManager.status(_module, _class, "getMCASmsForSubscribers","subscriber : "+subscriber, Thread.currentThread().getName(), null);
			
			SubscriberDto subDto = htSubscriberProvisioning.get(subscriber);
			ProductDto prodDto = null;
			if(subDto != null){
				prodDto = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
			}
			if(prodDto == null){
				prodDto = ProductDetails.defaultProduct;
			}
			String prodName = prodDto.getProductName();
			
			DebugManager.status(_module, _class, "getMCASmsForSubscribers","prodName : "+prodName, Thread.currentThread().getName(), null);
			
			String[] arrSmscNames = prodDto.getArrStrSmscName();
			
			// for getting smsc names in log
			mcaSmscCapacity = 0;
			try {
				for (int i = 0; i < arrSmscNames.length; i++) {
					mcaSmscCapacity += MCAXMLParser.smscDetails.get(arrSmscNames[i]).capacity;
					DebugManager.trace(_module, _class, "getMCASmsForSubscribers",
							"arrSmscNames  : " + arrSmscNames[i] + " Thread : " + Thread.currentThread().getName(),
							Thread.currentThread().getName(), null);
					//mcaSmscCapacity = mcaSmscCapacity - 25;
				}
				
			} catch (Exception e) {
				DebugManager.exception(_module, _class, "getMCASmsForSubscribers", e, Thread.currentThread().getName(), null);
			}
			
			DebugManager.information(_module, _class, "getMCASmsForSubscribers", "MCA SMSC capacity:" + mcaSmscCapacity,
					Thread.currentThread().getName(), null);
			
			String smscName = arrSmscNames[random.nextInt(arrSmscNames.length)];
			
			DebugManager.status(_module, _class, "getMCASmsForSubscribers","smscName : "+smscName, Thread.currentThread().getName(), null);

			
			int noOfSmsPerDay = prodDto.getNoOfSmsPerDay();
			
			int noOfSmsForToday = 0;
						
			String code = "ENG";
			if(subDto != null && subDto.getVern_supp().equals("Y")){
				code = subDto.getLanguage();
				if (code.equalsIgnoreCase("DEF")){
					code = MCAXMLParser.defaultLangugeCodeMca; // modified by rupesh
				}
				code = code.toUpperCase();
				if (!MCAXMLParser.smsTextDetails.containsKey(code))
					code = "ENG";
			}
						
			if(noOfSmsPerDay > 0)
			{
				noOfSmsForToday = subDto.getNoOfSmsForToday();
				if(noOfSmsForToday >= noOfSmsPerDay)
				{
					continue;
				}
			}
			
			DebugManager.trace(_module, _class, "getMCASmsForSubscribers","noOfSmsPerDay : "+noOfSmsPerDay+" noOfSmsForToday : "+noOfSmsForToday, 
					                    Thread.currentThread().getName(), null);
			
			McaMissedcallDto mcaMcDto = htMcaMissedcallDto.get(subscriber);
			
			arrStrSmsText = createSMSTextForNewRecord(mcaMcDto, htSubscriberProvisioning);
			smsText = arrStrSmsText[0];
			replaceSmsText = arrStrSmsText[1];
			String strCallBackNumber = prodDto.getSmsSendingNo();
			DebugManager.status(_module, _class, "getMCASmsForSubscribers", "subscriber="+subscriber+",strCallBackNumber="+strCallBackNumber+",smscName="+smscName+"," +
					"smsText="+smsText+",replaceSmsText="+replaceSmsText, Thread.currentThread().getName(), null);			
			Sms newSms = new Sms(
								subscriber, 
								MESSAGE_TYPE_BULK_PROMO_MCA,
								SMS_STATUS_CREATED,
								smsText, 
								strCallBackNumber, 
								replaceSmsText, 
								false, 
								smscName
								);
			if(code.equalsIgnoreCase("ENG")){
				newSms.setUnicode(false);
			}else
				newSms.setUnicode(true);
			
			newSms.setProdName(prodName);
			vecSms.add(newSms);
			
			if(noOfSmsPerDay > 0){
				updateNumberOfSmsForToday(subscriber, noOfSmsForToday+1);
				subDto.setNoOfSmsForToday(noOfSmsForToday+1);
			}
		}
		
		return vecSms;
	
	}
	
	/**
	 * This method updat's the no of sms for today for a subscriber 
	 * @param subscriber
	 * @param count
	 */
	public void updateNumberOfSmsForToday(String subscriber, int count){
		DebugManager.status(_module, _class, "updateNumberOfSmsForToday","count : "+count, Thread.currentThread().getName(), null);

		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		ConfigDTO.getDaoDTO().getSubscriptionDao().updateNoOfSmsForToday(con, subscriber, count);
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	}

	/**
	 * This method creates a Hash table of subscriber as key and SubscriberDto object as value and returns it
	 * 
	 * @param con
	 *            DB connection object from connection pool
	 * @param vMCASubscribers
	 *            vector of subscribers
	 * @return
	 *            hash table of key as subscriber and SubscriberDto(class containing details of subscriber) objects as values
	 */
	public Hashtable<String,SubscriberDto> getSubscriberProvisioning(Connection con, Vector<String> vMCASubscribers){
		
		Hashtable<String, SubscriberDto> htSubscribersProvisioning = new Hashtable<String, SubscriberDto>();
				
		if(vMCASubscribers == null){
		   return htSubscribersProvisioning;
		}		
		DebugManager.status(_module, _class, "getSubscriberProvisioning", "Size of vecStrSubscribers ="+vMCASubscribers.size(), 
				Thread.currentThread().getName(), null);
		
		Vector<Subscriber> vecSubscriber = ConfigDTO.getDaoDTO().getSubscriptionDao().getSubscribers(con, vMCASubscribers);
		try{
			for(int i=0; i<vecSubscriber.size(); i++){
				Subscriber subscriber = vecSubscriber.get(i);
				
				String strSubscriber = subscriber.getSubscriber();
				String mobileNumber = subscriber.getMobileNumber();
				int productID = subscriber.getProductID();
				int noOfDaysCharged = subscriber.getNoofDaysCharged();
				java.util.Date dtStartTime = subscriber.getStartTime();
				int noOfSmsForToday = subscriber.getNoOfSmsForToday();
				Date chargingStartTime = subscriber.getCharging_startTime();
				
				
				Hashtable<String, String> htBlackAndWhite = null;
				Hashtable<String, String> htBlackAndWhiteForLca = null;
				
				ProductDto prodDto = ProductDetails.htProductIdToProduct.get(new Integer(productID));
				String strBlcOrWht = prodDto.getBlackOrWhite();
				String strBlcOrWhtForLca = prodDto.getBlackOrWhiteForLca();
				
				if(strBlcOrWht != null){
				if(strBlcOrWht.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_BLACKLIST) || 
					strBlcOrWht.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_WHITELIST))
				{
					htBlackAndWhite = ConfigDTO.getDaoDTO().getSubscriptionDao().getBlackWhiteList(con, strSubscriber);
				}
				} 
				
				if(strBlcOrWhtForLca != null){	 
					if(strBlcOrWhtForLca.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_BLACKLIST_FOR_LCA) || 
						strBlcOrWhtForLca.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_WHITELIST_FOR_LCA)){
						htBlackAndWhiteForLca = ConfigDTO.getDaoDTO().getSubscriptionDao().getBlackWhiteListForLca(con, strSubscriber);
					}
				}
				
				SubscriberDto subDto = new SubscriberDto(
				strSubscriber, 
				mobileNumber, 
				productID,  
				dtStartTime, 
				noOfSmsForToday, 
				htBlackAndWhite, 
				htBlackAndWhiteForLca,
				subscriber.getLanguage(),
				subscriber.getVernSupp(),noOfDaysCharged,chargingStartTime
				);
				
				
				htSubscribersProvisioning.put(strSubscriber, subDto);
				
				String provisioned_mc = prodDto.getMcaAvailable();
				
				
				if(provisioned_mc == null){
					DebugManager.detail(_module,_class, "getSubscriberProvisioning", ">>provisioned_mc is null", Thread.currentThread().getName(), null);
				}
			
			}
		}catch(Exception e){
			DebugManager.exception(_module, _class, "getSubscriberProvisioning", e, Thread.currentThread().getName(), null);
		}
	
		return htSubscribersProvisioning;
	}

	/**
	 * This gets  the Vector of unique subscribers from the concurrent hash map
	 * 
	 * @param conHmSubsWhoGotMC
	 *            a ConcurrentHashMap having subscriber as key and value
	 * @return
	 */
	public Vector<String> getSubscribers(ConcurrentHashMap<String, String> conHmSubsWhoGotMC)
	{
		Vector<String> vecStrSubscsiber = new Vector<String>();
		try{
			DebugManager.status(_module, _class, "getSubscribers"," conHmSubsWhoGotMC.size() : "+conHmSubsWhoGotMC.size()+" "+
		                         DaemonUtils.getCompleteDate(System.currentTimeMillis()), Thread.currentThread().getName(), null);
			Enumeration<String> enumStrSubscriber = conHmSubsWhoGotMC.keys();
			while(enumStrSubscriber.hasMoreElements()){
				String strKey = enumStrSubscriber.nextElement();
				vecStrSubscsiber.add(conHmSubsWhoGotMC.remove(strKey));
			}
		}catch(Exception e){
			DebugManager.exception(_module, _class, "getSubscriber", e, Thread.currentThread().getName(), null);
			return null;
		}

		return vecStrSubscsiber;
	}	
	
	/**
	 * This method creat's sms for the provisioned subscribers and returns it
	 * 
	 * @param mcaMcDto
	 *        it contains the detail of a subscriber
	 * @param htSubscriberProvisioning
	 *        a hash table that contains subscriber as key and SubscriberDtoobject as value
	 * @return
	 *        array of sms texts
	 */
	private String[] createSMSTextForNewRecord(McaMissedcallDto mcaMcDto, 
									Hashtable<String, SubscriberDto> htSubscriberProvisioning){
		String premiumShortCode = ConfigDTO.getMissedCallsDTO().getPremiumShortCode();
		String strSubscriber = mcaMcDto.getSubscriber();
		DebugManager.status(_module, _class, "createSMSTextForNewRecord",">>>>>Entered,strSubscriber="+strSubscriber, Thread.currentThread().getName(), null);
		int productId = -1;
		SubscriberDto subDto = null;
		
		if(htSubscriberProvisioning != null){
			subDto = htSubscriberProvisioning.get(strSubscriber);
			if(subDto != null){
				productId = subDto.getProductID();
			}else{
				productId = ProductDetails.defaultProduct.getProductID();
			}
		}else{
			productId = ProductDetails.defaultProduct.getProductID();
		}
		
		String code = "ENG";
		String promoCode = "ENG";
		if (subDto != null){
		
			if(subDto.getVern_supp().equals("Y")){
				code = subDto.getLanguage();
				promoCode = subDto.getLanguage();
				if (code.equalsIgnoreCase("DEF")){
					code = MCAXMLParser.defaultLangugeCodeMca; // modified by rupesh
					promoCode = MCAXMLParser.defaultPromoLanguageCodeMca; // modified by rupesh
				}
				code = code.toUpperCase();
				promoCode = promoCode.toUpperCase();
				if(!MCAXMLParser.smsTextDetails.containsKey(code)){
					code = "ENG";
				}
				if(!MCAXMLParser.smsTextDetails.containsKey(promoCode)){
					promoCode = "ENG";
				}
			}
		}
		
	
		int totalMissedCallsForReplace = 0;
		int newMissedCalls = 0;
		boolean isPreviousFromTimeAvailable = true;
		String finalIncrementalMessage = null;
		String finalConsolidatedMessage = null;
		String[] arrStrSmsText = new String[2];
		newMissedCalls = mcaMcDto.getCount();
	
		finalIncrementalMessage = replaceValues(
												newMissedCalls,
												premiumShortCode,
												code
												);
		DebugManager.status(_module, _class, "createSMSTextForNewRecord", "#2. the incremental message while inserting to vm_sms is " + finalIncrementalMessage,Thread.currentThread().getName(), null);
			
		finalConsolidatedMessage = replaceValues(
												newMissedCalls,
												premiumShortCode,
												code
												);
		DebugManager.status(_module, _class, "createSMSTextForNewRecord", "#2. the replace message1 while inserting to vm_sms is " + finalConsolidatedMessage,Thread.currentThread().getName(), null);
	
	
		DebugManager.trace(_module, _class, "createSMSTextForNewRecord","ConfigDTO.getAdServerDTO().isEnabled() : "+ConfigDTO.getAdServerDTO().isEnabled(), Thread.currentThread().getName(), null);
		DebugManager.trace(_module, _class, "createSMSTextForNewRecord","ConfigDTO.getAdServerDTO().isMcaEnabled() : "+ConfigDTO.getAdServerDTO().isMcaEnabled(), Thread.currentThread().getName(), null);
		
		//################## This is done for Ad server ##################
		if(ConfigDTO.getAdServerDTO().isEnabled() && ConfigDTO.getAdServerDTO().isMcaEnabled()){
			String responseOfAdServer = "";
			responseOfAdServer = adMessage.hitSite(strSubscriber, finalIncrementalMessage);
			if(responseOfAdServer != null && !responseOfAdServer.equalsIgnoreCase("")){
				finalIncrementalMessage = finalIncrementalMessage + " " + responseOfAdServer;
			}else{
				if(ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString()){
					String strPromoMessage = null;
	
					if(ConfigDTO.getCommonDTO().isLocalPromotionRequired()){
						if(subDto != null && subDto.getDtStartTime() != null)
							strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromotionalMessage(productId, subDto.getDtStartTime().getTime());
						else
							strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromoExpiryMessage(productId);
					}
					
					if(strPromoMessage != null && !strPromoMessage.equalsIgnoreCase("")){
						String tmpFinalIncrementalMessage = finalIncrementalMessage + " " +  strPromoMessage;  
						if(tmpFinalIncrementalMessage.length() + 1 < SMS_SIZE ){
							finalIncrementalMessage = tmpFinalIncrementalMessage;
						}
					}
				}
			}
			responseOfAdServer = "";
			responseOfAdServer = adMessage.hitSite(strSubscriber, finalConsolidatedMessage);
			
			if(responseOfAdServer != null && !responseOfAdServer.equalsIgnoreCase("")){
				finalConsolidatedMessage = finalConsolidatedMessage + " " + responseOfAdServer;
			}else{
				if(ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString()){
					String strPromoMessage = null;
					
					if(ConfigDTO.getCommonDTO().isLocalPromotionRequired()){
						if(subDto != null && subDto.getDtStartTime() != null)
							strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromotionalMessage(productId, subDto.getDtStartTime().getTime());
						else
							strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromoExpiryMessage(productId);
					}
					
					if(strPromoMessage != null && !strPromoMessage.equalsIgnoreCase("")){
						DebugManager.trace(_module, _class, "createSMSTextForNewRecord", "promoMessage : "+strPromoMessage,Thread.currentThread().getName(), null);
						if(isPreviousFromTimeAvailable){
							String tmpFinalConsolidatedMessage = finalConsolidatedMessage + " " + strPromoMessage;
							if(tmpFinalConsolidatedMessage.length() + 1 < SMS_SIZE ){
								finalConsolidatedMessage = tmpFinalConsolidatedMessage;
							}
						}
					}
				}
			}
		}else{
			String strPromoMessage = null;
			
			if(ConfigDTO.getCommonDTO().isLocalPromotionRequired()){
				if(subDto != null && subDto.getDtStartTime() != null)
					strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromotionalMessage(productId, subDto.getDtStartTime().getTime());
				else
					strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromoExpiryMessage(productId);
			}
			
			if(strPromoMessage != null && !strPromoMessage.equalsIgnoreCase("")){
				DebugManager.trace(_module, _class, "createSMSTextForNewRecord", "promoMessage : "+strPromoMessage,Thread.currentThread().getName(), null);
				String tmpFinalIncrementalMessage = finalIncrementalMessage + " " +  strPromoMessage;  
				if(tmpFinalIncrementalMessage.length() + 1 < SMS_SIZE ){
					finalIncrementalMessage = tmpFinalIncrementalMessage;
				}
				
				if(isPreviousFromTimeAvailable){
					String tmpFinalConsolidatedMessage = finalConsolidatedMessage + " " + strPromoMessage;
					if(tmpFinalConsolidatedMessage.length() + 1 < SMS_SIZE ){
						finalConsolidatedMessage = tmpFinalConsolidatedMessage;
					}
				}
			}
		}
		
		arrStrSmsText[0] = finalIncrementalMessage;
		arrStrSmsText[1] = finalConsolidatedMessage;
		DebugManager.trace(_module, _class, "createSMSTextForNewRecord", "finalIncrementalMessage="+finalIncrementalMessage,Thread.currentThread().getName(), null);
		DebugManager.trace(_module, _class, "createSMSTextForNewRecord", "finalConsolidatedMessage="+finalConsolidatedMessage,Thread.currentThread().getName(), null);
		return arrStrSmsText;
	}
	
	
	/**
	 * @param timesCalled
	 *        no of times a missed calls came from the same number
	 * @param callbacknumber
	 *        the no to be called back
	 * @param code
	 *        language code
	 * @return
	 *        sms text
	 */
	public String replaceValues(int timesCalled, String callbacknumber,String code) {
		
		DebugManager.status(_module, _class, "replaceValues", ">>1. timesCalled="+timesCalled +",callbacknumber="+callbacknumber+" code "+code, Thread.currentThread().getName(), null);
				
		String replacedMessage = getReplacedMessage(timesCalled, callbacknumber, code);
		DebugManager.status(_module, _class, "replaceValues", "replacedMessage= "+replacedMessage, Thread.currentThread().getName(), null);
		
		return replacedMessage;
	}
	
	/**
	 * This method calculates the message interval in form of hours and minutes
	 * @return
	 */
	public String getMessageInterval(){
		String messageInterval;
		
		long noOfMinutes = messageDuration/DaemonUtils.oneMinuteInMillis;
		long noOfHours;
		
		if(noOfMinutes >= 60){
			noOfHours = noOfMinutes/60;
			noOfMinutes = noOfMinutes%60;
			
			messageInterval = ""+noOfHours+" hours "+noOfMinutes+" minutes";
		}else{
			messageInterval = ""+noOfMinutes+" minutes"; 
		}
		
		return messageInterval;
	}
	
	
	/**
	 * replaceValues()
	 * Form the mid part of the alert message by replacing the constants.
	 */
	public String getReplacedMessage(int timesCalled, String callbacknumber, String code) {
		
		DebugManager.trace(_module, _class, "getReplacedMessage", ">> timesCalled="+timesCalled +",callbacknumber="+callbacknumber+" MCAXMLParser.smsTextDetails.get(code) "+MCAXMLParser.smsTextDetails.get(code), Thread.currentThread().getName(), null);
		String message = MCAXMLParser.smsTextDetails.get(code).getMcaMissedCallMessageStart();
		
		int pos;
		if((pos = message.indexOf("NUM_TIMES"))!= -1){
			message = message.substring(0,pos-1) + timesCalled + message.substring(pos + NUM_TIMES.length())+" ";
		}

		if(timesCalled == 1){
			message = message + MCAXMLParser.smsTextDetails.get(code).getMcaMissedCallMessageMidOneCall();
		}else if (timesCalled > 1){
			message = message + MCAXMLParser.smsTextDetails.get(code).getMcaMissedCallMessageMidManyCalls();
		}
				
		if((pos = message.indexOf("MESSAGE_DURATION"))!= -1){
			message = message.substring(0,pos-1) + DaemonUtils.getTimeInHoursOrMinutes(messageDuration,MCAXMLParser.smsTextDetails.get(code).
					getMcaMissedCallMessageEndHours()) + message.substring(pos + MESSAGE_DURATION.length());
		}
		
		return message;
	}	
	
}
