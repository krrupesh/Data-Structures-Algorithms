package com.onmobile.apps.ivm.daemons.missedcalls;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.Connection;

import com.onmobile.common.debug.DebugManager;
import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.IDaemonConsts;
import com.onmobile.apps.ivm.daemons.common.LockObjects;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.dto.SubscriberDto;
import com.onmobile.apps.ivm.daemons.sms.VMSMSDispatcher;

public class VMMTThread extends Thread implements IDaemonConsts {
	
	protected String _class = "VMMTThread";
	protected String m_strThreadName = "VMMTThread";
	protected int maxNumOfHrsForReplace = -1;
	protected Connection conSubscriberProvisioning;
	protected String m_strSMSSendingNumber;
	protected int smsCountTokenNumber;
	protected int fromTimeTokenNumber;
	protected long insertedSMSId = 0;
	protected String m_countryCode;
	protected boolean m_bPrependCntryCodeInSmsText;
	protected VMMTDispatcher m_vmmtdisp = null;
	protected int m_thread_id = -1;
	protected String m_reason = MC_REASON_UNREACHABLE;
	protected String m_MissedCallProcessingType = "MCA";
	protected String m_reasons_supported = "'" + MC_REASON_UNREACHABLE + "'";
	protected String[] m_arrPromotionalMessages = null;
	protected int m_promotionalMessageIndex = 0;
	private boolean m_boolStop = false;
	// protected String m_subMCProcessingType = "MCI";
	protected boolean m_silentSMS = false;
	protected VMMTThreadProcessor mcaThreadProcessor = null;
	protected VMMTThreadProcessor mciThreadProcessor = null;
	protected Connection con = null;
            

	public VMMTThread(int threadID, String mcProcessingType, boolean silentSMS) {
		super("VMMTThread #" + threadID);
		m_thread_id = threadID;
		m_strThreadName = Thread.currentThread().getName();
		m_MissedCallProcessingType = mcProcessingType;
		m_silentSMS = silentSMS;

		if (m_MissedCallProcessingType.equalsIgnoreCase("both")
				|| m_MissedCallProcessingType.equalsIgnoreCase("MCA")) {
			mcaThreadProcessor = new VMMCAMTThreadProcessor(threadID,
					m_MissedCallProcessingType, m_silentSMS);
		}

		if (m_MissedCallProcessingType.equalsIgnoreCase("both")
				|| m_MissedCallProcessingType.equalsIgnoreCase("MCI")) {
			mciThreadProcessor = new VMMCIMTThreadProcessor(threadID,
					m_MissedCallProcessingType, m_silentSMS);
		}

		DebugManager.detail(_module, _class, "VMMTThread",
				">>Constructor:m_thread_id=" + m_thread_id, Thread
						.currentThread().getName(), null);
	}

	public VMMTThread(String threadname) {
		super(threadname);
		m_strThreadName = Thread.currentThread().getName();
	}

	public boolean init(int debuglevel, boolean consoleEcho,
			VMSMSDispatcher smsDispatcher, VMMTDispatcher vmmtmcp,
			String reasonsSupported) {
		if (((DebugManager) DebugManager.getDebugManagerObject(debuglevel,
				consoleEcho, _module)) == null) {
			System.out.println("DebugManager.init failed");
		}

		if (m_MissedCallProcessingType.equalsIgnoreCase("both")
				|| m_MissedCallProcessingType.equalsIgnoreCase("MCA")) {
			mcaThreadProcessor.init(debuglevel, consoleEcho, smsDispatcher,
					vmmtmcp, reasonsSupported);
		}
		if (m_MissedCallProcessingType.equalsIgnoreCase("both")
				|| m_MissedCallProcessingType.equalsIgnoreCase("MCI")) {
			mciThreadProcessor.init(debuglevel, consoleEcho, smsDispatcher,
					vmmtmcp, reasonsSupported);
		}

		m_vmmtdisp = vmmtmcp;

		m_reasons_supported = reasonsSupported;

		if (!readConfiguration()) {
			return false;
		}
		conSubscriberProvisioning = VMNotificationDaemon.getConFactory()
				.getConnection();
		return true;
	}

	public Connection getConnectionForSubscriberProvisioning() {
		if (conSubscriberProvisioning != null) {
			return conSubscriberProvisioning;
		} else {
			conSubscriberProvisioning = VMNotificationDaemon.getConFactory()
					.getConnection();
			return conSubscriberProvisioning;
		}
	}

	public void stop(boolean stop) {
		m_boolStop = stop;
	}
	
	
	
	
	public void processMissedCalls(Vector<String> vecStrSubscriber,
			String strMcProcessingType,
			Hashtable<String, SubscriberDto> htSubscribersProvisioning) {
		
		DebugManager.status(_module, _class, "processMissedCalls", "strMcProcessingType : "+strMcProcessingType, Thread.currentThread()
				.getName(), null);
		
		VMMTThreadProcessor mcThreadProcessor = null;

		if (vecStrSubscriber != null && vecStrSubscriber.size() != 0) {
		
			if (strMcProcessingType.equalsIgnoreCase(PROVISIONED_TO_MCA)) { // M
				mcThreadProcessor = mcaThreadProcessor;
			} else if (strMcProcessingType.equalsIgnoreCase(PROVISIONED_TO_MCI)
					|| strMcProcessingType
							.equalsIgnoreCase(PROVISIONED_TO_MCA_JUST_FOR_DSR)) {
				mcThreadProcessor = mciThreadProcessor;
			} else if (strMcProcessingType
					.equalsIgnoreCase(PROVISIONED_TO_MC_NO)) {
				ConfigDTO
						.getDaoDTO()
						.getMissedCallDao()
						.deleteUnWantedMissedcalls(
								VMMTDispatcher.getMtDispatcherDbConnection(),
								vecStrSubscriber);
				return;
			} else if (strMcProcessingType
					.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_MCI_PROMO)) { // P
				mcThreadProcessor = mcaThreadProcessor;
				mcThreadProcessor.sendMcaAlertToPromoteMci(vecStrSubscriber,
						m_thread_id, htSubscribersProvisioning);
				ConfigDTO
						.getDaoDTO()
						.getMissedCallDao()
						.deleteUnWantedMissedcalls(
								VMMTDispatcher.getMtDispatcherDbConnection(),
								vecStrSubscriber);
				return;
			} else if (strMcProcessingType
					.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_MCI_PROMO_WITH_DETAILS)) { // D
				mcThreadProcessor = mcaThreadProcessor;
				mcaThreadProcessor.sendMcaAlertToPromoteMciWithDetails(
						vecStrSubscriber, m_thread_id,
						htSubscribersProvisioning);
				ConfigDTO
						.getDaoDTO()
						.getMissedCallDao()
						.deleteUnWantedMissedcalls(
								VMMTDispatcher.getMtDispatcherDbConnection(),
								vecStrSubscriber);
				return;
			} else if (strMcProcessingType.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_BULK_PROMO)) {
				//mcThreadProcessor = mcaThreadProcessor; // not required we are handling it by separate daemon
				mcaThreadProcessor.handleMcaForBulkPromo(vecStrSubscriber,m_thread_id, htSubscribersProvisioning);
				return;
			}else if (strMcProcessingType.equalsIgnoreCase(PROVISIONED_TO_CHARGING)  && ConfigDTO.getChargingDTO().isEnabled()) {
				mcThreadProcessor = mciThreadProcessor;	
				//UmpHit umpHit = new UmpHit();				
				vecStrSubscriber = ChargingHit.getChargingHit().getAlreadyChargedSubscribers(VMMTDispatcher.getMtDispatcherDbConnection(),mcThreadProcessor,vecStrSubscriber,htSubscribersProvisioning);		
			}
		if(mcThreadProcessor != null){
			mcThreadProcessor.processSubscribers(vecStrSubscriber,m_arrPromotionalMessages, strMcProcessingType,htSubscribersProvisioning);
		}
		}

	}

	public boolean readConfiguration() {
		try {
			m_strSMSSendingNumber = ConfigDTO.getMissedCallsDTO()
					.getSMSCallbackNumber();
			m_countryCode = ConfigDTO.getSmsTextDTO().getCountryCode();
			m_bPrependCntryCodeInSmsText = ConfigDTO.getMissedCallsDTO()
					.isPrependCountryCodeInSMSText();
			// smsCountTokenNumber =
			// ConfigDTO.getSmsTextDTO().getPropertyMC_NUMTIMES_TOKEN_POSITION();
			// fromTimeTokenNumber =
			// ConfigDTO.getSmsTextDTO().getPropertyMC_FROMCALLTIME_TOKEN_POSITION();
			m_reasons_supported = m_vmmtdisp.getReasonsSupported();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}

	public void run() {
		DebugManager.status(_module, _class, "run", "VMMTThread="
				+ Thread.currentThread().getName() + " started. m_boolStop="
				+ m_boolStop, Thread.currentThread().getName(), null);
		Vector<String> vecStrSubscribers = null;
		Vector<String> vMcaSubscribers;
		Vector<String> vMcaToPromoteMciSubscribers;
		Vector<String> vMcaToPromoteMciWithDetailsSubscribers;
		Vector<String> vMciSubscribers;
		Vector<String> vNotSubscribed;
		Vector<String> vMcaToBulkPromoteSubscribers;
		Vector<String>  vChargingSubscribers; 
		Hashtable<String, SubscriberDto> htSubscribersProvisioning = null;

		while (!m_boolStop) {
			con = VMNotificationDaemon.getConFactory()
					.getConnection();
			try {
				vecStrSubscribers = m_vmmtdisp.getSubscribers(m_thread_id);
			    //DebugManager.fatalError(_module, _class, " inside run m_thread_id :"+m_thread_id+" current time "+DaemonUtils.getCompleteDate(System.currentTimeMillis())+" vecStrSubscribers size : "+vecStrSubscribers.size()+" "+vecStrSubscribers, "","", "");

				if (vecStrSubscribers.size() > 0) {
					DebugManager.status(_module, _class, "run", ">>VMMTThread="
							+ Thread.currentThread().getName()
							+ ", size of vecStrSubscribers = "
							+ vecStrSubscribers.size(), Thread.currentThread()
							.getName(), null);
				} else {
					synchronized (LockObjects.getInstance().getLockMc()) {
						LockObjects.getInstance().getLockMc().wait();
					}
				}
				/*
				 * log logging if(vecStrSubscribers.size() > 0){ for(int i=0;
				 * i<vecStrSubscribers.size(); i++){ String strSubscriber =
				 * vecStrSubscribers.get(i); DebugManager.status(_module,
				 * _class, "run",
				 * ">>VMMTThread="+Thread.currentThread().getName()
				 * +", strSubscriber="+strSubscriber,
				 * Thread.currentThread().getName(),null); } }
				 */
				VMMTThreadProcessor mcThreadProcessor = mcaThreadProcessor;
				if (mcThreadProcessor == null) {
					mcThreadProcessor = mciThreadProcessor;
				}
				vMcaSubscribers = new Vector<String>();
				vMcaToPromoteMciSubscribers = new Vector<String>();
				vMcaToPromoteMciWithDetailsSubscribers = new Vector<String>();
				vMciSubscribers = new Vector<String>();
				vNotSubscribed = new Vector<String>();
				vMcaToBulkPromoteSubscribers = new Vector<String>();
                vChargingSubscribers = new Vector<String>();
				if (vecStrSubscribers != null && vecStrSubscribers.size() != 0) {
					// if some subscriber present then do subscriber
					// provisioning
					htSubscribersProvisioning = mcThreadProcessor
							.getSubscriberProvisioning(con, vecStrSubscribers,
									vMciSubscribers, vMcaSubscribers,
									vMcaToPromoteMciSubscribers,
									vMcaToPromoteMciWithDetailsSubscribers,
									vNotSubscribed,
									vMcaToBulkPromoteSubscribers,vChargingSubscribers);
				} else {
					// DebugManager.status(_module,
					// _class,"run","No Records Obtained",Thread.currentThread().getName(),null);
					continue;
				}
				// DebugManager.status(_module, _class, "processMissedCalls",
				// ">>size of Vectors :  vecStrSubscribers="+vecStrSubscribers.size()+",vMCASubscribers="+vMCASubscribers.size()
				// +", vMCAToPromoteMciWithDetailsSubscribers="+vMCAToPromoteMciWithDetailsSubscribers.size()
				// +", vMCAToPromoteMciSubscribers="+vMCAToPromoteMciSubscribers.size()
				// +", vMCISubscribers="+vMCISubscribers.size()
				// +", vNotSubscribed="+vNotSubscribed.size(),
				// Thread.currentThread().getName(), null);

				processMissedCalls(vMcaSubscribers, PROVISIONED_TO_MCA,
						htSubscribersProvisioning);
				processMissedCalls(vMcaToPromoteMciSubscribers,
						PROVISIONED_TO_MCA_FOR_MCI_PROMO,
						htSubscribersProvisioning);
				processMissedCalls(vMcaToPromoteMciWithDetailsSubscribers,
						PROVISIONED_TO_MCA_FOR_MCI_PROMO_WITH_DETAILS,
						htSubscribersProvisioning);
			    //DebugManager.fatalError(_module, _class, "processMissedCalls called inside run : current time "+DaemonUtils.getCompleteDate(System.currentTimeMillis()), "","", "");
				processMissedCalls(vMcaToBulkPromoteSubscribers,
						PROVISIONED_TO_MCA_FOR_BULK_PROMO,
						htSubscribersProvisioning);
			    //DebugManager.fatalError(_module, _class, "processMissedCalls finished inside run : current time "+DaemonUtils.getCompleteDate(System.currentTimeMillis()), "","", "");

				processMissedCalls(vMciSubscribers, PROVISIONED_TO_MCI,
						htSubscribersProvisioning);
				processMissedCalls(vNotSubscribed, PROVISIONED_TO_MC_NO,
						htSubscribersProvisioning);
				processMissedCalls(vChargingSubscribers, PROVISIONED_TO_CHARGING,
						htSubscribersProvisioning);
			} catch (Exception e) {
				DebugManager.exception(_module, _class, "run", e, Thread
						.currentThread().getName(), null);
			} finally {
				VMNotificationDaemon.getConFactory().releaseConnection(con);
			}
		}

	}

}
