package com.ipbase.utils;

import com.ipbase.bean.User;
import com.ipbase.bean.UserArticle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

/**
 * @author 李先华 2015年10月5日
 */
public class CommonUtils
{
	public static boolean isValidEmail( String paramString )
	{
		String regex = "[a-zA-Z0-9_\\.]{1,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
		if ( paramString.matches( regex ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean isValidMobiNumber( String paramString )
	{
		String regex = "^1\\d{10}$";
		if ( paramString.matches( regex ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO( String mobileNums )
	{
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if ( TextUtils.isEmpty( mobileNums ) )
			return false;
		else
			return mobileNums.matches( telRegex );
	}

	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork( Context context )
	{
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService( Context.CONNECTIVITY_SERVICE );
		boolean wifi = con.getNetworkInfo( ConnectivityManager.TYPE_WIFI )
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo( ConnectivityManager.TYPE_MOBILE )
				.isConnectedOrConnecting();
		if ( wifi | internet )
		{
			// 执行相关操作
			return true;
		}
		else
		{
			return false;
		}
	}

	public static UserArticle UserToArticleUser( User user )
	{
		if ( user == null )
			return null;

		UserArticle uArticle = new UserArticle();
		uArticle.setPhone( user.getPhone() );
		uArticle.setPassword( user.getPassword() );
		uArticle.setNickname( user.getNickname() );
		uArticle.setEmail( user.getEmail() );
		uArticle.setAipay( user.getAipay() );
		uArticle.setWeixin( user.getWeixin() );

		return uArticle;
	}
}