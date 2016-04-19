package com.ipbase.widget;

import org.kymjs.kjframe.ui.ViewInject;

import com.ipbase.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyDialog extends Dialog
{
	final static public int INPUT = 0;
	final static public int OKCancel = 1;
	final static public int SELFINPUT = 2;

	private String mInfo;
	private Button mOKButton, mCancelButton;
	private TextView mTips;
	private boolean mFlag;
	private int mType;
	private EditText mEdittext;
	private onClickOKButtonListener mListener;

	public interface onClickOKButtonListener
	{
		public void back( String returnInfo );
	}

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		init();
	}

	public MyDialog( Context cxt, String titleString, String info,
			Boolean flag, onClickOKButtonListener listener )
	{
		super( cxt );
		setTitle( titleString );
		mInfo = info;
		mFlag = flag;
		mListener = listener;
		mType = INPUT;
	}

	public MyDialog( Context cxt, String titleString, String info,
			Boolean flag, onClickOKButtonListener listener, int type )
	{
		super( cxt );
		setTitle( titleString );
		mInfo = info;
		mFlag = flag;
		mListener = listener;
		mType = type;
	}

	private void init()
	{
		switch ( mType )
		{
			case INPUT :
				makeFirstStyle();
				break;

			case OKCancel :
				makeSecondStyle();
				break;
				
			case SELFINPUT:
				makeThirdStyle();
				break;

			default :
				break;
		}

	}

	private void makeFirstStyle()
	{
		setContentView( R.layout.edittext_dialog );
		mTips = (TextView) findViewById( R.id.tips_edit );
		if ( mFlag )
		{
			mTips.setText( "请输入昵称" );
		}
		else
		{
			mTips.setText( "请输入邮箱" );
		}
		mEdittext = (EditText) findViewById( R.id.nickName_edit );
		mEdittext.setText( mInfo );
		mEdittext.setSelectAllOnFocus( true );
		mOKButton = (Button) findViewById( R.id.nickName_button );
		mOKButton.setOnClickListener( new View.OnClickListener()
		{
			@ Override
			public void onClick( View v )
			{
				String returnInfo = mEdittext.getText().toString();
				if ( returnInfo == null || returnInfo.trim().equals( "" ) )
				{
					if ( mFlag )
					{
						ViewInject.toast( "请填写昵称再提交" );
					}
					else
					{
						ViewInject.toast( "请填写邮箱再提交" );
					}
					return;
				}
				if ( !mFlag )
				{
					if ( !returnInfo.contains( "@" )
							|| !returnInfo.contains( ".com" ) )
					{
						ViewInject.toast( "邮箱格式不正确" );
						return;
					}
				}

				mListener.back( returnInfo );
				MyDialog.this.dismiss();
			}
		} );
	}

	private void makeSecondStyle()
	{
		setContentView( R.layout.okcanceldialog );
		mTips = (TextView) findViewById( R.id.tips_edit2 );
		mTips.setText( mInfo );
		mOKButton = (Button) findViewById( R.id.OK_button );
		mOKButton.setText( "是" );
		mOKButton.setOnClickListener( new View.OnClickListener()
		{
			@ Override
			public void onClick( View view )
			{
				mListener.back( "OK" );
				MyDialog.this.dismiss();
			}
		} );
		mCancelButton = (Button) findViewById( R.id.Cancel_button );
		mCancelButton.setText( "否" );
		mCancelButton.setOnClickListener( new View.OnClickListener()
		{
			@ Override
			public void onClick( View view )
			{
				mListener.back( "Cancel" );
				MyDialog.this.dismiss();
			}
		} );
	}
	
	private void makeThirdStyle()
	{
		setContentView( R.layout.edittext_dialog );
		mTips = (TextView) findViewById( R.id.tips_edit );
		mTips.setText( "对 Ta 说点儿什么" );
		
		mEdittext = (EditText) findViewById( R.id.nickName_edit );
		mEdittext.setSelectAllOnFocus( true );
		mOKButton = (Button) findViewById( R.id.nickName_button );
		mOKButton.setOnClickListener( new View.OnClickListener()
		{
			@ Override
			public void onClick( View v )
			{
				String returnInfo = mEdittext.getText().toString();
				if ( returnInfo == null || returnInfo.trim().equals( "" ) )
				{
					ViewInject.toast( "空空如也呢，亲~~" );
					return;
				}

				mListener.back( returnInfo );
				MyDialog.this.dismiss();
			}
		} );
	}
}