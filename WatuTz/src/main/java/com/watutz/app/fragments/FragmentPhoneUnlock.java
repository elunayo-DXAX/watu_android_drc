package com.watutz.app.fragments;

import static com.watutz.app.MainActivity.goToChatPage;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.watutz.app.GeneralCommands;
import com.watutz.app.R;
import com.watutz.app.font.FirmaTextView;


public class FragmentPhoneUnlock extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_unlock, container, false);

        FirmaTextView step1Text = view.findViewById(R.id.step1Txt);
        FirmaTextView step2Text = view.findViewById(R.id.step2Txt);
        FirmaTextView step3Text = view.findViewById(R.id.step3Txt);
        FirmaTextView step4Text = view.findViewById(R.id.step4Txt);
        FirmaTextView step5Text = view.findViewById(R.id.step5Txt);
        FirmaTextView step6Text = view.findViewById(R.id.step6Txt);

        if (step1Text != null) {
            step1Text.setText(Html.fromHtml(getString(R.string.unlock_step1_sentence)));
        }
        if (step2Text != null) {
            step2Text.setText(Html.fromHtml(getString(R.string.unlock_step2_sentence)));
        }
        if (step3Text != null) {
            step3Text.setText(Html.fromHtml(getString(R.string.unlock_step3_sentence)));
        }
        if (step4Text != null) {
            step4Text.setText(Html.fromHtml(getString(R.string.unlock_step4_sentence)));
        }

        if (step5Text != null) {
            step5Text.setText(Html.fromHtml(getString(R.string.unlock_step5_sentence)));
        }

        if (step6Text != null) {
            step6Text.setText(Html.fromHtml(getString(R.string.unlock_step6_sentence)));
        }

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());


        View btnChatWithUs = view.findViewById(R.id.btnChatWithUs);
        View btnDeviceStolen = view.findViewById(R.id.btnDeviceStolen);
        View btnRecoveryProcess = view.findViewById(R.id.btnRecoveryProcess);

        btnChatWithUs.setOnClickListener(v -> {
            // To DO: Open chat/support screen
            GeneralCommands.logEvent(getContext(), "click_chat_with_us_button_phone_unlock", "click_chat_with_us_button_phone_unlock");
            goToChatPage("fragment_unlock_phone");

        });
        btnRecoveryProcess.setOnClickListener(v -> {
            switchFragment(new FragmentRecoveryProcess());
        });
        btnDeviceStolen.setOnClickListener(v -> {
            switchFragment(new FragmentDeviceStolen());
        });

        return view;
    }

    private void switchFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
} 