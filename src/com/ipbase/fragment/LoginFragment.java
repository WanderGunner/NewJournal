package com.ipbase.fragment;

import org.kymjs.kjframe.KJHttp;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.ipbase.R;
import com.ipbase.utils.CommonUtils;
import com.ipbase.utils.UIHelper;

/**
 * 
 * ClassName: LoginFragment
 * 
 * @Description: 登陆界面
 * @author kesar
 * @date 2015-9-3
 */
public class LoginFragment extends SimpleBackFragment implements TextWatcher
{
	private String WEB_PATH;

	@ BindView( id = R.id.login_et_phone )
	private EditText mEtPhone;
	@ BindView( id = R.id.login_et_password )
	private EditText mEtPassword;
	@ BindView( id = R.id.login_tv_weixin, click = true )
	private TextView mTvWeiXin; // 微信登陆
	@ BindView( id = R.id.login_img_weixin, click = true )
	private ImageView mImgWeiXin; // 微信登陆
	@ BindView( id = R.id.login_tv_register, click = true )
	private TextView mTvRegister; // 注册
	@ BindView( id = R.id.login_tv_forget_pwd, click = true )
	private TextView mTvForgetPwd; // 忘记密码
	@ BindView( id = R.id.login_btn, click = true )
	private Button mBtnLogin; // 登陆按钮

	private KJHttp kjh;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_login, null );
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		setTitle( "登陆" );
		// 初始化EditText的监听器
		initTextChangedListener();
		// autoLogin();
	}

	@ Override
	protected void initData()
	{
		super.initData();
		WEB_PATH = getString( R.string.url_base )
				+ getString( R.string.url_login );
		kjh = new KJHttp();
	}

	@ Override
	protected void widgetClick( View v )
	{
		super.widgetClick( v );
		switch ( v.getId() )
		{
			case R.id.login_btn : // 手机号登陆操作
				ManualLogin( mEtPhone.getText().toString().trim(), mEtPassword
						.getText().toString().trim() );
				break;
			case R.id.login_tv_weixin :
			case R.id.login_img_weixin : // 微信登陆
				doWeiXinLogin();
				break;
			case R.id.login_tv_register : // 跳转到注册界面
				outsideAty.changeFragment( new RegisterFragment() );
				break;
			case R.id.login_tv_forget_pwd : // 跳转到忘记密码界面
				outsideAty.changeFragment( new ForgetPasswordFragment() );
				break;
			default :
				break;
		}
	}

	private void ManualLogin( String PhoneNum, String Password )
	{
		if ( !checkInput( PhoneNum, Password ) )
		{
			ViewInject.toast( "请输入用户名和密码" );
			return;
		}
		if ( CommonUtils.hasNetwork( getActivity() ) )
		{
			doLogin( PhoneNum, Password );
		}
		else
		{
			ViewInject.toast( "请检查网络连接" );
		}
	}

	/**
	 * 
	 * @Description: 初始化输入框监听事件
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void initTextChangedListener()
	{
		mEtPhone.addTextChangedListener( this );
		mEtPassword.addTextChangedListener( this );
	}

	/**
	 * 
	 * @Description: 执行登陆操作
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void doLogin( String PhoneNum, String Password )
	{
		UIHelper.getUserFromServer( WEB_PATH, kjh, PhoneNum, Password, this );
	}

	/**
	 * 
	 * @Description: 微信登陆操作
	 * @param
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private void doWeiXinLogin()
	{
		ViewInject.toast( "微信登陆" );
	}

	/**
	 * 
	 * @Description: 检查输入是否合格
	 * @param
	 * @return boolean
	 * @throws
	 * @author kesar
	 * @date 2015-9-3
	 */
	private boolean checkInput( String phone, String password )
	{
		if ( StringUtils.isEmpty( phone ) || StringUtils.isEmpty( password ) )
		{
			return false;
		}
		return true;
	}

	// 用不到的方法
	@ Override
	public void beforeTextChanged( CharSequence s, int start, int count,
			int after )
	{

	}

	// 用不到的方法
	@ Override
	public void onTextChanged( CharSequence s, int start, int before, int count )
	{

	}

	@ Override
	public void afterTextChanged( Editable s )
	{
		// 判断是否让登陆按钮可用
		String accout = mEtPhone.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();
		if ( checkInput( accout, password ) )
		{
			mBtnLogin.setEnabled( true );
		}
		else
		{
			mBtnLogin.setEnabled( false );
		}
	}
}
