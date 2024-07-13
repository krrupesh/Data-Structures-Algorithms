package com.onmobile.apps.ivm.daemons.missedcalls;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.util.Calendar;
import java.util.Vector;
import java.sql.Connection;
import com.onmobile.common.debug.DebugManager;
import com.onmobile.apps.ivm.daemons.adServer.AdMessage;
import com.onmobile.apps.ivm.daemons.common.IDaemonConsts;
import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.ProductDetails;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.config.xmlParsing.MCAXMLParser;
import com.onmobile.apps.ivm.daemons.db.model.Sms;
import com.onmobile.apps.ivm.daemons.dto.MciMessageAndCallbackNumberDto;
import com.onmobile.apps.ivm.daemons.dto.MciMissedcallDto;
import com.onmobile.apps.ivm.daemons.dto.ProductDto;
import com.onmobile.apps.ivm.daemons.dto.SubscriberDto;

public class VMMCIMTThreadProcessor extends VMMTThreadProcessor implements IDaemonConsts 
{
	protected String _class = "VMMCIMTThreadProcessor";
	protected String m_strThreadName = "VMMCIMTThreadProcessor";

	/*
	 * protected String m_strMCInfoStart; protected String
	 * m_strMCInfoMidOneCall; protected String m_strMCInfoMidManyCalls;
	 * protected String m_strMCInfoSeparator; protected String
	 * m_strMCInfoNoMessage; protected String m_strMCInfoEnd; protected String
	 * m_strMCInfoEndMessageIndexPresentAt; protected boolean
	 * m_bMCInfoEndMessageIndexPresentAt; protected String
	 * m_strMCInfoMessageIndex; protected String m_strMCInfoPageNumber;
	 * 
	 * protected String m_strMCIfSenderNoIsCallerInfoStart; protected String
	 * m_strMCIfSenderNoIsCallerInfoMidOneCall; protected String
	 * m_strMCIfSenderNoIsCallerInfoMidManyCalls; protected String
	 * m_strMCIfSenderNoIsCallerInfoSeparator; protected String
	 * m_strMCIfSenderNoIsCallerInfoNoMessage; protected String
	 * m_strMCIfSenderNoIsCallerInfoEnd; protected String
	 * m_strMCIfSenderNoIsCallerInfoEndMessageIndexPresentAt; protected boolean
	 * m_bMCIfSenderNoIsCallerInfoEndMessageIndexPresentAt; protected String
	 * m_strMCIfSenderNoIsCallerInfoMessageIndex; protected String
	 * m_strMCIfSenderNoIsCallerInfoPageNumber;
	 */

	protected boolean m_bCollateCallers = true;
	protected boolean m_bSenderNumberAsCallbackNumber = false;
	protected boolean m_bPrependCountryCodeForSenderNo = false;
	protected boolean m_bPrependCountryCodeIfCallbackNumberIsCallerNumber = false;
	protected boolean isNeedToAddPlusB4CountryCode = false;
	protected String smsCallBackNumber = null;
	protected final String NUM_OF_TIMES = "%NUMTIMES%";
	protected final String FROMTIME = "%FROM_CALL_TIME%";
	protected final String TOTIME = "%TO_CALL_TIME%";
	protected final String CALLBACKNUMBER = "%CALLBACKNUMBER%";
	protected final String LASTTIME = "%LASTTIME%";
	protected final String CALLER = "%CALLER%";
	protected final String MOBILE_NUMBER = "Mobile No";
	protected final String PRESENT_SMS_NUMBER = "%PRESENT_SMS%";
	protected final String TOTAL_NUMBER_OF_SMS = "%TOTAL_SMS%";
	protected AdMessage adMessage = null;
	protected boolean isDumpMciMissedcalls;

	public VMMCIMTThreadProcessor(int threadID, String mcProcessingType, boolean silentSMS) {
		super(threadID, mcProcessingType, silentSMS, MESSAGE_TYPE_MCI, MC_PROCESSED_ONLY_NOTIFIED);
		m_thread_id = threadID;
		m_strThreadName = Thread.currentThread().getName();
		m_MissedCallProcessingType = mcProcessingType;
		m_smsTypeToInsert = MESSAGE_TYPE_MCI;
		m_mcProcessedToUpdate = MC_PROCESSED_ONLY_NOTIFIED;
		
	}

	public boolean readConfiguration() 
	{
		if (super.readConfiguration() == false)return false;
		try {
			DebugManager.trace(_module, _class, "readConfiguration", "entered VMMCIMTThreadProcessor", m_strThreadName,
					null);
			smsCallBackNumber = ConfigDTO.getMissedCallsDTO().getSMSCallbackNumber();
			m_countryCode = ConfigDTO.getSmsTextDTO().getCountryCode();
			DebugManager.trace(_module, _class, "readConfiguration", "entered VMMCIMTThreadProcessor code:"
					+ m_countryCode, m_strThreadName, null);
			// Normal MCI message
			/*
			 * m_strMCInfoStart =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessageStart() + " ";
			 * m_strMCInfoMidOneCall =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessageMidOneCall();
			 * m_strMCInfoMidManyCalls =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessageMidManyCalls();
			 * m_strMCInfoSeparator =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessageInfoSeparator()
			 * + " "; if(m_strMCInfoSeparator.length() > 2) {
			 * m_strMCInfoSeparator = " " + m_strMCInfoSeparator; }
			 * 
			 * m_strMCInfoNoMessage =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessageNoMessage();
			 * m_strMCInfoEndMessageIndexPresentAt =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessagePageIndex();
			 * m_bMCInfoEndMessageIndexPresentAt =
			 * (m_strMCInfoEndMessageIndexPresentAt
			 * .equalsIgnoreCase("start"))?true:false;
			 * 
			 * m_strMCInfoMessageIndex =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessagePageIndex();
			 * m_strMCInfoPageNumber =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessagePageNumber();
			 * m_strMCInfoEnd =
			 * ConfigDTO.getSmsTextDTO().getMissedCallInfoMessageEnd();
			 * if(m_strMCInfoEnd.length() > 1) { m_strMCInfoEnd = " " +
			 * m_strMCInfoEnd; }
			 * 
			 * // MCI message if sender number is caller number
			 * m_strMCIfSenderNoIsCallerInfoStart = ConfigDTO.getSmsTextDTO().
			 * getMissedCallInfoMessageIfSenderNumberIsCallerNumberStart() +
			 * " "; m_strMCIfSenderNoIsCallerInfoMidOneCall =
			 * ConfigDTO.getSmsTextDTO
			 * ().getMissedCallInfoMessageIfSenderNumberIsCallerNumberMidOneCall
			 * (); m_strMCIfSenderNoIsCallerInfoMidManyCalls =
			 * ConfigDTO.getSmsTextDTO
			 * ().getMissedCallInfoMessageIfSenderNumberIsCallerNumberMidManyCalls
			 * (); m_strMCIfSenderNoIsCallerInfoSeparator =
			 * ConfigDTO.getSmsTextDTO
			 * ().getMissedCallInfoMessageIfSenderNumberIsCallerNumberInfoSeparator
			 * ();
			 * 
			 * if(m_strMCIfSenderNoIsCallerInfoSeparator.length() > 2) {
			 * m_strMCIfSenderNoIsCallerInfoSeparator = " " +
			 * m_strMCIfSenderNoIsCallerInfoSeparator; }
			 * 
			 * m_strMCIfSenderNoIsCallerInfoNoMessage =
			 * ConfigDTO.getSmsTextDTO()
			 * .getMissedCallInfoMessageIfSenderNumberIsCallerNumberNoMessage();
			 * m_strMCIfSenderNoIsCallerInfoEndMessageIndexPresentAt =
			 * ConfigDTO.getSmsTextDTO().
			 * getMissedCallInfoMessageIfSenderNumberIsCallerNumberPageIndex();
			 * m_bMCIfSenderNoIsCallerInfoEndMessageIndexPresentAt =
			 * (m_strMCIfSenderNoIsCallerInfoEndMessageIndexPresentAt
			 * .equalsIgnoreCase("start"))?true:false;
			 * 
			 * m_strMCIfSenderNoIsCallerInfoMessageIndex =
			 * ConfigDTO.getSmsTextDTO
			 * ().getMissedCallInfoMessageIfSenderNumberIsCallerNumberPageIndex
			 * (); m_strMCIfSenderNoIsCallerInfoPageNumber =
			 * ConfigDTO.getSmsTextDTO
			 * ().getMissedCallInfoMessageIfSenderNumberIsCallerNumberPageNumber
			 * (); m_strMCIfSenderNoIsCallerInfoEnd = ConfigDTO.getSmsTextDTO().
			 * getMissedCallInfoMessageIfSenderNumberIsCallerNumberEnd();
			 * if(m_strMCIfSenderNoIsCallerInfoEnd.length() > 1) {
			 * m_strMCIfSenderNoIsCallerInfoEnd = " " +
			 * m_strMCIfSenderNoIsCallerInfoEnd; }
			 */

			m_bPrependCntryCodeInSmsText = ConfigDTO.getMissedCallsDTO().isPrependCountryCodeInSMSText();
			// smsCountTokenNumber =
			// ConfigDTO.getSmsTextDTO().getPropertyMC_NUMTIMES_TOKEN_POSITION();
			// fromTimeTokenNumber =
			// ConfigDTO.getSmsTextDTO().getPropertyMC_FROMCALLTIME_TOKEN_POSITION();
			m_reasons_supported = m_vmmtdisp.getReasonsSupported();
			m_bSenderNumberAsCallbackNumber = ConfigDTO.getMissedCallsDTO().isMCISenderNumberAsCallbackNumber();
			smsCallBackNumber = ConfigDTO.getMissedCallsDTO().getSMSCallbackNumber();
			m_bCollateCallers = ConfigDTO.getMissedCallsDTO().isMCICollateCallers();
			m_bPrependCountryCodeForSenderNo = ConfigDTO.getMissedCallsDTO().isMCIPrependCountryCodeForSenderNumber();
			m_bPrependCountryCodeIfCallbackNumberIsCallerNumber = ConfigDTO.getMissedCallsDTO()
					.isMciPrependCountryCodeIfCallbackNumberIsCallerNumber();
			isNeedToAddPlusB4CountryCode = ConfigDTO.getMissedCallsDTO()
					.isMciPrependPlusIfCallbackNumberIsCallerNumber();
			adMessage = AdMessage.getInstance();
			isDumpMciMissedcalls = ConfigDTO.getCommonDTO().isStoreMciMissedcalls();
		} catch (Exception e) {
			e.printStackTrace();
			DebugManager.exception(_module, _class, "readConfiguration", e, m_strThreadName, null);
			return false;
		}

		return true;
	}

	public Vector<MciMessageAndCallbackNumberDto> createMessageIfSenderNumberIsCallerNumber(String subscriber,
			Vector<MciMissedcallDto> vecMciMcDto, Hashtable<String, SubscriberDto> htSubscriberProvisioning) {
		// DebugManager.detail(_module, _class,
		// "createMessageIfSenderNumberIsCallerNumber", ">>>>>Entered.",
		// Thread.currentThread().getName(), "");
		SubscriberDto subDto = htSubscriberProvisioning.get(subscriber);
		String code = "ENG";
		if (subDto != null && subDto.getVern_supp().equals("Y")) {
			code = subDto.getLanguage();
			if (code.equalsIgnoreCase("DEF")) {
				code = MCAXMLParser.defaultLangugeCode;
			}
			code = code.toUpperCase();
			if(!MCAXMLParser.smsTextDetails.containsKey(code))
				code = "ENG";
		}

		Vector<MciMessageAndCallbackNumberDto> vecMciMessageAndCallbackNumberDto = new Vector<MciMessageAndCallbackNumberDto>();
		MciMissedcallDto mciMcDto;
		Vector<String> vecCallers = new Vector<String>();

		// if no missedcalls send message like "you have no missed calls"
		if (vecMciMcDto == null || vecMciMcDto.size() == 0) {
			MciMessageAndCallbackNumberDto mciMsgAndCallbackNoDto = new MciMessageAndCallbackNumberDto(
			// m_strMCIfSenderNoIsCallerInfoNoMessage,
					MCAXMLParser.smsTextDetails.get(code)
							.getMissedCallInfoMessageIfSenderNumberIsCallerNumberNoMessage(), ConfigDTO
							.getMissedCallsDTO().getSMSCallbackNumber());
			vecMciMessageAndCallbackNumberDto.add(mciMsgAndCallbackNoDto);
			return vecMciMessageAndCallbackNumberDto;
		}

		boolean oldDatePresent = false;
		for (int i = 0; i < vecMciMcDto.size(); i++) {
			Date dtLastCalledTime = vecMciMcDto.get(i).getDtLastCalledTime();
			Calendar calCalledTime = Calendar.getInstance();
			calCalledTime.setTime(dtLastCalledTime);
			if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calCalledTime.get(Calendar.DAY_OF_YEAR)) {
				oldDatePresent = true;
				break;
			}
		}
		// DebugManager.detail(_module, _class,
		// "createMessageIfSenderNumberIsCallerNumber",
		// "oldDatePresent "+oldDatePresent, Thread.currentThread().getName(),
		// "");

		for (int i = 0; i < vecMciMcDto.size(); i++) {
			mciMcDto = vecMciMcDto.get(i);
			String callerNum = mciMcDto.getCaller();
			if (subDto != null) {
				ProductDto prodDto = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
				if (prodDto != null) {
					String strBlackOrWhite = prodDto.getBlackOrWhite();
					if (strBlackOrWhite != null) {
						if (strBlackOrWhite.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_BLACKLIST)) {
							if (subDto.getHtBlackAndWhite().get(callerNum) != null) {
								vecCallers.add(callerNum);
								continue;
							}
						}
						if (strBlackOrWhite.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_WHITELIST)) {
							if (subDto.getHtBlackAndWhite().get(callerNum) == null) {
								vecCallers.add(callerNum);
								continue;
							}
						}
					}
				}
			}

			if (m_bPrependCntryCodeInSmsText) {
				if (callerNum != null) {
					if (!callerNum.trim().startsWith("+")) {
						if (callerNum.trim().startsWith("0")) {
							callerNum = callerNum.trim().substring(1); // remove
																		// 0.
							if (callerNum.trim().startsWith(m_countryCode)) {
								if (callerNum.length() > 10) {
									callerNum = "+" + callerNum; // just add a
																	// '+'.
								}
							} else {
								callerNum = "+" + m_countryCode + callerNum;// add
																			// CC.
							}
						} else if (callerNum.trim().startsWith(m_countryCode)) {
							if (callerNum.length() > 10) {
								callerNum = "+" + callerNum; // just add a '+'.
							}
						} else if (callerNum.trim().equalsIgnoreCase("")) {
							callerNum = "<UNKNOWN>";
						} else {
							callerNum = "+" + m_countryCode + callerNum.trim();
						}
					}
				} else {
					callerNum = "<UNKNOWN>";
				}
			}

			int timesCalled = mciMcDto.getCount();
			Date dtLastTime = mciMcDto.getDtLastCalledTime();
			String message = replaceValuesIfSenderNumberIsCallerNumber(callerNum, timesCalled, dtLastTime,
					oldDatePresent, code);
			String completeMessage = "";
			if (MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageIfSenderNumberIsCallerNumberPageIndex()
					.trim().equalsIgnoreCase("start")) {
				completeMessage = MCAXMLParser.smsTextDetails.get(code)
						.getMissedCallInfoMessageIfSenderNumberIsCallerNumberStart()
						+ message
						+ MCAXMLParser.smsTextDetails.get(code)
								.getMissedCallInfoMessageIfSenderNumberIsCallerNumberEnd();
			} else {
				completeMessage = MCAXMLParser.smsTextDetails.get(code)
						.getMissedCallInfoMessageIfSenderNumberIsCallerNumberStart()
						+ message
						+ MCAXMLParser.smsTextDetails.get(code)
								.getMissedCallInfoMessageIfSenderNumberIsCallerNumberEnd();
			}
			// ####################### This is done for Ad Server hitting.
			// #######################//
			if (ConfigDTO.getAdServerDTO().isEnabled() && ConfigDTO.getAdServerDTO().isMciEnabled()) {
				String responseMessage = "";
				responseMessage = adMessage.hitSite(subscriber, completeMessage);

				if (responseMessage != null && !responseMessage.equalsIgnoreCase("")) {
					completeMessage = completeMessage + " " + responseMessage;
				} else {
					if (ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString()) {
						completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning, subscriber,
								null);
					}
				}
			} else {
				completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning, subscriber, callerNum);
			}
			// DebugManager.detail(_module, _class,
			// "createMessageIfSenderNumberIsCallerNumber", ">>>>>Subscriber = "
			// + subscriber + ", completeMessage is " + completeMessage,
			// Thread.currentThread().getName(), null);
			MciMessageAndCallbackNumberDto obj = new MciMessageAndCallbackNumberDto(completeMessage, callerNum);
			vecMciMessageAndCallbackNumberDto.add(obj);

		}
		if (vecCallers != null && vecCallers.size() > 0) {
			Connection con = VMNotificationDaemon.getConFactory().getConnection();
			ConfigDTO.getDaoDTO().getMissedCallDao().deleteBlackListedMissedcalls(con, subscriber, vecCallers);
			VMNotificationDaemon.getConFactory().releaseConnection(con);
		}

		return vecMciMessageAndCallbackNumberDto;
	}

	public Vector<MciMessageAndCallbackNumberDto> createMessage(String subscriber,
			Vector<MciMissedcallDto> vecMciMcDto, Hashtable<String, SubscriberDto> htSubscriberProvisioning,
			String callBackNumber) {
		// DebugManager.detail(_module, _class, "createMessage",
		// ">>>>>Entered.", Thread.currentThread().getName(), "");
		SubscriberDto subDto = htSubscriberProvisioning.get(subscriber);

		String code = "ENG";
		if (subDto !=null && subDto.getVern_supp().equals("Y")) {
			code = subDto.getLanguage();
			if (code.equalsIgnoreCase("DEF")) {
				code = MCAXMLParser.defaultLangugeCode;
			}

			code = code.toUpperCase();
			if(!MCAXMLParser.smsTextDetails.containsKey(code))
				code = "ENG";
		}
		Vector<MciMessageAndCallbackNumberDto> vecMciMessageAndCallbackNumberDto = new Vector<MciMessageAndCallbackNumberDto>();
		MciMissedcallDto mciMcDto;
		String finalMessage = "";
		int iMessageIndex = 0;
		boolean oldDatePresent = false;
		Vector<String> vecCallers = new Vector<String>();

		if (vecMciMcDto == null || vecMciMcDto.size() == 0) {
			MciMessageAndCallbackNumberDto mciMsgAndCallbackNoDto = new MciMessageAndCallbackNumberDto(
					MCAXMLParser.smsTextDetails.get(code)
							.getMissedCallInfoMessageIfSenderNumberIsCallerNumberNoMessage(),
					// m_strMCIfSenderNoIsCallerInfoNoMessage,
					ConfigDTO.getMissedCallsDTO().getSMSCallbackNumber());
			vecMciMessageAndCallbackNumberDto.add(mciMsgAndCallbackNoDto);

			return vecMciMessageAndCallbackNumberDto;
		}

		for (int i = 0; i < vecMciMcDto.size(); i++) {
			Date dtLastCalledTime = vecMciMcDto.get(i).getDtLastCalledTime();
			Calendar calCalledTime = Calendar.getInstance();
			calCalledTime.setTime(dtLastCalledTime);
			if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calCalledTime.get(Calendar.DAY_OF_YEAR)) {
				oldDatePresent = true;
				break;
			}
		}
		// DebugManager.detail(_module, _class, "createMessage",
		// "oldDatePresent "+oldDatePresent, Thread.currentThread().getName(),
		// "");

		for (int i = 0; i < vecMciMcDto.size(); i++) {
			mciMcDto = vecMciMcDto.get(i);
			String callerNum = mciMcDto.getCaller();

			if (subDto != null) {
				ProductDto prodDto = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
				if (prodDto != null) {
					String strBlackOrWhite = prodDto.getBlackOrWhite();
					if (strBlackOrWhite != null) {
						if (strBlackOrWhite.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_BLACKLIST)) {
							if (subDto.getHtBlackAndWhite().get(callerNum) != null) {
								vecCallers.add(callerNum);
								continue;
							}
						}
						if (strBlackOrWhite.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_WHITELIST)) {
							if (subDto.getHtBlackAndWhite().get(callerNum) == null) {
								vecCallers.add(callerNum);
								continue;
							}
						}
					}
				}
			}
			// DebugManager.status(_module, _class, "createMessage",
			// "SUBSCRIBER="+mciMcDto.getSubscriber()+",CALLER="+callerNum,
			// Thread.currentThread().getName(), "");
			if (m_bPrependCntryCodeInSmsText) {
				if (callerNum != null) {
					if (!callerNum.trim().startsWith("+")) {
						if (callerNum.trim().startsWith("0")) {
							callerNum = callerNum.trim().substring(1);
							if (callerNum.trim().startsWith(m_countryCode)) {
								if (callerNum.trim().length() <= 10) {
									callerNum = "+" + m_countryCode + callerNum;
								} else {
									callerNum = "+" + callerNum;
								}
							} else {
								callerNum = "+" + m_countryCode + callerNum;
							}
						} else if (callerNum.trim().startsWith(m_countryCode)) {
							if (callerNum.trim().length() <= 10) {
								callerNum = "+" + m_countryCode + callerNum.trim();
							} else {
								callerNum = "+" + callerNum.trim();
							}
						} else if (callerNum.trim().equalsIgnoreCase("")) {
							callerNum = "<UNKNOWN>";
							continue;
						} else {
							callerNum = "+" + m_countryCode + callerNum.trim();
						}
					}

				} else {
					callerNum = "<UNKNOWN>";
					continue;
				}
			}

			int timesCalled = mciMcDto.getCount();
			Date dtLastTime = mciMcDto.getDtLastCalledTime();
			String message = replaceValues(callerNum, timesCalled, dtLastTime, oldDatePresent, code);
			String prefix = (i > 0) ? MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageInfoSeparator()
					: "";
			String strMessagePageNumber = replaceValues(MCAXMLParser.smsTextDetails.get(code)
					.getMissedCallInfoMessagePageNumber(), iMessageIndex + 1, -1);

			if (m_bCollateCallers) {
				if ((MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageStart().length()
						+ finalMessage.length() + prefix.length() + message.length())
						+ MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageEnd().length()
						+ strMessagePageNumber.length() + 1 > SMS_SIZE) {
					String completeMessage = "";
					if (MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessagePageIndex().trim()
							.equalsIgnoreCase("start")) {
						completeMessage = strMessagePageNumber + " "
								+ MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageStart() + finalMessage;
					} else {
						completeMessage = MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageStart()
								+ finalMessage + " " + strMessagePageNumber;
					}
					// ####################### This is done for Ad Server
					// hitting. #######################//
					if (ConfigDTO.getAdServerDTO().isEnabled() && ConfigDTO.getAdServerDTO().isMciEnabled()) {
						String responseMessage = "";
						responseMessage = adMessage.hitSite(subscriber, completeMessage);
						if (responseMessage != null && !responseMessage.equalsIgnoreCase("")) {
							completeMessage = completeMessage + " " + responseMessage;
						} else {
							if (ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString()) {
								completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning,
										subscriber, null);
							}
						}

					} else {
						if (ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString()) {
							completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning,
									subscriber, null);
						}
					}
					// DebugManager.trace(_module, _class, "createMessage",
					// completeMessage, Thread.currentThread().getName(), null);
					MciMessageAndCallbackNumberDto obj = new MciMessageAndCallbackNumberDto(completeMessage,
							callBackNumber);
					vecMciMessageAndCallbackNumberDto.add(obj);
					finalMessage = message;
					iMessageIndex++;
				} else {
					finalMessage += prefix + message;
				}
			} else {
				String completeMessage = "";
				if (i == 0) {
					finalMessage = message;
				} else {
					if (MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessagePageIndex().trim()
							.equalsIgnoreCase("start")) {
						completeMessage = MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageStart()
								+ finalMessage + MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageEnd();
					} else {
						completeMessage = MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageStart()
								+ finalMessage + MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageEnd();
					}
					// DebugManager.detail(_module, _class, "createMessage",
					// "Subscriber="+subscriber+",completeMessage="+completeMessage+".",
					// Thread.currentThread().getName(), "");
					// ####################### This is done for Ad Server
					// hitting. #######################//
					if (ConfigDTO.getAdServerDTO().isEnabled() && ConfigDTO.getAdServerDTO().isMciEnabled()) {
						String responseMessage = "";
						responseMessage = adMessage.hitSite(subscriber, completeMessage);

						if (responseMessage != null && !responseMessage.equalsIgnoreCase("")) {
							completeMessage = completeMessage + " " + responseMessage;
						} else {
							if (ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString()) {
								completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning,
										subscriber, null);
							}
						}
					} else {
						if (ConfigDTO.getCommonDTO().isLocalPromotionRequired()) {
							completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning,
									subscriber, null);
						}
					}
					MciMessageAndCallbackNumberDto obj = new MciMessageAndCallbackNumberDto(completeMessage,
							callBackNumber);

					vecMciMessageAndCallbackNumberDto.add(obj);
					finalMessage = message;
					iMessageIndex++;
				}
			}

		}

		if (!finalMessage.equals("")) {
			// DebugManager.status(_module, _class, "createMessage",
			// ">>Subscriber = " + subscriber + ", finalMessage = " +
			// finalMessage, Thread.currentThread().getName(), null);
			String strMessagePageNumber = replaceValues(MCAXMLParser.smsTextDetails.get(code)
					.getMissedCallInfoMessagePageNumber(), iMessageIndex + 1, -1);
			String completeMessage = MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageStart()
					+ finalMessage + MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageEnd();

			if (iMessageIndex != 0) {
				if (m_bCollateCallers) {
					if (MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessagePageIndex().trim()
							.equalsIgnoreCase("start")) {
						completeMessage = strMessagePageNumber + " " + completeMessage;
					} else {
						completeMessage = completeMessage + " " + strMessagePageNumber;
					}
				}
			}

			// ####################### This is done for Ad Server hitting.
			// #######################//
			if (ConfigDTO.getAdServerDTO().isEnabled() && ConfigDTO.getAdServerDTO().isMciEnabled()) {
				String responseMessage = "";
				responseMessage = adMessage.hitSite(subscriber, completeMessage);

				if (responseMessage != null && !responseMessage.equalsIgnoreCase("")) {
					completeMessage = completeMessage + " " + responseMessage;
				} else {
					if (ConfigDTO.getAdServerDTO().isLocalAdMessageTobeAddedIfAdServerResponseIsEmptyString()) {
						completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning, subscriber,
								null);
					}
				}
			} else {
				completeMessage = addPromotionMessage(completeMessage, htSubscriberProvisioning, subscriber, null);
			}

			// DebugManager.detail(_module, _class, "createMessage",
			// ">>>>>Subscriber = " + subscriber + ", completeMessage " +
			// completeMessage, Thread.currentThread().getName(), null);
			MciMessageAndCallbackNumberDto obj = new MciMessageAndCallbackNumberDto(completeMessage, callBackNumber);
			vecMciMessageAndCallbackNumberDto.add(obj);
		}

		if (vecCallers != null && vecCallers.size() > 0) {
			Connection con = VMNotificationDaemon.getConFactory().getConnection();
			ConfigDTO.getDaoDTO().getMissedCallDao().deleteBlackListedMissedcalls(con, subscriber, vecCallers);
			VMNotificationDaemon.getConFactory().releaseConnection(con);
		}

		return vecMciMessageAndCallbackNumberDto;
	}

	public String addPromotionMessage(String completeMessage,
			Hashtable<String, SubscriberDto> htSubscriberProvisioning, String subscriber, String callerNo) {
		java.util.Date dtStartTime = null;
		int productId = -1;
		SubscriberDto subDto = null;
		boolean bolNeedToGiveDynamicCallerNo = false;

		if (callerNo != null) {
			bolNeedToGiveDynamicCallerNo = true;
		}
		String promoCode = "ENG";

		if (htSubscriberProvisioning != null) {
			subDto = htSubscriberProvisioning.get(subscriber);
			if (subDto != null) {
				productId = subDto.getProductID();
				// strProductId = ""+productId;
				if (subDto.getVern_supp().equals("Y")) {
					promoCode = subDto.getLanguage();
					if (promoCode.equalsIgnoreCase("DEF")) {
						promoCode = MCAXMLParser.defaultPromoLanguageCode;
					}

					promoCode = promoCode.toUpperCase();
					if(!MCAXMLParser.smsTextDetails.containsKey(promoCode))
						promoCode = "ENG";
				}
			} else {
				productId = ProductDetails.defaultProduct.getProductID();
				// strProductId = ""+productId;
			}
		} else {
			productId = ProductDetails.defaultProduct.getProductID();
			// strProductId = ""+productId;
		}

		String strPromoMessage = null;

		if (ConfigDTO.getCommonDTO().isLocalPromotionRequired()) {
			if (subDto != null) {
				dtStartTime = subDto.getDtStartTime();
				if (dtStartTime == null) {
					DebugManager.status(_module, _class, "addPromotionMessage", ">>>>>dtStartTime=null for SUBSCRIBER="
							+ subDto.getSubscriber() + ",manually set dtStartTime=60 mins before current time.", Thread
							.currentThread().getName(), null);
					dtStartTime = new java.util.Date(System.currentTimeMillis() - 60 * 60 * 1000);
				}

				strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromotionalMessage(productId,
						dtStartTime.getTime());
			} else {
				strPromoMessage = MCAXMLParser.smsTextDetails.get(promoCode).getPromoExpiryMessage(productId);
			}
			DebugManager.trace(_module, _class, "addPromotionalMessage", "promoMessage : " + strPromoMessage, Thread
					.currentThread().getName(), null);
		}

		if (strPromoMessage != null && !strPromoMessage.trim().equals("")) {
			if (bolNeedToGiveDynamicCallerNo) {
				int pos;
				while ((pos = strPromoMessage.indexOf(MOBILE_NUMBER)) != -1) {
					strPromoMessage = strPromoMessage.substring(0, pos) + callerNo
							+ strPromoMessage.substring(pos + MOBILE_NUMBER.length());
				}
			}
			if (completeMessage.length() + strPromoMessage.length() + 1 < SMS_SIZE) {
				completeMessage = completeMessage + " " + strPromoMessage;
				// DebugManager.trace(_module, _class, "addPromotionMessage",
				// "SUBSCRIBER="+subscriber + ",FINAL_MESSAGE="+completeMessage
				// + ",PROMO_MESSAGE="+strPromoMessage,
				// Thread.currentThread().getName(), null);
			}
		} else {
			// DebugManager.trace(_module, _class, "addPromotionMessage",
			// ">>>>>SUBSCRIBER="+subscriber +",FINAL_MESSAGE="+completeMessage
			// + ",PROMO_MESSAGE="+strPromoMessage,
			// Thread.currentThread().getName(), null);
		}

		return completeMessage;
	}

	public String replaceValues(String caller, int timesCalled, Date dtLastTime, boolean oldDatesPresent, String code) {

		String message;
		String lastTime = DaemonUtils.getFormattedDate(dtLastTime, "h:mm a");
		Calendar calLastTime = Calendar.getInstance();
		calLastTime.setTime(dtLastTime);

		if (oldDatesPresent) {
			if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calLastTime.get(Calendar.DAY_OF_YEAR)) {
				lastTime = lastTime + " on " + DaemonUtils.getFormattedDate(dtLastTime, "MMM d");
			} else {
				lastTime = lastTime + " today";
			}
		}

		if (timesCalled == 1) {
			message = MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageMidOneCall();
		} else {
			message = MCAXMLParser.smsTextDetails.get(code).getMissedCallInfoMessageMidManyCalls();
		}

		int pos;
		// Replace NUMTIMES
		while ((pos = message.indexOf(NUM_OF_TIMES)) != -1) {
			message = message.substring(0, pos) + timesCalled + message.substring(pos + NUM_OF_TIMES.length());
		}

		// Replace CALLER
		while ((pos = message.indexOf(CALLER)) != -1) {
			message = message.substring(0, pos) + caller + message.substring(pos + CALLER.length());
		}

		// Replace LASTTIME
		while ((pos = message.indexOf(LASTTIME)) != -1) {
			message = message.substring(0, pos) + lastTime + message.substring(pos + LASTTIME.length());
		}

		return message;
	}

	public String replaceValuesIfSenderNumberIsCallerNumber(String caller, int timesCalled, Date dtLastTime,
			boolean oldDatesPresent, String code) {

		String message;
		String lastTime = DaemonUtils.getFormattedDate(dtLastTime, "h:mm a");
		Calendar calLastTime = Calendar.getInstance();
		calLastTime.setTime(dtLastTime);

		if (oldDatesPresent) {
			if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != calLastTime.get(Calendar.DAY_OF_YEAR)) {
				lastTime = lastTime + " on " + DaemonUtils.getFormattedDate(dtLastTime, "MMM d");
			} else {
				lastTime = lastTime + " today";
			}
		}

		if (timesCalled == 1) {
			message = MCAXMLParser.smsTextDetails.get(code)
					.getMissedCallInfoMessageIfSenderNumberIsCallerNumberMidOneCall();
			// m_strMCIfSenderNoIsCallerInfoMidOneCall;
		} else {
			message = MCAXMLParser.smsTextDetails.get(code)
					.getMissedCallInfoMessageIfSenderNumberIsCallerNumberMidManyCalls();
			// m_strMCIfSenderNoIsCallerInfoMidManyCalls;
		}

		int pos;
		// Replace NUMTIMES
		while ((pos = message.indexOf(NUM_OF_TIMES)) != -1) {
			message = message.substring(0, pos) + timesCalled + message.substring(pos + NUM_OF_TIMES.length());
		}

		// Replace CALLER
		while ((pos = message.indexOf(CALLER)) != -1) {
			message = message.substring(0, pos) + caller + message.substring(pos + CALLER.length());
		}

		// Replace LASTTIME
		while ((pos = message.indexOf(LASTTIME)) != -1) {
			message = message.substring(0, pos) + lastTime + message.substring(pos + LASTTIME.length());
		}

		return message;
	}

	public String replaceValues(String message, int presentSms, int totalSMS) {

		int pos;
		// Replace PRESENT_SMS_NUMBER
		while (presentSms > 0 && (pos = message.indexOf(PRESENT_SMS_NUMBER)) != -1) {
			message = message.substring(0, pos) + presentSms + message.substring(pos + PRESENT_SMS_NUMBER.length());
		}

		// Replace TOTAL_NUMBER_OF_SMS
		while (totalSMS > 0 && (pos = message.indexOf(TOTAL_NUMBER_OF_SMS)) != -1) {
			message = message.substring(0, pos) + totalSMS + message.substring(pos + TOTAL_NUMBER_OF_SMS.length());
		}

		return message;
	}

	public Vector<Sms> getMciSmsForAllSubscribers(Hashtable<String, Vector<MciMissedcallDto>> htMciMissedcall,
			Hashtable<String, SubscriberDto> htSubscriberProvisioning) {

		if (htMciMissedcall != null) {
			DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "TOTAL mci missdcalls ="
					+ htMciMissedcall.size(), Thread.currentThread().getName(), null);
		}
		Vector<Sms> vecSms = new Vector<Sms>();
		Enumeration<String> enumStr = htMciMissedcall.keys();

		while (enumStr.hasMoreElements()) {
			String callBackNumber = null;
			Vector<MciMessageAndCallbackNumberDto> vecMciMessageAndCallbackNumberDto = new Vector<MciMessageAndCallbackNumberDto>();
			ProductDto prodDto = null;
			String smsType = MESSAGE_TYPE_MCI;
			boolean isDsrRequired = false;
			String subscriber = enumStr.nextElement();
			DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "subscriber=" + subscriber, Thread
					.currentThread().getName(), null);
			Vector<MciMissedcallDto> vecMciMcDto = htMciMissedcall.get(subscriber);
			SubscriberDto subDto = htSubscriberProvisioning.get(subscriber);

			String code = "ENG";
			if (subDto != null && subDto.getVern_supp().equals("Y")) {
				code = subDto.getLanguage();
				if (code.equalsIgnoreCase("DEF")) {
					code = MCAXMLParser.defaultLangugeCode;
				}

				code = code.toUpperCase();
				if(!MCAXMLParser.smsTextDetails.containsKey(code))
					code = "ENG";
			}

			if (subDto != null) {
				prodDto = ProductDetails.htProductIdToProduct.get(new Integer(subDto.getProductID()));
			} else {
							prodDto = ProductDetails.defaultProduct;
			}

			String prodName = prodDto.getProductName();
			callBackNumber = prodDto.getSmsSendingNo();
			String strLcaAvailable = prodDto.getLcaAvailable();

			if (strLcaAvailable.equalsIgnoreCase(PROVISIONED_TO_LCA_YES)) {
				DebugManager.status(_module, _class, "getMciSmsForAllSubscribers",
						"LCA Available is true, isDsrRequired is set to true.", Thread.currentThread().getName(), null);
				isDsrRequired = true;
			} else if (strLcaAvailable.equalsIgnoreCase(PROVISIONED_TO_LCA_SILENT)) {
				isDsrRequired = true;
				smsType = MESSAGE_TYPE_SILENT_MCI;
			}

			String[] arrStrSmscName = prodDto.getArrStrSmscName();
			DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "After arrStrSmscName:"
					+ arrStrSmscName.length, Thread.currentThread().getName(), null);
			String smscName = arrStrSmscName[random.nextInt(arrStrSmscName.length)];

			if (callBackNumber == null) {
				DebugManager.fatalError(_module, _class, "getMciSmsForAllSubscribers",
						"callbacknumber is null for subscriber=" + subscriber, Thread.currentThread().getName(), null);
				callBackNumber = smsCallBackNumber;
			}
			DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "After callbacknumber" + callBackNumber,
					Thread.currentThread().getName(), null);
			if (callBackNumber.equalsIgnoreCase("CALLER")) {
				m_bCollateCallers = false;
				m_bSenderNumberAsCallbackNumber = true;
				vecMciMessageAndCallbackNumberDto = createMessageIfSenderNumberIsCallerNumber(subscriber, vecMciMcDto,
						htSubscriberProvisioning);
			} else {
				m_bCollateCallers = ConfigDTO.getMissedCallsDTO().isMCICollateCallers();
				DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "create Message", Thread
						.currentThread().getName(), null);
				vecMciMessageAndCallbackNumberDto = createMessage(subscriber, vecMciMcDto, htSubscriberProvisioning,
						callBackNumber);
			}

			if (vecMciMessageAndCallbackNumberDto != null) {
				for (int i = 0; i < vecMciMessageAndCallbackNumberDto.size(); i++) {
					String message = vecMciMessageAndCallbackNumberDto.get(i).getMessage();
					/*
					 * if(!message.contains("missed")){ continue; }
					 */

					int noOfSmsPerDay = prodDto.getNoOfSmsPerDay();
					int noOfSmsForToday = 0;

					if (noOfSmsPerDay > 0) {
						noOfSmsForToday = subDto.getNoOfSmsForToday();
						if (noOfSmsForToday >= noOfSmsPerDay) {
							if (noOfSmsForToday < 99) {
								String strLimitSms = MCAXMLParser.smsTextDetails.get(code).getLimitIntimationMessage()
										.replace("%NO_OF_SMS_FOR_ONE_DAY%", "" + noOfSmsPerDay);
								String[] arrStrSmscNameForLimitSms = prodDto.getArrStrSmscName();
								String smscNameForLimitSms = arrStrSmscName[random
										.nextInt(arrStrSmscNameForLimitSms.length)];

								Sms newSms = new Sms(subscriber, MESSAGE_TYPE_LIMIT_INTIMATION, SMS_STATUS_CREATED,
										strLimitSms, ConfigDTO.getSmsEngineDTO().getDefaultSendingNumber(), message,
										isDsrRequired, smscNameForLimitSms);
								newSms.setProdName(prodName);
								if (code.equalsIgnoreCase("ENG")) {
									newSms.setUnicode(false);
								} else
									newSms.setUnicode(true);
								vecSms.add(newSms);
								updateNumberOfSmsForToday(subscriber, 99);
								subDto.setNoOfSmsForToday(99);
								continue;
							} else {
								continue;
							}
						}
					}

					if (prodDto.getMcaAvailable().equalsIgnoreCase(PROVISIONED_TO_MCA_JUST_FOR_DSR)) {
						message = MCAXMLParser.smsTextDetails.get(code).getCustomizedMessageToGetDsrNode();
						smsType = MESSAGE_TYPE_CUSTOMIZED_FOR_GETTING_DSR;
					} else {
						message = replaceValues(message, -1, vecMciMessageAndCallbackNumberDto.size());

						DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "#1. subscriber="
								+ subscriber + ",callBackNumber=" + callBackNumber + ",message=" + message, Thread
								.currentThread().getName(), null);

						callBackNumber = vecMciMessageAndCallbackNumberDto.get(i).getCallbackNumber();
						DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "#2. subscriber="
								+ subscriber + ",callBackNumber=" + callBackNumber + ",message=" + message, Thread
								.currentThread().getName(), null);

						callBackNumber = getCallBackNumber(callBackNumber, m_bPrependCountryCodeForSenderNo,
								m_bSenderNumberAsCallbackNumber, isNeedToAddPlusB4CountryCode);
						DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "#3. subscriber="
								+ subscriber + ",callBackNumber=" + callBackNumber + ",message=" + message, Thread
								.currentThread().getName(), null);
					}

					Sms newSms = new Sms(subscriber, smsType, SMS_STATUS_CREATED, message, callBackNumber, message,
							isDsrRequired, smscName);
					if (code.equalsIgnoreCase("ENG")) {
						newSms.setUnicode(false);
					} else
						newSms.setUnicode(true);
					newSms.setProdName(prodName);
					DebugManager.status(_module, _class, "getMciSmsForAllSubscribers", "subscriber=" + subscriber
							+ ",callBackNumber=" + callBackNumber + ",message=" + message, Thread.currentThread()
							.getName(), null);
					vecSms.add(newSms);

					if (noOfSmsPerDay > 0) {
						updateNumberOfSmsForToday(subscriber, noOfSmsForToday + 1);
						subDto.setNoOfSmsForToday(noOfSmsForToday + 1);
					}
				}
			} else {
				DebugManager.trace(_module, _class, "processMissedCallsForMCI", ">>>>>vecMessages is null.",
						m_strThreadName, null);
			}

		}

		return vecSms;
	}

	public void updateNumberOfSmsForToday(String subscriber, int count) {
		Connection con = VMNotificationDaemon.getConFactory().getConnection();
		ConfigDTO.getDaoDTO().getSubscriptionDao().updateNoOfSmsForToday(con, subscriber, count);
		VMNotificationDaemon.getConFactory().releaseConnection(con);
	}

	public String getCallBackNumber(String number, boolean m_bPrependCountryCodeForSenderNo,
			boolean bolCallBackNumberAsCallerNumber, boolean isNeedToAddPlusB4CountryCode) {
		String callbacknumber = number;
		
		if (m_bPrependCountryCodeForSenderNo
				|| (bolCallBackNumberAsCallerNumber && m_bPrependCountryCodeIfCallbackNumberIsCallerNumber)) {

			if (callbacknumber != smsCallBackNumber) {
				
				// if callback number doesn't start with +
				if (!callbacknumber.trim().startsWith("+")) {
					if (callbacknumber.trim().startsWith("0")) {
						callbacknumber = callbacknumber.trim().substring(1);

						if (callbacknumber.trim().startsWith(m_countryCode)) {
							if (callbacknumber.trim().length() <= 10) {
								if (isNeedToAddPlusB4CountryCode) {
									callbacknumber = "+" + m_countryCode + callbacknumber;
								} else {
									callbacknumber = m_countryCode + callbacknumber;
								}
							} else {
								if (isNeedToAddPlusB4CountryCode) {
									callbacknumber = "+" + callbacknumber;
								}
							}
						} else {
							if (isNeedToAddPlusB4CountryCode) {
								callbacknumber = "+" + m_countryCode + callbacknumber;
							} else {
								callbacknumber = m_countryCode + callbacknumber;
							}
						}
					} else if (callbacknumber.trim().startsWith(m_countryCode)) {
						if (callbacknumber.trim().length() <= 10) {
							if (isNeedToAddPlusB4CountryCode) {
								callbacknumber = "+" + m_countryCode + callbacknumber;
							} else {
								callbacknumber = m_countryCode + callbacknumber;
							}
						} else {
							if (isNeedToAddPlusB4CountryCode) {
								callbacknumber = "+" + callbacknumber;
							}
						}
					} else {
						if (callbacknumber.trim().length() != 10) {
							// it can be a international number, so don't add
							// country code
							if (isNeedToAddPlusB4CountryCode) {
								callbacknumber = "+" + callbacknumber;
							}
						} else {
							if (isNeedToAddPlusB4CountryCode) {
								callbacknumber = "+" + m_countryCode + callbacknumber.trim();
							} else {
								callbacknumber = m_countryCode + callbacknumber.trim();
							}
						}
					}
				} else {
					if (!isNeedToAddPlusB4CountryCode) {
						callbacknumber = callbacknumber.substring(1);
					}

				}

				DebugManager.trace(_module, _class, "processMissedCallsForMCI",
						">>callbacknumber after adding country code=" + callbacknumber, m_strThreadName, null);
			}
		} else {
		
			if (callbacknumber.trim().startsWith("+")) {
				callbacknumber = callbacknumber.substring(1);
				if (callbacknumber.startsWith(m_countryCode)) {
					if (callbacknumber.length() == 12) {
						callbacknumber = callbacknumber.substring(2);
					}
				}

			} else {
				if (callbacknumber.trim().startsWith("0")) {
					callbacknumber = callbacknumber.substring(1);
				}
				
				if (callbacknumber.trim().startsWith(m_countryCode)) {
					if (callbacknumber.trim().length() == 12) {
						callbacknumber = callbacknumber.substring(2);
					}
				}
			}
		}

		return callbacknumber;

	}

	public void processMissedCalls(Connection con, Vector<String> vecStrSubscriber,
			Hashtable<String, SubscriberDto> htSubscriberProvisioning) {
		Hashtable<String, Vector<MciMissedcallDto>> htMciMissedcall = ConfigDTO.getDaoDTO().getMissedCallDao()
				.getMciMissedcalls(con, vecStrSubscriber, htSubscriberProvisioning);

		Vector<Sms> vecSms = getMciSmsForAllSubscribers(htMciMissedcall, htSubscriberProvisioning);

		

		if (ConfigDTO.getCommonDTO().isLcaActive()) {
			Hashtable<String, String> htMciMissedcallForLcaSubscribers = new Hashtable<String, String>();
			Enumeration<String> enumSub = htMciMissedcall.keys();
			if (enumSub != null) {
				while (enumSub.hasMoreElements()) {
					String strSub = enumSub.nextElement();
					if (strSub != null) {
						SubscriberDto subDtoObj = htSubscriberProvisioning.get(strSub);
						if (subDtoObj != null) {
							int prodID = subDtoObj.getProductID();
							ProductDto prodDto = ProductDetails.htProductIdToProduct.get(prodID);
							if (prodDto != null) {
									if (prodDto.getLcaAvailable().equalsIgnoreCase(PROVISIONED_TO_LCA_YES)
										|| prodDto.getLcaAvailable().equalsIgnoreCase(PROVISIONED_TO_LCA_SILENT)) {

									htMciMissedcallForLcaSubscribers.put(strSub, strSub);
									htMciMissedcall.remove(strSub);
								}
							}
						}
					}
				}
			}
			ConfigDTO.getDaoDTO().getMissedCallDao()
					.dumpMciMissedCallsForLcaSubs(con, htMciMissedcallForLcaSubscribers);
			ConfigDTO.getDaoDTO().getMissedCallDao()
					.deleteMciMissedcallsForLcaSubscribers(con, htMciMissedcallForLcaSubscribers);
		}
        
		ConfigDTO.getDaoDTO().getSmsDao().saveSms(con, vecSms);
		
		if (isDumpMciMissedcalls) {
			ConfigDTO.getDaoDTO().getMissedCallDao().updateMciMissedcalls(con, htMciMissedcall);
		} else {
			ConfigDTO.getDaoDTO().getMissedCallDao().deleteMciMissedcalls(con, htMciMissedcall);
		}

	}

}