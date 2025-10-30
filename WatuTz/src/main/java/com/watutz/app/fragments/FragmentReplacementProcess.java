package com.watutz.app.fragments;

import static com.watutz.app.MainActivity.goToChatPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.font.FirmaTextView;

public class FragmentReplacementProcess extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_replacement_process, container, false);

        // Set colored/bold text for each step
        FirmaTextView step1Text = view.findViewById(R.id.step1Text);
        FirmaTextView step2Text = view.findViewById(R.id.step2Text);
        FirmaTextView step3Text = view.findViewById(R.id.step3Text);
        FirmaTextView step4Text = view.findViewById(R.id.step4Text);
        FirmaTextView step5Text = view.findViewById(R.id.step5Text);
        FirmaTextView step6Text = view.findViewById(R.id.step6Text);
        FirmaTextView step7Text = view.findViewById(R.id.step7Text);
        if (step1Text != null)
            step1Text.setText(android.text.Html.fromHtml(getString(R.string.replacement_step_1)));
        if (step2Text != null)
            step2Text.setText(android.text.Html.fromHtml(getString(R.string.replacement_step_2)));
        if (step3Text != null)
            step3Text.setText(android.text.Html.fromHtml(getString(R.string.replacement_step_3)));
        if (step4Text != null)
            step4Text.setText(android.text.Html.fromHtml(getString(R.string.replacement_step_4)));

        if (step5Text != null)
            step5Text.setText(android.text.Html.fromHtml(getString(R.string.replacement_step_5)));

        if (step6Text != null)
            step6Text.setText(android.text.Html.fromHtml(getString(R.string.replacement_step_6)));

        if (step7Text != null)
            step7Text.setText(android.text.Html.fromHtml(getString(R.string.replacement_step_7)));

        View btnChatWithUs = view.findViewById(R.id.btnChatWithUs);
        View btnDeviceStolen = view.findViewById(R.id.btnDeviceStolen);
        View btnServiceCenter = view.findViewById(R.id.btnServiceCenter);

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());


        btnChatWithUs.setOnClickListener(v -> {
            // To DO: Open chat/support screen
            GeneralCommands.logEvent(getContext(), "click_chat_with_us_button_replacement", "click_chat_with_us_button_replacement");
//            MainActivity.openWhatsAppWithMessage(getContext().getResources().getString(R.string.hello));

            goToChatPage("fragment_replacement_processes");
        });
        btnDeviceStolen.setOnClickListener(v -> {
            // Open replacement process screen
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, new FragmentDeviceStolen())
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


        return view;
    }
} 