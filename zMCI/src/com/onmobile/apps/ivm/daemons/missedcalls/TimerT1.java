package com.onmobile.apps.ivm.daemons.missedcalls;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlParsing.MCAXMLParser;
import com.onmobile.common.debug.DebugManager;

/**
 * @author rupesh.kumar
 * 
 * This class a timer task which callback method is called at every 5 minute
 *
 */
public class TimerT1 extends TimerTask {
	
	protected String _class = "TimerT1";
	public static final String _module = "MCA";
	private boolean timerStartedFirstTime = true;
	public static boolean firstRunAfterBlackout = true;
	public static boolean createTimerForSmallerBlackoutDuration = true;
	public static Timer schedulTaskTimer = null;

	
	VMMCAMTMissedCallProcessor vMMCAMTMissedCallProcessor;

	public TimerT1(VMMCAMTMissedCallProcessor vMMCAMTMissedCallProcessor) {
        this.vMMCAMTMissedCallProcessor = vMMCAMTMissedCallProcessor;
	}
	
	@Override
	public void run() {
		DebugManager.status(_module, _class, "run","", Thread.currentThread().getName(), null);

		try {
			long currTime = System.currentTimeMillis();
			
			if(timerStartedFirstTime){
				VMMCAMTMissedCallProcessor.scheduletimerstartedfirsttime = true;
				long bootuptime = getBootTimeAfterServerStartUp(currTime);
				
				DebugManager.status(_module, _class, "run","bootuptime "+bootuptime+" currTime "+currTime, Thread.currentThread().getName(), null);
		        
				// create scheduler outside blackout period only
		        if(!((currTime >= VMMCAMTMissedCallProcessor.blackOutStartTimeInMillis) && (currTime < (VMMCAMTMissedCallProcessor.blackOutEndTimeInMillis + 5*DaemonUtils.oneMinuteInMillis)))){

		        	// if boot up time is greater than current time then only start timer first time
		        	if(bootuptime >= currTime){
		        		
		        		long delay = bootuptime - currTime;
		        		DebugManager.status(_module, _class, "run","timerStartedFirstTime delay to start Timer SeduleTask "+delay, Thread.currentThread().getName(), null);
		        		
		        		createSheduleTimer(delay); // first start up at boot up time
		        	}		        	
		        }				
		        timerStartedFirstTime = false;				
			}
			
			VMMCAMTMissedCallProcessor.initializeBlackoutStartEndTime();
			DebugManager.status(_module, _class, "run","blackout end "+DaemonUtils.getCompleteDate(VMMCAMTMissedCallProcessor.blackOutEndTimeInMillis)+" currTime "+DaemonUtils.getCompleteDate(currTime), Thread.currentThread().getName(), null);

			// after blackout period start timer to process records at fixed interval of time
			if((VMMCAMTMissedCallProcessor.blackOutEndTimeInMillis <= currTime) && 
					(currTime < VMMCAMTMissedCallProcessor.blackOutEndTimeInMillis + 5*DaemonUtils.oneMinuteInMillis)){
				
				DebugManager.status(_module, _class, "run","timer started after comming out of black out period ", Thread.currentThread().getName(), null);
				
				if(!(MCAXMLParser.blackOutSleepDurationInMillis < VMMCAMTMissedCallProcessor.timeIntervalInMillis)){
					// at any time only one timer should be running
					if(schedulTaskTimer != null){
						schedulTaskTimer.cancel();
						schedulTaskTimer = null;
					}
					
					firstRunAfterBlackout = true;
					createSheduleTimer(0L);
				}
			}	
		} catch (Exception e) {
			DebugManager.exception(_module, _class, "run", e, Thread.currentThread().getName(), null);
		}
			
	}
	
	/**
	 * This method creat's a timer to process the missed calls
	 * 
	 * @param delay
	 *         delay to start the timer
	 */
	private void createSheduleTimer(Long delay){
		if(schedulTaskTimer == null){
			DebugManager.status(_module, _class, "createSheduleTimer","timer ScheduledTask started with delay "+delay, Thread.currentThread().getName(), null);
			Timer timer = new Timer(); 
			ScheduledTask st = new ScheduledTask(vMMCAMTMissedCallProcessor,timer);
			timer.scheduleAtFixedRate(st, delay, VMMCAMTMissedCallProcessor.timeIntervalInMillis);
			schedulTaskTimer = timer;
		}else{
			DebugManager.status(_module, _class, "createSheduleTimer","Timer is already running !", Thread.currentThread().getName(), null);
		}
	}
	
	/**
	 * This method get the time till which records has to be processed at server startup
	 * 
	 * @param currTime
	 *      current system time
	 * @return
	 *      time till which records has to be processed at server start up
	 */
	public long getBootTimeAfterServerStartUp(Long currTime){
		List<Long> list = VMMCAMTMissedCallProcessor.serverStartuptoTimeList;
		DebugManager.status(_module, _class, "getBootTimeAfterServerStartUp","list size "+list.size()+" "+list, Thread.currentThread().getName(), null);

		int i = 0;
		long bootupTime = 0;
		if (list.size() > 0) {
			bootupTime =  list.get(0);
			while (list.get(i) <= currTime) {
				i++;
				if (i == list.size()) {
					break;
				}
				bootupTime = list.get(i);
			}
			list.clear();
		}
		VMMCAMTMissedCallProcessor.serverStartuptoTimeList.clear();
		
		DebugManager.status(_module, _class, "getBootTimeAfterServerStartUp","boot time after server start up"+DaemonUtils.getCompleteDate(bootupTime), 
				                              Thread.currentThread().getName(), null);

		return bootupTime;
	}

}
