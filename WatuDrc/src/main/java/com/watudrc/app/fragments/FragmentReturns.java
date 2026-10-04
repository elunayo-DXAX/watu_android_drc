package com.watudrc.app.fragments;

import static com.watudrc.app.MainActivity.goToChatPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.watudrc.app.GeneralCommands;
import com.watudrc.app.R;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.util.FragmentUtils;

public class FragmentReturns extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_returns, container, false);

        // Set colored/bold text for each step
        FirmaTextView step1Text = view.findViewById(R.id.step1Text);
        FirmaTextView step2Text = view.findViewById(R.id.step2Text);
        FirmaTextView step3Text = view.findViewById(R.id.step3Text);
        FirmaTextView step4Text = view.findViewById(R.id.step4Text);
        FirmaTextView step5Text = view.findViewById(R.id.step5Text);
        FirmaTextView step6Text = view.findViewById(R.id.step6Text);

        if (step1Text != null)
            step1Text.setText(android.text.Html.fromHtml(getString(R.string.recovery_step_1)));
        if (step2Text != null)
            step2Text.setText(android.text.Html.fromHtml(getString(R.string.recovery_step_2)));
        if (step3Text != null)
            step3Text.setText(android.text.Html.fromHtml(getString(R.string.recovery_step_3)));
        if (step4Text != null)
            step4Text.setText(android.text.Html.fromHtml(getString(R.string.recovery_step_4)));
        if (step5Text != null)
            step5Text.setText(android.text.Html.fromHtml(getString(R.string.recovery_step_5)));
        if (step6Text != null)
            step6Text.setText(android.text.Html.fromHtml(getString(R.string.recovery_step_6)));


        View btnChatWithUs = view.findViewById(R.id.btnChatWithUs);
        View btnReplacementProcess = view.findViewById(R.id.btnReplacementProcess);
        View btnServiceCenter = view.findViewById(R.id.btnServiceCenter);

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v ->
                FragmentUtils.safePopBackStack(FragmentReturns.this)
        );



        btnChatWithUs.setOnClickListener(v -> {
            // To DO: Open chat/support screen
            GeneralCommands.logEvent(getContext(), "support_hub_opened_resource_returns", "support_hub_opened_resource_returns");
            goToChatPage("resources_returns");
        });

        btnReplacementProcess.setOnClickListener(v -> {
            switchFragment(new FragmentReplacementProcess());
        });
        btnServiceCenter.setOnClickListener(v -> {
            switchFragment(new FragmentServiceCenter());
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