package com.onmobile.apps.ivm.daemons.missedcalls;

import java.util.concurrent.LinkedBlockingQueue;

import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.common.debug.DebugManager;

public class MCAGetDumpingThread implements Runnable {

	public static final String _module = "MCA";
	protected static String _class = "MCAGetDumpingThread";

	public static LinkedBlockingQueue<String[]> mcaRecordInfo = new LinkedBlockingQueue<String[]>();

	@Override
	public void run() {
		DebugManager.status(_module, _class, "run","Thread Started !", Thread.currentThread().getName(), null);

		while (true) {

			try {
				String[] mcaInfo = mcaRecordInfo.take();

				long fromTime = Long.parseLong(mcaInfo[0]);
				long toTime = Long.parseLong(mcaInfo[1]);
				String whereCondition = mcaInfo[2];

				DebugManager.status(
						_module,
						_class,
						"run",
						"queue size : " + mcaRecordInfo.size() + "fromTime : "
								+ DaemonUtils.getCompleteDate(fromTime)
								+ " toTime : "
								+ DaemonUtils.getCompleteDate(toTime), Thread
								.currentThread().getName(), null);

				ConfigDTO.getDaoDTO().getMissedCallDao()
						.dumpIntoMcaGet(fromTime, toTime, whereCondition);

			}catch(Exception e){
				DebugManager.exception(_module, _class, "run", e, Thread.currentThread().getName(),null);
			}

		}
	}

}
