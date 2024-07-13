package com.onmobile.apps.ivm.daemons.missedcalls;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import redis.clients.jedis.Jedis;

import com.onmobile.apps.ivm.daemons.common.DaemonUtils;
import com.onmobile.apps.ivm.daemons.common.IDaemonConsts;
import com.onmobile.apps.ivm.daemons.common.OzoneStarter;
import com.onmobile.apps.ivm.daemons.common.ProductDetails;
import com.onmobile.apps.ivm.daemons.common.VMNotificationDaemon;
import com.onmobile.apps.ivm.daemons.config.xmlNodes.ConfigDTO;
import com.onmobile.apps.ivm.daemons.db.model.MissedCall;
import com.onmobile.apps.ivm.daemons.db.model.Subscriber;
import com.onmobile.apps.ivm.daemons.dto.ProductDto;
import com.onmobile.apps.ivm.daemons.dto.SubscriberDto;
import com.onmobile.apps.ivm.daemons.monitoring.AlarmDTO;
import com.onmobile.apps.ivm.daemons.monitoring.AlarmUtility;
import com.onmobile.apps.ivm.daemons.monitoring.MonitoringConstants;
import com.onmobile.common.debug.DebugManager;

public class ChargingHit implements IDaemonConsts {
	private static final String _class = "ChargingHit";
	private static String HOST;
	private static int PORT;
	private static String PROXY_HOST;
	private static int PROXY_PORT;
	private static String POST_URL;
	private static String GET_URL;
	// private static final int HTTP_CONNECT_TIMEOUT = 0;

	private static int HTTP_RESPONSE_TIMEOUT;
	protected static boolean isAlarmRaised = false;
	protected static boolean isHitSuccess = false;
	static {
		DebugManager.status(_module, _class, "Static block", "Intialising", Thread.currentThread().getName(), null);
		try {
			if (ConfigDTO.getChargingDTO().isEnabled()) {
				HOST = ConfigDTO.getChargingDTO().getHost();
				PORT = ConfigDTO.getChargingDTO().getPort();
				PROXY_HOST = ConfigDTO.getChargingDTO().getProxyHost();
				PROXY_PORT = ConfigDTO.getChargingDTO().getProxyPort();
				HTTP_RESPONSE_TIMEOUT = ConfigDTO.getChargingDTO().getResponseTimeOutInSec() * 1000;
				if (ConfigDTO.getChargingDTO().isSyncHit()) {
					DebugManager.status(_module, _class, "Static block", "Intialising  Prism details",
							Thread.currentThread().getName(), null);

					GET_URL = "http://" + HOST + ":" + PORT + ConfigDTO.getChargingDTO().getServlet()
							+ "?user=<user>&pass=<pass>&msisdn=<msisdn>&srvkey=<srvkey>&mode=<mode>&type=U&eventkey=<eventkey>";
				} else {
					DebugManager.status(_module, _class, "Static block", "Intialising  Ump details",
							Thread.currentThread().getName(), null);

					POST_URL = "http://" + HOST + ":" + PORT + ConfigDTO.getChargingDTO().getServlet();
				}

			}
		} catch (Exception e) {
			DebugManager.fatalError(_module, _class, "Static block", "Failed to Intialise" + e,
					Thread.currentThread().getName(), null);
		}
	}

	/**
	 * Charged subscriber Processor
	 */
	private VMMTThreadProcessor mcThreadProcessor;

	/**
	 * Promotional message
	 */
	private String[] m_arrPromotionalMessages = null;

	/**
	 * Enable if multithreaded Processing required
	 */
	private boolean isMultiThreadedRequired = false;

	/**
	 * Executor pool
	 */
	private static Map<Thread, Thread> pool = new HashMap<Thread, Thread>(10);

	/**
	 * SubscriberToBeCharged Shared Queue
	 */
	ConcurrentLinkedQueue<String> sharedQueue = new ConcurrentLinkedQueue<String>();

	/**
	 * Processed Subscriber Shared Queue
	 */
	static ConcurrentLinkedQueue<String> ProcessedQueue = new ConcurrentLinkedQueue<String>();

	/**
	 * Subscriber Charging Response from UMP/Prism
	 */
	// private static Map<String, String> umpResponse = new
	// ConcurrentHashMap<String, String>(500);

	/**
	 * 
	 */
	Hashtable<String, SubscriberDto> htSubscribersProvisioningGlobal = new Hashtable<String, SubscriberDto>();

	/**
	 * Wait X milli sec after each UMP Hit
	 */
	private final Integer WAIT_AFTER_EACH_HIT = 0;

	private static ChargingHit chargingHit;

	/**
	 * Adds subscribers to redis. Extracts the subscribers who have already been
	 * charged for fixed no of days as well as on current day are returned. Rest
	 * are sent for charging
	 * 
	 * @param mcThreadProcessor2
	 * 
	 * @param vecStrSubscriber
	 * @param htSubscribersProvisioning
	 * @return
	 */
	private static Connection chargingHitDbConn = null;

	public static Connection getChargingHitDbConnection() {
		try {
			if (chargingHitDbConn != null && (!chargingHitDbConn.isClosed())) {
				DebugManager.trace(_module, _class, "getChargingHitDbConnection",
						"<< get db connection for charginghit", Thread.currentThread().getName(), null);
				return chargingHitDbConn;
			} else {
				DebugManager.trace(_module, _class, "getChargingHitDbConnection",
						"<< get db connection for charginghit as it is null", Thread.currentThread().getName(), null);
				chargingHitDbConn = VMNotificationDaemon.getConFactory().getConnection();
				return chargingHitDbConn;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chargingHitDbConn;
	}

	public Vector<String> getAlreadyChargedSubscribers(Connection con, VMMTThreadProcessor mcThreadProcessor2,
			Vector<String> subscribers, Hashtable<String, SubscriberDto> htSubscribersProvisioning) {

		try {
			DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
					"subscribers size:" + subscribers.size(), Thread.currentThread().getName(), null);

			mcThreadProcessor = mcThreadProcessor2;

			Vector<String> subscriberToBeCharged = new Vector<String>();
			Vector<String> subscriberToBeUpdated = new Vector<String>();

			// pick from vm_missedcalls_failed table

			ConcurrentHashMap<String, String> conHmSubsWhoGotMC = new ConcurrentHashMap<String, String>(500);
			try {
				DebugManager.status(_module, _class, "getAlreadyChargedSubscribers", "Connection:" + con.isClosed(),
						null, null);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			List<MissedCall> listMissedCall = ConfigDTO.getDaoDTO().getMissedCallDao().pickFailedMissedCalls(con);
			if (listMissedCall.size() > 0) {
				conHmSubsWhoGotMC = getUniqueSubsWhoGotMC(listMissedCall);
				Enumeration<String> enumStrSubscriber = conHmSubsWhoGotMC.keys();
				Vector<String> vecStrSubscsiber = new Vector<String>();
				while (enumStrSubscriber.hasMoreElements()) {
					String strKey = enumStrSubscriber.nextElement();
					if (!subscribers.contains(strKey)) {
						vecStrSubscsiber.add(conHmSubsWhoGotMC.remove(strKey));
						subscribers.add(strKey);
					}

				}
				htSubscribersProvisioning.putAll(getSubscriberProvisioning(con, vecStrSubscsiber));
			}

			// VMNotificationDaemon.getConFactory().releaseConnection(con);
			Iterator<String> sub = subscribers.iterator();
			// Vector<String> alreadyCharged = new Vector<String>();

			Map<String, String> subscribersForDateUpdationMap = new LinkedHashMap<String, String>();
			while (sub.hasNext()) {
				String subscriber = (String) sub.next();
				if (!htSubscribersProvisioning.containsKey(subscriber)) {
					htSubscribersProvisioning.put(subscriber, htSubscribersProvisioningGlobal.get(subscriber));
				}
				SubscriberDto subDto = htSubscribersProvisioning.get(subscriber);

				String newChargingTime = DaemonUtils.getChargingTime(subDto.getChargingStartTime(),
						subDto.getDtStartTime());

				if (DaemonUtils.isChargingTimelesser(subDto.getChargingStartTime(), newChargingTime)) {
					DebugManager.status(_module, _class,
							"getAlreadyChargedSubscribers", "sub:" + subscriber + " DATES:"
									+ subDto.getChargingStartTime() + " New Time:" + newChargingTime,
							Thread.currentThread().getName(), null);
					// Connection con1 = getUmpHitDbConnection();
					subscribersForDateUpdationMap.put(subscriber, newChargingTime);
				} else {
					DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
							"sub:" + subscriber + " charging time is not less!", Thread.currentThread().getName(),
							null);
				}

				DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
						"sub:" + subscriber + " htSubscribersProvisioning contains:"
								+ htSubscribersProvisioning.containsKey(subscriber) + "Already Charged:"
								+ subscribers.size(),
						Thread.currentThread().getName(), null);
			}

			List<String> updationResultList = ConfigDTO.getDaoDTO().getSubscriptionDao().updateSubscriberTimeBatch(con,
					subscribersForDateUpdationMap);

			DebugManager.status(_module, _class, "getAlreadyChargedSubscribers", "Before the broken while!",
					Thread.currentThread().getName(), null);
			Jedis jedis = JedisFactory.getFactory().getConnection();
			DebugManager.status(_module, _class, "getAlreadyChargedSubscribers", "Jedis.Ping:" + jedis.ping(), null,
					null);
			for (int i = 0; i < updationResultList.size(); i++) {
				String values[] = updationResultList.get(i).split(":");
				String subscriber = values[0];
				String result = values[1];
				if (!result.equals("true")) {
					DebugManager.fatalError(_module, _class, "getAlreadyChargedSubscribers",
							"Subscriber updation of chargingtime n counter failed for " + subscriber,
							Thread.currentThread().getName(), null);
					continue;
				} else {
					DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
							"sub:" + subscriber + " jedis exist start!", Thread.currentThread().getName(), null);
					if (jedis.exists(subscriber)) {
						jedis.del(subscriber);
					}
					DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
							"sub:" + subscriber + " jedis exist end!", Thread.currentThread().getName(), null);
					SubscriberDto subDto = htSubscribersProvisioning.get(subscriber);
					subDto.setNoofDaysCharged(0);
				}
			}
			DebugManager.status(_module, _class, "getAlreadyChargedSubscribers", "After the broken while!",
					Thread.currentThread().getName(), null);
			sub = subscribers.iterator();
			while (sub.hasNext()) {
				String subscriber = (String) sub.next();
				SubscriberDto subDto = htSubscribersProvisioning.get(subscriber);
				if (subDto.getNoofDaysCharged() < ConfigDTO.getChargingDTO().getNoOfDaysToCharge()) {
					DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
							"sub:" + subscriber + " jedis exist check start!", Thread.currentThread().getName(), null);
					if (jedis.exists(subscriber)) {
						String[] redisValue = jedis.get(subscriber).split("\\|");
						if (!redisValue[0].equalsIgnoreCase(REDIS_CHARGING_SUCCESS)) {
							if (redisValue[0].equalsIgnoreCase(REDIS_CHARGING_AWAITING)) {
								sub.remove();
								subscriberToBeUpdated.add(subscriber);
							} else if (redisValue[0].equalsIgnoreCase(REDIS_CHARGING_FAILURE)) {
								sub.remove();
								subscriberToBeUpdated.add(subscriber);
								subscriberToBeCharged.add(subscriber);
								if (!htSubscribersProvisioningGlobal.containsKey(subscriber)) {
									htSubscribersProvisioningGlobal.put(subscriber,
											htSubscribersProvisioning.get(subscriber));
								}
							}
						}
					} else {
						DebugManager.status(_module, _class, "getSubscribersNotChargedForFixedDays",
								"sub:" + subscriber + " Came to else,sub not in redis",
								Thread.currentThread().getName(), null);

						sub.remove();
						subscriberToBeUpdated.add(subscriber);
						subscriberToBeCharged.add(subscriber);
						if (!htSubscribersProvisioningGlobal.containsKey(subscriber)) {
							htSubscribersProvisioningGlobal.put(subscriber, htSubscribersProvisioning.get(subscriber));
						}
					}
					DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
							"sub:" + subscriber + " jedis exist check end!", Thread.currentThread().getName(), null);
				} else {
					DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
							"sub:" + subscriber + " jedis exist check - else part!", Thread.currentThread().getName(),
							null);
				}
			}
			JedisFactory.getFactory().returnConnection(jedis);

			DebugManager.status(_module, _class, "getAlreadyChargedSubscribers",
					"size of vecStrSubscriber" + subscriberToBeCharged.size() + "Already Charged:" + subscribers.size(),
					null, null);
			if (subscriberToBeUpdated.size() > 0) {
				ConfigDTO.getDaoDTO().getMissedCallDao().changeMissedCallsProcessedState(con, subscriberToBeUpdated);
			}
			if (subscriberToBeCharged.size() > 0) {
				saveInRedis(subscriberToBeCharged);
				// ConfigDTO.getDaoDTO().getMissedCallDao().changeMissedCallsProcessedState(con,
				// subscriberToBeCharged);
				DebugManager.status(_module, _class, "getAlreadyChargedSubscribers", "Going to call chargingphit", null,
						null);
				hitUMP(subscriberToBeCharged);
			}

			// VMNotificationDaemon.getConFactory().releaseConnection(con);

		} catch (Exception e) {
			DebugManager.exception(_class, _module, "getAlreadyChargedSubscribers", e, Thread.currentThread().getName(),
					null);
		}
		return subscribers;
	}

	/**
	 * Adds subcribers to redis to AWAITING state if it doesn't exist in redis
	 * with expire time(00:00)
	 * 
	 * @param vecStrSubToBeCharged
	 * @return
	 */
	public void saveInRedis(Vector<String> subscriberToBeCharged) {
		Calendar calobj = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		Jedis jedis = JedisFactory.getFactory().getConnection();
		Iterator<String> sub = subscriberToBeCharged.iterator();

		while (sub.hasNext()) {
			String msisdn = sub.next().toString();
			if (!jedis.exists(msisdn) || jedis.get(msisdn).split("\\|")[0].equalsIgnoreCase(REDIS_CHARGING_FAILURE)) {
				jedis.set(msisdn, REDIS_CHARGING_AWAITING + "|null");
				String b[] = format.format(calobj.getTime()).split(":");
				Long expiryTime = (86400
						- ((Long.parseLong(b[0]) * 60 * 60 + Long.parseLong(b[1]) * 60 + Long.parseLong(b[2]))));
				jedis.expireAt(msisdn, (System.currentTimeMillis() / 1000L) + expiryTime);
			}
		}
		JedisFactory.getFactory().returnConnection(jedis);
	}

	/**
	 * Starts Charging Process
	 * 
	 * @param vecStrSubToBeCharged
	 */
	public void hitUMP(Vector<String> subscriberToBeCharged) {
		DebugManager.status(_module, _class, "hitUMP", "vecStrSubToBeCharged size" + subscriberToBeCharged.size(),
				Thread.currentThread().getName(), null);

		if (subscriberToBeCharged == null || subscriberToBeCharged.size() <= 0)
			return;

		Iterator<String> sub = subscriberToBeCharged.iterator();

		getChargingHit().startProcess();

		while (sub.hasNext()) {
			getChargingHit().AddToWaitingQueue(sub.next().toString());
		}

	}

	public Hashtable<String, SubscriberDto> getSubscriberProvisioning(Connection con,
			Vector<String> vecStrSubscribers) {
		Vector<Subscriber> vecSubscriber = ConfigDTO.getDaoDTO().getSubscriptionDao().getSubscribers(con,
				vecStrSubscribers);
		Hashtable<String, SubscriberDto> htSubscribersProvisioning = new Hashtable<String, SubscriberDto>();
		try {
			for (int i = 0; i < vecSubscriber.size(); i++) {
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

				if (strBlcOrWht != null) {
					if (strBlcOrWht.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_BLACKLIST)
							|| strBlcOrWht.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_WHITELIST)) {
						htBlackAndWhite = ConfigDTO.getDaoDTO().getSubscriptionDao().getBlackWhiteList(con,
								strSubscriber);
					}
				}

				if (strBlcOrWhtForLca != null) {
					if (strBlcOrWhtForLca.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_BLACKLIST_FOR_LCA)
							|| strBlcOrWhtForLca.equalsIgnoreCase(VM_PRODUCT_PROVISIONED_TO_WHITELIST_FOR_LCA)) {
						htBlackAndWhiteForLca = ConfigDTO.getDaoDTO().getSubscriptionDao().getBlackWhiteListForLca(con,
								strSubscriber);
					}
				}
				SubscriberDto subDto = new SubscriberDto(strSubscriber, mobileNumber, productID, dtStartTime,
						noOfSmsForToday, htBlackAndWhite, htBlackAndWhiteForLca, subscriber.getLanguage(),
						subscriber.getVernSupp(), noOfDaysCharged, chargingStartTime);
				htSubscribersProvisioningGlobal.put(strSubscriber, subDto);
				htSubscribersProvisioning.put(strSubscriber, subDto);

			}
		} catch (Exception e) {
			DebugManager.exception(_module, _class, "getSubscriberProvisioning", e, Thread.currentThread().getName(),
					null);
		}
		return htSubscribersProvisioning;

	}

	public ConcurrentHashMap<String, String> getUniqueSubsWhoGotMC(List<MissedCall> listMC) {
		ConcurrentHashMap<String, String> conHm = new ConcurrentHashMap<String, String>();
		if (listMC != null) {
			Iterator<MissedCall> itr = listMC.iterator();
			// int count = 0;
			while (itr.hasNext()) {
				// count++;
				MissedCall mc = itr.next();
				String strSubscriber = mc.getSubscriber();
				// DebugManager.status(_module, _class, "getUniqueSubsWhoGotMC",
				// "#"+count +". strSubscriber="+strSubscriber,
				// Thread.currentThread().getName(), null);
				conHm.put(strSubscriber, strSubscriber);
			}
		}
		DebugManager.status(_module, _class, "getUniqueSubsWhoGotMC", "size of conHm = " + conHm.size(),
				Thread.currentThread().getName(), null);
		return conHm;
	}

	/**
	 * Add subscriber to Waiting shared queue
	 * 
	 * @param subscriber
	 */
	public void AddToWaitingQueue(String subscriber) {

		if (subscriber == null || subscriber.trim().length() < 0) {
			return;
		}

		try {
			synchronized (sharedQueue) {
				DebugManager.status(_module, _class, "AddToWaitingQueue", " SUB: " + subscriber,
						Thread.currentThread().getName(), null);
				sharedQueue.add(subscriber);
				sharedQueue.notifyAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add subscriber for Charging status verification
	 * 
	 * @param subscriber
	 */
	public void AddToProcessedQueue(String subscriber) {

		if (subscriber == null || subscriber.trim().length() < 0) {
			return;
		}

		try {
			synchronized (ProcessedQueue) {
				DebugManager.status(_module, _class, "AddToProcessedQueue", " SUB: " + subscriber,
						Thread.currentThread().getName(), null);
				ProcessedQueue.add(subscriber);
				ProcessedQueue.notifyAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns UMP Object
	 * 
	 * @return
	 */
	public static ChargingHit getChargingHit() {

		if (chargingHit == null) {
			synchronized (ChargingHit.class) {
				if (chargingHit == null) {
					chargingHit = new ChargingHit();
					updateStatus();
				}
			}
		}
		return chargingHit;
	}

	/**
	 * Initiates threads to start processing
	 * 
	 * @return
	 */
	public void startProcess() {
		if (!isMultiThreadedRequired) {
			if (pool.isEmpty()) {
				synchronized (pool) {
					if (pool.isEmpty()) {
						Thread executor = new Thread(getChargingHit().new Executor(), Thread.currentThread().getName());
						pool.put(Thread.currentThread(), executor);
						executor.start();
					}
				}
			}
		} else {
			if (!pool.containsKey(Thread.currentThread())) {
				Thread executor = new Thread(getChargingHit().new Executor(), Thread.currentThread().getName());
				pool.put(Thread.currentThread(), executor);
				executor.start();
			}
		}
	}

	/**
	 * Update Status
	 */
	private static void updateStatus() {
		Thread executor = new Thread(getChargingHit().new sendBack(30 * 1000L), "STATUS_UPDATE");
		executor.start();
		DebugManager.status(_module, _class, "updateStatus", " Start processing " + ProcessedQueue.size(),
				Thread.currentThread().getName(), null);
	}

	/**
	 * Executes Process
	 * 
	 * @author
	 */
	class Executor implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					chargeSubscriber();
					Thread.sleep(WAIT_AFTER_EACH_HIT);
				} catch (Exception e) {
					/*
					 * DebugManager.status(_module, _class,
					 * "chargeSubscriber--Run", "Error " + e +
					 * Thread.currentThread().getName() + " is waiting , size: "
					 * + sharedQueue.size(), null, null);
					 */
				}
			}
		}

		/**
		 * Charge subscriber
		 */
		private void chargeSubscriber() {

			while (sharedQueue.size() < 0) {
				synchronized (sharedQueue) {
					DebugManager.status(_module, _class, "chargeSubscriber", "Queue is empty "
							+ Thread.currentThread().getName() + " is waiting , size: " + sharedQueue.size(), null,
							null);
					try {
						sharedQueue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			synchronized (sharedQueue) {
				sharedQueue.notifyAll();
				int statusCode = 0;
				String response = "";
				String subscriber = sharedQueue.poll();

				try {
					if (subscriber != null) {
						DebugManager.status(_module, _class, "chargeSubscriber",
								" processing charging for " + subscriber + "Type:"
										+ ConfigDTO.getChargingDTO().isSyncHit(),
								Thread.currentThread().getName(), null);

						if (ConfigDTO.getChargingDTO().isSyncHit()) {
							String getUrl = makeGetUrl(subscriber);
							HttpMethod httpGetRequest = new GetMethod(getUrl);
							HttpClient httpclient = new HttpClient();

							// httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(HTTP_CONNECT_TIMEOUT);
							httpclient.getHttpConnectionManager().getParams().setSoTimeout(HTTP_RESPONSE_TIMEOUT);

							if (PROXY_HOST != null && PROXY_HOST.length() > 0 && !"null".equalsIgnoreCase(PROXY_HOST)
									&& !"NONE".equalsIgnoreCase(PROXY_HOST)) {

								HostConfiguration hc = new HostConfiguration();
								hc.setProxy(PROXY_HOST, PROXY_PORT);

								statusCode = httpclient.executeMethod(hc, httpGetRequest);
								response = new String(httpGetRequest.getResponseBody());
							} else {
								DebugManager.status(_module, _class, "chargeSubscriber", "Hit the prism url:" + getUrl,
										Thread.currentThread().getName(), null);

								statusCode = httpclient.executeMethod(httpGetRequest);
								response = new String(httpGetRequest.getResponseBody());
							}
							DebugManager.status(_module, _class, "chargeSubscriber",
									"Status Code:" + statusCode + " Response:" + response + " subscriber:" + subscriber,
									Thread.currentThread().getName(), null);

							handlePrismResponse(statusCode, response, subscriber);

						} else {
							HttpClient httpclient = new HttpClient();
							HttpMethod httpPostRequest = new PostMethod(POST_URL);
							// httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(HTTP_CONNECT_TIMEOUT);
							httpclient.getHttpConnectionManager().getParams().setSoTimeout(HTTP_RESPONSE_TIMEOUT);

							((PostMethod) httpPostRequest).addParameters(appendData(subscriber));

							if (PROXY_HOST != null && PROXY_HOST.length() > 0 && !"null".equalsIgnoreCase(PROXY_HOST)
									&& !"NONE".equalsIgnoreCase(PROXY_HOST)) {

								HostConfiguration hc = new HostConfiguration();
								hc.setProxy(PROXY_HOST, PROXY_PORT);
								statusCode = httpclient.executeMethod(hc, httpPostRequest);
								response = new String(httpPostRequest.getResponseBody());
							} else {
								DebugManager.status(_module, _class, "chargeSubscriber", "Hit the ump url:" + POST_URL,
										Thread.currentThread().getName(), null);
								statusCode = httpclient.executeMethod(httpPostRequest);
								response = new String(httpPostRequest.getResponseBody());
							}

							DebugManager.status(
									_module, _class, "chargeSubscriber", "Status Code:" + statusCode + " ,Response:"
											+ response + " ,subscriber:" + subscriber,
									Thread.currentThread().getName(), null);
							handleUmpResponse(statusCode, response, subscriber);
						}
					}
				} catch (ConnectTimeoutException e) {
					DebugManager.fatalError(_module, _class, "chargeSubscriber",
							"Connection Time out for subscriber:" + subscriber, Thread.currentThread().getName(), null);
					isHitSuccess = false;
					raiseAndClearAlarm(subscriber, statusCode);
				} catch (SocketTimeoutException e) {
					DebugManager.fatalError(_module, _class, "chargeSubscriber",
							"Socket Time out for subscriber:" + subscriber, Thread.currentThread().getName(), null);
					isHitSuccess = false;
					raiseAndClearAlarm(subscriber, statusCode);
				} catch (Exception e) {
					isHitSuccess = false;
					raiseAndClearAlarm(subscriber, statusCode);
					DebugManager.fatalError(_module, _class, "chargeSubscriber",
							"Charging failed for subscriber:" + subscriber + " status code-->" + e,
							Thread.currentThread().getName(), null);
					e.printStackTrace();
				} finally {
					// httpclient.close();
				}
			}
		}

		private void handlePrismResponse(int statusCode, String response, String subscriber) {
			DebugManager.status(_module, _class, "handlePrismResponse", response + " for msisdn:" + subscriber,
					Thread.currentThread().getName(), null);
			if (statusCode == HTTP_SUCCESS) {
				isHitSuccess = true;
				Jedis jedis = JedisFactory.getFactory().getConnection();
				jedis.eval(
						"local ttl = redis.call('ttl', ARGV[1]) if ttl > 0 then return redis.call('SETEX', ARGV[1], ttl, ARGV[2]) end",
						0, subscriber, REDIS_CHARGING_SUCCESS + "|" + response.split("\\|")[1]);
				// umpResponse.put(subscriber, response);
				JedisFactory.getFactory().returnConnection(jedis);
				Connection con = getChargingHitDbConnection();
				ConfigDTO.getDaoDTO().getMissedCallDao().updateSubcriberTable(con, subscriber);
				VMNotificationDaemon.getConFactory().releaseConnection(con);
				AddToProcessedQueue(subscriber);
				raiseAndClearAlarm(subscriber, statusCode);

			} else {
				isHitSuccess = false;
				htSubscribersProvisioningGlobal.remove(subscriber);
				DebugManager.fatalError(_module, _class, "handlePrismResponse", "Going to raise alarm",
						Thread.currentThread().getName(), null);
				raiseAndClearAlarm(subscriber, statusCode);

				if (statusCode == HTTP_LOW_BALANCE) {
					DebugManager.fatalError(_module, _class, "handlePrismResponse",
							"Prism Response status :" + statusCode + ", Response :" + response,
							Thread.currentThread().getName(), null);
					Jedis jedis = JedisFactory.getFactory().getConnection();
					String redisUpdation = (String) jedis.eval(
							"local ttl = redis.call('ttl', ARGV[1]) if ttl > 0 then return redis.call('SETEX', ARGV[1], ttl, ARGV[2]) end",
							0, subscriber, REDIS_CHARGING_FAILURE + "|" + response.split("\\|")[1]);
					JedisFactory.getFactory().returnConnection(jedis);
					DebugManager.status(_module, _class, "handlePrismResponse",
							"Redis updated to failure:" + redisUpdation, Thread.currentThread().getName(), null);
					AddToProcessedQueue(subscriber);
				} else {
					DebugManager.fatalError(_module, _class, "handlePrismResponse",
							"Prism Response status:" + statusCode + ", Response :" + response,
							Thread.currentThread().getName(), null);
				}
			}

		}

		/**
		 * handle UMP Response
		 * 
		 * @param subscriber
		 * @param responseStr
		 * @param statuscode
		 * 
		 * @return
		 */
		private void handleUmpResponse(int statusCode, String response, String subscriber) {
			DebugManager.status(_module, _class, "handleUmpResponse:", response + " for msisdn:" + subscriber,
					Thread.currentThread().getName(), null);
			
			if (statusCode == HTTP_SUCCESS) {
				isHitSuccess = true;
				Jedis jedis = JedisFactory.getFactory().getConnection();
				jedis.eval(
						"local ttl = redis.call('ttl', ARGV[1]) if ttl > 0 then return redis.call('SETEX', ARGV[1], ttl, ARGV[2]) end",
						0, subscriber, REDIS_CHARGING_AWAITING + "|" + response);
				// umpResponse.put(subscriber, response);
				JedisFactory.getFactory().returnConnection(jedis);
				AddToProcessedQueue(subscriber);
				raiseAndClearAlarm(subscriber, statusCode);

			} else {
				isHitSuccess = false;
				htSubscribersProvisioningGlobal.remove(subscriber);
				raiseAndClearAlarm(subscriber, statusCode);
				DebugManager.fatalError(_module, _class, "handleUmpResponse", "Going to raise alarm",
						Thread.currentThread().getName(), null);

				if (statusCode == HTTP_BAD_REQUEST) {
					DebugManager.fatalError(_module, _class, "handleUmpResponse",
							"UMP Response status:" + statusCode
									+ " Due to missing parameters(Mandatory parameters are null)",
							Thread.currentThread().getName(), null);
				} else if (statusCode == HTTP_AUTHENTICATION_ERROR) {
					DebugManager.fatalError(_module, _class, "handleUmpResponse",
							"UMP Response status:" + statusCode
									+ " Due to authentication failure(Incorrect/missing username or password)",
							Thread.currentThread().getName(), null);
				} else if (statusCode == HTTP_URL_CREDENTIALS_ERROR) {
					DebugManager.fatalError(_module, _class, "handleUmpResponse",
							"UMP Response status:" + statusCode
									+ " Due to authentication failure(Incorrect/missing ump ip/port)",
							Thread.currentThread().getName(), null);
				} else {
					DebugManager.fatalError(_module, _class, "handleUmpResponse", "UMP Response status:" + statusCode,
							Thread.currentThread().getName(), null);
				}
			}
		}

		public void raiseAndClearAlarm(String subscriber, int statusCode) {

			DebugManager.status(_module, _class, "raiseAndClearAlarm", "isHitSuccess=" + isHitSuccess, null, null);

			if (isHitSuccess) {

				if (ConfigDTO.getMonitoringDTO().isActive()) {
					// int alarmId =
					// AlarmUtility.mapAlarm.get(MonitoringConstants.ALARM_MISSEDCALLS_UNSUCCESSFULL_UMP_HIT);
					if (isAlarmRaised) {
						isAlarmRaised = false;
						DebugManager.status(_module, _class, "raiseAndClearAlarm",
								"Clearing the unsuccessful charging hit alarm", null, null);
						AlarmDTO alarmDTO = new AlarmDTO(
								MonitoringConstants.ALARM_MISSEDCALLS_UNSUCCESSFULL_CHARGING_HIT,
								MonitoringConstants.ALARM_CLEAR_UNSUCCESSFULL_CHARGING_HIT, false);
						try {
							AlarmUtility.queue.add(alarmDTO);
							DebugManager.status(_module, _class, "raiseAndClearAlarm", "Added to trap queue", null,
									null);
						} catch (IllegalStateException e) {
							DebugManager.exception(_module, _class, "raiseAndClearAlarm", e,
									Thread.currentThread().getName(), null);
						}
					}
				}
			} else {
				DebugManager.status(_module, _class, "raiseAndClearAlarm", "Charging hit failed ...", null, null);
				if (statusCode != HTTP_LOW_BALANCE) {
					Jedis jedis = JedisFactory.getFactory().getConnection();
					if (jedis.exists(subscriber)) {
						jedis.del(subscriber);
					}
					JedisFactory.getFactory().returnConnection(jedis);
					Connection con = getChargingHitDbConnection();
					ConfigDTO.getDaoDTO().getMissedCallDao().dumpFailedMissedCalls(con, subscriber);
					VMNotificationDaemon.getConFactory().releaseConnection(con);
				}
				if (ConfigDTO.getMonitoringDTO().isActive()) {
					if (ConfigDTO.getMonitoringDTO().isActive() && OzoneStarter.OZONE_DAEMON) {
						DebugManager.status(_module, _class, "raiseAndClearAlarm", "isAlarmRaised" + isAlarmRaised,
								null, null);
						if (!isAlarmRaised) {
							try {

								AlarmDTO alarmDTO = new AlarmDTO(
										MonitoringConstants.ALARM_MISSEDCALLS_UNSUCCESSFULL_CHARGING_HIT,
										MonitoringConstants.ALARM_RAISE_UNSUCCESSFULL_CHARGING_HIT, true);
								DebugManager.fatalError(_module, _class, "raiseAndClearAlarm",
										"Trap added for "
												+ MonitoringConstants.ALARM_MISSEDCALLS_UNSUCCESSFULL_CHARGING_HIT,
										null, null);

								AlarmUtility.queue.add(alarmDTO);

								isAlarmRaised = true;
							} catch (IllegalStateException e) {
								DebugManager.fatalError(_module, _class, "raiseAndClearAlarm", "ERROR" + e, null, null);
							}
						} else {
							// alarm already raised
							DebugManager.fatalError(_module, _class, "raiseAndClearAlarm",
									"alarm for " + MonitoringConstants.ALARM_MISSEDCALLS_UNSUCCESSFULL_CHARGING_HIT
											+ " already raised",
									Thread.currentThread().getName(), null);
						}

					}
				}
			}
		}

		/**
		 * Appends data to post method before hitting the URL
		 * 
		 * @param subscriber
		 * @return
		 */
		public NameValuePair[] appendData(String subscriber) {
			NameValuePair[] params = null;
			try {
				DebugManager.status(_module, _class, "appendData", "Going to append data",
						Thread.currentThread().getName(), null);

				String dsrUrl = "http://" + ConfigDTO.getChargingDTO().getDsrHost() + ":"
						+ ConfigDTO.getChargingDTO().getDsrPort()
						+ "/mcaUmp/UmpCallBackHandler?msisdn=<msisdn>&unique_id=<umpid>&status=<message>";
				DebugManager.trace(_module, _class, "appendData", " DSR url:" + dsrUrl,
						Thread.currentThread().getName(), null);
				params = new NameValuePair[] { new NameValuePair("username", ConfigDTO.getChargingDTO().getUserName()),
						new NameValuePair("password", ConfigDTO.getChargingDTO().getUserPwd()),
						new NameValuePair("message", ConfigDTO.getSmsTextDTO().getMissedCallChargingMessage()),
						new NameValuePair("sender", ConfigDTO.getChargingDTO().getSender()),
						new NameValuePair("recipient", subscriber),
						new NameValuePair("event_Billing_Id", ConfigDTO.getChargingDTO().getEvent_billing_id()),
						new NameValuePair("unique_id", DaemonUtils.getUniqueId(subscriber)),
						new NameValuePair("request_dsr", "true"),
						// new NameValuePair("dsrUrl", URLEncoder.encode(dsrUrl,
						// "UTF-8"))
						new NameValuePair("dsrUrl", dsrUrl) };

				DebugManager.status(_module, _class, "appendData", "Finished appending data",
						Thread.currentThread().getName(), null);

			} catch (Exception e) {
				DebugManager.fatalError(_module, _class, "appendData", "Error in appending data to url" + e,
						Thread.currentThread().getName(), null);

			}
			return params;
		}
	}

	/**
	 * Process charged Subscribers
	 * 
	 */
	class sendBack implements Runnable {

		Queue<String> processQueuePending = new LinkedList<String>();
		private final long processCheckInterval;
		ConcurrentLinkedQueue<String> queue;

		sendBack(long interval) {
			queue = ProcessedQueue;
			processCheckInterval = interval;
		}

		@SuppressWarnings("static-access")
		@Override
		public void run() {
			while (true) {
				try {

					DebugManager.status(_module, _class, "sendBack ", "thread going to sleep ", null, null);
					Thread.currentThread().sleep(processCheckInterval);
					DebugManager.status(_module, _class, "sendBack ",
							"thread going to start process after sleep:" + System.currentTimeMillis(), null, null);

					processChargedSubscriber();
					ProcessedQueue.addAll(processQueuePending);
					processQueuePending.clear();

				} catch (InterruptedException e) {
					DebugManager.status(_module, _class, "sendBack ", "Errror" + e + ProcessedQueue.size(), null, null);
				}

			}
		}

		private void processChargedSubscriber() {
			Jedis jedis = JedisFactory.getFactory().getConnection();

			Vector<String> chargedSubscriber = new Vector<String>();
			while (queue.size() > 0) {

				DebugManager.status(_module, _class, "sendBack.processChargedSubscriber ",
						"Going to check redis success status", null, null);

				String sub = queue.poll();

				DebugManager.status(_module, _class, " sendBack.processChargedSubscriber ", "Subscriber:" + sub, null,
						null);
				if (jedis.exists(sub)) {
					String status = jedis.get(sub).split("\\|")[0];
					DebugManager.status(_module, _class, " sendBack.processChargedSubscriber ",
							"Jedis.Status:" + status.equals(REDIS_CHARGING_SUCCESS), null, null);

					try {
						if (status.equals(REDIS_CHARGING_SUCCESS)) {
							chargedSubscriber.add(sub);
						}
						DebugManager.status(_module, _class, " sendBack.processChargedSubscriber ",
								"Jedis.Status:" + status, null, null);
						if (status.equals(REDIS_CHARGING_FAILURE)) {
							DebugManager.status(_module, _class, " sendBack.processChargedSubscriber ", "Going to del",
									null, null);
							Connection con = getChargingHitDbConnection();
							ConfigDTO.getDaoDTO().getMissedCallDao().deleteFailedMissedCalls(con, sub);
							// VMNotificationDaemon.getConFactory().releaseConnection(con);
						} else if (status.equals(REDIS_CHARGING_AWAITING)) {
							if (!processQueuePending.contains(sub)) {
								processQueuePending.add(sub);
							}
						}
					} catch (Exception e) {
						DebugManager.status(_module, _class, " sendBack.processChargedSubscriber ", "Error:" + e, null,
								null);
					}
				}
				if (chargedSubscriber.size() > 0) {
					DebugManager.status(_module, _class, "sendBack.processChargedSubscriber",
							"chargedSubscriber:" + chargedSubscriber.size(), null, null);
					mcThreadProcessor.processSubscribers(chargedSubscriber, m_arrPromotionalMessages,
							PROVISIONED_TO_CHARGING, htSubscribersProvisioningGlobal);
					Iterator it = chargedSubscriber.iterator();
					DebugManager.status(_module, _class, "sendBack.processChargedSubscriber",
							"htSubscribersProvisioningGlobal:" + htSubscribersProvisioningGlobal.size(), null, null);
					if (it.hasNext()) {
						String sub1 = it.next().toString();
						htSubscribersProvisioningGlobal.remove(sub1);
					}
					chargedSubscriber.clear();
				}
			}
			JedisFactory.getFactory().returnConnection(jedis);
		}
	}

	public String makeGetUrl(String subscriber) {
		String url = GET_URL;
		try {
			url = url.replace("<user>", ConfigDTO.getChargingDTO().getUserName());
			url = url.replace("<pass>", ConfigDTO.getChargingDTO().getUserPwd());
			url = url.replace("<msisdn>", subscriber);
			url = url.replace("<srvkey>", ConfigDTO.getChargingDTO().getServiceKey());
			url = url.replace("<eventkey>", ConfigDTO.getChargingDTO().getEvent_Key());
			url = url.replace("<mode>", ConfigDTO.getChargingDTO().getMode());
			DebugManager.status(_module, _class, "makeGetUrl", "Get url formed:" + url, null, null);
		} catch (Exception e) {
			DebugManager.fatalError(_module, _class, "makeGetUrl",
					"Error in creating url,parameters may be missing(Params required:userName,userPwd,serviceKey,eventkey,mode). "
							+ e,
					null, null);
		}
		return url;
	}
}
