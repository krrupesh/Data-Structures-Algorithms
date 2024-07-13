package com.onmobile.apps.ivm.daemons.missedcalls;

import java.sql.Connection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.config.xmlParsing.MCAXMLParser;
import com.onmobile.common.debug.DebugManager;

/**
 * This class process the missed calls at regular interval of time
 * @author rupesh.kumar
 *
 */
public class ScheduledTask extends TimerTask {
	
	protected String _class = "ScheduledTask";
	public static final String _module = "MCA";
	
	Timer timer;
	VMMCAMTMissedCallProcessor vMMCAMTMissedCallProcessor;

	public ScheduledTask(VMMCAMTMissedCallProcessor vMMCAMTMissedCallProcessor, Timer timer) {
        this.vMMCAMTMissedCallProcessor = vMMCAMTMissedCallProcessor;
        this.timer = timer;
	}
	
	@Override
	public void run() {		
		
		createNewThreadsToGetOlderRecordTime();
		
		try {		
			vMMCAMTMissedCallProcessor.startThread(System.currentTimeMillis(), timer);			
		} catch (Exception e) {
			DebugManager.exception(_module, _class, "run", e, Thread.currentThread().getName(), null);
		}
	}
	
	
	public void createNewThreadsToGetOlderRecordTime(){
		
		ExecutorService executor = Executors.newSingleThreadExecutor();

		executor.execute(new Runnable() {
			@Override
			public void run() {
				checkForOlderMCARecords();
			}
		});
		
		executor.shutdown();
	}
	
	public void checkForOlderMCARecords(){
		
		Connection con = null;
		
		try {		
			// check if records are older than double of blackout duration	
			con = VMNotificationDaemon.getConFactory().getConnection();
			long oldestTime = ConfigDTO.getDaoDTO().getMissedCallDao().getOldestRecordCalledtimeFromMCATable(con);
			long currentTime = System.currentTimeMillis();
			
			if(oldestTime != 0){				
				if((currentTime - oldestTime) > 2*MCAXMLParser.blackOutSleepDurationInMillis){
					DebugManager.fatalError(_module, _class, "checkForOlderMCARecords","MCA Missed calls came at "+DaemonUtils.getCompleteDate(oldestTime)+" has not been process since "+DaemonUtils.getTimeInHHMMSSFormat(currentTime - oldestTime), Thread.currentThread().getName(), null);
				}
			}
			
		} catch (Exception e) {
			DebugManager.exception(_module, _class, "run", e, Thread.currentThread().getName(), null);
		}finally{
			VMNotificationDaemon.getConFactory().releaseConnection(con);
		}
	}
}
