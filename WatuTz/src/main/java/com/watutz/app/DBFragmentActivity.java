package com.watutz.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;


import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

import com.ypyproductions.dialog.utils.IDialogFragmentListener;
import com.ypyproductions.net.task.IDBConstantURL;
import com.ypyproductions.utils.ResolutionUtils;



public class DBFragmentActivity extends AppCompatActivity implements IDBConstantURL, IDialogFragmentListener,IWhereMyLocationConstants {
	
	public static String TAG = DBFragmentActivity.class.getSimpleName();
	private static Dialog  mProgressDialog;
	private int screenWidth;
	private int screenHeight;
	
	protected static DrawerLayout mDrawerLayout;
	private static ListView mDrawerList;
	
	StoreAndGetFromPrefs storageObject;
	static Context mContext;
    
    static RelativeLayout searchView;
    
    
    public static void setSearchView(RelativeLayout sView)
	{
		// TODO Auto-generated method stub
		searchView= sView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFormat(PixelFormat.RGBA_8888);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		
		int[] mRes=ResolutionUtils.getDeviceResolution(this);
		if(mRes!=null && mRes.length==2){
			screenWidth=mRes[0];
			screenHeight=mRes[1];
		}
		mContext = getApplicationContext();
		storageObject = new StoreAndGetFromPrefs(mContext);
		
		
		
	}
	
	public static void setDrawerDetails(DrawerLayout mLayout, ListView mList )
	{
		mDrawerLayout = mLayout;
		mDrawerList = mList;

	}

	
   


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if(searchView != null)
			{
		      searchView.setVisibility(View.GONE);
			}

			
			if(mDrawerLayout.isDrawerOpen(mDrawerList))
			{
				mDrawerLayout.closeDrawer(mDrawerList);
				
				return false;
			}
			
			
			else
			{
			showDialogFragment(DIALOG_QUIT_APPLICATION);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showDialogFragment(int idDialog) {
		FragmentManager mFragmentManager = getSupportFragmentManager();
		switch (idDialog) {
		case DIALOG_LOSE_CONNECTION:
			createWarningDialog(DIALOG_LOSE_CONNECTION, R.string.title_warning, R.string.info_lose_internet).show(mFragmentManager, "DIALOG_LOSE_CONNECTION");
			break;
		case DIALOG_EMPTY:
			createWarningDialog(DIALOG_EMPTY, R.string.title_warning, R.string.info_empty).show(mFragmentManager, "DIALOG_EMPTY");
			break;
		case DIALOG_QUIT_APPLICATION:
			createQuitDialog().show(mFragmentManager, "DIALOG_QUIT_APPLICATION");
			break;
		case DIALOG_SEVER_ERROR:
			createWarningDialog(DIALOG_SEVER_ERROR, R.string.title_warning, R.string.info_server_error).show(mFragmentManager, "DIALOG_SEVER_ERROR");
			break;
		default:
			break;
		}
	}

	public DialogFragment createWarningDialog(int idDialog, int titleId, int messageId) {
		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(idDialog, android.R.drawable.ic_dialog_alert, titleId, android.R.string.ok, messageId);
		
			
		return mDAlertFragment;
	}

	private DialogFragment createQuitDialog() {
		int mTitleId = R.string.title_confirm;
		int mYesId = R.string.title_yes;
		int mNoId = R.string.title_no;
		int iconId = R.drawable.ic_launcher;
		int messageId = R.string.quit_message;

		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(DIALOG_QUIT_APPLICATION, iconId, mTitleId, mYesId, mNoId, messageId);
		return mDAlertFragment;

	}



	

	public static void dimissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	@Override
	public void doPositiveClick(int idDialog) {
		switch (idDialog) {
		case DIALOG_QUIT_APPLICATION:
			onDestroyData();
			finish();
			break;
		default:
			break;
		}
	}
	
	 

	@Override
	public void doNegativeClick(int idDialog) {

	}
	
	public void onDestroyData(){
	
		
	}
	
	public void showMenu(int resMenuId, int resId, PopupMenu.OnMenuItemClickListener mOnClick) {
		View mView = findViewById(resMenuId);
		PopupMenu popup = new PopupMenu(this, mView);
		try {
			Field[] fields = popup.getClass().getDeclaredFields();
			for (Field field : fields) {
				if ("mPopup".equals(field.getName())) {
					field.setAccessible(true);
					Object menuPopupHelper = field.get(popup);
					Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
					Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
					setForceIcons.invoke(menuPopupHelper, true);
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(resId, popup.getMenu());
		popup.setOnMenuItemClickListener(mOnClick);
		popup.show();
	}
	
	  @Override
	    protected void onStop()
	        {
	            // TODO Auto-generated method stub
	            super.onStop();
	        }
	  
	  @Override
	    protected void onResume() {
	      super.onResume();
	   }

	    @Override
	    protected void onPause() {
	      super.onPause();
	    }
	    
	    
	    @Override
	    protected void onStart(){
	   	 super.onStart();
	   	 

     			
	    
	   }
	    
	   

}
