package com.watutz.app.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageUtil {
	
	public static void displayImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void displayImageBlurred(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	
	public static void displayLessRoundImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, LESS_ROUND_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void displayRoundImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, getRoundDisplayImageOptions(), listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void displayRoundImageUser(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, ROUND_DISPLAY_IMAGE_OPTIONS_USER, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void loadImage(String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.loadImage(path, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}
	
	public static DisplayImageOptions getRoundDisplayImageOptions() {
		return ROUND_DISPLAY_IMAGE_OPTIONS;
	}


	//TODO Change default image
	private static final DisplayImageOptions.Builder DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
	.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
	.displayer(new FadeInBitmapDisplayer(300, true, false, false))
	.cacheOnDisk(true)
	.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);





private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
	
    .showImageForEmptyUri(android.R.color.transparent)
    .showImageOnFail(android.R.color.transparent)
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .considerExifParams(true)
    .bitmapConfig(Bitmap.Config.RGB_565)
  	.build();
private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
	.displayer(new RoundedBitmapDisplayer(500))
	
    .showImageForEmptyUri(android.R.color.transparent)
    .showImageOnFail(android.R.color.transparent)
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .considerExifParams(true)
    .bitmapConfig(Bitmap.Config.RGB_565)
    .build();

private static final DisplayImageOptions LESS_ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
	.displayer(new RoundedBitmapDisplayer(15))
    .showImageForEmptyUri(android.R.color.transparent)
    .showImageOnFail(android.R.color.transparent)
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .considerExifParams(true)
    .bitmapConfig(Bitmap.Config.RGB_565)
	.build();

private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS_USER = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
	.displayer(new RoundedBitmapDisplayer(500))
	
    .showImageForEmptyUri(android.R.color.transparent)
    .showImageOnFail(android.R.color.transparent)
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .considerExifParams(true)
    .bitmapConfig(Bitmap.Config.RGB_565)
    .build();

}
