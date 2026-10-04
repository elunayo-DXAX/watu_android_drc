package com.watudrc.app.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.PaymentProcessesActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.view.MaterialRippleLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FragmentPaymentProcess extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
    String method;
    String amount;
    int numberOfDays;
    double days;
    String userName;
    String deviceName;
    String pagaNumber, ussdCode, paybill, loanNumber, company;

    String totalDays;
    FirmaTextView textUsername, textDevice, textPagaNumber, amountText, numberOfDaysText, textTransferMethod, textBankTransfer, textMobileTransfer, textThirdPartyTransfer, paymentInstructions, ftvDevice;

    ImageView iconImage, iconCopyPaga, iconDevice;

    int assetTypeId;

    MaterialRippleLayout backRips, bankPayDoneRips, bankPayCancelRips, mobilePayDoneRips, mobilePayCancelRips, thirdPartyPayDoneRips, thirdPartyPayCancelRips;

    RelativeLayout btnDone, mobileTransfer, thirdPartyTransfer;


    private static StoreAndGetFromPrefs storageObject;
    public static PaymentProcessesActivity parentActivity;
    static Context mContext;
    RelativeLayout iconView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_payment_process, container, false);
        parentActivity = (PaymentProcessesActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        return parentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = requireActivity().getIntent().getExtras();
        if (bundle != null) {
            method = bundle.getString("method");
            amount = bundle.getString("amount");
            days = bundle.getDouble("numberOfDays"); // 👈 fix type!
            userName = bundle.getString("user");
            deviceName = bundle.getString("device");
            assetTypeId = bundle.getInt("assetTypeId");
            pagaNumber = bundle.getString("pagaNumber");
            ussdCode = bundle.getString("ussdCode");
            paybill = bundle.getString("paybill");
            loanNumber = bundle.getString("loanNumber");
            company = bundle.getString("company");
        }

        days =BigDecimal.valueOf(days).setScale(2, RoundingMode.DOWN).doubleValue();

        numberOfDays = (int) days;

        if (numberOfDays > 1) {
            totalDays = numberOfDays + " "+getString(R.string.days);
        } else totalDays = numberOfDays + " "+getString(R.string.day);


        initViews();
        setViews();
        setUIListeners();
    }

    private void initViews() {

        backRips = parentView.findViewById(R.id.backRips);

        textUsername = parentView.findViewById(R.id.textUsername);
        textDevice = parentView.findViewById(R.id.textDevice);
        ftvDevice = parentView.findViewById(R.id.ftvDevice);
        iconDevice = parentView.findViewById(R.id.iconDevice);
        amountText = parentView.findViewById(R.id.textAmount);
        numberOfDaysText = parentView.findViewById(R.id.textDays);
        iconView = parentView.findViewById(R.id.iconView);

        iconImage = parentView.findViewById(R.id.iconImage);


        btnDone = parentView.findViewById(R.id.btnDone);
//        bankPayCancelRips = parentView.findViewById(R.id.bankPayCancelRips);

        paymentInstructions = parentView.findViewById(R.id.tv_instructions);

    }

    private void setDeviceIcon(int assetTypeId) {

        if (iconDevice == null || ftvDevice == null || mContext == null) {
            return;
        }

        if (assetTypeId == 4) {
            iconDevice.setImageResource(R.drawable.ics_simu);
            ftvDevice.setText(mContext.getResources().getString(R.string.device));
        } else ftvDevice.setText(mContext.getResources().getString(R.string.asset));
        if (assetTypeId == 1 || assetTypeId == 5) iconDevice.setImageResource(R.drawable.ics_boda);
        if (assetTypeId == 10) iconDevice.setImageResource(R.drawable.ics_icon_tv);
        if (assetTypeId == 2 || assetTypeId == 7 || assetTypeId == 9)
            iconDevice.setImageResource(R.drawable.ics_tuk_tuk);
    }

    private void setViews() {

        if (method.equals("mpesa")) {
            iconView.setBackgroundResource(R.xml.rounded_list_transparent_green_stroke);
            if (storageObject.retrieveLanguageFromPrefs().contains("fr")) {
                Glide.with(this)
                        .load(R.drawable.ics_pay_via_fr)
                        .into(iconImage);

            } else {
                Glide.with(this)
                        .load(R.drawable.ics_pay_via)
                        .into(iconImage);
            }

            paymentInstructions.setText(android.text.Html.fromHtml(getString(R.string.paympesa, ussdCode, paybill, loanNumber, company)));


        } else if (method.equals("afrimoney")) {
            iconView.setBackgroundResource(R.xml.rounded_list_transparent_purple_stroke);

            if (storageObject.retrieveLanguageFromPrefs().contains("fr")) {
                Glide.with(this)
                        .load(R.drawable.ics_icon_pay_afrimoney_fr)
                        .into(iconImage);

            } else {

                Glide.with(this)
                        .load(R.drawable.ics_icon_pay_afrimoney)
                        .into(iconImage);
            }

            paymentInstructions.setText(android.text.Html.fromHtml(getString(R.string.payafrimoney, ussdCode, paybill, loanNumber, company)));

        }

        textUsername.setText(userName);
        textDevice.setText(deviceName);
        amountText.setText(amount);
        numberOfDaysText.setText(totalDays);
        setDeviceIcon(assetTypeId);

//        textBankTransfer.setText(android.text.Html.fromHtml(getString(R.string.paybybanktransfer)));
//        textMobileTransfer.setText(android.text.Html.fromHtml(getString(R.string.paybymobiletransfer)));
//        textThirdPartyTransfer.setText(android.text.Html.fromHtml(getString(R.string.paybyathirdparty)));

    }

    private void setUIListeners() {

        backRips.setOnClickListener(v -> {
            goBack();
        });


        btnDone.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_pay_done", "click_pay_done");
            openHome();
        });

//        bankPayCancelRips.setOnClickListener(v -> {goBack();});
//
//        mobilePayDoneRips.setOnClickListener(v -> {openHome();});
//        mobilePayCancelRips.setOnClickListener(v -> {goBack();});
//
//        thirdPartyPayDoneRips.setOnClickListener(v -> {openHome();});
//        thirdPartyPayCancelRips.setOnClickListener(v -> {goBack();});

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {

                // TODO stop audio playback
            }
        }
    }

    public void goToMainPage() {
        Intent mIntent = new Intent(requireContext(), MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

//    private void openTransactions() {
//        Intent mIntent = new Intent(requireActivity(), TransactionsActivity.class);
//        mIntent.putExtra(KEY_START_FROM, "payments_process");
//        GeneralCommands.goToPage(parentActivity, mIntent, false);
//    }


    private void openHome() {
        try {
            MainActivity.viewPager.setCurrentItem(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        goBack();
        ;

    }

    private void copyTextToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) parentActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(parentActivity, "Paga Number copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

    public void goBack() {
        //MainActivity.viewPager.setCurrentItem(0);
        requireActivity().finish();

    }


}
