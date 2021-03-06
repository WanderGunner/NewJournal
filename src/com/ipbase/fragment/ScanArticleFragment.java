package com.ipbase.fragment;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ipbase.R;
import com.ipbase.bean.FragmentPages;
import com.ipbase.ui.SimpleBackActivity;
import com.ipbase.widget.NcmWebView;
import com.ipbase.widget.NcmWebView.OnScrollChangeListener;
import com.ipbase.widget.NcmWebViewClient;

/**
 * ClassName: ScanArticleFragment
 * 
 * @Description: 浏览文章内容页面
 * @author xinyi
 * @date 2016-02-20
 */
public class ScanArticleFragment extends SimpleBackFragment
{
	// 访问文章的 url
	private String WEB_PATH_Article;
	// 文章 ID
	private String mArticleID;
	private String mTitle;

	@ BindView( id = R.id.webView )
	private NcmWebView webView;
	@ BindView( id = R.id.progress )
	private ProgressBar mProgressBar;

	@ BindView( id = R.id.btn_write_comment, click = true )
	private Button mWriteComments;
	@ BindView( id = R.id.btn_look_other_comment, click = true )
	private Button mLookComments;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_webview, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		getBundle();

		// 网络连接相关
		WEB_PATH_Article = getString( R.string.url_base )
				.replace( "/rest", "/" )
				+ getString( R.string.url_article )
				+ "/show/" + mArticleID;

		// 设置标题栏
		setTitle( mTitle );
	}

	private void getBundle()
	{
		/* 获取数据 */
		Bundle bundle = getArguments();
		if ( bundle != null )
		{
			String temp = bundle.getString( "ArticleID" );
			if ( temp != null && !temp.equals( "" ) )
			{
				mArticleID = temp;
			}
			else
			{
				mArticleID = "";
			}
			temp = bundle.getString( "ArticleTitle" );
			if ( temp != null && !temp.equals( "" ) )
			{
				mTitle = temp;
			}
			else
			{
				mTitle = "";
			}
		}
		else
		{
			mArticleID = "";
			mTitle = "";
		}
	}

	@ SuppressLint( { "SetJavaScriptEnabled", "ClickableViewAccessibility" } )
	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		webView.loadData( "", "text/html", "utf-8" );
		webView.setOnScrollChangeListener( listener );
		webView.setWebViewClient( new NcmWebViewClient( mProgressBar ) );
		webView.setWebChromeClient( new WebChromeClient()
		{
			@ Override
			public void onProgressChanged( WebView view, int newProgress )
			{
				mProgressBar.setProgress( newProgress );
			}
		} );
		init();
	}

	private void init()
	{
		if ( mArticleID != null && mArticleID.length() > 0 )
		{
			webView.loadUrl( WEB_PATH_Article );
		}
		else
		{
			ViewInject.toast( "网络错误或未知原因，请重试！" );
		}
	}

	@ Override
	protected void widgetClick( View view )
	{
		super.widgetClick( view );
		Bundle bundle = new Bundle();
		bundle.putString( "ArticleID", mArticleID );
		switch ( view.getId() )
		{
			case R.id.btn_write_comment :
				SimpleBackActivity.postShowWith( outsideAty,
						FragmentPages.WriteComments_Page, bundle );
				break;

			case R.id.btn_look_other_comment :
				SimpleBackActivity.postShowWith( outsideAty,
						FragmentPages.LookComments_Page, bundle );
				break;

			default :
				break;
		}

	}

	private OnScrollChangeListener listener = new OnScrollChangeListener()
	{
		@ Override
		public void onScrollChanged( int l, int t, int oldl, int oldt )
		{
		}

		@ Override
		public void onPageTop( int l, int t, int oldl, int oldt )
		{
		}

		@ Override
		public void onPageEnd( int l, int t, int oldl, int oldt )
		{
		}
	};
};