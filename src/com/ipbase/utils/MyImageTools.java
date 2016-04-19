package com.ipbase.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

import com.ipbase.AppConfig;

/**
 * xinyi
 */
public class MyImageTools
{
	/**
	 * 压缩图片
	 *
	 * @param srcPath
	 * @return
	 */
	public static Bitmap compressImageFromFile( String srcPath )
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile( srcPath, newOpts );

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 1280f;//
		float ww = 720;//
		int be = 1;
		if ( w > h && w > ww )
		{
			be = (int) ( newOpts.outWidth / ww );
		}
		else if ( w < h && h > hh )
		{
			be = (int) ( newOpts.outHeight / hh );
		}
		if ( be <= 0 )
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile( srcPath, newOpts );
		return bitmap;
	}

	/**
	 * 保存图片到SD卡
	 *
	 * @param bitmap
	 * @return
	 */
	public static String saveToSdCard( Bitmap bitmap, String pos, String imageName )
	{
		String state = Environment.getExternalStorageState();
		File file = null;
		if ( state.equals( Environment.MEDIA_MOUNTED ) )
		{
			String saveDir = Environment.getExternalStorageDirectory()
					+ pos;
			File saveFile = new File( saveDir );
			if ( !saveFile.exists() )
			{
				saveFile.mkdir();
			}
			file = new File( saveDir, imageName );

			try
			{
				FileOutputStream out = new FileOutputStream( file );
				if ( bitmap.compress( Bitmap.CompressFormat.JPEG, 50, out ) )
				{
					out.flush();
					out.close();
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			return file.getAbsolutePath();
		}
		return null;
	}

}
