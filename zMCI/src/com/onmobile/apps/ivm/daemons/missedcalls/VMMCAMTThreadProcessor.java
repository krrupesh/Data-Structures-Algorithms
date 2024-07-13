package com.onmobile.apps.ivm.daemons.missedcalls;


import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
import com.onmobile.common.debug.DebugManager;
import com.onmobile.apps.ivm.daemons.adServer.AdMessage;
import com.onmobile.apps.ivm.daemons.common.IDaemonConsts;
import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.ProductDetails;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.config.xmlParsing.MCAXMLParser;
import com.onmobile.apps.ivm.daemons.db.model.Sms;
import com.onmobile.apps.ivm.daemons.dto.McaMissedcallDto;
import com.onmobile.apps.ivm.daemons.dto.ProductDto;
import com.onmobile.apps.ivm.daemons.dto.SubscriberDto;




public class VMMCAMTThreadProcessor extends VMMTThreadProcessor implements IDaemonConsts 
{
	protected String _class = "VMMCAMTThreadProcessor";
	protected String m_strThreadName = "VMMCAMTThreadProcessor";
	/*protected String m_strAlertStart;
	protected String m_strAlertMidOneTime;
	protected String m_strAlertMidManyTimes;
	protected String m_strAlertMidManyTimesAtOnce;
	protected String m_strAlertEnd;*/
	protected final String NUM_OF_TIMES = "%NUMTIMES%";
	protected final String FROMTIME = "%FROM_CALL_TIME%";
	protected final String TOTIME = "%TO_CALL_TIME%";
	protected final String CALLBACKNUMBER = "%CALLBACKNUMBER%";
	protected final String LASTTIME = "%LASTTIME%";
	protected long m_lExpireBeforeHrs;
    AdMessage adMessage = null;
    protected String premiumShortCode = null;
      
    
    
    
   	public VMMCAMTThreadProcessor(int threadID, String mcProcessingType, boolean silentSMS)
	{
		super(threadID,mcProcessingType,silentSMS,MESSAGE_TYPE_MC_NOTIFICATION,MC_PROCESSED_ONLY_NOTIFIED);
		m_thread_id = threadID;
		m_strThreadName = Thread.currentThread().getName();
		m_MissedCallProcessingType = mcProcessingType ;
		m_smsTypeToInsert = MESSAGE_TYPE_MC_NOTIFICATION;
		m_mcProcessedToUpdate = MC_PROCESSED_ONLY_NOTIFIED;
	}
	
    // added by rupesh
	public VMMCAMTThreadProcessor() {
		super();
	}


	public boolean readConfiguration()
	{
		DebugManager.status(_module, _class, "readConfiguration", " entered VMMCAThreadProcesser", Thread.currentThread().getName(), null);
		if(!super.readConfiguration()){
			DebugManager.status(_module, _class, "readConfiguration", "super.readConfiguration() returns false.", Thread.currentThread().getName(), null);
			return false;
		}
		DebugManager.trace(_module, _class, "readConfiguration", " read config for base class VMThreadProcesser", Thread.currentThread().getName(), null);
		try{
			smsCallBackNumber = ConfigDTO.getMissedCallsDTO().getSMSCallbackNumber();
			/*m_strAlertStart = ConfigDTO.getSmsTextDTO().getMissedCallAlertMessageStart();
			m_strAlertMidOneTime = ConfigDTO.getSmsTextDTO().getMissedCallAlertMessageMidOneCall();
			m_strAlertMidManyTimes = ConfigDTO.getSmsTextDTO().getMissedCallAlertMessageMidManyCalls();
			m_strAlertMidManyTimesAtOnce = ConfigDTO.getSmsTextDTO().getMissedCallAlertMessageMidManyCallsSameTime();
			m_strAlertEnd = ConfigDTO.getSmsTextDTO().getMissedCallAlertMessageEnd();*/
			m_lExpireBeforeHrs = ConfigDTO.getMissedCallsDTO().getExpiryHours() * (60*60*1000);
			m_countryCode = ConfigDTO.getSmsTextDTO().getCountryCode();
			m_bPrependCntryCodeInSmsText = ConfigDTO.getMissedCallsDTO().isPrependCountryCodeInSMSText();
			/*smsCountTokenNumber = ConfigDTO.getSmsTextDTO().getPropertyMC_NUMTIMES_TOKEN_POSITION();
			fromTimeTokenNumber = ConfigDTO.getSmsTextDTO().getPropertyMC_FROMCALLTIME_TOKEN_POSITION();*/
			m_reasons_supported = m_vmmtdisp.getReasonsSupported();
			adMessage = AdMessage.getInstance();
			premiumShortCode = ConfigDTO.getMissedCallsDTO().getPremiumShortCode();
		}catch(Exception e) {
			DebugManager.exception(_module, _class, "readConfiguration", e, Thread.currentThread().getName(), null);
			return false;
		}
				
		return true;
	}
	
	

	
	

	/**
	 * replaceValues()
	 * Form the mid part of the alert message by replacing the constants.
	 */
	public String replaceValues(int timesCalled, String fromTime, String toTime, String callbacknumber,String code) 
	{
		DebugManager.status(_module, _class, "replaceValues", ">>2. timesCalled="+timesCalled +",fromTime="+fromTime +",toTime="+toTime +",callbacknumber="+callbacknumber, Thread.currentThread().getName(), null);
		String message =  MCAXMLParser.smsTextDetails.get(code).getMissedCallAlertMessageStart() ;
			//m_strAlertStart;
		DebugManager.status(_module, _class, "replaceValues", "1. message="+message, Thread.currentThread().getName(), null);
		
		if(timesCalled == 1){
			message = message + " " + MCAXMLParser.smsTextDetails.get(code).getMissedCallAlertMessageMidOneCall(); 
			//m_strAlertMidOneTime;
			DebugManager.status(_module, _class, "replaceValues", "2. message="+message, Thread.currentThread().getName(), null);
		}else{
			if(fromTime.equalsIgnoreCase(toTime)){
				message = message + MCAXMLParser.smsTextDetails.get(code).getMissedCallAlertMessageMidManyCallsSameTime(); 
				//m_strAlertMidManyTimesAtOnce; 
				DebugManager.status(_module, _class, "replaceValues", "3. message="+message, Thread.currentThread().getName(), null);
			}else{
				message = message + " " + MCAXMLParser.smsTextDetails.get(code).getMissedCallAlertMessageMidManyCalls(); 
				//m_strAlertMidManyTimes;
				DebugManager.status(_module, _class, "replaceValues", "4. message="+message, Thread.currentThread().getName(), null);
			}
		}
		message = message + MCAXMLParser.smsTextDetails.get(code).getMissedCallAlertMessageEnd(); 
		//m_strAlertEnd;
		DebugManager.status(_module, _class, "replaceValues", "5. message="+message, Thread.currentThread().getName(), null);
		int pos;
		// Replace NUMTIMES
		while((pos = message.indexOf(NUM_OF_TIMES)) != -1){
			message = message.substring(0,pos) + timesCalled + message.substring(pos + NUM_OF_TIMES.length());
		}
		DebugManager.status(_module, _class, "replaceValues", "Replace NUMTIMES,message="+message, Thread.currentThread().getName(), null);
		
		// Replace FROMTIME
		while((pos = message.indexOf(FROMTIME)) != -1){
			message = message.substring(0,pos) + fromTime + message.substring(pos + FROMTIME.length());
			
		}
		DebugManager.status(_module, _class, "replaceValues", "Replace FROMTIME,message="+message, Thread.currentThread().getName(), null);

		// Replace TOTIME
		while((pos = message.indexOf(TOTIME)) != -1){
			message = message.substring(0,pos) + toTime + message.substring(pos + TOTIME.length());
		}
		DebugManager.status(_module, _class, "replaceValues", "Replace TOTIME,message="+message, Thread.currentThread().getName(), null);
		
		// Replace CALLBACKNUMBER
		while((pos = message.indexOf(CALLBACKNUMBER)) != -1){
			message = message.substring(0,pos) + callbacknumber + message.substring(pos + CALLBACKNUMBER.length());
		}
		DebugManager.status(_module, _class, "replaceValues", "Replace CALLBACKNUMBER,message="+message, Thread.currentThread().getName(), null);
			
		return message;
	}
	
	
	
	public String replaceValues(
								int timesCalled, 
								java.util.Date dtFrom, 
								java.util.Date dtTo, 
								String callbacknumber,
								String code
								) 
	{
		
		DebugManager.status(_module, _class, "replaceValues", ">>1. timesCalled="+timesCalled +",dtFrom="+dtFrom +",dtTo="+dtTo +",callbacknumber="+callbacknumber, Thread.currentThread().getName(), null);
		String fromTime = DaemonUtils.getFormattedDate(dtFrom, "h:mm a");
		String toTime = DaemonUtils.getFormattedDate(dtTo,"h:mm a");
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(dtFrom);
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(dtTo);
		
		if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calFrom.get(Calendar.DAY_OF_YEAR) || Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calTo.get(Calendar.DAY_OF_YEAR)){
			DebugManager.trace(_module, _class, "replaceValues", "either from or to date is yesterday", Thread.currentThread().getName(), null);
			if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calFrom.get(Calendar.DAY_OF_YEAR)){
				fromTime = fromTime + " on " + DaemonUtils.getFormattedDate(dtFrom, "MMM d");
			}else{
				fromTime = fromTime + " today";
			}
			if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calTo.get(Calendar.DAY_OF_YEAR)){
				toTime = toTime + " on " + DaemonUtils.getFormattedDate(dtTo, "MMM d");
			}else{
				toTime = toTime + " today";
			}
		}
		
		String replacedMessage = replaceValues(timesCalled, fromTime, toTime, callbacknumber, code);
		DebugManager.status(_module, _class, "replaceValues", "replacedMessage= "+replacedMessage, Thread.currentThread().getName(), null);
		return replacedMessage;
	}
	
	
	
	private String[] createSMSTextForNewRecord(
									Sms lastSentSms, 
									McaMissedcallDto mcaMcDto, 
									Hashtable<String, SubscriberDto> htSubscriberProvisioning
									)
	{
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
					code = MCAXMLParser.defaultLangugeCode;
					promoCode = MCAXMLParser.defaultPromoLanguageCode;
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
		Date newFromTime = mcaMcDto.getMinDate();
		DebugManager.status(_module, _class, "createSMSTextForNewRecord", ">>newMissedCalls="+newMissedCalls +",newFromTime="+newFromTime,Thread.currentThread().getName(), null);
		
		if(lastSentSms != null && lastSentSms.getSmsRefID() != null){
			String lastUnsentSmsText = lastSentSms.getSmsText();
			String lastUnsentReplaceMessageText = lastSentSms.getReplaceSmsText();

			boolean bIsReplaced = lastSentSms.isReplaced();
		
			int oldCumulativeMissedCalls = 0;
			String oldCumulativeFromTime = "";
			if(lastUnsentReplaceMessageText != null && bIsReplaced){
				DebugManager.trace(_module, _class, "createSMSTextForNewRecord", "Subscriber"+strSubscriber+",lastUnsentReplaceMessageText="+lastUnsentReplaceMessageText,Thread.currentThread().getName(), null);
				oldCumulativeMissedCalls = DaemonUtils.getSMSCountFromMessage(lastUnsentReplaceMessageText, MCAXMLParser.smsTextDetails.get(code).getPropertyMC_NUMTIMES_TOKEN_POSITION());
				oldCumulativeFromTime = DaemonUtils.getFromTimeFromMessage(lastUnsentReplaceMessageText,MCAXMLParser.smsTextDetails.get(code).getPropertyMC_FROMCALLTIME_TOKEN_POSITION());
			}else{
				//DebugManager.status(_module, _class, "createSMSTextForNewRecord", "Subscriber"+strSubscriber+",lastUnsentSmsText="+lastUnsentSmsText+",smsCountTokenNumber="+smsCountTokenNumber,Thread.currentThread().getName(), null);
				oldCumulativeMissedCalls = DaemonUtils.getSMSCountFromMessage(lastUnsentSmsText, MCAXMLParser.smsTextDetails.get(code).getPropertyMC_NUMTIMES_TOKEN_POSITION());
				oldCumulativeFromTime = DaemonUtils.getFromTimeFromMessage(lastUnsentSmsText, MCAXMLParser.smsTextDetails.get(code).getPropertyMC_FROMCALLTIME_TOKEN_POSITION());
			}
			totalMissedCallsForReplace = oldCumulativeMissedCalls + newMissedCalls;

			Date newToTime = new java.util.Date();
			String cumulativeToTime = DaemonUtils.getFormattedDate(newToTime, "h:mm a"); 

			if(oldCumulativeFromTime.indexOf(" on ") != -1){
				cumulativeToTime = cumulativeToTime + " today";
			}else{
				Date dtFrom = getDateFromString(oldCumulativeFromTime);
				if(dtFrom != null){
					isPreviousFromTimeAvailable = true;
				}else{ 
					isPreviousFromTimeAvailable = false;
				}
				Date dtTo = DaemonUtils.getDate(cumulativeToTime, "h:mm a");
				if(dtFrom != null && dtFrom.after(dtTo)){
					Calendar calyesterday = Calendar.getInstance();
					calyesterday.add(Calendar.DATE, -1);
					String yesterday = DaemonUtils.getFormattedDate(calyesterday.getTime(), "MMM d");
					oldCumulativeFromTime = oldCumulativeFromTime+" on "+yesterday;
					cumulativeToTime = cumulativeToTime + " today";
				}
			}
			

			finalIncrementalMessage = replaceValues(
													newMissedCalls, 
													newFromTime, 
													newToTime, 
													premiumShortCode,
													code
													);
			DebugManager.status(_module, _class, "createSMSTextForNewRecord", "#1. the incremental message while inserting to vm_sms is " + finalIncrementalMessage,Thread.currentThread().getName(), null);

			if(isPreviousFromTimeAvailable){
				finalConsolidatedMessage = replaceValues(
													totalMissedCallsForReplace, 
													oldCumulativeFromTime, 
													cumulativeToTime, 
													premiumShortCode,
													code
													);
			}
			DebugManager.status(_module, _class, "createSMSTextForNewRecord", "#1. the replace message1 while inserting to vm_sms is " + finalConsolidatedMessage,Thread.currentThread().getName(), null);
		}else{
			finalIncrementalMessage = replaceValues(
													newMissedCalls,
													newFromTime,
													new java.util.Date(),
													premiumShortCode,
													code
													);
			DebugManager.status(_module, _class, "createSMSTextForNewRecord", "#2. the incremental message while inserting to vm_sms is " + finalIncrementalMessage,Thread.currentThread().getName(), null);
				
			finalConsolidatedMessage = replaceValues(
													newMissedCalls,
													newFromTime,
													new java.util.Date(),
													premiumShortCode,
													code
													);
			DebugManager.status(_module, _class, "createSMSTextForNewRecord", "#2. the replace message1 while inserting to vm_sms is " + finalConsolidatedMessage,Thread.currentThread().getName(), null);
		}
		
		
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
	
	public Vector<Sms> getNewSmsForAllSubscribers(
									Hashtable<String, McaMissedcallDto> htMcaMissedcallDto, 
									Hashtable<String, Sms> htLastSentSms, 
									Hashtable<String, SubscriberDto> htSubscriberProvisioning
									)
	{
		Vector<Sms> vecSms = new Vector<Sms>();
		String smsText = null;
		String replaceSmsText = null;
		String[] arrStrSmsText = null;
				
		Enumeration<String> enStrMcDto = htMcaMissedcallDto.keys();
		
		while(enStrMcDto.hasMoreElements())
		{
			String subscriber = enStrMcDto.nextElement();
			SubscriberDto subDto = htSubscriberProvisioning.get(subscriber);
			ProductDto prodDto = null;
			if(subDto != null){
				prodDto = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
			}
			if(prodDto == null){
				prodDto = ProductDetails.defaultProduct;
			}
			String prodName = prodDto.getProductName();
			String[] arrSmscNames = prodDto.getArrStrSmscName();
			String smscName = arrSmscNames[random.nextInt(arrSmscNames.length)];
			
			int noOfSmsPerDay = prodDto.getNoOfSmsPerDay();
			int noOfSmsForToday = 0;
			
			String code = "ENG";
			if(subDto != null && subDto.getVern_supp().equals("Y")){
				code = subDto.getLanguage();
				if (code.equalsIgnoreCase("DEF")){
					code = MCAXMLParser.defaultLangugeCode;
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
					/*
					if(noOfSmsForToday < 99)
					{
						String strLimitSms = ConfigDTO.getSmsTextDTO().getLimitIntimationMessageForMca();
						String[] arrStrSmscNameForLimitSms = prodDto.getArrStrSmscName();
						String smscNameForLimitSms = arrSmscNames[random.nextInt(arrStrSmscNameForLimitSms.length)];
						
						Sms newSms = new Sms(
											subscriber, 
											MESSAGE_TYPE_LIMIT_INTIMATION, 
											SMS_STATUS_CREATED, 
											strLimitSms,
											ConfigDTO.getSmsEngineDTO().getDefaultSendingNumber(), 
											strLimitSms, 
											false, 
											smscNameForLimitSms
						);
						vecSms.add(newSms);
						updateNumberOfSmsForToday(subscriber, 99);
						subDto.setNoOfSmsForToday(99);
						continue;
					}else{
						continue;
					}
					*/
					continue;
				}
			}
			
			
			
			McaMissedcallDto mcaMcDto = htMcaMissedcallDto.get(subscriber);
			Sms lastSentSms = null;
			if (htLastSentSms != null) {
				lastSentSms = htLastSentSms.get(subscriber);
			}
			arrStrSmsText = createSMSTextForNewRecord(lastSentSms, mcaMcDto, htSubscriberProvisioning);
			smsText = arrStrSmsText[0];
			replaceSmsText = arrStrSmsText[1];
			String strCallBackNumber = prodDto.getSmsSendingNo();
			DebugManager.status(_module, _class, "getNewSmsForAllSubscribers", "subscriber="+subscriber+",strCallBackNumber="+strCallBackNumber+",smscName="+smscName+",smsText="+smsText+",replaceSmsText="+replaceSmsText, Thread.currentThread().getName(), null);			
			Sms newSms = new Sms(
								subscriber, 
								MESSAGE_TYPE_MC_NOTIFICATION,
								SMS_STATUS_CREATED,
								smsText, 
								strCallBackNumber, 
								replaceSmsText, 
								false, 
								smscName
								);
			if(code.equalsIgnoreCase("ENG")){
				newSms.setUnicode(false);
			}
			else
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
	
	
	
	public void updateNumberOfSmsForToday(String subscriber, int count){
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		ConfigDTO.getDaoDTO().getSubscriptionDao().updateNoOfSmsForToday(con, subscriber, count);
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	}
	
	
	
		
								
	public void processMissedCalls(Connection con,
			Vector<String> vecStrSubscriber,
			Hashtable<String, SubscriberDto> htSubscriberProvisioning) {
		Hashtable<String, McaMissedcallDto> htMcaMissedcallDto = ConfigDTO
				.getDaoDTO().getMissedCallDao()
				.getMcaMissedcalls(con, vecStrSubscriber);
		ConfigDTO.getDaoDTO().getMissedCallDao()
					.dumpMcaMissedCallsExcludingBullkPromo(con,
							vecStrSubscriber);

		Hashtable<String, Sms> htLastSentSms = ConfigDTO.getDaoDTO().getSmsDao()
				.getLastSentSmsForMCA(con, vecStrSubscriber);
		
		Vector<Sms> vecSms = getNewSmsForAllSubscribers(htMcaMissedcallDto,
				htLastSentSms, htSubscriberProvisioning);

		ConfigDTO.getDaoDTO().getSmsDao().saveSms(con, vecSms);

		if (htLastSentSms != null) {
			ConfigDTO.getDaoDTO().getSmsDao()
					.deleteLastSentSmsForMCA(con, htLastSentSms);
		}
		ConfigDTO.getDaoDTO().getMissedCallDao()
				.deleteMcaMissedcalls(con, vecStrSubscriber);
	}
}