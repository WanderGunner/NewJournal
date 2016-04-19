package com.ipbase.residemenu;

import java.io.File;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipbase.AppConfig;
import com.ipbase.R;
import com.ipbase.widget.CircleImage;

public class ResideMenuInfo extends LinearLayout
{
	/** menu item icon */
	private CircleImage iv_icon;
	/** menu item title */
	private TextView tv_username;

	public ResideMenuInfo( Context context )
	{
		super( context );
		initViews( context );
	}

	public ResideMenuInfo( Context context, int icon, String title )
	{
		super( context );
		initViews( context );
		iv_icon.setImageResource( icon );
		tv_username.setText( title );
	}

	public ResideMenuInfo( Context context, String phone, String title )
	{
		super( context );
		initViews( context );
		setIcon( phone );
		tv_username.setText( title );
	}

	private void initViews( Context context )
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		inflater.inflate( R.layout.residemenu_info, this );
		iv_icon = (CircleImage) findViewById( R.id.image_icon );
		tv_username = (TextView) findViewById( R.id.tv_userinfo );
	}

	/**
	 * set the icon color;
	 * 
	 * @param icon
	 */
	public void setIcon( int icon )
	{
		iv_icon.setImageResource( icon );
	}

	public void setIcon( String phone )
	{
		// 保存用户头像缓存的文件夹
		final String iconFolder = Environment.getExternalStorageDirectory()
				+ AppConfig.touXiangPath;
		// 当前用户的头像图片名称
		final String iconName = phone + "_icon.png";
		File file = new File( iconFolder + iconName );
		if ( file.exists() )
		{
			iv_icon.setImageBitmap( BitmapFactory.decodeFile( file
					.getAbsolutePath() ) );
		}
		else
		{
			iv_icon.setImageResource( R.drawable.icon_man_white );
		}
	}

	/**
	 * set the title with string;
	 * 
	 * @param title
	 */
	public void setTitle( String title )
	{
		tv_username.setText( title );
	}
}