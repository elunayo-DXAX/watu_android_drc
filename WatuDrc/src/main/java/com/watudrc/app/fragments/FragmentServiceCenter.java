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

public class FragmentServiceCenter extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_center, container, false);

        FirmaTextView step1Text = view.findViewById(R.id.step1Text);
        FirmaTextView step2Text = view.findViewById(R.id.step2Text);
        FirmaTextView step3Text = view.findViewById(R.id.step3Text);
        FirmaTextView step4Text = view.findViewById(R.id.step4Text);
        FirmaTextView step5Text = view.findViewById(R.id.step5Text);

        if (step1Text != null) step1Text.setText(android.text.Html.fromHtml(getString(R.string.service_center_step_1)));
        if (step2Text != null) step2Text.setText(android.text.Html.fromHtml(getString(R.string.service_center_step_2)));
        if (step3Text != null) step3Text.setText(android.text.Html.fromHtml(getString(R.string.service_center_step_3)));
        if (step4Text != null) step4Text.setText(android.text.Html.fromHtml(getString(R.string.service_center_step_4)));
        if (step5Text != null) step5Text.setText(android.text.Html.fromHtml(getString(R.string.service_center_step_5)));


        View btnFindSamsungASC = view.findViewById(R.id.btnFindSamsungASC);

        View btnDeviceBeyondRepair = view.findViewById(R.id.btnDeviceBeyondRepair);


        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> FragmentUtils.safePopBackStack(new FragmentServiceCenter()));


        btnFindSamsungASC.setOnClickListener(v -> {
            // TODO: Open chat/support screen
            GeneralCommands.logEvent(getContext(), "click_find_samsung_asc_button_service_centre", "click_find_samsung_asc_button_service_centre");
//            MainActivity.openWhatsAppWithMessage(getContext().getResources().getString(R.string.hello));
            goToChatPage("fragment_service_centre");
        });

        btnDeviceBeyondRepair.setOnClickListener(v -> {
            switchFragment(new FragmentDeviceBeyondRepair());

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