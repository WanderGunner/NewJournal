package com.ipbase.utils;

import org.kymjs.kjframe.ui.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.ipbase.AppConfig;
import com.ipbase.R;

/**
 * 
 * ClassName: SMSUtils
 * 
 * @Description: 发送验证码短信的工具类
 * @author kesar
 * @date 2015年9月23日
 */
public class SMSUtils
{
	final static public int Msg_Thread_Runing = -1;
	final static public int Msg_Thread_Stop = -2;
	final static public int Msg_Thread_YanZheng = -3;

	private Button mBtnVerification;
	private SMSCallBack mSmsCallBack; // 短信回调

	public SMSUtils( Activity outActivity, Button button )
	{
		mBtnVerification = button;
		// 这个只需要初始化一次就可以使用
		SMSSDK.initSDK( outActivity, AppConfig.Mob_APPKEY,
				AppConfig.Mob_APPSECRET );
	}

	/**
	 * 
	 * @Description: 初始化监听器
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	public void initSMS( SMSCallBack callBack )
	{
		mSmsCallBack = callBack;
		// 事件监听器
		EventHandler eventHandler = new EventHandler()
		{
			public void afterEvent( int event, int result, Object data )
			{
				Message msg = new Message();
				msg.what = Msg_Thread_YanZheng;
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mHandler.sendMessage( msg );
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler( eventHandler );
	}

	@ SuppressLint( "HandlerLeak" )
	private Handler mHandler = new Handler()
	{
		@ Override
		public void handleMessage( Message msg )
		{
			super.handleMessage( msg );
			switch ( msg.what )
			{
				case Msg_Thread_Runing : // 线程运行
					int time = (Integer) msg.obj;
					mBtnVerification.setText( time + "s" );
					mBtnVerification.setEnabled( false );
					break;

				case Msg_Thread_Stop : // 线程停止
					mBtnVerification.setEnabled( true );
					mBtnVerification.setText( R.string.btn_verification );
					break;

				case Msg_Thread_YanZheng :
					int event = msg.arg1;
					int result = msg.arg2;
					Object data = msg.obj;

					if ( event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE )
					{
						// 点击注册按钮后验证验证码的正确性执行的动作
						afterSubmit( result, data );
					}
					else if ( event == SMSSDK.EVENT_GET_VERIFICATION_CODE )
					{
						// 点击获取验证码按钮后的执行动作
						afterGet( result, data );
					}
					else if ( event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES )
					{
						// 返回支持发送验证码的国家列表
					}
					// closeProgressDialog();
					break;

				default :
					break;
			}
		}
	};

	private void afterSubmit( int result, Object data )
	{
		if ( result == SMSSDK.RESULT_COMPLETE )
		{
			if ( null != mSmsCallBack )
			{
				mSmsCallBack.doAfterSubmit();
			}
			ViewInject.toast( "验证成功" );
		}
		else
		{
			if ( null != data )
			{
				( (Throwable) data ).printStackTrace();
			}
			ViewInject.toast( "验证码输入有误" );
		}
	}

	private void afterGet( int result, Object data )
	{
		if ( result == SMSSDK.RESULT_COMPLETE )
		{
			boolean smart = (Boolean) data;
			if ( smart )
			{
				if ( null != mSmsCallBack )
				{
					mSmsCallBack.doAfterGet( true );
				}
				ViewInject.toast( "通过智能验证" );
			}
			else
			{
				if ( null != mSmsCallBack )
				{
					mSmsCallBack.doAfterGet( false );
				}
				ViewInject.toast( "验证码已发送" );
			}
		}
		else
		{
			if ( null != data )
			{
				( (Throwable) data ).printStackTrace();
			}
			// 验证码发送错误
			ViewInject.toast( "验证码发送出错，请重试" );
		}
	}

	public Handler getHandler()
	{
		return mHandler;
	}

};