package com.phn.base.component;

import org.json.simple.JSONObject;

public interface PhnConstants {

	public static String TestUserName1 = "test1";
	public static String TestUserName2 = "test2";
	public static String TestPassword = "111222";

	public static String User_Tokens_Device_Mobile = "mobile";
	public static String User_Tokens_Device_Desktop = "desktop";

	public static String SocketIO_Namespace = "/phn_v1";
	public static String SocketIO_Data_Event = "data";

	public static int SocketIO_Data_Type_LoginByUsername = 0;
	public static int SocketIO_Data_Type_LoginByToken = 1;
	public static int SocketIO_Data_Type_Logout = 10;

	// code:Succes
	public static int Response_Success = 200;
	public static int Response_Fail = 201;
	
	public static int Response_NoNeedToUpdateVersion = 210;

	// code:Register
	public static int Response_DidUidRegister = 550;
	public static int Response_NoOpenIdRegister = 551;
	public static int Response_HasOpenIdRegister = 552;
	public static int Response_HasOpenIdRegisterWithoutInfo = 553;
	public static int Response_RegisterHasUsername = 554;
	public static int Response_RegisterWrongCdoe = 555;
	public static int Response_RegisterHasPhoneNumber = 556;
	public static int Response_NoPhoneNumberRegister = 557;

	// code:Login
	public static int Response_NotLogin = 560;
	public static int Response_LoginFailed = 561;
	public static int Response_LoginNotfoundUser = 562;
	public static int Response_LoginWrongPwd = 563;

	// code:SMS
	public static int Response_SendSmsFail = 570;
	public static int Response_HadSendSmsManyTimes = 571;
	public static int Response_InvaildPhoneNumber = 572;

	// Media
	public static int MediaType_Text = 0;
	public static int MediaType_Image = 1;
	public static int MediaType_Gif = 2;
	public static int MediaType_Audiio = 3;
	public static int MediaType_Video = 4;

	// User Source
	public static int SourceType_Puer = 1;

	// others
	public static int Response_NoPrivilege = 601;

	public static String getResponseMessage(int code, String... args) {
		if (code == Response_Success) {
			return "操作成功";
		} else if (code == Response_Fail) {
			return "操作失败";
		} else if (code == Response_NoNeedToUpdateVersion) {
			return "不需要更新版本";
		}
		// code:Register
		else if (code == Response_DidUidRegister) {
			return "已经注册";
		} else if (code == Response_NoOpenIdRegister) {
			return "需要微信注册";
		} else if (code == Response_HasOpenIdRegister) {
			return "已经微信注册";
		} else if (code == Response_HasOpenIdRegisterWithoutInfo) {
			return "微信注册未填写信息";
		} else if (code == Response_RegisterHasUsername) {
			return "用户名已经注册";
		} else if (code == Response_RegisterWrongCdoe) {
			return "验证码错误";
		} else if (code == Response_RegisterHasPhoneNumber) {
			return "手机号已经被注册";
		} else if (code == Response_NoPhoneNumberRegister) {
			return "手机号未被注册";
		}
		// code:Login
		else if (code == Response_NotLogin) {
			return "未登录";
		} else if (code == Response_LoginFailed) {
			return "登录失败";
		} else if (code == Response_LoginNotfoundUser) {
			return "用户名或密码错误";
		} else if (code == Response_LoginFailed) {
			return "用户名或密码错误";
		}
		// code:SMS
		else if (code == Response_SendSmsFail) {
			return "用户名已经注册";
		} else if (code == Response_HadSendSmsManyTimes) {
			return "发送短信次数过多";
		} else if (code == Response_InvaildPhoneNumber) {
			return "手机号无效";
		}
		// others
		else if (code == Response_NoPrivilege) {
			return "无权限";
		}
		return "";
	}

	public static JSONObject getResult(int code, String message, Object data) {
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("message", message);
		if (data != null)
			result.put("data", data);
		return result;
	}
}
