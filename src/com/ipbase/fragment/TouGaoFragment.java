package com.ipbase.fragment;

import java.io.File;

import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.bmob.BmobPro;
import com.ipbase.AppContext;
import com.ipbase.R;
import com.ipbase.bean.UserArticle;
import com.ipbase.utils.CommonUtils;
import com.smile.filechoose.api.ChooserType;
import com.smile.filechoose.api.ChosenFile;
import com.smile.filechoose.api.FileChooserListener;
import com.smile.filechoose.api.FileChooserManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * ClassName: TouGaoFragment
 * 
 * @Description: 投稿界面
 * @author xinyi
 * @date 2015-11-28
 */
public class TouGaoFragment extends SimpleBackFragment implements
		FileChooserListener
{
	private BaseAdapter mBaseAdapter;
	@ BindView( id = R.id.file_listview )
	private ListView mListView;
	private ChosenFile mChoosedFile;
	private FileChooserManager mFileChM;
	private ProgressDialog mProgressDialog;
	
	@ Override
	protected View inflaterView( LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle )
	{
		return View.inflate( outsideAty, R.layout.fragment_tougao, null );
	}

	@ Override
	protected void initData()
	{
		super.initData();
		setTitle( "投稿" );
		initListView();
	}

	private void initListView()
	{
		mBaseAdapter = new ArrayAdapter< String >( getActivity(),
				R.layout.file_list_item, R.id.tv_item, getResources()
						.getStringArray( R.array.bmob_newfile_arrays ) );
		mListView.setAdapter( mBaseAdapter );
		mListView.setOnItemClickListener( new OnItemClickListener()
		{
			@ Override
			public void onItemClick( AdapterView< ? > parent, View view,
					int pos, long id )
			{
				ChooseType( pos + 1 );
			}
		} );
	}

	private void ProgressDialogInit()
	{
		mProgressDialog = new ProgressDialog( getActivity() );
		mProgressDialog.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
		mProgressDialog.setIndeterminate( false );
		mProgressDialog.setCancelable( true );
		mProgressDialog.setCanceledOnTouchOutside( false );
	}

	private void ChooseType( int pos )
	{
		switch ( pos )
		{
			case 1 :
				if ( !CommonUtils.hasNetwork( getActivity() ) )
				{
					ViewInject.toast( "请检查网络连接!" );
					return;
				}
				ViewInject.toast( "请选择一份文档" );
				pickFile();
				break;

			case 2 :
				clearCache();
				ViewInject.toast( "缓存清理成功" );
				break;

			default :
				break;
		}
	}

	public void pickFile()
	{
		mFileChM = new FileChooserManager( this );
		mFileChM.setFileChooserListener( this );
		try
		{
			mFileChM.choose();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	private void upload()
	{
		ProgressDialogInit();
		mProgressDialog.setTitle( "上传中..." );
		mProgressDialog.show();

		File file = new File( mChoosedFile.getFilePath() );
		final BmobFile bmobFile = new BmobFile( file );

		bmobFile.upload( getActivity(), new UploadFileListener()
		{
			@ Override
			public void onSuccess()
			{
				UserArticle user = CommonUtils.UserToArticleUser( AppContext
						.getInstance().getUser() );
				user.setWord( bmobFile );
				user.save( getActivity(), new SaveListener()
				{
					@ Override
					public void onSuccess()
					{
						ViewInject.toast( "上传成功" );
					}

					@ Override
					public void onFailure( int statuscode, String errormsg )
					{
						ViewInject.toast( "保存上传对象失败，错误码：" + statuscode
								+ ",  错误原因：" + errormsg );
					}
				} );
				mProgressDialog.dismiss();
			}

			public void onProgress( Integer value )
			{
				mProgressDialog.setProgress( value );
			}

			@ Override
			public void onFailure( int statuscode, String errormsg )
			{
				ViewInject.toast( "上传失败，错误码：" + statuscode + ",  错误原因："
						+ errormsg );
				mProgressDialog.dismiss();
			}
		} );
	}

	private void clearCache()
	{
		BmobPro.getInstance( getActivity() ).clearCache();
	}

	@ Override
	public void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult( requestCode, resultCode, data );

		if ( requestCode == ChooserType.REQUEST_PICK_FILE
				&& resultCode == Activity.RESULT_OK )
		{
			if ( mFileChM == null )
			{
				mFileChM = new FileChooserManager( this );
				mFileChM.setFileChooserListener( this );
			}
			mFileChM.submit( requestCode, data );
		}
	}

	@ Override
	public void onError( String errStr )
	{

	}

	@ Override
	public void onFileChosen( ChosenFile file )
	{
		mChoosedFile = file;
		getActivity().runOnUiThread( new Runnable()
		{
			@ Override
			public void run()
			{
				new AlertDialog.Builder( getActivity() )
						.setTitle( "投稿确认" )
						.setMessage(
								"确定将 " + mChoosedFile.getFileName()
										+ " 投稿到《新中医》吗？" )
						.setPositiveButton( "是", new OnClickListener()
						{
							@ Override
							public void onClick( DialogInterface dialog,
									int which )
							{
								upload();
							}
						} ).setNegativeButton( "否", null ).show();
			}
		} );
	}

	@ Override
	public void onDestroy()
	{
		super.onDestroy();
	}

};