package com.ipbase.fragment;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.StringUtils;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.bean.User;
import com.ipbase.utils.CommonUtils;

/**
 * 
 * ClassName: FindPasswordFragment
 * 
 * @Description: 找回密码
 * @author xinyi
 * @date 2015-10-3
 */
public class ChangePasswordFragment extends SimpleBackFragment implements
		TextWatcher
{
	private String WEB_PATH;

	@ BindView( id = R.id.change_pwd_et_original_password )
	private EditText mEtPassword;
	@ BindView( id = R.id.change_pwd_et_newpassword )
	private EditText mEtNewPassword;
	@ BindView( id = R.id.change_pwd_et_renewpassword )
	private EditText mEtReNewPassword;
	@ BindView( id = R.id.change_pwd_btn_commit_change, click = true )
	private Button mBtnCommit;

	private KJHttp mKjHttp;
	private String oldPassword;
	private String newPassword;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_change_password,
				null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		WEB_PATH = getString( R.string.url_base )
				+ getString( R.string.url_users );
		mKjHttp = new KJHttp();
		// 旧密码
		oldPassword = AppContext.getInstance().getUser().getPassword();
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		setTitle( "修改密码" );
		initTextChangedListener();
	}

	@ Override
	protected void widgetClick( View v )
	{
		super.widgetClick( v );
		switch ( v.getId() )
		{
			case R.id.change_pwd_btn_commit_change :
				doCommit();
				break;
		}
	}

	/**
	 * 
	 * @Description: 初始化EditText的监听器
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	public void initTextChangedListener()
	{
		mEtPassword.addTextChangedListener( this );
		mEtNewPassword.addTextChangedListener( this );
		mEtReNewPassword.addTextChangedListener( this );
	}

	/**
	 * 
	 * @Description: 提交操作
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void doCommit()
	{
		if ( !CommonUtils.hasNetwork( getActivity() ) )
		{
			ViewInject.toast( "请检查网络连接!" );
			return;
		}
		String password = mEtPassword.getText().toString().trim();
		newPassword = mEtNewPassword.getText().toString().trim();
		String reNewPassword = mEtReNewPassword.getText().toString().trim();
		if ( StringUtils.isEmpty( password ) )
		{
			ViewInject.toast( "原密码不能为空" );
			return;
		}
		if ( StringUtils.isEmpty( newPassword ) )
		{
			ViewInject.toast( "新密码不能为空" );
			return;
		}
		if ( StringUtils.isEmpty( reNewPassword ) )
		{
			ViewInject.toast( "确认新密码不能为空" );
			return;
		}
		if ( !newPassword.equals( reNewPassword ) )
		{
			ViewInject.toast( "两次输入的新密码不一致" );
			return;
		}

		// 判断原密码是否输入正确
		if ( checkOriginPwd( password ) )
		{
			changePwdNow();
		}
	}

	/**
	 * 
	 * @Description: 检查原密码是否匹配
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	private boolean checkOriginPwd( String password )
	{
		// 判断是否一致
		if ( !oldPassword.equals( password ) )
		{
			ViewInject.toast( "原密码输入错误" );
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @Description: 修改密码
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015年9月23日
	 */
	private void changePwdNow()
	{
		HttpParams params = new HttpParams();
		final User user = AppContext.getInstance().getUser();
		user.setPassword( newPassword );
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(
				User.class, "phone", "password" );
		params.putJsonParams( JSON.toJSONString( user, filter ) );

		mKjHttp.jsonPut( WEB_PATH, params, new HttpCallBack()
		{
			@ Override
			public void onSuccess( String t )
			{
				super.onSuccess( t );
				ViewInject.toast( t );
				AppContext.getInstance().updateUser( user );

				onBackClick();
			}

			@ Override
			public void onFailure( int errorNo, String strMsg )
			{
				super.onFailure( errorNo, strMsg );
				user.setPassword( oldPassword );
				ViewInject.toast( "网络请求失败:error:" + errorNo + ", strMsg:"
						+ strMsg );
			}
		} );
	}

	@ Override
	public void beforeTextChanged( CharSequence s, int start, int count,
			int after )
	{
	}

	@ Override
	public void onTextChanged( CharSequence s, int start, int before, int count )
	{
	}

	@ Override
	public void afterTextChanged( Editable s )
	{
		String password = mEtPassword.getText().toString().trim();
		String newPassword = mEtNewPassword.getText().toString().trim();
		String reNewPassword = mEtReNewPassword.getText().toString().trim();
		if ( StringUtils.isEmpty( password )
				|| StringUtils.isEmpty( newPassword )
				|| StringUtils.isEmpty( reNewPassword ) )
		{
			mBtnCommit.setEnabled( false );
		}
		else
		{
			mBtnCommit.setEnabled( true );
		}
	}

}
