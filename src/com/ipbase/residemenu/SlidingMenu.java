package com.ipbase.residemenu;

import org.kymjs.kjframe.ui.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

import com.ipbase.AppApplication;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.bean.FragmentPages;
import com.ipbase.bean.User;
import com.ipbase.residemenu.ResideMenu.OnMenuListener;
import com.ipbase.ui.MainActivity;
import com.ipbase.ui.SimpleBackActivity;

/**
 * ClassName: SlidingMenu
 * 
 * @Description: 自定义的简便侧滑类
 * @author xinyi
 * @date 2015-9-5
 * 
 * */
public class SlidingMenu
{
	// 主界面左上角图片按钮
	private Button leftMenu;
	// 当前上下文
	private Activity mActivity;
	// 侧滑菜单
	private ResideMenu mResideMenu;

	// 更改密码
	private ResideMenuItem mItemChangePassword;
	// 已收藏
	private ResideMenuItem mItemCollection;
	// 已购买
	private ResideMenuItem mItemBuy;
	// 投稿
	private ResideMenuItem mItemTouGao;
	// 已投稿件
	private ResideMenuItem mItemYiTouGaoJian;
	// 通知
	private ResideMenuItem mItemTongZhi;
	// 版本更新
	private ResideMenuItem mItemGengXin;
	// 意见反馈
	private ResideMenuItem mItemFanKui;
	// 注销
	private ResideMenuItem mItemZhuXiao;
	// 个人信息
	private ResideMenuInfo mInfo;

	// 侧滑菜单关闭状态
	private boolean is_closed = true;

	// 更新返回
	UpdateResponse ur;

	/**
	 * ClassName: SlidingMenu
	 * 
	 * @Description: 自定义的简便侧滑类，防止 MainActivity 代码过长，不易阅读
	 * @author xinyi
	 * @param Activity
	 *            上下文 Button 左侧图标按钮
	 * @date 2015-9-5
	 * 
	 * */
	public SlidingMenu( Activity contextActivity, Button contextLeftButton )
	{
		mActivity = contextActivity;
		leftMenu = contextLeftButton;
		initMenu();
		setListener();
	}

	@ SuppressWarnings( "deprecation" )
	private void initMenu()
	{
		// attach to current activity;
		mResideMenu = new ResideMenu( mActivity );
		// resideMenu.setBackground( R.drawable.menu_background );
		mResideMenu.setBackground( R.color.layout_left_menu );
		mResideMenu.attachToActivity( mActivity );
		mResideMenu.setMenuListener( new OnMenuListener()
		{
			@ Override
			public void openMenu()
			{
				is_closed = false;
				leftMenu.setVisibility( View.GONE );
			}

			@ Override
			public void closeMenu()
			{
				is_closed = true;
				leftMenu.setVisibility( View.VISIBLE );
			}
		} );
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		mResideMenu.setScaleValue( 0.65f );
		// 禁止使用右侧菜单
		mResideMenu.setDirectionDisable( ResideMenu.DIRECTION_RIGHT );

		// //////////// 新写的 \\\\\\\\\\\\\\\

		// create menu items;
		mItemChangePassword = new ResideMenuItem( mActivity,
				R.drawable.icon_lock, "更改密码" );
		mItemCollection = new ResideMenuItem( mActivity, R.drawable.icon_heart,
				"已收藏" );
		mItemBuy = new ResideMenuItem( mActivity, R.drawable.icon_cart, "已购买" );
		mItemTouGao = new ResideMenuItem( mActivity, R.drawable.icon_mail, "投稿" );
		mItemYiTouGaoJian = new ResideMenuItem( mActivity,
				R.drawable.icon_checklist, "已投稿件" );

		mItemTongZhi = new ResideMenuItem( mActivity, R.drawable.icon_chat,
				"通知" );

		mItemGengXin = new ResideMenuItem( mActivity, R.drawable.icon_thunder,
				"版本更新" );
		mItemFanKui = new ResideMenuItem( mActivity, R.drawable.icon_send,
				"意见反馈" );
		mItemZhuXiao = new ResideMenuItem( mActivity, R.drawable.icon_logout,
				"注销" );

		mResideMenu
				.addMenuItem( mItemChangePassword, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemCollection, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemBuy, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemTouGao, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemYiTouGaoJian, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemTongZhi, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemGengXin, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemFanKui, ResideMenu.DIRECTION_LEFT );
		mResideMenu.addMenuItem( mItemZhuXiao, ResideMenu.DIRECTION_LEFT );
		String info = "未登录";
		if ( isLogin() )
		{
			User user = AppContext.getInstance().getUser();
			info = "欢迎您，"
					+ ( ( user.getNickname() == null || user.getNickname()
							.equals( "" ) ) ? user.getPhone() : user
							.getNickname() );
			mInfo = new ResideMenuInfo( mActivity, R.drawable.icon_man_white,
					info );
			mInfo.setIcon( user.getPhone() );
		}
		else
		{
			mInfo = new ResideMenuInfo( mActivity, R.drawable.icon_man_white,
					info );
		}

		mResideMenu.addMenuInfo( mInfo );
	}

	private void setListener()
	{
		leftMenu.setOnClickListener( new View.OnClickListener()
		{
			@ Override
			public void onClick( View view )
			{
				mResideMenu.openMenu( ResideMenu.DIRECTION_LEFT );
			}
		} );

		mItemChangePassword.setOnClickListener( mOnClickListener );
		mItemCollection.setOnClickListener( mOnClickListener );
		mItemBuy.setOnClickListener( mOnClickListener );
		mItemTouGao.setOnClickListener( mOnClickListener );
		mItemYiTouGaoJian.setOnClickListener( mOnClickListener );
		mItemTongZhi.setOnClickListener( mOnClickListener );
		mItemGengXin.setOnClickListener( mOnClickListener );
		mItemFanKui.setOnClickListener( mOnClickListener );
		mItemZhuXiao.setOnClickListener( mOnClickListener );

		mInfo.setOnClickListener( mOnClickListener );
	}

	private OnClickListener mOnClickListener = new OnClickListener()
	{
		@ Override
		public void onClick( View view )
		{
			if ( view == mItemChangePassword )
			{
				if ( isLogin() )
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.ChangePassword_Page );
				}
				else
				{
					ViewInject.toast( "请先登录!" );
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
			else if ( view == mItemCollection )
			{				
				if ( isLogin() )
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Collection_Page );
				}
				else
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
			else if ( view == mItemBuy )
			{
				if ( isLogin() )
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.YiGouMai_Page );
				}
				else
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
			else if ( view == mItemTouGao )
			{
				if ( isLogin() )
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.TouGao_Page );
				}
				else
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
			else if ( view == mItemYiTouGaoJian )
			{
				if ( isLogin() )
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.YiTouGao_page );
				}
				else
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
			else if ( view == mItemTongZhi )
			{
				if ( isLogin() )
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Notice_Page );
				}
				else
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
			else if ( view == mItemGengXin )
			{
				/*
				 * Bundle bundle = new Bundle(); bundle.putString( "flog",
				 * "版本更新" ); SimpleBackActivity.postShowWith( mActivity,
				 * FragmentPages.Setting_Page, bundle );
				 */
				// 手动更新
				BmobUpdateAgent.forceUpdate( AppApplication.getInstance()
						.getMainActivity() );

				BmobUpdateAgent.setUpdateListener( new BmobUpdateListener()
				{

					@ Override
					public void onUpdateReturned( int updateStatus,
							UpdateResponse updateInfo )
					{
						// 以下适用于V3.4.4之前版本
						if ( updateStatus == UpdateStatus.Yes )
						{// 版本有更新
							ur = updateInfo;
						}
						else if ( updateStatus == UpdateStatus.No )
						{
							Toast.makeText(
									AppApplication.getInstance()
											.getMainActivity(), "当前版本已经是最新版本",
									Toast.LENGTH_SHORT ).show();
						}
						else if ( updateStatus == UpdateStatus.TimeOut )
						{
							Toast.makeText(
									AppApplication.getInstance()
											.getMainActivity(), "查询出错或查询超时",
									Toast.LENGTH_SHORT ).show();
						}
					}
				} );
			}
			else if ( view == mItemFanKui )
			{
				if ( isLogin() )
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Feedback_Page );
				}
				else
				{
					ViewInject.toast( "请先登录!" );
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
			else if ( view == mItemZhuXiao )// 注销账户
			{
				AppContext.getInstance().deleteUser(
						AppContext.getInstance().getUser() );
				MainActivity activity = AppApplication.getInstance()
						.getMainActivity();
				if ( activity != null )
				{
					activity.reFresh();
				}
			}
			else if ( view == mInfo )
			{
				if ( isLogin() )
				{
					Bundle bundle = new Bundle();
					bundle.putString( "flog", "个人信息" );
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.PersonInfo, bundle );
				}
				else
				{
					SimpleBackActivity.postShowWith( mActivity,
							FragmentPages.Login_Page );
				}
			}
		}
	};

	/**
	 * 
	 * @Description: 判断用户是否已登陆
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xinyi
	 * @date 2015-9-5
	 */
	private boolean isLogin()
	{
		if ( AppContext.getInstance().getUser() != null )
		{
			return true;
		}
		return false;
	}

	public ResideMenu getResideMenu()
	{
		return mResideMenu;
	}

	public boolean getIsClosed()
	{
		return is_closed;
	}

	/**
	 * 
	 * @Description: 刷新登录状态菜单
	 * @param
	 * @return void
	 * @throws
	 * @author xinyi
	 * @date 2015年9月28日
	 */
	public void reFresh()
	{
		String info = "未登录";
		if ( isLogin() )
		{
			User user = AppContext.getInstance().getUser();
			info = "欢迎您，"
					+ ( ( user.getNickname() == null || user.getNickname()
							.equals( "" ) ) ? user.getPhone() : user
							.getNickname() );
			mInfo.setIcon( user.getPhone() );
		}
		else
		{
			mResideMenu.closeMenu();
			mInfo.setIcon( R.drawable.icon_man_white );
			ViewInject.toast( "已注销" );
		}
		if ( mInfo != null )
		{
			mInfo.setTitle( info );
		}
	}
}