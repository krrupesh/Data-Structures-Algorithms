package com.onmobile.apps.ivm.daemons.missedcalls;

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.common.debug.DebugManager;

public class MissedCallDumpingThread extends TimerTask
{
	
	public final static long INTERVAL = 10*1000;

	public void run()
	{
		DebugManager.status("MCA", "MissedCallDumpingThread", "run", "Entered..", Thread.currentThread().getName(), null);
		long startTime = System.currentTimeMillis();
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		ConfigDTO.getDaoDTO().getMissedCallDao().dumpMciMissedCalls(con);
		VMNotificationDaemon.getConFactory().releaseConnection(con);
		long endTime = System.currentTimeMillis();
		DebugManager.status("MCA", "MissedCallDumpingThread", "run", "TIME TAKEN ="+(endTime-startTime) +" milli seconds.", Thread.currentThread().getName(), null);
	}


	public static void start(){
		TimerTask timerTask = new MissedCallDumpingThread();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, INTERVAL, INTERVAL);
	}

	public static void main(String[] args)
	{
		MissedCallDumpingThread.start();
	}
}
