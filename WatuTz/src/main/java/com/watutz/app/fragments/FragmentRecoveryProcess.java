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
import com.watutz.app.font.FirmaTextView;

public class FragmentRecoveryProcess extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recovery_process, container, false);


        // Set colored/bold text for each step
        FirmaTextView step1Text = view.findViewById(R.id.step1Text);
        FirmaTextView step2Text = view.findViewById(R.id.step2Text);
        FirmaTextView step3Text = view.findViewById(R.id.step3Text);
        FirmaTextView step4Text = view.findViewById(R.id.step4Text);
        FirmaTextView step5Text = view.findViewById(R.id.step5Text);
        FirmaTextView step6Text = view.findViewById(R.id.step6Text);
        FirmaTextView step7Text = view.findViewById(R.id.step7Text);

        if (step1Text != null)
            step1Text.setText(android.text.Html.fromHtml(getString(R.string.returns_step_1)));
        if (step2Text != null)
            step2Text.setText(android.text.Html.fromHtml(getString(R.string.returns_step_2)));
        if (step3Text != null)
            step3Text.setText(android.text.Html.fromHtml(getString(R.string.returns_step_3)));
        if (step4Text != null)
            step4Text.setText(android.text.Html.fromHtml(getString(R.string.returns_step_4)));

        if (step5Text != null)
            step5Text.setText(android.text.Html.fromHtml(getString(R.string.returns_step_5)));
        if (step6Text != null)
            step6Text.setText(android.text.Html.fromHtml(getString(R.string.returns_step_6)));
        if (step7Text != null)
            step7Text.setText(android.text.Html.fromHtml(getString(R.string.returns_step_3)));

        View btnChatWithUs = view.findViewById(R.id.btnChatWithUs);
        View btnServiceCenter = view.findViewById(R.id.btnServiceCenter);
        View btnReplacementProcess = view.findViewById(R.id.btnReplacementProcess);

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());


        btnChatWithUs.setOnClickListener(v -> {
            // To DO: Open chat/support screen
            GeneralCommands.logEvent(getContext(), "click_chat_with_us_button_returns", "click_chat_with_us_button_returns");
//            MainActivity.openWhatsAppWithMessage(getContext().getResources().getString(R.string.hello));
            goToChatPage("fragment_returns");
        });

        btnServiceCenter.setOnClickListener(v -> {
            switchFragment(new FragmentServiceCenter());
        });

        btnReplacementProcess.setOnClickListener(v -> {
            switchFragment(new FragmentReplacementProcess());
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