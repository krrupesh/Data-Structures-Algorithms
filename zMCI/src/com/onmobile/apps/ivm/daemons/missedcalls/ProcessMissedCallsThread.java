package com.onmobile.apps.ivm.daemons.missedcalls;

import java.sql.Connection;
import java.util.Timer;

import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.common.debug.DebugManager;

/**
 * This thread process all the missed calls from vm_missedcalls_mca table and delete it

 * @author rupesh.kumar
 *
 */
public class ProcessMissedCallsThread implements Runnable{
	
	protected String _class = "ProcessMissedCallsThread";
	public static final String _module = "MCA";

	VMMCAMTMissedCallProcessor vMMCAMTMissedCallProcessor;
	Timer timer;
	long toTime;
	String whereCondition="";
	
	public ProcessMissedCallsThread(
			VMMCAMTMissedCallProcessor vMMCAMTMissedCallProcessor, long toTime,Timer timer) {
		super();
		this.vMMCAMTMissedCallProcessor = vMMCAMTMissedCallProcessor;
		this.timer = timer;
		this.toTime = toTime;
	}

	@Override
	public void run() {
		vMMCAMTMissedCallProcessor.runHelper(toTime, timer);
	}

}
