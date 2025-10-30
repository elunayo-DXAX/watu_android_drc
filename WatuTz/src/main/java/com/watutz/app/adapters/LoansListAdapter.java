package com.watutz.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.watutz.app.GeneralCommands;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.mobileservices.GetData;
import com.watutz.app.objects.AssetObject;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.util.FirebaseConfig;
import com.watutz.app.util.ImageUtil;
import com.watutz.app.view.MaterialRippleLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class LoansListAdapter extends RecyclerView.Adapter<LoansListAdapter.ViewHolder> implements IWhereMyLocationConstants, Filterable{

	private ArrayList<AssetObject> mItems = new ArrayList<AssetObject>();
	private ArrayList<AssetObject> mOriginalItems= new ArrayList<AssetObject>();

	private static Context mContext;
	static StoreAndGetFromPrefs storageObject;
	LayoutInflater vi;

	static Activity parentActivity;

	static RelativeLayout loadingView;
	private Handler handler = new Handler();

	LinearLayoutCompat branchView;

	//How To Pay Unlock
	CarouselView previewImagesUnlockView;
	ViewListener viewListenerUnlock;
	String carousel_view_unlock="";
	int[] previewImagesUnlock = {R.drawable.t1,R.drawable.t2,R.drawable.t3,R.drawable.t4,R.drawable.t5};
	boolean has_how_to_pay_tooltip=true;


	//How to Pay ToolTIp
	RelativeLayout payTipView;
	ViewListener viewListenerPay;
	CarouselView previewImagesPayView;
	String carousel_view_pay="";

	int[] previewImagesPay = {R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5};

	boolean has_how_to_temporary_unlock_tooltip=true;

	FirebaseConfig remoteConfig;
	FirebaseRemoteConfig mFirebaseConfig;

	String current_ad_card="";



	int default_timeout=8;



	boolean accessibility_has_pay_icon=true;
	//Accesibility
	ImageView payIcon;



	public LoansListAdapter(Activity activity, ArrayList<AssetObject> list, RelativeLayout loadingView) {
		parentActivity = activity;
		mContext = parentActivity.getApplicationContext();

		this.loadingView=loadingView;

		mItems = list;
		mOriginalItems = list;

		storageObject = new StoreAndGetFromPrefs(mContext);
		Locale locale = new Locale(storageObject.retrieveLanguageFromPrefs()); // or "sw" for Swahili
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		mContext.getResources().updateConfiguration(config, parentActivity.getBaseContext().getResources().getDisplayMetrics());



	}

	public void clearAll() {
		handler.removeCallbacksAndMessages(null);
	}
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loan, parent, false);
		return new ViewHolder(view);
	}



	@Override
	public void onBindViewHolder(ViewHolder v, int position) {
		v.bindData(position, mItems);


	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}



	class ViewHolder extends RecyclerView.ViewHolder {

		FirmaTextView loanCategory,loanType,deviceActiveH;
		ImageView loanIcon,iconLock,unlockIcon;
		SeekBar seekBar;
		private CountDownTimer timerUnlock, timerPayment;

		RelativeLayout activeUntilView,btnPay,btnUnlock,temporaryUnlockView,unlockLimitView;

		FirmaTextView accountStatus,unlockText,loanIssueDateText,loanBalanceTitle,loanModel,loanMaturityDateText,progressStatus,loanCurrency,loanBalance, loanTotalPayment,loanAmountOverdue,loanDailyAmount,loanWeeklyAmount,loanMonthlyAmount;



		FirmaTextView deviceActiveDate ;
		FirmaTextView deviceActiveCount ;
		FirmaTextView issueDateH ;

		FirmaTextView maturityDateH,activeBranchText;
		RelativeLayout btnRefresh;

		LinearLayout indicatorView;


		int position;

		//How To Pay
		MaterialRippleLayout payToolTipRips;

		RelativeLayout btnPayTip;

		MaterialRippleLayout unlockToolTipRips;
		RelativeLayout btnUnlockTip;

		public ViewHolder(@NonNull View v) {
			super(v);
			// inflate the layout
			vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// well set up the ViewHolder
			progressStatus = (FirmaTextView) v.findViewById(R.id.progressStatus);
//
//
			loanCategory = (FirmaTextView) v.findViewById(R.id.loanCategory);
			loanType = (FirmaTextView) v.findViewById(R.id.loanType);
			loanIcon = (ImageView) v.findViewById(R.id.loanIcon);
			iconLock = (ImageView) v.findViewById(R.id.iconLock);

			deviceActiveH = (FirmaTextView) v.findViewById(R.id.deviceActiveH);
			activeUntilView = (RelativeLayout) v.findViewById(R.id.activeUntilView);

			deviceActiveDate = (FirmaTextView) v.findViewById(R.id.deviceActiveDate);
			deviceActiveCount = (FirmaTextView) v.findViewById(R.id.deviceActiveCount);
			issueDateH = (FirmaTextView)  v.findViewById(R.id.issueDateH);

			maturityDateH = (FirmaTextView)  v.findViewById(R.id.maturityDateH);
			loanMaturityDateText = (FirmaTextView)  v.findViewById(R.id.loanMaturityDateText);


			//Pay ToolTips
			payTipView = (RelativeLayout) v.findViewById(R.id.payTipView);


			unlockToolTipRips = (MaterialRippleLayout) v.findViewById(R.id.unlockToolTipRips);
			payToolTipRips = (MaterialRippleLayout) v.findViewById(R.id.payToolTipRips);


			btnPayTip = (RelativeLayout) v.findViewById(R.id.btnPayTip);
			btnUnlockTip = (RelativeLayout) v.findViewById(R.id.btnUnlockTip);


			indicatorView= v.findViewById(R.id.indicatorView);


			seekBar = (SeekBar) v.findViewById(R.id.seekBar);

			accountStatus = (FirmaTextView) v.findViewById(R.id.accountStatus);
			loanIssueDateText = (FirmaTextView) v.findViewById(R.id.loanIssueDateText);
			loanMaturityDateText = (FirmaTextView) v.findViewById(R.id.loanMaturityDateText);
			loanBalanceTitle = (FirmaTextView) v.findViewById(R.id.loanBalanceTitle);

			branchView= v.findViewById(R.id.branchView);

			//Pay Icon
			payIcon = (ImageView) v.findViewById(R.id.payIcon);


			loanCurrency = (FirmaTextView) v.findViewById(R.id.loanCurrency);
			loanBalance = (FirmaTextView) v.findViewById(R.id.loanBalance);
			btnPay = (RelativeLayout) v.findViewById(R.id.btnPay);

			temporaryUnlockView = (RelativeLayout) v.findViewById(R.id.temporaryUnlockView);

			btnRefresh= v.findViewById(R.id.btnRefresh);

			loanModel = (FirmaTextView) v.findViewById(R.id.loanModel);

			activeBranchText = (FirmaTextView)  v.findViewById(R.id.activeBranchText);


			loanType = (FirmaTextView) v.findViewById(R.id.loanType);

			loanTotalPayment = (FirmaTextView) v.findViewById(R.id.loanTotalPayment);
			loanAmountOverdue = (FirmaTextView) v.findViewById(R.id.loanAmountOverdue);
			loanDailyAmount = (FirmaTextView) v.findViewById(R.id.loanDailyAmount);
			loanWeeklyAmount = (FirmaTextView) v.findViewById(R.id.loanWeeklyAmount);
			loanMonthlyAmount = (FirmaTextView) v.findViewById(R.id.loanMonthlyAmount);

			btnUnlock = (RelativeLayout) v.findViewById(R.id.btnUnlock);
			unlockLimitView = (RelativeLayout) v.findViewById(R.id.unlockLimitView);

			unlockIcon = (ImageView) v.findViewById(R.id.unlockIcon);
			unlockText = (FirmaTextView) v.findViewById(R.id.unlockText);

//			countdownRunnable = new CountdownRunnable(handler,unlockText, storageObject.retrieveUserTempActiveTillFromPrefs());



		}


		public void bindData(int position, ArrayList<AssetObject> mItems) {
			// Bind your data to the views
			// ...

//				handler.removeCallbacks(countdownRunnable);
//				handler.postDelayed(countdownRunnable, 100);


			AssetObject loan= mItems.get(position);
			//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // You can customize the format
			SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy"); // You can customize the format
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm"); // You can customize the format



			String category= loan.getLoanProductName();
			String type= ""+loan.getAssetTypeId();
			String currency=loan.getCurrencyCode().equals("KES")?"TZS. ":loan.getCurrencyCode();
			int num1 = Integer.parseInt(loan.getRemainingBalance());
			int num2 =  Integer.parseInt(loan.getTotalRepayment());

			String activeBranch=loan.getBranchName();

//				int amountToUnlock = Integer.parseInt(""+amountToUnlock);

			String amount= GeneralCommands.fixCommas(""+(num2 -num1));
			int totalPaid=num2 -num1;
			String outstandingBalance=GeneralCommands.fixCommas(""+(num2-totalPaid));


			float progress = num2 > 0 ? ((float)(num2 - num1) / num2) * 100 : 0;
			String dateAsOf=mContext.getResources().getString(R.string.asof)+" "+ GeneralCommands.getCurrentDateTime();
			String lType=loan.getAssetTypeName();
			String activeUntilDate= sdf.format(loan.getDeviceActiveTill());
			String activeUntilTime= sdfTime.format(loan.getDeviceActiveTill());

			String nextPaymentDate= sdf.format(loan.getNextPaymentDueDate());


			String issueDate= sdf.format(loan.getDisbursementDate());
			String maturityDate= sdf.format(loan.getMaturityDate());
			String status= loan.getLoanStatus();
			String balance= GeneralCommands.fixCommas(loan.getRemainingBalance());

			String overdueAmount= GeneralCommands.fixCommas(loan.getOverdueAmount());
			String dailyAmount= GeneralCommands.fixCommas(loan.getDailyPayment());
			String weeklyAmount= GeneralCommands.fixCommas(loan.getWeeklyPayment());
			String monthlyAmount= GeneralCommands.fixCommas(loan.getMonthlyPayment());

			long targetTimestamp =loan.getDeviceActiveTill(); // The timestamp you want to count down to
			long currentTime = System.currentTimeMillis();
			long timeRemaining = targetTimestamp - currentTime;


			long targetTimestampPayment =loan.getNextPaymentDueDate(); // The timestamp you want to count down to
			long currentTimePayment = System.currentTimeMillis();
			long timeRemainingPayment = targetTimestampPayment - currentTimePayment;



			String model= loan.getVehicleMakeName() + " | "+loan.getVehicleModelName();
			model= model.toLowerCase().contains("null") ?"" :model;


			loanCategory.setText(lType);
			loanType.setText(lType);

			seekBar.setProgress((int) progress);
			seekBar.setEnabled(false);

			accountStatus.setText(mContext.getResources().getString(R.string.accountstatus)+" "+dateAsOf);
			loanIssueDateText.setText(issueDate);
			loanMaturityDateText.setText(maturityDate);
			loanCurrency.setText(currency);
			loanBalance.setText(balance);

			loanTotalPayment.setText(currency+" "+ outstandingBalance);
			loanAmountOverdue.setText(currency+" "+overdueAmount);

			loanDailyAmount.setText(currency+" "+dailyAmount);
			loanWeeklyAmount.setText(currency+" "+weeklyAmount);
			loanMonthlyAmount.setText(currency+" "+monthlyAmount);


			loanModel.setText(model);


			if(activeBranch == null || activeBranch.equals("") )
			{
				branchView.setVisibility(View.GONE);

			}
			else {
				branchView.setVisibility(View.VISIBLE);
				activeBranchText.setText(activeBranch);
			}


			deviceActiveDate.setText(nextPaymentDate);


			if(type.equals("4"))  //SmartPhone
			{
				//Show Temporary Unlock View
				//Hide Temp Unlock if Config is Zero
				if(storageObject.retrieveTempUnlockConfigFromPrefs() ==0)//Smart Phone
				{
					temporaryUnlockView.setVisibility(View.GONE);
				}
				else
				{
					temporaryUnlockView.setVisibility(View.VISIBLE);
				}

				issueDateH.setText(mContext.getResources().getString(R.string.deviceacquired));
				maturityDateH.setVisibility(View.GONE);
				loanMaturityDateText.setVisibility(View.GONE);

				//Remove Seekbar
				progressStatus.setVisibility(View.GONE);
				seekBar.setVisibility(View.GONE);



			}
			else
			{


				//Return Seekbar
				progressStatus.setVisibility(View.VISIBLE);
				seekBar.setVisibility(View.VISIBLE);


				//Not a PHone SO Hide Temporary Unlock View
				temporaryUnlockView.setVisibility(View.GONE);
				maturityDateH.setVisibility(View.VISIBLE);
				issueDateH.setText(mContext.getResources().getString(R.string.issuedateh));

				loanMaturityDateText.setVisibility(View.VISIBLE);

			}

			if(type.equals("4"))  //SmartPhone
			{
				deviceActiveDate.setVisibility(View.GONE);
				deviceActiveCount.setVisibility(View.VISIBLE);

				if(timeRemaining>0)
				{
					//Not Yet Overdue/Is NOT Locked
					storageObject.setIsPhoneLocked(false);
					temporaryUnlockView.setVisibility(View.GONE);
					loanBalance.setText(""+dailyAmount);
					loanBalanceTitle.setText(mContext.getString(R.string.dailyamount));

					if(storageObject.showPayTooltip())
					{
						//Not Locked
						payToolTipRips.setVisibility(View.VISIBLE);
						unlockToolTipRips.setVisibility(View.GONE);
					}

					btnPay.setBackgroundResource(R.xml.rounded_list_button_green);


					if(timerPayment!=null) timerPayment.cancel();
					timerPayment= new CountDownTimer(timeRemaining, 1000) {
						public void onTick(long millisUntilFinished) {
							// Calculate hours, minutes, and seconds
							long days = millisUntilFinished / 3600000/24;
							long hours = millisUntilFinished / 3600000;
							long minutes = (millisUntilFinished % 3600000) / 60000;
							long seconds = (millisUntilFinished % 60000) / 1000;

							// Update the TextViews
							String hrs= String.format("%02d", hours);
							String mns = String.format("%02d", minutes);
							String scs =String.format("%02d", seconds);
							if(hours>24)
							{
								hours= hours % 24;
								hrs= String.format("%02d", hours);


							}

//								deviceActiveCount.setText(days+ "d "+ hrs+"h "+mns+"m "+scs+"s ");
							deviceActiveCount.setText(activeUntilDate+ " "+ activeUntilTime);




							if(days < 0)
							{
//								Toast.makeText(mContext, "Less"+days, Toast.LENGTH_SHORT).show();

								//Smart Phone
								activeUntilView.setBackgroundResource(R.xml.rounded_list_button_orange_stroke);
								iconLock.setColorFilter(mContext.getResources().getColor(R.color.theme_orange));
								deviceActiveCount.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
								deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
								deviceActiveCount.setTextColor(mContext.getResources().getColor(R.color.theme_orange));


							}
							else {

//								Toast.makeText(mContext, "More"+days, Toast.LENGTH_SHORT).show();

								activeUntilView.setBackgroundResource(R.xml.rounded_list_button_green_stroke);
								iconLock.setColorFilter(mContext.getResources().getColor(R.color.theme_green));
								deviceActiveCount.setTextColor(mContext.getResources().getColor(R.color.theme_green));
								deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_green));

							}

						}

						public void onFinish() {
							// Handle countdown completion, if needed
						}
					}.start();




				}
				else
				{
					//Is Overdue/Locked
					storageObject.setIsPhoneLocked(true);
					deviceActiveH.setText(mContext.getResources().getString(R.string.devicelockedon));
					deviceActiveCount.setText(activeUntilDate+ " "+ activeUntilTime);
					activeUntilView.setBackgroundResource(R.xml.rounded_list_button_orange_stroke);
					iconLock.setColorFilter(mContext.getResources().getColor(R.color.theme_orange));
					deviceActiveCount.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
					deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_orange));


					if(storageObject.showTemporaryUnlockTooltip())
					{
						//Locked
						payToolTipRips.setVisibility(View.GONE);
						unlockToolTipRips.setVisibility(View.VISIBLE);
					}

					btnPay.setBackgroundResource(R.xml.rounded_list_button_orange);

					if(storageObject.retrieveTempUnlockConfigFromPrefs() ==0)//Smart Phone
					{
						temporaryUnlockView.setVisibility(View.GONE);
					}
					else
					{
						temporaryUnlockView.setVisibility(View.VISIBLE);
					}

					loanBalanceTitle.setText(mContext.getString(R.string.amountduetounlock));
					loanBalance.setText(""+dailyAmount);

				}


			}
			else
			{

				if(timeRemaining < 0 )
				{
					//Not a Phone and Overdue
					activeUntilView.setBackgroundResource(R.xml.rounded_list_button_orange_stroke);
					deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
					loanBalance.setText(""+overdueAmount);
					loanBalanceTitle.setText(mContext.getString(R.string.amountoverdue));

				}
				else {

					//Not a phone and not  Overdue
					activeUntilView.setBackgroundResource(R.xml.rounded_list_button_green_stroke);
					deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_green));


				}
				deviceActiveDate.setVisibility(View.VISIBLE);
				deviceActiveCount.setVisibility(View.GONE);

			}

			//Till There
			if(type.equals("4"))//SmartPhone
			{

				activeUntilView.setVisibility(View.VISIBLE);
				loanIcon.setImageDrawable(mContext.getDrawable(R.drawable.ics_simu));
				iconLock.setImageDrawable(mContext.getDrawable(R.drawable.ics_lock));

			}
			else if(type.equals("2"))//TukTuk
			{

				activeUntilView.setVisibility(View.VISIBLE);
				loanIcon.setImageDrawable(mContext.getDrawable(R.drawable.ics_tuk_tuk));
				deviceActiveH.setText(mContext.getString(R.string.nextpaymentdate));
				iconLock.setImageDrawable(mContext.getDrawable(R.drawable.ics_emergency));
				loanBalanceTitle.setText(mContext.getString(R.string.loanbalance));


				loanType.setTextColor(mContext.getResources().getColor(R.color.theme_green));
				loanBalance.setTextColor(mContext.getResources().getColor(R.color.theme_green));
				loanCurrency.setTextColor(mContext.getResources().getColor(R.color.theme_green));
				btnPay.setBackgroundResource(R.xml.rounded_list_button_green);


			}

			else if(type.equals("1"))//Boda
			{
				activeUntilView.setVisibility(View.VISIBLE);
				loanIcon.setImageDrawable(mContext.getDrawable(R.drawable.ics_boda));
				deviceActiveH.setText(mContext.getString(R.string.nextpaymentdate));
				iconLock.setImageDrawable(mContext.getDrawable(R.drawable.ics_emergency));



				loanType.setTextColor(mContext.getResources().getColor(R.color.theme_color));
				loanBalance.setTextColor(mContext.getResources().getColor(R.color.theme_color));
				loanCurrency.setTextColor(mContext.getResources().getColor(R.color.theme_color));
				btnPay.setBackgroundResource(R.xml.rounded_list_button);

			}
			else if(type.equals("3"))//Gari
			{
				activeUntilView.setVisibility(View.GONE);
				loanIcon.setImageDrawable(mContext.getDrawable(R.drawable.ics_gari));

				loanType.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
				loanBalance.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
				loanCurrency.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
				btnPay.setBackgroundResource(R.xml.rounded_list_button_orange);


			}


			btnPay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					storageObject.saveCurrentAssetObjectToPrefs(mItems.get(position));


					JSONObject obj = new JSONObject();

					try {


						obj.put("assetLoanNumber",mItems.get(position).getLoanNumber());
						obj.put("assetName",mItems.get(position).getAssetTypeName());
						obj.put("assetTypeId",mItems.get(position).getAssetTypeId());


					} catch (JSONException e) {
						e.printStackTrace();
					}

					GeneralCommands.logEventObject(parentActivity,"click_pay_loan_asset_","click_pay_loan_asset",obj);

				}
			});


			btnUnlock.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {


					JSONObject obj = new JSONObject();

					try {
						obj.put("loanNumber",mItems.get(position).getLoanNumber());

					} catch (JSONException e) {
						e.printStackTrace();
					}

					GeneralCommands.logEventObject(parentActivity,"click_temporary_unlock","click_temporary_unlock",obj);



					GetData ob1 = new GetData();
					ob1.initialize(parentActivity,loadingView);

					ob1.userTemporaryUnlock(mItems.get(position).getLoanNumber(),false);

					if(storageObject.retrieveTemporaryUnlockCountFromPrefs() > storageObject.retrieveTempUnlockConfigFromPrefs())
					{
						//Exceeded the unlock attempts so reste the attempts count back to 0
						storageObject.saveTemporaryUnlockCountToPrefs(0);

					}


					ob1.setTemporaryUnlockListener(new GetData.TemporaryUnlockListener() {
						@Override
						public void onObjectReady(String title) {

						}

						@Override
						public void onDataLoaded(long waitingTime) {

//						       Toast.makeText(mContext,"Time"+waitingTime,Toast.LENGTH_SHORT).show();
							setWaitingTime(timerUnlock,unlockLimitView,unlockText, unlockIcon, btnUnlock, waitingTime);

						}
					});



				}
			});




			btnRefresh.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {


					JSONObject obj = new JSONObject();

					try {
						obj.put("loanNumber",loan.getLoanNumber());

					} catch (JSONException e) {
						e.printStackTrace();
					}

					GeneralCommands.logEventObject(parentActivity,"click_refresh_loan","click_refresh_loan",obj);


					GetData ob1= new GetData();
					ob1.initializeSilent(parentActivity);
					ob1.userGetLoanDetails(loan.getLoanNumber(),true);
					ob1.setLoanDetailsListener(new GetData.LoanDetailsListener() {
						@Override
						public void onObjectReady(String title) {
						}

						@Override
						public void onDataLoaded(AssetObject data) {

							if(data!=null)
							{
								Toast.makeText(mContext,mContext.getResources().getString(R.string.loanassetupdated,""+lType),Toast.LENGTH_SHORT).show();
								mItems.set(position,data);
								notifyDataSetChanged();

							}
						}
					});
				}
			});



			if(mItems.size()>1)
			{
				indicatorView.setVisibility(View.VISIBLE);
				addIndicatorViews(indicatorView,position);
			}
			else {
				indicatorView.setVisibility(View.GONE);
			}

			// Create and start the countdown timer
			setWaitingTime(timerUnlock,unlockLimitView,unlockText, unlockIcon, btnUnlock, storageObject.retrieveUserTempActiveTillFromPrefs());

			//How to Pay Tooltip

			btnPayTip.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					openDialogPayCarousel();
				}
			});

			btnUnlockTip.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					openDialogUnlockCarousel();
				}
			});



			try
			{
				setConfigsPay();
				setCarouselDetailsPay();
				setConfigsUnlock();
				setCarouselDetailsUnlock();

			}catch(Exception e){}

			//EndView

			//pay_icon


			if(!storageObject.showPayIcon())
			{
				payIcon.setVisibility(View.GONE);

			}
			else {
				payIcon.setVisibility(View.VISIBLE);

			}

			//how_to_pay_tooltip

			if(type.equals("4"))  //SmartPhone
			{
				payTipView.setVisibility(View.VISIBLE);

			}
			else {
				payTipView.setVisibility(View.GONE);

			}







			//return v;



		}

		public void updateTimer(long millisUntilFinished) {
			// Update the UI with the remaining time
			// ...
		}

		public void cancelTimer() {
			if (timerUnlock != null) {
				timerUnlock.cancel();
			}

			if (timerPayment != null) {
				timerPayment.cancel();
			}
		}





	}


	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {

				mItems = (ArrayList<AssetObject>) results.values; // has the filtered values
				notifyDataSetChanged();  // notifies the data with new filtered values
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
				ArrayList<AssetObject> FilteredArrList = new ArrayList<AssetObject>();

				if (mOriginalItems == null) {
					mOriginalItems = new ArrayList<AssetObject>(mItems); // saves the assetObjectal data in mAssetObjectalValues
				}

				/********
				 *
				 *  If constraint(CharSequence that is received) is null returns the mAssetObjectalValues(AssetObjectal) values
				 *  else does the Filtering and returns FilteredArrList(Filtered)
				 *
				 ********/
				if (constraint == null || constraint.length() == 0) {

					// set the AssetObjectal result to return
					results.count = mOriginalItems.size();
					results.values = mOriginalItems;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < mOriginalItems.size(); i++) {
						String data =""+ mOriginalItems.get(i).getLoanProductName();
						if (data.toLowerCase().contains(constraint.toString())) {

							AssetObject item = mOriginalItems.get(i);
							FilteredArrList.add(item);
						}
					}
					// set the Filtered result to return
					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				return results;
			}
		};
		return filter;
	}

	public void deselectAllItems(ArrayList<AssetObject> mItems)
	{
		for(int i=0;i<mItems.size();i++)
		{
			AssetObject assetObject= (AssetObject) mItems.get(i);
			assetObject.setSelected(false);
		}

		notifyDataSetChanged();
	}


	public void addIndicatorViews(LinearLayout view,int position)
	{
		view.removeAllViews();
		for(int i=0;i<mItems.size();i++){
			View child = vi.inflate(R.layout.list_item_indicator, null);
			RelativeLayout iconCircle= child.findViewById(R.id.iconCircle);

			if(i== position)
			{
				iconCircle.setBackgroundResource(R.xml.rounded_circle_theme_small);
			}
			else {

				iconCircle.setBackgroundResource(R.xml.rounded_circle_stroke_theme);

			}

			view.addView(child);
		}

	}

	public static void setWaitingTime(CountDownTimer timerUnlock, RelativeLayout unlockLimitView,FirmaTextView unlockText,ImageView unlockIcon, RelativeLayout btnUnlock, long waitingTime)
	{
		if(waitingTime < 0)
		{
			Toast.makeText(mContext, mContext.getResources().getString(R.string.maxunlockattempts), Toast.LENGTH_SHORT).show();
			//Max Attempts Reached
			unlockLimitView.setVisibility(View.VISIBLE);

			unlockText.setText(mContext.getResources().getString(R.string.temporaryunlock));
			unlockIcon.setImageResource(R.drawable.ics_unlock_locked);
			btnUnlock.setBackgroundResource(R.xml.rounded_list_button_orange);

			//Reset Active Till
			storageObject.saveTempActiveTillToPrefs(0);

			btnUnlock.setEnabled(true);


		}
		else if(waitingTime>0)
		{
			unlockLimitView.setVisibility(View.GONE);
			btnUnlock.setEnabled(false);


			long currentTimePayment = System.currentTimeMillis();
			long timeRemainingPayment = waitingTime - currentTimePayment;

			if(timeRemainingPayment<0)
			{
				//Already Unlcoked
				unlockText.setText(mContext.getResources().getString(R.string.devicealreadyunlocked));
				unlockIcon.setImageResource(R.drawable.ics_unlock_timer);
				btnUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);

				storageObject.saveTempActiveTillToPrefs(0);


			}
			else
			{


				if(timerUnlock!=null) timerUnlock.cancel();
				//There is time
				timerUnlock= new CountDownTimer(timeRemainingPayment, 1000) {
					public void onTick(long millisUntilFinished) {
						// Calculate hours, minutes, and seconds
						long days = millisUntilFinished / 3600000/24;
						long hours = millisUntilFinished / 3600000;
						long minutes = (millisUntilFinished % 3600000) / 60000;
						long seconds = (millisUntilFinished % 60000) / 1000;

						// Update the TextViews
						String hrs= String.format("%02d", hours);
						String mns = String.format("%02d", minutes);
						String scs =String.format("%02d", seconds);
						if(hours>24)
						{
							hours= hours % 24;
							hrs= String.format("%02d", hours);


						}

						unlockText.setText(mContext.getResources().getString(R.string.temporarilyunlockedfor)+"\n"+ days+ ":"+ hrs+":"+mns+":"+scs+" "+mContext.getResources().getString(R.string.minutesremaining));


					}

					public void onFinish()
					{
						// Handle countdown completion, if needed
						unlockText.setText(mContext.getResources().getString(R.string.temporaryunlock));
						unlockIcon.setImageResource(R.drawable.ics_unlock);
						btnUnlock.setBackgroundResource(R.xml.rounded_list_button_orange_light);

						storageObject.saveTempActiveTillToPrefs(0);
						btnUnlock.setEnabled(true);



					}
				}.start();


				unlockIcon.setImageResource(R.drawable.ics_unlock_timer);
				btnUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);


			}



		}


	}


	//HOw To Pay

	public void openDialogUnlockCarousel()
	{
		View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_pay_carousel, null);


		final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
				.setContentHolder(new com.orhanobut.dialogplus.ViewHolder(contentView))
				.setGravity(DialogPlus.Gravity.CENTER)
				.setBackgroundColorResourceId(R.color.black)
				.setCancelable(true)
				.create();


		//Content View
		View content = dialogPlus.getHolderView();

		RelativeLayout loadingView= (RelativeLayout) content.findViewById(R.id.loadingItems);

		RelativeLayout btnBack= (RelativeLayout) content.findViewById(R.id.btnBack);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialogPlus.dismiss();
			}
		});




		previewImagesUnlockView = (CarouselView) contentView.findViewById(R.id.previewImagesView);


		try
		{
			setConfigsUnlock();
			setCarouselDetailsUnlock();

		}catch(Exception e){}




		dialogPlus.show();
	}

	public void openDialogPayCarousel()
	{
		View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_pay_carousel, null);


		final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
				.setContentHolder(new com.orhanobut.dialogplus.ViewHolder(contentView))
				.setGravity(DialogPlus.Gravity.CENTER)
				.setBackgroundColorResourceId(R.color.black)
				.setCancelable(true)
				.create();


		//Content View
		View content = dialogPlus.getHolderView();

		RelativeLayout loadingView= (RelativeLayout) content.findViewById(R.id.loadingItems);

		RelativeLayout btnBack= (RelativeLayout) content.findViewById(R.id.btnBack);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialogPlus.dismiss();
			}
		});




		previewImagesPayView = (CarouselView) contentView.findViewById(R.id.previewImagesView);


		try
		{
			setConfigsPay();
			setCarouselDetailsPay();

		}catch(Exception e){}




		dialogPlus.show();
	}

	public void setConfigsUnlock()
	{
		//Remote Configs
		remoteConfig= new FirebaseConfig();
		mFirebaseConfig=remoteConfig.getConfigs();
		mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
			@Override
			public void onComplete(@NonNull Task<Boolean> task) {
				if (task.isSuccessful())
				{
					boolean updated = task.getResult();

//					Toast.makeText(mContext, "Fetch and activate succeeded"+ has_how_to_temporary_unlock_tooltip,Toast.LENGTH_SHORT).show();

					carousel_view_unlock = mFirebaseConfig.getString("how_to_temporary_unlock_tooltip_carousel");
					has_how_to_temporary_unlock_tooltip = mFirebaseConfig.getBoolean("has_how_to_temporary_unlock_tooltip");
					accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");


					storageObject.setTemporaryUnlockTooltip(has_how_to_temporary_unlock_tooltip);
					storageObject.setPayIcon(accessibility_has_pay_icon);

					if(carousel_view_unlock!=null && !carousel_view_unlock.equals("") ) {
						try {
							JSONObject obj = new JSONObject(carousel_view_unlock);

							Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
							refreshCarouselDetailsUnlock(obj);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}







				}
				else {
					Log.d("ZeFetchFailed","ZeFetchFailed");
				}

			}
		});

		mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
			@Override
			public void onUpdate(ConfigUpdate configUpdate) {
				//Log.d("ZeConfigs", "Updated keys: " + configUpdate.getUpdatedKeys());

				mFirebaseConfig.activate().addOnCompleteListener(new OnCompleteListener() {
					@Override
					public void onComplete(@NonNull Task task) {
						carousel_view_unlock = mFirebaseConfig.getString("how_to_temporary_unlock_tooltip_carousel");
						has_how_to_temporary_unlock_tooltip = mFirebaseConfig.getBoolean("has_how_to_temporary_unlock_tooltip");
						accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");




						storageObject.setTemporaryUnlockTooltip(has_how_to_temporary_unlock_tooltip);
						storageObject.setPayIcon(accessibility_has_pay_icon);

						if(carousel_view_unlock!=null && !carousel_view_unlock.equals("") ) {
							try {
								JSONObject obj = new JSONObject(carousel_view_unlock);
								Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
								refreshCarouselDetailsUnlock(obj);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

					}
				});
			}

			@Override
			public void onError(FirebaseRemoteConfigException error) {
				Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
			}
		});
	}

	public void setCarouselDetailsUnlock()
	{

		viewListenerUnlock = new ViewListener() {
			@Override
			public View setViewForPosition(int position) {
				View customView = vi.inflate(R.layout.carousel_view_custom, null);
				//set view attributes here

				ImageView imageView = customView.findViewById(R.id.imageView);
				if(previewImagesUnlock!=null && previewImagesUnlock.length > position) imageView.setImageResource(previewImagesUnlock[position]);

				FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
				FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);

				mainMsg.setVisibility(View.GONE);
				subMsg.setVisibility(View.GONE);


				return customView;
			}
		};

		previewImagesUnlockView.setPageCount(previewImagesUnlock.length);
		previewImagesUnlockView.setViewListener(viewListenerUnlock);



	}

	public void refreshCarouselDetailsUnlock(JSONObject jsonObject) throws JSONException {
		JSONArray tanzaniaArray =new JSONArray();
		JSONArray dataArray =new JSONArray();

		// Check if the object contains "tanzania" data
		if (jsonObject.has("tanzania")) {
			// Get the "tanzania" array


			JSONObject newJsonObject= jsonObject.getJSONObject("tanzania");
			if(newJsonObject.has("sw") && storageObject.retrieveLanguageFromPrefs().equals("sw"))
			{
				tanzaniaArray= newJsonObject.getJSONArray("sw");


			}

			else
			{
				tanzaniaArray= newJsonObject.getJSONArray("en");
			}

		}




		dataArray=tanzaniaArray;

//		Log.d("ZeArrayzFin","ZeArrayzFIn"+storageObject.retrieveLanguageFromPrefs()+":"+dataArray.toString());

		if(dataArray.length() > 0 && previewImagesUnlockView!=null )
		{
			previewImagesUnlockView.setSlideInterval(default_timeout*1000); //Set Slide Interval
			previewImagesUnlockView.setPageCount(dataArray.length());

			JSONArray finalDataArray = dataArray;
			viewListenerUnlock = new ViewListener() {
				@Override
				public View setViewForPosition(int position) {
					View customView = vi.inflate(R.layout.carousel_view_custom, null);
					//set view attributes here

					ImageView imageView = customView.findViewById(R.id.imageView);

					FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
					FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);


					try {
						JSONObject tanzaniaObject = finalDataArray.getJSONObject(position);

						// Extract individual items from the "" object
						String title = tanzaniaObject.getString("title");
						String message = tanzaniaObject.getString("message");
						String image = tanzaniaObject.getString("image");
						String timeout_seconds = tanzaniaObject.getString("timeout_seconds");

						mainMsg.setVisibility(View.GONE);
						subMsg.setVisibility(View.GONE);

						if(timeout_seconds!=null && !timeout_seconds.equals("") )
						{

							try
							{
								int seconds= Integer.parseInt(timeout_seconds);
								if(seconds > 0)
								{
									default_timeout=seconds;
									previewImagesUnlockView.setSlideInterval(default_timeout*1000);
								}

							}catch(Exception e)
							{
								e.printStackTrace();
							}

						}

						// Do something with the extracted title and message
						ImageUtil.displayLessRoundImage(imageView, image, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String s, View view) {

							}

							@Override
							public void onLoadingFailed(String s, View view, FailReason failReason) {
								if(previewImagesUnlock !=null && previewImagesUnlock.length >
										position && imageView!=null )imageView.setImageResource(previewImagesUnlock[position]);

							}

							@Override
							public void onLoadingComplete(String s, View view, Bitmap bitmap) {

							}

							@Override
							public void onLoadingCancelled(String s, View view) {
								if(previewImagesUnlock !=null && previewImagesUnlock.length > position && imageView!=null )imageView.setImageResource(previewImagesUnlock[position]);
							}
						});



					} catch (JSONException e) {
						e.printStackTrace();
					}


					return customView;
				}
			};

			previewImagesUnlockView.setPageCount(dataArray.length());
			previewImagesUnlockView.setViewListener(viewListenerUnlock);

		}




	}


	//Pay
	public void setConfigsPay()
	{
		//Remote Configs
		remoteConfig= new FirebaseConfig();
		mFirebaseConfig=remoteConfig.getConfigs();
		mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
			@Override
			public void onComplete(@NonNull Task<Boolean> task) {
				if (task.isSuccessful())
				{
					boolean updated = task.getResult();

//					Toast.makeText(mContext, "Fetch and activate succeeded"+ has_how_to_temporary_unlock_tooltip,Toast.LENGTH_SHORT).show();

					carousel_view_pay = mFirebaseConfig.getString("how_to_pay_tooltip_carousel");
					has_how_to_pay_tooltip = mFirebaseConfig.getBoolean("has_how_to_pay_tooltip");
					accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");


					storageObject.setPayTooltip(has_how_to_pay_tooltip);
					storageObject.setPayIcon(accessibility_has_pay_icon);

					if(carousel_view_pay!=null && !carousel_view_pay.equals("") ) {
						try {
							JSONObject obj = new JSONObject(carousel_view_pay);

							Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
							refreshCarouselDetailsPay(obj);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}







				}
				else {
					Log.d("ZeFetchFailed","ZeFetchFailed");
				}

			}
		});

		mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
			@Override
			public void onUpdate(ConfigUpdate configUpdate) {
				//Log.d("ZeConfigs", "Updated keys: " + configUpdate.getUpdatedKeys());

				mFirebaseConfig.activate().addOnCompleteListener(new OnCompleteListener() {
					@Override
					public void onComplete(@NonNull Task task) {
						carousel_view_pay = mFirebaseConfig.getString("how_to_pay_tooltip_carousel");
						has_how_to_pay_tooltip = mFirebaseConfig.getBoolean("has_how_to_pay_tooltip");
						accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");




						storageObject.setPayTooltip(has_how_to_pay_tooltip);
						storageObject.setPayIcon(accessibility_has_pay_icon);

						if(carousel_view_pay!=null && !carousel_view_pay.equals("") ) {
							try {
								JSONObject obj = new JSONObject(carousel_view_pay);
								Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
								refreshCarouselDetailsPay(obj);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

					}
				});
			}

			@Override
			public void onError(FirebaseRemoteConfigException error) {
				Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
			}
		});
	}

	public void setCarouselDetailsPay()
	{

		viewListenerPay = new ViewListener() {
			@Override
			public View setViewForPosition(int position) {
				View customView = vi.inflate(R.layout.carousel_view_custom, null);
				//set view attributes here

				ImageView imageView = customView.findViewById(R.id.imageView);
				if(previewImagesPay!=null && previewImagesPay.length > position) imageView.setImageResource(previewImagesPay[position]);

				FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
				FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);

				mainMsg.setVisibility(View.GONE);
				subMsg.setVisibility(View.GONE);


				return customView;
			}
		};

		previewImagesPayView.setPageCount(previewImagesPay.length);
		previewImagesPayView.setViewListener(viewListenerPay);



	}

	public void refreshCarouselDetailsPay(JSONObject jsonObject) throws JSONException {
		JSONArray tanzaniaArray =new JSONArray();
		JSONArray dataArray =new JSONArray();

		// Check if the object contains "tanzania" data
		if (jsonObject.has("tanzania")) {
			// Get the "tanzania" array


			JSONObject newJsonObject= jsonObject.getJSONObject("tanzania");
			if(newJsonObject.has("sw") && storageObject.retrieveLanguageFromPrefs().equals("sw"))
			{
				tanzaniaArray= newJsonObject.getJSONArray("sw");


			}

			else
			{
				tanzaniaArray= newJsonObject.getJSONArray("en");
			}

		}




		dataArray=tanzaniaArray;

//		Log.d("ZeArrayzFin","ZeArrayzFIn"+storageObject.retrieveLanguageFromPrefs()+":"+dataArray.toString());

		if(dataArray.length() > 0 && previewImagesPayView!=null )
		{
			previewImagesPayView.setSlideInterval(default_timeout*1000);//Set Slide Interval
			previewImagesPayView.setPageCount(dataArray.length());

			JSONArray finalDataArray = dataArray;
			viewListenerPay = new ViewListener() {
				@Override
				public View setViewForPosition(int position) {
					View customView = vi.inflate(R.layout.carousel_view_custom, null);
					//set view attributes here

					ImageView imageView = customView.findViewById(R.id.imageView);

					FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
					FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);


					try {
						JSONObject tanzaniaObject = finalDataArray.getJSONObject(position);

						// Extract individual items from the "" object
						String title = tanzaniaObject.getString("title");
						String message = tanzaniaObject.getString("message");
						String image = tanzaniaObject.getString("image");
						String timeout_seconds = tanzaniaObject.getString("timeout_seconds");

						mainMsg.setVisibility(View.GONE);
						subMsg.setVisibility(View.GONE);

						if(timeout_seconds!=null && !timeout_seconds.equals("") )
						{

							try
							{
								int seconds= Integer.parseInt(timeout_seconds);
								if(seconds > 0)
								{
									default_timeout=seconds;
									previewImagesPayView.setSlideInterval(default_timeout*1000);
								}

							}catch(Exception e)
							{
								e.printStackTrace();
							}

						}

						// Do something with the extracted title and message
						ImageUtil.displayLessRoundImage(imageView, image, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String s, View view) {

							}

							@Override
							public void onLoadingFailed(String s, View view, FailReason failReason) {
								if(previewImagesPayView !=null && previewImagesPay.length > position && imageView!=null )imageView.setImageResource(previewImagesPay[position]);

							}

							@Override
							public void onLoadingComplete(String s, View view, Bitmap bitmap) {

							}

							@Override
							public void onLoadingCancelled(String s, View view) {
								if(previewImagesPay !=null && previewImagesPay.length > position && imageView!=null )imageView.setImageResource(previewImagesPay[position]);
							}
						});



					} catch (JSONException e) {
						e.printStackTrace();
					}


					return customView;
				}
			};

			previewImagesPayView.setPageCount(dataArray.length());
			previewImagesPayView.setViewListener(viewListenerPay);

		}




	}




}