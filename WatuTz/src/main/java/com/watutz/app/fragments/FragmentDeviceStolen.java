package com.watutz.app.fragments;

import static com.watutz.app.MainActivity.goToChatPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.watutz.app.GeneralCommands;
import com.watutz.app.R;

public class FragmentDeviceStolen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_stolen, container, false);

        View btnChatWithUs = view.findViewById(R.id.btnChatWithUs);
        View btnReplacementProcess = view.findViewById(R.id.btnReplacementProcess);
        View btnServiceCenter = view.findViewById(R.id.btnServiceCenter);

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());


        btnChatWithUs.setOnClickListener(v -> {
            // To DO: Open chat/support screen
            GeneralCommands.logEvent(getContext(), "click_chat_with_us_button_device_stolen", "click_chat_with_us_button_device_stolen");
//            MainActivity.openWhatsAppWithMessage(getContext().getResources().getString(R.string.hello));
            goToChatPage("fragment_device_stolen");
        });
        btnReplacementProcess.setOnClickListener(v -> {
            // Open replacement process screen
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, new FragmentReplacementProcess())
                    .addToBackStack(null)
                    .commit();
        });
        btnServiceCenter.setOnClickListener(v -> {
            // Open recovery process screen
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, new FragmentServiceCenter())
                    .addToBackStack(null)
                    .commit();
        });

        // Set colored/bold text for each step
        com.watutz.app.font.FirmaTextView step1Text = view.findViewById(R.id.step1Text);
        com.watutz.app.font.FirmaTextView step2Text = view.findViewById(R.id.step2Text);
        com.watutz.app.font.FirmaTextView step4Text = view.findViewById(R.id.step4Text);
        com.watutz.app.font.FirmaTextView step7Text = view.findViewById(R.id.step7Text);
        if (step1Text != null)
            step1Text.setText(android.text.Html.fromHtml(getString(R.string.device_stolen_step1)));
        if (step2Text != null)
            step2Text.setText(android.text.Html.fromHtml(getString(R.string.device_stolen_step2)));
        if (step4Text != null)
            step4Text.setText(android.text.Html.fromHtml(getString(R.string.email_customercare_watuafrica_co_tz)));
        if (step7Text != null)
            step7Text.setText(android.text.Html.fromHtml(getString(R.string.device_stolen_step7)));
        return view;
    }
} 