package com.watudrc.app;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.ImageUtil;
import com.watudrc.app.view.MaterialRippleLayout;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

public class ImageDetailActivity extends BaseActivity implements IWhereMyLocationConstants{
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;
    
	Toolbar toolbar;
    public TextView titleBarText;
    public RelativeLayout btnBack;
    
    ImageView resultImage;
    ProgressBar progressBar;
    MaterialRippleLayout downloadRips;
    FirmaTextView downloadText;
    
    String imageCaption="";
    String imageUrl="";


    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storageObject= new StoreAndGetFromPrefs(getApplicationContext());
		setContentView(R.layout.activity_image_detail);
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		
        toolbar = (Toolbar) findViewById(R.id.toolbar);
		
        
		titleBarText = (TextView) toolbar.findViewById(R.id.titleBarText);
		titleBarText.setTypeface(Common.getFirma());
		
		btnBack = (RelativeLayout) toolbar.findViewById(R.id.btnBack);
		
		resultImage = (ImageView) findViewById(R.id.resultImage);
		progressBar= (ProgressBar) findViewById(R.id.progressBar);

		downloadRips= (MaterialRippleLayout) findViewById(R.id.downloadRips);
		downloadText= (FirmaTextView) findViewById(R.id.downloadText);


		
		Intent theIntent = getIntent();
		if(theIntent != null)
		{
			imageCaption = theIntent.getStringExtra(IMAGE_CAPTION);
			imageUrl = theIntent.getStringExtra(IMAGE_URL);
			
		}
		
		titleBarText.setText(imageCaption);
		
		btnBack.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				goBack();

			}
		});
		
		String urlData= imageUrl;
		boolean cacheFound;
        List<String> memCache = MemoryCacheUtils.findCacheKeysForImageUri(urlData, ImageLoader.getInstance().getMemoryCache());
        cacheFound = !memCache.isEmpty();
        if (!cacheFound) {
            File discCache = DiskCacheUtils.findInCache(urlData, ImageLoader.getInstance().getDiscCache());
            if (discCache != null) {
                cacheFound = discCache.exists();
            }
        }
        
        if(cacheFound)
        {
        	downloadRips.setVisibility(View.GONE);
    		ImageUtil.displayImage(resultImage, urlData, null);
        }
        
        else
        {
        	downloadRips.setVisibility(View.VISIBLE);
			downloadPic(urlData,downloadText, resultImage,downloadRips,progressBar);


        }
        
		
	}
	
	
	
	

	  
	  public void hideKeyboard()
		{
			InputMethodManager inputManager = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE); 

	        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
	                   InputMethodManager.HIDE_NOT_ALWAYS);
			
		}
	  
	  @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				goBack();
				
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	  
	  
	  public void goBack()
	  {
		  finish();
		  overridePendingTransition(R.anim.fadein, R.anim.fadeout);		  
		
	  }
	  
	  public void downloadPic(final String urlData, final FirmaTextView downloadText, final ImageView resultImage, final MaterialRippleLayout downloadRips, final ProgressBar progressBar)
	    {

	        DisplayImageOptions opts = new DisplayImageOptions.Builder()
	                .cacheInMemory(true)
	                .cacheOnDisk(true)
	                .imageScaleType(ImageScaleType.EXACTLY)
	                .displayer(new FadeInBitmapDisplayer(500))
	                .build();

	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
	                mContext)
	                .defaultDisplayImageOptions(opts)
	                .memoryCache(new WeakMemoryCache())
	                .build();

	        if (!ImageLoader.getInstance().isInited())
	            ImageLoader.getInstance().init(config);
	        
	        
	    	 ImageLoader.getInstance().displayImage(urlData,resultImage,opts , new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
	                downloadText.setText(mContext.getResources().getString(R.string.download));

				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					downloadRips.setVisibility(View.VISIBLE);
	                downloadText.setText(mContext.getResources().getString(R.string.retry));
	                
	                downloadRips.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							downloadPic(urlData, downloadText, resultImage, downloadRips, progressBar);
						}
					});
					//Show retry option
			}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					downloadRips.setVisibility(View.GONE);
					
					ImageView resultView= (ImageView) view;
			    	ImageUtil.displayImage(resultView, imageUri, null);

				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
					//Show Download AGain
					downloadRips.setVisibility(View.VISIBLE);
	                downloadText.setText(mContext.getResources().getString(R.string.retry));

					
				}
			}, new ImageLoadingProgressListener() {
	            @Override
	            public void onProgressUpdate(String imageUri, View view, int current, int total) {
	                int percent = (current * 100) / total;
	                progressBar.setProgress(percent);
	                downloadText.setText(percent+"%");
	                
	            }
	        });
	    }  
	  
	
}
