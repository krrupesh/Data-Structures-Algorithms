package com.onmobile.apps.ivm.daemons.missedcalls;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;
import com.onmobile.common.debug.DebugManager;
import com.onmobile.apps.ivm.daemons.common.IDaemonConsts;
import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.ProductDetails;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.config.xmlParsing.MCAXMLParser;
import com.onmobile.apps.ivm.daemons.db.model.Sms;
import com.onmobile.apps.ivm.daemons.db.model.Subscriber;
import com.onmobile.apps.ivm.daemons.dto.McaMissedcallDto;
import com.onmobile.apps.ivm.daemons.dto.ProductDto;
import com.onmobile.apps.ivm.daemons.dto.SubscriberDto;
import com.onmobile.apps.ivm.daemons.sms.VMSMSDispatcher;


public abstract class VMMTThreadProcessor implements IDaemonConsts 
{
	protected static String _class = "VMMTThreadProcessor";
	protected String m_strThreadName = "VMMTThreadProcessor";
	protected int maxNumOfHrsForReplace = -1;
	protected int m_iConnectionRetries = 3;
	protected Statement stmt;
	protected int smsCountTokenNumber;
	protected int fromTimeTokenNumber;
	protected long insertedSMSId = 0;
	protected String m_countryCode;
	protected boolean m_bPrependCntryCodeInSmsText; 
	protected VMMTDispatcher m_vmmtdisp = null;
	protected int m_thread_id = -1;
	protected String m_reason = MC_REASON_UNREACHABLE;
	protected String m_MissedCallProcessingType = "MCA";
	protected String m_missedCallType = "M";
	protected String m_reasons_supported = "'" + MC_REASON_UNREACHABLE + "'";
	protected final String NUM_OF_TIMES = "%NUMTIMES%";
	protected final String FROMTIME = "%FROM_CALL_TIME%";
	protected final String TOTIME = "%TO_CALL_TIME%";
	protected final String CALLBACKNUMBER = "%CALLBACKNUMBER%";
	protected final String LASTTIME = "%LASTTIME%";
	protected final String CALLER = "%CALLER%";
	protected final String PRESENT_SMS_NUMBER="%PRESENT_SMS%";
	protected final String TOTAL_NUMBER_OF_SMS="%TOTAL_SMS%";
	protected final String MT_EXPIRY_TIME = "MC_EXPIRY_HOURS";
	protected final String MC_SUBSCRIPTION_AVAILABLE = "PROV_SUBSCRIPTION_AVAILABLE";
	protected long m_lExpireBeforeHrs;
	protected String m_smsTypeToInsert = null;
	protected String m_mcProcessedToUpdate = null;
	protected boolean m_bLandLineMCA = false;

	protected int waitMinutesForProcessingMissedcalls = 0;

	protected String strMcaTopromoteMciMessage = null;
	protected String strCustomizedMessageTogetDsr = null;
	protected boolean m_bPrependCountryCodeForSenderNo = false;
	protected boolean m_bPrependPlusForSenderNo = false;
	protected String smsCallBackNumber;
	Random random = new Random();

	public VMMTThreadProcessor(int threadID, String mcProcessingType, boolean silentSMS, String smstype, String mcprocessed)
	{
		m_thread_id = threadID;
		m_strThreadName = Thread.currentThread().getName();
		m_MissedCallProcessingType = mcProcessingType ;
		m_smsTypeToInsert = smstype;
		m_mcProcessedToUpdate = mcprocessed;
	}

	public VMMTThreadProcessor(String threadname)
	{
		m_strThreadName = Thread.currentThread().getName();
	}

	// added by rupesh
	public VMMTThreadProcessor() {
		// TODO Auto-generated constructor stub
	}

	public boolean init(int debuglevel, boolean consoleEcho, VMSMSDispatcher smsDispatcher, VMMTDispatcher vmmtmcp, String reasonsSupported)
	{
		try{
			if(((DebugManager)DebugManager.getDebugManagerObject(debuglevel, consoleEcho, _module)) == null){
				System.out.println("DebugManager.init failed");
				//return false;
			}

			m_vmmtdisp = vmmtmcp;
			m_reasons_supported = reasonsSupported;

			if(!readConfiguration()){
				return false;
			}
		}catch(Exception e){
			DebugManager.exception(_module, _class, "init", e, Thread.currentThread().getName(), null);
		}

		return true;
	}


	public void processSubscribers(
			Vector<String> vSubscribers, 
			String[] promoMessages, 
			String mctype, 
			Hashtable<String, SubscriberDto> htSubscribersProvisioning)
	{
		DebugManager.status(_module, _class, "processSubscribers mctype "+mctype+" this "+this, Thread.currentThread().getName() + " started.", Thread.currentThread().getName(), null);
		try{
			m_missedCallType = mctype;
			if(vSubscribers != null && vSubscribers.size()!=0) {
				processMissedCalls(
						VMMTDispatcher.getMtDispatcherDbConnection(), 
						vSubscribers, 
						htSubscribersProvisioning);
			}else{
			//DebugManager.trace(_module, _class,"processSubscribers",">>vSubscribers may be null or empty ", Thread.currentThread().getName(),null);
			}

		}catch(Exception e){
			DebugManager.exception(_module, _class,"processSubscribers",e,Thread.currentThread().getName(),null);
		}
	}

	public boolean readConfiguration()
	{
		DebugManager.trace(_module, _class, "readConfiguration", " entered VMMTThreadProcesser", Thread.currentThread().getName(), null);
		try{
			m_countryCode = ConfigDTO.getSmsTextDTO().getCountryCode();
			DebugManager.trace(_module, _class, "readConfiguration", " entered VMMTThreadProcesser countrycode:"+m_countryCode, Thread.currentThread().getName(), null);
			m_bPrependCntryCodeInSmsText = ConfigDTO.getMissedCallsDTO().isPrependCountryCodeInSMSText();
			m_lExpireBeforeHrs = ConfigDTO.getMissedCallsDTO().getExpiryHours() * (60*60*1000);
			//smsCountTokenNumber = ConfigDTO.getSmsTextDTO().getPropertyMC_NUMTIMES_TOKEN_POSITION();
			//fromTimeTokenNumber = ConfigDTO.getSmsTextDTO().getPropertyMC_FROMCALLTIME_TOKEN_POSITION();
			m_reasons_supported = m_vmmtdisp.getReasonsSupported();
			m_bLandLineMCA = ConfigDTO.getMissedCallsDTO().isLandLineMcaEnabled();
			waitMinutesForProcessingMissedcalls = ConfigDTO.getMissedCallsDTO().getWaitMinutes();
			//strMcaTopromoteMciMessage = ConfigDTO.getSmsTextDTO().getMcaMessageForPromotingMci();
			//strCustomizedMessageTogetDsr = ConfigDTO.getSmsTextDTO().getCustomizedMessageToGetDsrNode();
			smsCallBackNumber = ConfigDTO.getMissedCallsDTO().getSMSCallbackNumber();
			m_bPrependCountryCodeForSenderNo = ConfigDTO.getMissedCallsDTO().isNeedToPrependCountryCodeForSubscriberForMcaPromotingMci();
			m_bPrependPlusForSenderNo = ConfigDTO.getMissedCallsDTO().isNeedToPrependPlusForSubscriberForMcaPromotingMci();
		}catch(Exception e){
			DebugManager.exception(_module, _class, "readConfiguration", e, Thread.currentThread().getName(), null);
			waitMinutesForProcessingMissedcalls = 0;
			m_bLandLineMCA = false;
		}
		return true;
	}

	public abstract void processMissedCalls(Connection con, Vector<String> vecStrSubscribers, Hashtable<String, SubscriberDto> htSubscriberProvisioning);






	public Hashtable<String,SubscriberDto> getSubscriberProvisioning(
									Connection con, 
									Vector<String> vecStrSubscribers, 
									Vector<String> vMCISubscribers, 
									Vector<String> vMCASubscribers, 
									Vector<String> vMCAToPromoteMciSubscribers, 
									Vector<String> vMCAToPromoteMciWithDetailsSubscribers, 
									Vector<String> vNotSubscribed,
									Vector<String> vMcaToBulkPromoteSubscribers, Vector<String> vChargingSubscribers)
	{
		Hashtable<String, SubscriberDto> htSubscribersProvisioning = new Hashtable<String, SubscriberDto>();
		if(vecStrSubscribers == null){
			return htSubscribersProvisioning;
		}
		DebugManager.status(_module, _class, "getSubscriberProvisioning", "Size of vecStrSubscribers ="+vecStrSubscribers.size(), Thread.currentThread().getName(), null);
		
		Vector<Subscriber> vecSubscriber = ConfigDTO.getDaoDTO().getSubscriptionDao().getSubscribers(con, vecStrSubscribers);
		
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
							strBlcOrWhtForLca.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_WHITELIST_FOR_LCA))
					{
						htBlackAndWhiteForLca = ConfigDTO.getDaoDTO().getSubscriptionDao().
						getBlackWhiteListForLca(con, strSubscriber);
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
					continue;
				}else{
					if(provisioned_mc.equalsIgnoreCase(PROVISIONED_TO_MCI) ||
							provisioned_mc.equalsIgnoreCase(PROVISIONED_TO_MCA_JUST_FOR_DSR)){
						DebugManager.status(_module,_class, "getSubscriberProvisioning", "subscriber="+subscriber+", provisioned_mc="+provisioned_mc, Thread.currentThread().getName(), null);
						vMCISubscribers.add(strSubscriber);
					}else if(provisioned_mc.equalsIgnoreCase(PROVISIONED_TO_MCA)){
						vMCASubscribers.add(strSubscriber);
					}else if(provisioned_mc.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_MCI_PROMO)){
						vMCAToPromoteMciSubscribers.add(strSubscriber);
					}else if(provisioned_mc.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_MCI_PROMO_WITH_DETAILS)){
						vMCAToPromoteMciWithDetailsSubscribers.add(strSubscriber);
					}else if(provisioned_mc.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_BULK_PROMO)){
						vMcaToBulkPromoteSubscribers.add(strSubscriber);
					}
					else if(provisioned_mc.equalsIgnoreCase(PROVISIONED_TO_CHARGING)){
						vChargingSubscribers.add(strSubscriber);
					}
					else{
						DebugManager.status(_module,_class, "getSubscriberProvisioning", "subscriber="+subscriber.getSubscriber()+", provisioned_mc=Not Provisioned", Thread.currentThread().getName(), null);
						vNotSubscribed.add(strSubscriber);
					}
				}
			}
		}catch(Exception e){
			DebugManager.exception(_module, _class, "getSubscriberProvisioning", e, Thread.currentThread().getName(), null);
		}
		String defaultMcAvailable = ProductDetails.defaultProduct.getMcaAvailable();
		for(int i=0; i<vecStrSubscribers.size(); i++){
			String subscriber = vecStrSubscribers.get(i);
			if(vMCISubscribers.contains(subscriber)){
				continue;
			}else if(vMCASubscribers.contains(subscriber)){
				continue;
			}else if(vMCAToPromoteMciSubscribers.contains(subscriber)){
				continue;
			}else if(vMCAToPromoteMciWithDetailsSubscribers.contains(subscriber)){
				continue;
			}else if(vNotSubscribed.contains(subscriber)){
				continue;
			}else if(vMcaToBulkPromoteSubscribers.contains(subscriber)){
				continue;
			}else if(vChargingSubscribers.contains(subscriber)){
				continue;
			}
			else{
				if(defaultMcAvailable.equalsIgnoreCase(PROVISIONED_TO_MCI)){
					//DebugManager.detail(_module,_class, "getSubscriberProvisioning", "subscriber="+subscriber+", defaultMcAvailable="+defaultMcAvailable, Thread.currentThread().getName(), null);
					vMCISubscribers.add(subscriber);
				}else if(defaultMcAvailable.equalsIgnoreCase(PROVISIONED_TO_MCA)){
					//DebugManager.detail(_module,_class, "getSubscriberProvisioning", "subscriber="+subscriber+", defaultMcAvailable="+defaultMcAvailable, Thread.currentThread().getName(), null);
					vMCASubscribers.add(subscriber);
				}else if(defaultMcAvailable.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_MCI_PROMO)){
					//DebugManager.detail(_module,_class, "getSubscriberProvisioning", "subscriber="+subscriber+", defaultMcAvailable="+defaultMcAvailable, Thread.currentThread().getName(), null);
					vMCAToPromoteMciSubscribers.add(subscriber);
				}else if(defaultMcAvailable.equalsIgnoreCase(PROVISIONED_TO_MCA_FOR_MCI_PROMO_WITH_DETAILS)){
					//DebugManager.detail(_module,_class, "getSubscriberProvisioning", "subscriber="+subscriber+", defaultMcAvailable="+defaultMcAvailable, Thread.currentThread().getName(), null);
					vMCAToPromoteMciWithDetailsSubscribers.add(subscriber);
				}else if(defaultMcAvailable.equalsIgnoreCase(PROVISIONED_TO_CHARGING)){
					vChargingSubscribers.add(subscriber);
				}else{
					//DebugManager.detail(_module,_class, "getSubscriberProvisioning", "subscriber="+subscriber+", defaultMcAvailable=Not Subscribed", Thread.currentThread().getName(), null);
					vNotSubscribed.add(subscriber);
				}
			}
		}
		return htSubscribersProvisioning;
	}

	public void sendMcaAlertToPromoteMci(
									Vector<String> vecStrSubscribers, 
									int mtThread_id, Hashtable<String, 
									SubscriberDto> htSubscribersProvisioning)
	{
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		Vector<Sms> vecSms = new Vector<Sms>();
		
		for(int i=0; i<vecStrSubscribers.size(); i++)
		{
			String subscriber = vecStrSubscribers.get(i);
			SubscriberDto subDto = htSubscribersProvisioning.get(subscriber);
			ProductDto prodDto = null;
			if(subDto != null){
				prodDto = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
			}
			if(prodDto == null){
				prodDto = ProductDetails.defaultProduct;
			}
			
			int noOfSmsPerDay = prodDto.getNoOfSmsPerDay();
			int noOfSmsForToday = 0;
			
			String[] arrStrSmscName = prodDto.getArrStrSmscName();
			String smscName = arrStrSmscName[random.nextInt(arrStrSmscName.length)];
			String strCallBackNo = prodDto.getSmsSendingNo();
			String prodName = prodDto.getProductName();
			
			String code = "ENG";
			if(subDto != null && subDto.getVern_supp().equals("Y")){
				code = subDto.getLanguage();
				if (code.equalsIgnoreCase("DEF")){
					code = MCAXMLParser.defaultLangugeCode;
				}
				code = code.toUpperCase();
				if(!MCAXMLParser.smsTextDetails.containsKey(code))
					code="ENG";
			}
			
			
			if(noOfSmsPerDay > 0)
			{
				noOfSmsForToday = subDto.getNoOfSmsForToday();
				if(noOfSmsForToday >= noOfSmsPerDay)
				{
					if(noOfSmsForToday < 99)
					{
						
						
						String strLimitSms= MCAXMLParser.smsTextDetails.get(code).getLimitIntimationMessageForPromotionalMci();
						
						Sms newSms = new Sms(
								subscriber, 
								MESSAGE_TYPE_LIMIT_INTIMATION, 
								SMS_STATUS_CREATED, 
								strLimitSms,
								ConfigDTO.getSmsEngineDTO().getDefaultSendingNumber(), 
								strLimitSms, 
								false, 
								smscName
						);
						newSms.setProdName(prodName);
						if(code.equalsIgnoreCase("ENG")){
							newSms.setUnicode(false);
						}
						else
							newSms.setUnicode(true);
						vecSms.add(newSms);
						updateNumberOfSmsForToday(subscriber, 99);
						subDto.setNoOfSmsForToday(99);
						continue;
					}else{
						continue;
					}
				}
			}
			
			
			if(ConfigDTO.getMissedCallsDTO().isNeedToPrependCountryCodeForCallBackNumberForMcaPromotingMci()){
				strCallBackNo = MCAXMLParser.smsTextDetails.get(code).getCountryCode() + strCallBackNo;
				//strCallBackNo = ConfigDTO.getSmsTextDTO().getCountryCode() + strCallBackNo;
			}
			if(ConfigDTO.getMissedCallsDTO().isNeedToPrependPlusForCallBackNumberForMcaPromotingMci()){
				strCallBackNo = "+" + strCallBackNo;
			}
			
			String smsText = MCAXMLParser.smsTextDetails.get(code).getMcaMessageForPromotingMci();
			Sms sms = new Sms(
					subscriber, 
					MESSAGE_TYPE_MCA_PROMOTE_MCI, 
					SMS_STATUS_CREATED,
					smsText, 
					strCallBackNo, 
					smsText, 
					false, 
					smscName
			);
			sms.setProdName(prodName);
			if(code.equalsIgnoreCase("ENG")){
				sms.setUnicode(false);
			}
			else
				sms.setUnicode(true);
			vecSms.add(sms);
			
			if(noOfSmsPerDay > 0){
				updateNumberOfSmsForToday(subscriber, noOfSmsForToday+1);
				subDto.setNoOfSmsForToday(noOfSmsForToday+1);
			}
		}
		
		ConfigDTO.getDaoDTO().getSmsDao().saveSms(con, vecSms);
		if(ConfigDTO.getCommonDTO().isStoreMcaMissedcallsToPromoteMci()){
			ConfigDTO.getDaoDTO().getMissedCallDao().dumpMcaMissedCallsToPromoteMci(con, vecStrSubscribers);
		}else{
			ConfigDTO.getDaoDTO().getMissedCallDao().deleteMcaMissedcalls(con, vecStrSubscribers);
		}
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	}
	
	
	public void updateNumberOfSmsForToday(String subscriber, int count){
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		ConfigDTO.getDaoDTO().getSubscriptionDao().updateNoOfSmsForToday(con, subscriber, count);
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	}


	  public  void  handleMcaForBulkPromo(Vector<String> vSubscribers, int mtThread_id, Hashtable<String, SubscriberDto> htSubscribersProvisioning)
	  {  	
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		ConfigDTO.getDaoDTO().getMissedCallDao().dumpMcaMissedCalls(con, vSubscribers);		
		
		ConfigDTO.getDaoDTO().getMissedCallDao().deleteMcaMissedcalls(con, vSubscribers);
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	  }
	  
	public void sendMcaAlertToPromoteMciWithDetails(
						Vector<String> vSubscribers, 
						int mtThread_id, 
						Hashtable<String, SubscriberDto> htSubscribersProvisioning)
	{
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		Hashtable<String, McaMissedcallDto> htMcaMissedcallDto = ConfigDTO.getDaoDTO().getMissedCallDao().getMcaMissedcalls(con, vSubscribers);
		Vector<Sms> vecSms = new Vector<Sms>();
		Enumeration<String> enumTmp = htMcaMissedcallDto.keys();
		
		while(enumTmp.hasMoreElements())
		{
			String subscriber = enumTmp.nextElement();
			McaMissedcallDto mcDto = htMcaMissedcallDto.get(subscriber);
			SubscriberDto subDto = htSubscribersProvisioning.get(subscriber);
			ProductDto prodDto = null;
			
			if(subDto != null){
				DebugManager.status(_module, _class, "sendMcaAlertToPromoteMciWithDetails : subDto.getProductID() "+subDto.getProductID(), " "+DaemonUtils.getCompleteDate(System.currentTimeMillis()), "", "");

				prodDto  = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
			}
			if(prodDto == null){
				prodDto = ProductDetails.defaultProduct;
			}
			
			int noOfSmsPerDay = prodDto.getNoOfSmsPerDay();
			int noOfSmsForToday = 0;
			String prodName = prodDto.getProductName();
			
			String[] arrStrSmscName = prodDto.getArrStrSmscName();
			DebugManager.status(_module, _class, "sendMcaAlertToPromoteMciWithDetails : arrStrSmscName length "+arrStrSmscName.length, " "+DaemonUtils.getCompleteDate(System.currentTimeMillis()), "", "");

			String smscName = arrStrSmscName[random.nextInt(arrStrSmscName.length)];
			String strCallBackNo = prodDto.getSmsSendingNo();
			
			String code = "ENG";
			if(subDto != null && subDto.getVern_supp().equals("Y")){
				code = subDto.getLanguage();
				if (code.equalsIgnoreCase("DEF")){
					code = MCAXMLParser.defaultLangugeCode;
				}
				code = code.toUpperCase();
				if(!MCAXMLParser.smsTextDetails.containsKey(code))
					code="ENG";
			}
			
			if(noOfSmsPerDay > 0)
			{
				noOfSmsForToday = subDto.getNoOfSmsForToday();
				if(noOfSmsForToday >= noOfSmsPerDay)
				{
					if(noOfSmsForToday < 99)
					{
						String strLimitSms =  MCAXMLParser.smsTextDetails.get(code).getLimitIntimationMessageForPromotionalMci();
						//ConfigDTO.getSmsTextDTO().getLimitIntimationMessageForPromotionalMci();
						
						Sms newSms = new Sms(
								subscriber, 
								MESSAGE_TYPE_LIMIT_INTIMATION, 
								SMS_STATUS_CREATED, 
								strLimitSms,
								ConfigDTO.getSmsEngineDTO().getDefaultSendingNumber(), 
								strLimitSms, 
								false, 
								smscName
						);
						newSms.setProdName(prodName);
						if(code.equalsIgnoreCase("ENG")){
							newSms.setUnicode(false);
						}
						else
							newSms.setUnicode(true);
						vecSms.add(newSms);
						updateNumberOfSmsForToday(subscriber, 99);
						subDto.setNoOfSmsForToday(99);
						continue;
					}else{
						continue;
					}
				}
			}
			
			if(ConfigDTO.getMissedCallsDTO().isNeedToPrependCountryCodeForCallBackNumberForMcaPromotingMci()){
				strCallBackNo = MCAXMLParser.smsTextDetails.get(code).getCountryCode() + strCallBackNo;
					//ConfigDTO.getSmsTextDTO().getCountryCode() + strCallBackNo;
			}
			if(ConfigDTO.getMissedCallsDTO().isNeedToPrependPlusForCallBackNumberForMcaPromotingMci()){
				strCallBackNo = "+" + strCallBackNo;
			}
			int count = mcDto.getCount();
			Date maxDate = mcDto.getMaxDate();
			String message = "";
			if(count > 1){
				message = MCAXMLParser.smsTextDetails.get(code).getMcaMessageForPromotingMciWithDetailsManyCalls();
				message = replaceValuesForMcaAlertToPromoteMciWithDetailsManyCalls(message, count, maxDate);
			}
			else{
				message = MCAXMLParser.smsTextDetails.get(code).getMcaMessageForPromotingMciWithDetails();
				message = replaceValuesForMcaAlertToPromoteMciWithDetails(message, count, maxDate);
			}

			Sms sms = new Sms(
					subscriber, 
					MESSAGE_TYPE_MCA_PROMOTE_MCI_WITH_DETAILS, 
					SMS_STATUS_CREATED,
					message, 
					strCallBackNo, 
					message, 
					false, 
					smscName
			);
			sms.setProdName(prodName);
			if(code.equalsIgnoreCase("ENG")){
				sms.setUnicode(false);
			}
			else
				sms.setUnicode(true);

			vecSms.add(sms);
			
			if(noOfSmsPerDay > 0){
				updateNumberOfSmsForToday(subscriber, noOfSmsForToday+1);
				subDto.setNoOfSmsForToday(noOfSmsForToday+1);
			}

		}
		ConfigDTO.getDaoDTO().getSmsDao().saveSms(con, vecSms);
		if(ConfigDTO.getCommonDTO().isStoreMcaMissedcallsToPromoteMciWithdetails()){
			ConfigDTO.getDaoDTO().getMissedCallDao().dumpMcaMissedCallsToPromoteMciWithDetails(con, vSubscribers);
		}else{
			ConfigDTO.getDaoDTO().getMissedCallDao().deleteMcaMissedcalls(con, vSubscribers);
		}
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	}

	public void sendCustomizedMessageForGettingDsr(
								Vector<String> vecStrSubscribers, 
								int mtThread_id, Hashtable<String, 
								SubscriberDto> htSubscribersProvisioning)
	{
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		Vector<Sms> vecSms = new Vector<Sms>();
		for(int i=0; i<vecStrSubscribers.size(); i++){
			String subscriber = vecStrSubscribers.get(i);
			SubscriberDto subDto = htSubscribersProvisioning.get(subscriber);
			ProductDto prodDto = null;
			if(subDto != null){
				prodDto = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
			}
			if(prodDto == null){
				prodDto = ProductDetails.defaultProduct;
			}
			String prodName = prodDto.getProductName();
			String[] arrStrSmscName = prodDto.getArrStrSmscName();
			String smscName = arrStrSmscName[random.nextInt(arrStrSmscName.length)];
			String strCallBackNo = prodDto.getSmsSendingNo();

			Sms sms = new Sms(
					subscriber, 
					MESSAGE_TYPE_CUSTOMIZED_FOR_GETTING_DSR, 
					SMS_STATUS_CREATED,
					strCustomizedMessageTogetDsr, 
					strCallBackNo, 
					strCustomizedMessageTogetDsr, 
					false, 
					smscName
			);

			sms.setProdName(prodName);
			vecSms.add(sms);
		}
		ConfigDTO.getDaoDTO().getSmsDao().saveSms(con, vecSms);
		if(ConfigDTO.getCommonDTO().isStoreMcaMissedcallsToPromoteMci()){
			ConfigDTO.getDaoDTO().getMissedCallDao().dumpMcaMissedCallsToPromoteMci(con, vecStrSubscribers);
		}else{
			ConfigDTO.getDaoDTO().getMissedCallDao().deleteMcaMissedcalls(con, vecStrSubscribers);
		}
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	}


	public Date getDateFromString(String strDate){
		Date dtFrom = null;
		try{
			dtFrom = DaemonUtils.getDate(strDate, "h:mm");
		}catch(Exception e){
			StringTokenizer strtoken =  new StringTokenizer(strDate);
			String firstToken = "";
			if(strtoken.hasMoreTokens())
				firstToken = strtoken.nextToken();
			try{
				dtFrom = DaemonUtils.getDate(firstToken, "h:mm");
			}catch(Exception ex){
			}
		}
		return dtFrom;
	}

	public String replaceValuesForMcaAlertToPromoteMciWithDetails(String message, int count, Date maxCalledTime) {
		//DebugManager.status(_module, _class, "replaceValuesForMcaAlertToPromoteMciWithDetails", ">>message="+message +",count="+count +",maxCalledTime="+maxCalledTime, Thread.currentThread().getName(), null);

		String lastTime = DaemonUtils.getFormattedDate(maxCalledTime, "h:mm a");
		Calendar calCalledTime = Calendar.getInstance();
		calCalledTime.setTime(maxCalledTime);
		if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calCalledTime.get(Calendar.DAY_OF_YEAR)) {
			lastTime = lastTime + ", "+ DaemonUtils.getFormattedDate(maxCalledTime, "MMM d");
		}

		int pos;

		// Replace %LAST_CALLED_TIME%
		while((pos = message.indexOf("%LAST_CALLED_TIME%")) != -1){
				message = message.substring(0,pos) +  lastTime + message.substring(pos + "%LAST_CALLED_TIME%".length());
		}

		return message;
	}

	public String replaceValuesForMcaAlertToPromoteMciWithDetailsManyCalls(String message, int count, Date maxCalledTime) {
		//DebugManager.status(_module, _class, "replaceValuesForMcaAlertToPromoteMciWithDetails", ">>message="+message +",count="+count +",maxCalledTime="+maxCalledTime, Thread.currentThread().getName(), null);

		String lastTime = DaemonUtils.getFormattedDate(maxCalledTime, "h:mm a");
		Calendar calCalledTime = Calendar.getInstance();
		calCalledTime.setTime(maxCalledTime);
		if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calCalledTime.get(Calendar.DAY_OF_YEAR)) {
			lastTime = lastTime + ", "+ DaemonUtils.getFormattedDate(maxCalledTime, "MMM d");
		}

		int pos;
		// Replace %COUNT%
		while((pos = message.indexOf("%COUNT%")) != -1){
			message = message.substring(0,pos) + count + message.substring(pos + "%COUNT%".length());
		}

		// Replace %LAST_CALLED_TIME%
		while((pos = message.indexOf("%LAST_CALLED_TIME%")) != -1){
				message = message.substring(0,pos) +  lastTime + message.substring(pos + "%LAST_CALLED_TIME%".length());
		}

		return message;
	}



	public String getStackTrace(Throwable ex)
	{
		StringWriter traceBuffer = new StringWriter();
		String strTraceContent="";
		ex.printStackTrace(new PrintWriter(traceBuffer));
		strTraceContent=traceBuffer.toString();
		strTraceContent=strTraceContent.substring(0,strTraceContent.length()-2);//removing line separator
		strTraceContent = System.getProperty("line.separator")+" \t" + strTraceContent;

		return strTraceContent;
	}

}


