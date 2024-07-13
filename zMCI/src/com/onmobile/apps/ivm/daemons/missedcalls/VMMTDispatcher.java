package com.onmobile.apps.ivm.daemons.missedcalls;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.onmobile.apps.ivm.daemons.adServer.AdMessage;
import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.IDaemonConsts;
import com.onmobile.apps.ivm.daemons.common.LockObjects;
import com.onmobile.apps.ivm.daemons.common.ProductDetails;
//import com.onmobile.apps.ivm.daemons.common.PromotionalMessagesDetails;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.config.xmlParsing.MCAXMLParser;
import com.onmobile.apps.ivm.daemons.db.model.MissedCall;
import com.onmobile.apps.ivm.daemons.db.model.Sms;
import com.onmobile.apps.ivm.daemons.db.model.Subscriber;
import com.onmobile.apps.ivm.daemons.dto.ProductDto;
import com.onmobile.apps.ivm.daemons.reporting.novaReporting.CallNovaApi;
import com.onmobile.apps.ivm.daemons.sms.VMSMSDispatcher;
import com.onmobile.common.debug.DebugManager;



public class VMMTDispatcher extends Thread implements IDaemonConsts
{
	protected DebugManager Debug = null;
	protected long m_lTimeBetweenRuns;
	protected long m_lExpireTime;
	protected String m_presentProcessedTime="";
	protected Date m_dtPreviousExpireTime = null;
	protected VMSMSDispatcher m_smsDispatcher;	
	protected String m_strSMSSendingNumber;
	protected String m_mtDispatcherType = "MCI";
	protected String m_MissedCallProcessingType = "MCA";
	protected int m_mtthreadcount = 0;
	protected int m_mtbatchSize = 5;
	//protected int m_lThresholdRecordCount = 100;
	protected long m_lWaitMinutes;
	protected long m_lExpireBeforeHrs;
	protected String m_processingType;
	protected int m_processed_count = 0;
	protected final static String _class = "VMMTDispatcher";
	protected boolean m_bBusyProcessed;
	protected boolean m_bUnreachableProcessed;
	protected boolean m_bUnknownProcessed;
	protected boolean m_bNoAnswerProcessed;
	protected boolean m_bSwitchOffProcessed;
	protected String m_reasons_supported = "'" + MC_REASON_UNREACHABLE + "'";
	protected boolean m_bDBConnectionEstablished;
	protected int m_subscriber_index = -1;
	protected int m_iConnectionRetries = 3;
	protected Date m_minTimeNotProcessed;
	protected final String PROMOTIONAL_MESSAGE = "PROMOTIONAL_MESSAGE";
	protected String[] m_arrPromotionalMessage;
	protected int currentPromotionalMessageIndex = -1;
	private ArrayList<VMMTThread> m_alVMMTThreads = new ArrayList<VMMTThread>();
	private boolean m_dsrEnabledInAllSMSC = false;
	private boolean m_silentEnabled = false;
	protected static final String DISPATCH_SMSC_NAMES = "DISPATCH_SMSC_NAMES";
	protected int m_blackOutTimeFrom;
	protected int m_blackOutTimeTo;
	private boolean m_boolStop = false;
	private static long fromTime;
	private static long toTime;
	private static long retryTime;
	private static Connection mtDispatcherDbConn = null;
	private static Statement mtDispatcherDbStmt = null;
	private static long sleepUpToInMillis = 0;
	private static int tolerance = 100;
	private ConcurrentHashMap<String, String> conHmSubsWhoGotMC = new ConcurrentHashMap<String, String>(500);	
	
	 VMMCAMTMissedCallProcessor mcaMTMissedCallProcessor;
	 MCAGetDumpingThread mcaGetDumpingThread;

	public VMMTDispatcher(String processingType, boolean dsrEnabledInAllSMSC)
	{
		super("VMMTDispatcher");
		m_bDBConnectionEstablished=false;
		m_MissedCallProcessingType = processingType;
		m_dsrEnabledInAllSMSC = dsrEnabledInAllSMSC;
	}


	protected boolean readConfiguration()
	{
		try{			
			m_lWaitMinutes = ConfigDTO.getMissedCallsDTO().getWaitMinutes() * 60*1000;
			m_lExpireBeforeHrs = ConfigDTO.getMissedCallsDTO().getExpiryHours() * 60*60*1000;
			m_lTimeBetweenRuns = ConfigDTO.getMissedCallsDTO().getRunIntervalInSeconds() *1000;
			m_strSMSSendingNumber = ConfigDTO.getMissedCallsDTO().getSMSCallbackNumber();
			m_silentEnabled = ConfigDTO.getCommonDTO().isCommonSilentSMSActive();
			m_mtthreadcount = ConfigDTO.getMissedCallsDTO().getNumberOfWorkerThreads();
			m_mtbatchSize = ConfigDTO.getMissedCallsDTO().getBatchSize();
			//m_lThresholdRecordCount = ConfigDTO.getMissedCallsDTO().getThresholdRecordCount();
			m_bBusyProcessed = ConfigDTO.getMissedCallsDTO().isBusy();
			m_bUnreachableProcessed = ConfigDTO.getMissedCallsDTO().isUnreachable();
			m_bUnknownProcessed = ConfigDTO.getMissedCallsDTO().isUnknown();
			m_bNoAnswerProcessed = ConfigDTO.getMissedCallsDTO().isNoAnswer();
			m_bSwitchOffProcessed = ConfigDTO.getMissedCallsDTO().isSwitchedOff();
			m_reasons_supported = "";
			if(m_bUnreachableProcessed)
				m_reasons_supported += ",'" + MC_REASON_UNREACHABLE + "'";
			if(m_bSwitchOffProcessed)
				m_reasons_supported += ",'" + MC_REASON_SWITCHED_OFF + "'";
			if(m_bBusyProcessed)
				m_reasons_supported += ",'" + MC_REASON_BUSY + "'";
			if(m_bUnknownProcessed)
				m_reasons_supported += ",'" + MC_REASON_UNKNOWN + "'";
			if(m_bNoAnswerProcessed)
				m_reasons_supported += ",'" + MC_REASON_NOANSWER + "'";
			m_reasons_supported = m_reasons_supported.substring(1);

		}catch(Exception e){
			DebugManager.exception(_module, _class, "readConfiguration", e, Thread.currentThread().getName(), null);
			return false;
		}

		return true;
	}


	public String getReasonsSupported()
	{
		return m_reasons_supported;
	}


	public boolean init(int debuglevel, boolean consoleEcho, VMSMSDispatcher smsDispatcher){

		DebugManager.trace(_module, _class, "init", "## init method " , Thread.currentThread().getName(), null);
		if(smsDispatcher != null)
			m_smsDispatcher = smsDispatcher;

		if(!readConfiguration()){
			System.out.println("VMMTDispatcher readConfiguration() return false");
			return false;
		}


		for(int i = 0; i<m_mtthreadcount; i++){
			VMMTThread mtThread = new VMMTThread(
					i, 
					m_MissedCallProcessingType,
					m_silentEnabled && m_dsrEnabledInAllSMSC
			);
			boolean rc = mtThread.init(
					debuglevel, 
					consoleEcho, 
					smsDispatcher, 
					this, 
					m_reasons_supported
			);
			if(!rc){
				System.out.println("mtThread.init() return false");
				return false;
			}
			m_alVMMTThreads.add(mtThread);
			//  m_alVMMTThreads is an ArrayList which contain all the VMMTThread Objects
		}

		mtDispatcherDbConn = VMNotificationDaemon.getConFactory().getConnection(); // why getMtDispatcherDbConnection() was not called here
		try{
			mtDispatcherDbStmt = mtDispatcherDbConn.createStatement();
		}catch(Exception e){
			DebugManager.exception(_module, _class, "init", e, Thread.currentThread().getName(), null);
		}
		return true;
	}

	public static Connection getMtDispatcherDbConnection(){
		if(mtDispatcherDbConn != null){
			DebugManager.trace(_module, "VMMTDispatcher", "getMtDispatcherDbConnection", "got mtDispatcherDbConn connection", Thread.currentThread().getName(), null);
			return mtDispatcherDbConn;
		}else{
			DebugManager.trace(_module, "VMMTDispatcher", "getMtDispatcherDbConnection", "<<<<<mtDispatcherDbConn is null>>>>>", Thread.currentThread().getName(), null);
			mtDispatcherDbConn = VMNotificationDaemon.getConFactory().getConnection();
			return mtDispatcherDbConn;
		}
	}

	public static Statement getMtDispatcherDbStatement(){
		if(mtDispatcherDbStmt != null){
			return mtDispatcherDbStmt;
		}else{
			try{
				mtDispatcherDbStmt = getMtDispatcherDbConnection().createStatement();
			}catch(Exception e){
				DebugManager.exception(_module, "VMMTDispatcher", "getMtDispatcherDbStatement", e, Thread.currentThread().getName(), null);
			}
			return mtDispatcherDbStmt;
		}
	}


	public void stop(boolean stop){
		m_boolStop = stop;
		for (int i = 0; i<m_mtthreadcount; i++) {
			VMMTThread mtThread = (VMMTThread)m_alVMMTThreads.get(i);
			mtThread.stop(m_boolStop);
		}
		
		mcaMTMissedCallProcessor.stop(m_boolStop);
	}



	public void handleCallersFirst(List<MissedCall> mcRevMcaList, long toTime, long fromTime)
	{
		Vector<Sms> vecSms = new Vector<Sms>();
		Hashtable<String,String> ht = getUniqueCallersWhoGotMC(mcRevMcaList);
		DebugManager.status(_module, _class, "handleCallersFirst", ">>>ht="+ht, Thread.currentThread().getName(), null);
		Vector<String> vecLocalPromoMessages = null;
		String originalReverseMcaMessage = MCAXMLParser.smsTextDetails.get("ENG").getReverseMcaMessage();
		String originalReverseMcaMessageWithPromo = null;
		ConfigDTO.getDaoDTO().getMissedCallDao().initReverseMcaPrefixes(getMtDispatcherDbConnection());
		if(originalReverseMcaMessage == null){
			originalReverseMcaMessage = "Hello! I am presently not reachable. Please call me later.";
		}
		DebugManager.status(_module, _class, "handleCallersFirst", "originalReverseMcaMessage="+originalReverseMcaMessage, Thread.currentThread().getName(), null);

		if(!ConfigDTO.getAdServerDTO().isReverseMcaEnabled()){
			if(ConfigDTO.getAdServerDTO().isLocalpromotionForReverseMca()){
				vecLocalPromoMessages = ConfigDTO.getDaoDTO().getMissedCallDao().getLocalPromoMsgsForReverseMca(getMtDispatcherDbConnection());
			}
		}
		try{
			Enumeration<String> enumMC = ht.keys();
			if(enumMC != null)
			{
				while(enumMC.hasMoreElements())
				{
					String smscForReverseMCA = null;
					String subs_caller = (String) enumMC.nextElement();
					if(subs_caller != null){
						StringTokenizer strTokenizer = new StringTokenizer(subs_caller, "_");
						if(strTokenizer != null)
						{
							String subscriber = strTokenizer.nextToken();
							String caller = strTokenizer.nextToken();
							smscForReverseMCA = (String)DigitalTrie.getInstance().getBestMatch(caller);

							if(smscForReverseMCA != null)
							{
								DebugManager.status(_module, _class, "handleCallersFirst", ">>>caller="+caller +",best match="+DigitalTrie.getInstance().getBestMatch(caller), Thread.currentThread().getName(), null);


								if(ConfigDTO.getMissedCallsDTO().isNeedToPrependCountryCodeForCallBackNumberForReverseMca()){
									subscriber = "91" + subscriber;
								}
								if(ConfigDTO.getMissedCallsDTO().isNeedToPrependPlusForCallBackNumberForReverseMca()){
									subscriber = "+" + subscriber;
								}

								if(ConfigDTO.getMissedCallsDTO().isNeedToPrependCountryCodeForCallerForReverseMca()){
									caller = "91" + caller;
								}
								if(ConfigDTO.getMissedCallsDTO().isNeedToPrependPlusForCallerForReverseMca()){
									caller = "+" + caller;
								}

								if(ConfigDTO.getAdServerDTO().isEnabled())
								{
									if(ConfigDTO.getAdServerDTO().isReverseMcaEnabled())
									{
										originalReverseMcaMessageWithPromo = AdMessage.getInstance().hitSite(caller, originalReverseMcaMessage);
										if(originalReverseMcaMessageWithPromo != null)
										{
											if(originalReverseMcaMessageWithPromo.trim().equalsIgnoreCase(originalReverseMcaMessage.trim()))
											{
												DebugManager.trace(_module, _class, "handleCallersFirst", ">>>caller="+caller +",addServerReverseMcaMessage and originalReverseMcaMessage are same, means addServer not appended any message.", Thread.currentThread().getName(), null);
												if(ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString())
												{
													if(ConfigDTO.getAdServerDTO().isLocalpromotionForReverseMca())
													{
														if(vecLocalPromoMessages.size() > 0)
														{
															long currentTimeInMillis = System.currentTimeMillis();
															int index = (int)((currentTimeInMillis)%(vecLocalPromoMessages.size()));
															String promoMsg = (String)vecLocalPromoMessages.get(index);
															DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>currentTimeInMillis="+currentTimeInMillis +",size of vecLocalPromoMessages="+vecLocalPromoMessages.size() +",index="+index +",promoMsg="+promoMsg,Thread.currentThread().getName(),null);
															if(originalReverseMcaMessage.length() + promoMsg.length() <160){
																originalReverseMcaMessageWithPromo = originalReverseMcaMessage + promoMsg;
															}else{
																originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
																DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>originalReverseMcaMessage + promoMsg length is >= 160.",Thread.currentThread().getName(),null);
															}
														}else{
															originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
															DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>vecLocalPromoMessages size is 0. No entries in VM_PROMO_MESSAGES_REVERSE_MCA.",Thread.currentThread().getName(),null);
														}
													}
												}
											}
										}else{
											if(ConfigDTO.getAdServerDTO().isLocalpromotionForReverseMca()){
												if(vecLocalPromoMessages.size() > 0){
													long currentTimeInMillis = System.currentTimeMillis();
													int index = (int)((currentTimeInMillis)%(vecLocalPromoMessages.size()));
													String promoMsg = (String)vecLocalPromoMessages.get(index);
													DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>currentTimeInMillis="+currentTimeInMillis +",size of vecLocalPromoMessages="+vecLocalPromoMessages.size() +",index="+index +",promoMsg="+promoMsg,Thread.currentThread().getName(),null);
													if(originalReverseMcaMessage.length() + promoMsg.length() <160){
														originalReverseMcaMessageWithPromo = originalReverseMcaMessage + promoMsg;
													}else{
														originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
														DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>originalReverseMcaMessage + promoMsg length is >= 160.",Thread.currentThread().getName(),null);
													}
												}else{
													originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
													DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>vecLocalPromoMessages size is 0. No entries in VM_PROMO_MESSAGES_REVERSE_MCA.",Thread.currentThread().getName(),null);
												}
											}else{
												originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
											}
										}
									}else{
										if(ConfigDTO.getAdServerDTO().isLocalpromotionForReverseMca()){
											if(vecLocalPromoMessages.size() > 0){
												long currentTimeInMillis = System.currentTimeMillis();
												int index = (int)((currentTimeInMillis)%(vecLocalPromoMessages.size()));
												String promoMsg = (String)vecLocalPromoMessages.get(index);
												DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>currentTimeInMillis="+currentTimeInMillis +",size of vecLocalPromoMessages="+vecLocalPromoMessages.size() +",index="+index +",promoMsg="+promoMsg,Thread.currentThread().getName(),null);
												if(originalReverseMcaMessage.length() + promoMsg.length() <160){
													originalReverseMcaMessageWithPromo = originalReverseMcaMessage + promoMsg;
												}else{
													originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
													DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>originalReverseMcaMessage + promoMsg length is >= 160.",Thread.currentThread().getName(),null);
												}
											}else{
												originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
												DebugManager.detail(_module, _class, "handleCallersFirst",">>>>>vecLocalPromoMessages size is 0. No entries in VM_PROMO_MESSAGES_REVERSE_MCA.",Thread.currentThread().getName(),null);
											}
										}else{
											originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
										}
									}
								}
								else if(ConfigDTO.getAdServerDTO().isLocalpromotionForReverseMca())
								{
									if(vecLocalPromoMessages.size() > 0)
									{
										long currentTimeInMillis = System.currentTimeMillis();
										int index = (int)((currentTimeInMillis)%(vecLocalPromoMessages.size()));
										String promoMsg = (String)vecLocalPromoMessages.get(index);
										DebugManager.status(_module, _class, "handleCallersFirst",">>>>>currentTimeInMillis="+currentTimeInMillis +",size of vecLocalPromoMessages="+vecLocalPromoMessages.size() +",index="+index +",promoMsg="+promoMsg,Thread.currentThread().getName(),null);
										if(originalReverseMcaMessage.length() + promoMsg.length() <160){
											originalReverseMcaMessageWithPromo = originalReverseMcaMessage + promoMsg;
										}else{
											originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
											DebugManager.status(_module, _class, "handleCallersFirst",">>>>>originalReverseMcaMessage + promoMsg length is >= 160.",Thread.currentThread().getName(),null);
										}
									}else{
										originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
										DebugManager.status(_module, _class, "handleCallersFirst",">>>>>vecLocalPromoMessages size is 0. No entries in VM_PROMO_MESSAGES_REVERSE_MCA.",Thread.currentThread().getName(),null);
									}
								}
								else
								{
									originalReverseMcaMessageWithPromo = originalReverseMcaMessage;
								}


								Sms sms = new Sms(
										caller, 
										MESSAGE_TYPE_REVERSE_MCA, 
										SMS_STATUS_CREATED, 
										originalReverseMcaMessageWithPromo, 
										subscriber, 
										originalReverseMcaMessageWithPromo, 
										false, 
										smscForReverseMCA
								);

								sms.setProdName("RMCA");
								sms.setUnicode(false);
								vecSms.add(sms);
							}else{
								DebugManager.status(_module, _class, "handleCallersFirst", ">>>no best match found for caller="+caller, Thread.currentThread().getName(), null);
							}
						}
					}
				}
				ConfigDTO.getDaoDTO().getSmsDao().saveSms(VMMTDispatcher.getMtDispatcherDbConnection(), vecSms);
				ConfigDTO.getDaoDTO().getMissedCallDao().updateMissedcallsIfReverseMca(VMMTDispatcher.getMtDispatcherDbConnection());

			}
		}catch(Exception e){
			DebugManager.exception(_module,_class, "handleCallersFirst", e, Thread.currentThread().getName(), null);
		}

	}


	public String getTenDigitMobileNumber(String sub){
		if(sub.length() == 10){
			return sub;
		}else{
			if(sub.startsWith("91")){
				sub = sub.substring(2, sub.length());	
				return sub;
			}
			else if(sub.startsWith("+91")){
				sub = sub.substring(3,sub.length());
				return sub;
			}else if(sub.startsWith("0")){
				sub = sub.substring(1,sub.length());
				return sub;
			}
			else{
				return sub;
			}
		}
	}



	public void mtDispatcherDoze(){
		synchronized(LockObjects.getInstance().getLockMc()){ // why to notify always if some one is not waiting then
			LockObjects.getInstance().getLockMc().notifyAll();
		}

		if(VMMTDispatcher.sleepUpToInMillis > System.currentTimeMillis()){
			DebugManager.detail(_module, _class, "mtDispatcherDoze", "sleepUpTo for MTDispatcher is more than currTime.", Thread.currentThread().getName(),null);
			try{
				Thread.sleep( (VMMTDispatcher.sleepUpToInMillis - System.currentTimeMillis() ));
			}catch(Exception e){
				DebugManager.exception(_module, _class, "mtDispatcherDoze", e, Thread.currentThread().getName(), null);
			}
		}else{
			DebugManager.detail(_module, _class, "mtDispatcherDoze", "sleepUpTo for MTDispatcher is not more than currTime.", Thread.currentThread().getName(),null);
		}

	}

	public static long getFromTime() {
		return fromTime;
	}

	public static void setFromTime(long fromTime) {
		DebugManager.status(_module, _class, "setFromTime", "fromTime="+fromTime
				, Thread.currentThread().getName(), null);
		VMMTDispatcher.fromTime = fromTime;
	}
	public static long getToTime() {
		return toTime;
	}

	public static void setToTime(long toTime) {
		DebugManager.status(_module, _class, "setToTime", "toTime="+toTime
				, Thread.currentThread().getName(), null);

		VMMTDispatcher.toTime = toTime;
	}

	public static long getTime() {
		return retryTime;
	}

	public static void setTime(long time) {
		DebugManager.status(_module, _class, "setretryTime", "retryTime="+time
				, Thread.currentThread().getName(), null);

		VMMTDispatcher.retryTime = time;
	}


	public ConcurrentHashMap<String, String> getUniqueSubsWhoGotMC(List<MissedCall> listMC){
		ConcurrentHashMap<String, String> conHm = new ConcurrentHashMap<String, String>();
		if(listMC != null){
			Iterator<MissedCall> itr = listMC.iterator();
			//int count = 0;
			while(itr.hasNext()){
				//count++;
				MissedCall mc = itr.next();
				String strSubscriber = mc.getSubscriber();
				//DebugManager.status(_module, _class, "getUniqueSubsWhoGotMC", "#"+count +". strSubscriber="+strSubscriber, Thread.currentThread().getName(), null);
				conHm.put(strSubscriber, strSubscriber);
			}
		}
		DebugManager.status(_module, _class, "getUniqueSubsWhoGotMC", "size of conHm = "+conHm.size(), Thread.currentThread().getName(), null);
		return conHm;
	}

	public Hashtable<String, String> getUniqueCallersWhoGotMC(List<MissedCall> listMC){
		Hashtable<String, String> ht = new Hashtable<String, String>();
		if(listMC != null){
			Iterator<MissedCall> itr = listMC.iterator();
			while(itr.hasNext()){
				MissedCall mc = itr.next();
				String subscriber = mc.getSubscriber();
				String caller = mc.getCaller();
				subscriber = getTenDigitMobileNumber(subscriber);
				caller = getTenDigitMobileNumber(caller);
				String key = subscriber + "_" + caller;
				ht.put(key, key);
			}
		}

		return ht;
	}


	public Vector<String> getSubscribers(int threadid)
	{
		Vector<String> vecStrSubscsiber = new Vector<String>();
		try{
			//DebugManager.status(_module, _class, "getSubscribers", "#0. size()="+htSubscribersWhoGotMissedcall.size(), Thread.currentThread().getName(), null);
			if(conHmSubsWhoGotMC.size() >= ConfigDTO.getMissedCallsDTO().getBatchSize()){
				//DebugManager.status(_module, _class, "getSubscribers", "#1. size()="+htSubscribersWhoGotMissedcall.size(), Thread.currentThread().getName(), null);
				Enumeration<String> enumStrSubscriber = conHmSubsWhoGotMC.keys();
				int count = 0;
				while(enumStrSubscriber.hasMoreElements()){
					count++;
					if(count>m_mtbatchSize){
						break;
					}
					String strKey = enumStrSubscriber.nextElement();
					vecStrSubscsiber.add(conHmSubsWhoGotMC.remove(strKey));
				}
			}else{
				//DebugManager.status(_module, _class, "getSubscribers", "#2. size()="+htSubscribersWhoGotMissedcall.size(), Thread.currentThread().getName(), null);
				Enumeration<String> enumStrSubscriber = conHmSubsWhoGotMC.keys();
				while(enumStrSubscriber.hasMoreElements()){
					String strKey = enumStrSubscriber.nextElement();
					vecStrSubscsiber.add(conHmSubsWhoGotMC.remove(strKey));
				}
			}
		}catch(Exception e){
			DebugManager.exception(_module, _class, "getSubscriber", e, Thread.currentThread().getName(), null);
			return null;
		}

		return vecStrSubscsiber;
	}


	public void run(){
		try{
			//DebugManager.trace(_module, _class, "run", ">>VMMTDispatcher thread started " , Thread.currentThread().getName(), null);
			DebugManager.status(_module, _class, "run", "VMMTDispatcher thread started", Thread.currentThread().getName(), null);
			
			// added by rupesh
			if(MCAXMLParser.isMCAEnabeled){			
				mcaMTMissedCallProcessor = new VMMCAMTMissedCallProcessor(getReasonsSupported());
				VMMCAMTMissedCallProcessor.isServerStartUpFirstTime = true;
				VMMCAMTMissedCallProcessor.serverStartUpTime = System.currentTimeMillis();
				
				// start here VMMCAMTMissedCallProcessor thread
				Thread mcaProcessorThread = new Thread(mcaMTMissedCallProcessor);
				mcaProcessorThread.start();
				
				mcaGetDumpingThread = new MCAGetDumpingThread();
				Thread mcaGetThread = new Thread(mcaGetDumpingThread);
				mcaGetThread.start();				
				
				DebugManager.status(_module, _class, "run", "MCA Daemon Thread started", Thread.currentThread().getName(), null);
			}else{
				DebugManager.fatalError(_module, _class, "run", "MCA Daemon Thread is not started", Thread.currentThread().getName(), null);
			}

			
			long firstStartTime = System.currentTimeMillis();
			VMMTDispatcher.sleepUpToInMillis = firstStartTime + m_lTimeBetweenRuns + VMMTDispatcher.tolerance;
			/*if(ConfigDTO.getCommonDTO().isLocalPromotionRequired()){
				PromotionalMessagesDetails.getPromotionalMessagesDetails();
			}*/
			//DebugManager.trace(_module, _class, "run", ">>return from getPromotionalMessagesDetails" , Thread.currentThread().getName(), null);

			for(int i = 0; i<m_mtthreadcount; i++){
				VMMTThread mtThread = m_alVMMTThreads.get(i);
				mtThread.start(); 
			}

			while(!m_boolStop)
			{
				/*if(ConfigDTO.getCommonDTO().isLocalPromotionRequired()){
					PromotionalMessagesDetails.getPromotionalMessagesDetails();
				}*/
				
				//check if smsText config has changed - assume PromoDetails would be changed, that too rarely
				/*if (MCAXMLParser.smsTextFile != null) {
					long currentTimeStamp = MCAXMLParser.smsTextFile.lastModified();
					if (currentTimeStamp != MCAXMLParser.storedTimeStamp) {
						//re-parse the SMSText configuration for PromoDetails only for now.
						//change model so that this thread is not held up & entire smsText Config is re-parsed 
						MCAXMLParser.storedTimeStamp = currentTimeStamp;
						//re-parse file and store
					}
				}*/
				
				long time = 0;
				DebugManager.status(_module, _class, "run", "Charging enabled "+ConfigDTO.getChargingDTO().isEnabled(), Thread.currentThread().getName(), null);
			
				 if(ConfigDTO.getChargingDTO().isEnabled()){
					DebugManager.status(_module, _class, "run", "Charging enabled ,Setting RetryIntervalInMins", Thread.currentThread().getName(), null);
					 time = System.currentTimeMillis() - (ConfigDTO.getChargingDTO().getRetryIntervalInMins() * 60 * 1000);
				 }
			
				long startTime = System.currentTimeMillis();
				long waitMinutes = ConfigDTO.getMissedCallsDTO().getWaitMinutes()*60*1000;
				setTime(time);
				setToTime(startTime - waitMinutes);
				setFromTime(startTime - (ConfigDTO.getMissedCallsDTO().getExpiryHours() * 60*60*1000 +	ConfigDTO.getMissedCallsDTO().getRunIntervalInSeconds()*1000));
				List<MissedCall> mcRevMcaList = ConfigDTO.getDaoDTO().getMissedCallDao().getUnProcessedMissedcalls(getMtDispatcherDbConnection(), m_reasons_supported);
				DebugManager.status(_module, _class, "run", "size of mcRevMcaList="+mcRevMcaList.size(), Thread.currentThread().getName(), null);

				if(ConfigDTO.getMissedCallsDTO().isReverseMca()){
					DebugManager.status(_module, _class, "run", "Reverse MCA is true.", Thread.currentThread().getName(), null);
					handleCallersFirst(mcRevMcaList, toTime, fromTime);
				}
				
				conHmSubsWhoGotMC = getUniqueSubsWhoGotMC(mcRevMcaList);
								
				//Reporting - All incoming requests barring unnecessary callers - including product Name
				Vector<String> uniqueSubscribers = new Vector<String>();
				if(conHmSubsWhoGotMC.size() > 0){
					Enumeration<String> enumStrSubscriber = conHmSubsWhoGotMC.keys();
					while(enumStrSubscriber.hasMoreElements()){
						String strKey = enumStrSubscriber.nextElement();
						uniqueSubscribers.add(strKey);
					}
				}
				ConcurrentHashMap<String, Integer> subsProdMap ;
				subsProdMap = ConfigDTO.getDaoDTO().getSubscriptionDao().getSubscriberProduct(getMtDispatcherDbConnection(), uniqueSubscribers);
				MissedCall mc;	String strSubscriber;	Integer prodId;	ProductDto prodDto;	String prodName; String caller;
				if(mcRevMcaList != null){
					Iterator<MissedCall> itr = mcRevMcaList.iterator();
					while(itr.hasNext()){
						mc = itr.next();
						strSubscriber = mc.getSubscriber();
						prodId = subsProdMap.get(strSubscriber);
						prodDto = null;
						if(prodId != null){
							prodDto = ProductDetails.htProductIdToProduct.get(prodId);
						}
						if(prodDto == null)	prodDto = ProductDetails.defaultProduct;
						prodName = prodDto.getProductName();
						caller = mc.getCaller();
						//log called time, caller, subscriber, product name
						CallNovaApi.logDetailedMissedCallEvent(new java.util.Date(System.currentTimeMillis()), strSubscriber,
								caller, null, null,prodName);
						
					}
				}
				//End of Reporting
				
				VMMTDispatcher.sleepUpToInMillis = startTime + m_lTimeBetweenRuns;
				mtDispatcherDoze();
			}
		}catch(Exception e){
			DebugManager.exception(_module,_class, "run", e, Thread.currentThread().getName(), null);
		}
	}


}




