package socket.netty.msg;

public class MessageID {
	/**
	 * <b>心跳</b>
	 */
	public static final short hearBeatMsg = 0x00;
	/**
	 * <b>登录</b>
	 */
	public static final short loginMsg = 0x01;
	/**
	 * <b>驾驶员服务平台通用应答</b>
	 */
	public static final short driverAnswerMsg = 0x03;
	/**
	 * <b>预约订单抢单</b>
	 */
	public static final short preOrderOfferMsg = 0x12;
	/**
	 * <b>预约订单取消</b>
	 */
	public static final short preOrderOfferCancelMsg = 0x15;
	/**
	 * <b>查询当前在线驾驶员数量应答</b>
	 */
	public static final short driverOnLineNumberMsg = 0x19;
	/**
	 * <b>电召平台通用应答</b>
	 */
	public static final short callAnswerMsg = 0x81;
	/**
	 * <b>驾驶员上班、下班信息的通知</b>
	 */
	public static final short  workingMsg = 0x91;
	/**
	 * <b>驾驶员单笔营运结束通知</b>
	 */
	public static final short driverOrderOverMsg = 0x92;
	/**
	 * <b>驾驶员抢答成功的即时订单</b>
	 */
	public static final short driverGrabSuccessMsg = 0x93;
	/**
	 * <b>预约订单下发通知</b>
	 */
	public static final short orderIssuedNoticeMsg = 0x94 ;
	/**
	 * <b>驾驶员抢答成功的预约订单通知</b>
	 */
	public static final short driverPreOrderSuccessMsg = 0x95;
	/**
	 * <b>乘客位置通知</b>
	 */
	public static final short passengerPositionNoticeMsg = 0x96;
	/**
	 * <b>订单状态通知</b>
	 */
	public static final short orderStatusNoticeMsg = 0x97;
	/**
	 * <b>通知信息下发</b>
	 */
	public static final short informMsg = 0x98;
	/**
	 * <b>查询当前在线驾驶员数量</b>
	 */
	public static final short driverNumberMsg = 0x99;
	/**
	 * <b>乘客上车通知（新增）</b>
	 */
	public static final short  passengersOnNoticeMsg = 0x9A;
	/**
	 * <b>乘客下车通知（新增）</b>
	 */
	public static final short passengersOffNoticeMsg = 0x9B;
	/**
	 * <b>支付结果通知（新增）</b>
	 */
	public static final short paymentResultNoticeMsg  = 0x9C;
	/**
	 * <b>支付结果通知（新增）</b>
	 */
	public static final short carPositionMsg  = 0x9D;

	/**
	 * <b>翻牌消息（新增）</b>
	 */
	public static final int plateChangeMsg = 0x9E;
	/**
	 * <b>下发抢答结果信息</b>
	 */
	public static final short sendAnswerResultsMsg = (short) 0x8B01;
	/**
	 * <b>驾驶员抢答失败的预约订单通知（新增）</b>
	 */
	public static final short driverPreOrderFailureMsg = 0x60;
	/**
	 * <b>接单下发通知（新增）</b>
	 */
	public static final short orderIssuedNoticeNewMsg = 0x61;
	/**
	 * <b>下发通知（新增）</b>
	 */
	public static final short sendNotificationMsg = 0x9F;
	/**
	 * <b>查询当前在线驾驶员数量应答</b>
	 */
	public static byte uint8start = '@';
	public static byte uint8Type = 0x01;
	public static byte uint8version = 0x00;
	public static byte uint8business = 0x12;
	public static String persist = "0000000";
	/**
	 * <b>注销</b>
	 */
	public static final int cancellationMsg = 0x02;
	
	/**
	 * <b>登陸 u key</b>
	 */
	public static String loginMsgs = "";
	
}