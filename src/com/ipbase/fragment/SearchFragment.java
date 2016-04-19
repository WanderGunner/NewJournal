package com.ipbase.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.ipbase.R;
import com.ipbase.adapter.SearchAutoAdapter;
import com.ipbase.bean.Article;
import com.ipbase.bean.FragmentPages;
import com.ipbase.ui.SimpleBackActivity;
import com.ipbase.utils.CommonUtils;
import com.ipbase.utils.FastJsonTools;

/**
 * 搜索页面
 * 
 * @author 李先华 2015年10月28日上午12:44:30
 */
public class SearchFragment extends SimpleBackFragment implements TextWatcher
{
	public static final String SEARCH_HISTORY = "search_history";

	private String WEB_PATH;

	private KJHttp kjh;

	// 这里不能用KJFrame绑定控件id
	private EditText mEitSearch;
	private ImageView mIvSearch;
	private ImageView mIvDeleteText;

	@ BindView( id = R.id.lv_search_history )
	private ListView mLvSearchHistory;

	private SearchAutoAdapter mSearchAutoAdapter;

	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_search, null );
	}

	@ Override
	protected void initWidget( View parentView )
	{
		super.initWidget( parentView );
		// 隐藏原来标题栏，显示搜索标题栏
		setRLVisible( View.GONE );
		setRLSearchVisible( View.VISIBLE );
		findViewById();
		// 初始化EditText的监听器
		initTextChangedListener();
	}

	private void findViewById()
	{
		mEitSearch = (EditText) outsideAty.findViewById( R.id.et_search );

		mIvSearch = (ImageView) outsideAty.findViewById( R.id.iv_search );

		mIvDeleteText = (ImageView) outsideAty
				.findViewById( R.id.iv_delete_text );

		mIvDeleteText.setOnClickListener( this );
		mIvSearch.setOnClickListener( this );

	}

	private void initTextChangedListener()
	{
		mEitSearch.addTextChangedListener( this );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		init();
	}

	private void init()
	{
		mSearchAutoAdapter = new SearchAutoAdapter( outsideAty, 5 );
		mLvSearchHistory.setAdapter( mSearchAutoAdapter );
		mLvSearchHistory.setOnItemClickListener( new OnItemClickListener()
		{

			@ Override
			public void onItemClick( AdapterView< ? > arg0, View view,
					int position, long arg3 )
			{
				String data = (String) mSearchAutoAdapter.getItem( position );
				mEitSearch.setText( data );
				// mIvSearch.performClick();
			}
		} );

		WEB_PATH = getString( R.string.url_base )
				+ getString( R.string.url_search );

		kjh = new KJHttp();
	}

	@ Override
	protected void widgetClick( View view )
	{
		switch ( view.getId() )
		{
			case R.id.iv_search :
				saveSearchHistory();
				mSearchAutoAdapter.initSearchHistory();

				if ( !CommonUtils.hasNetwork( getActivity() ) )
				{
					ViewInject.toast( "请检查网络连接!" );
					return;
				}

				if ( mEitSearch.getText().toString().trim().isEmpty() )
				{
					ViewInject.toast( "请输入搜索内容(作者或者文章标题)" );
					return;
				}

				// 搜索词
				String mSearchWord = null;
				try
				{
					mSearchWord = URLEncoder.encode( mEitSearch.getText()
							.toString().trim(), "UTF-8" ); // 中文传输解决
				}
				catch ( UnsupportedEncodingException e )
				{
					e.printStackTrace();
				}
				// 搜索
				getSearchResultFromServer( WEB_PATH + mSearchWord, kjh, this );
				// SimpleBackActivity.postShowWith(outsideAty,
				// FragmentPages.SearchResult_Page);
				break;
			case R.id.iv_delete_text :
				mEitSearch.setText( "" );
				break;
			default :
				break;
		}
		super.widgetClick( view );
	}

	private void getSearchResultFromServer( String WEB_PATH, KJHttp kjh,
			final SimpleBackFragment fragment )
	{

		kjh.get( WEB_PATH, new HttpParams(), false, new HttpCallBack()
		{
			@ Override
			public void onSuccess( String t )
			{
				super.onSuccess( t );

				// Log.i("onSuccess ", t);
				try
				{
					ArrayList< Article > articleLists = null;
					JSONObject object = new JSONObject( t );
					boolean success = object.getBoolean( "result" );
					String articles = object.getString( "articles" );
					// Log.i("articlesTitle", articlesTitle);
					// Log.i("articlesAuthor", articlesAuthor);
					if ( success )
					{
						articleLists = (ArrayList< Article >) FastJsonTools
								.getBeans( articles, Article.class );

						/*
						 * for (int i = 0; i < articleLists.size(); i++) {
						 * Log.i("xianhuaTag", articleLists.get(i).toString());
						 * }
						 */

						Bundle bundle = new Bundle();
						// bundle.putParcelableArrayList("mArticles", articles);
						bundle.putString( "SearchWord", mEitSearch.getText()
								.toString().trim() );
						bundle.putSerializable( "mArticles", articleLists );
						SimpleBackActivity.postShowWith( outsideAty,
								FragmentPages.SearchResult_Page, bundle );
					}
					else
					{
						ViewInject.toast( object.getString( "msg" ) );
					}
				}
				catch ( JSONException e )
				{
					e.printStackTrace();
					ViewInject.toast( "JSONException " + e.getMessage() );
				}

			}

			@ Override
			public void onFailure( int errorNo, String strMsg )
			{
				super.onFailure( errorNo, strMsg );
				ViewInject.toast( "错误码:" + errorNo + ", 错误信息:" + strMsg );
			}

		} );

	}

	/**
	 * @Title: getArticles
	 * @Description: json解析为Article
	 * @param @param json
	 * @param @return
	 * @return List<Article>
	 * @throws
	 * @auhor 鲜花
	 * @date 2015年11月5日
	 */
	public static List< Article > getArticles( String json )
	{
		List< Article > list = new ArrayList< Article >();
		try
		{
			list = JSON.parseArray( json, Article.class );
		}
		catch ( Exception e )
		{
			ViewInject.toast( "getArticles error" + e.getMessage() );
			// Log.i("getArticles error", e.getMessage());
		}
		return list;
	}

	/*
	 * 保存搜索记录
	 */
	private void saveSearchHistory()
	{
		String text = mEitSearch.getText().toString().trim();
		if ( text.length() < 1 )
		{
			return;
		}
		SharedPreferences sp = outsideAty.getSharedPreferences( SEARCH_HISTORY,
				0 );
		String longhistory = sp.getString( SEARCH_HISTORY, "" );
		String[] tmpHistory = longhistory.split( "," );
		ArrayList< String > history = new ArrayList< String >(
				Arrays.asList( tmpHistory ) );
		// 检查历史记录是否已经存在当前输入的text，如果存在则删除
		if ( history.size() > 0 )
		{
			int i;
			for ( i = 0; i < history.size(); i++ )
			{
				if ( text.equals( history.get( i ) ) )
				{
					history.remove( i );
					break;
				}
			}
			// 如果记录大于4个，则移除最后一个数据再在最前面增加一个数据
			if ( history.size() > 4 )
			{
				history.remove( history.size() - 1 );
			}
			history.add( 0, text );
		}

		// 重新加，提交
		if ( history.size() > 0 ) // history.size()>1和history.size()>0一样
		{
			StringBuilder sb = new StringBuilder();
			for ( int i = 0; i < history.size(); i++ )
			{
				sb.append( history.get( i ) + "," );// 这句一开始添加一个数据时加了两个, why？
													// 用String也是 因为""
			}
			sp.edit().putString( SEARCH_HISTORY, sb.toString() ).commit();
		}
		else
		{
			sp.edit().putString( SEARCH_HISTORY, text + "," ).commit();
		}
	}

	@ Override
	public void beforeTextChanged( CharSequence s, int start, int count,
			int after )
	{

	}

	@ Override
	public void onTextChanged( CharSequence s, int start, int before, int count )
	{
		mSearchAutoAdapter.performFiltering( s );
	}

	@ Override
	public void afterTextChanged( Editable s )
	{
		if ( s.length() == 0 )
		{
			mIvDeleteText.setVisibility( View.GONE );
		}
		else
		{
			mIvDeleteText.setVisibility( View.VISIBLE );
		}
	}

};