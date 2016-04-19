package com.ipbase.cropper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;

import com.ipbase.AppConfig;
import com.ipbase.R;
import com.ipbase.fragment.PersonFragment;
import com.ipbase.utils.MyImageTools;

/**
 * Created by apple on 15-5-12.
 */
public class CropperActivity extends Activity
{
	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ROTATE_NINETY_DEGREES = 90;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";

	public int RESUQEST_CODE_CROP = 3;
	Bitmap croppedImage;
	CropImageView cropImageView;
	String tempPath;
	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	// Saves the state upon rotating the screen/restarting the activity
	@ Override
	protected void onSaveInstanceState( Bundle bundle )
	{
		super.onSaveInstanceState( bundle );
		bundle.putInt( ASPECT_RATIO_X, mAspectRatioX );
		bundle.putInt( ASPECT_RATIO_Y, mAspectRatioY );
	}

	// Restores the state upon rotating the screen/restarting the activity
	@ Override
	protected void onRestoreInstanceState( Bundle bundle )
	{
		super.onRestoreInstanceState( bundle );
		mAspectRatioX = bundle.getInt( ASPECT_RATIO_X );
		mAspectRatioY = bundle.getInt( ASPECT_RATIO_Y );
	}

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.activity_cropper );

		tempPath = getIntent().getStringExtra( "path" );
		if ( tempPath.equals( "" ) )
		{
			tempPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + AppConfig.imgCachePath + "image.png";
		}

		// Initialize components of the app
		cropImageView = (CropImageView) findViewById( R.id.question_img );
		// Sets initial aspect ratio to 10/10, for demonstration purposes
		cropImageView.setAspectRatio( DEFAULT_ASPECT_RATIO_VALUES,
				DEFAULT_ASPECT_RATIO_VALUES );
		Bitmap bitmap1 = MyImageTools.compressImageFromFile( tempPath );
		// Bitmap bitmap1 =BitmapFactory.decodeFile(tempPath);
		if ( bitmap1 != null )
		{
			cropImageView.setImageBitmap( bitmap1 );
		}
		else
		{
			cropImageView.setImageResource( R.drawable.ic_launcher );
		}

	}

	public void onClick( View view )
	{

		switch ( view.getId() )
		{
		// case R.id.Button_crop:
		// croppedImage = cropImageView.getCroppedImage();
		// ImageView croppedImageView = (ImageView)
		// findViewById(R.id.croppedImageView);
		// croppedImageView.setImageBitmap(croppedImage);
		// break;
			case R.id.question_photo_view_turn_right_btn :
				cropImageView.rotateImage( ROTATE_NINETY_DEGREES );
				break;
			case R.id.question_photo_view_turn_left_btn :
				cropImageView.rotateImage( ROTATE_NINETY_DEGREES );
				// cropImageView.setAspectRatio(progress, mAspectRatioY);
				// cropImageView.setFixedAspectRatio(isChecked);
				break;
			case R.id.question_photo_view_nav_submit :
				croppedImage = cropImageView.getCroppedImage();
				String path = MyImageTools
						.saveToSdCard( cropImageView.getCroppedImage(),
								AppConfig.imgCachePath, "image.png" );

				Intent intent = new Intent( this, PersonFragment.class );
				intent.putExtra( "imagePath", path );
				setResult( RESULT_OK, intent );
				finish();

				break;

			case R.id.question_photo_view_nav_btn_back :
				System.err.println( "question_photo_view_nav_btn_back" );
				finish();
				break;

			default :
				break;
		}
	}

}
