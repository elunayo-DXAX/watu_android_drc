package com.watudrc.app.fragments;

import static com.watudrc.app.MainActivity.goToChatPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.watudrc.app.GeneralCommands;
import com.watudrc.app.R;
import com.watudrc.app.font.FirmaTextView;

public class FragmentDeviceBeyondRepair extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_beyond_repair, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());

        FirmaTextView step1Text = view.findViewById(R.id.step1Text);
        FirmaTextView step2Text = view.findViewById(R.id.step2Text);
        FirmaTextView step3Text = view.findViewById(R.id.step3Text);
        FirmaTextView step4Text = view.findViewById(R.id.step4Text);

        step1Text.setText(getString(R.string.beyond_repair_step1));
        step2Text.setText(android.text.Html.fromHtml(getString(R.string.beyond_repair_step2)));
        step3Text.setText(android.text.Html.fromHtml(getString(R.string.beyond_repair_step3)));
        step4Text.setText(android.text.Html.fromHtml(getString(R.string.beyond_repair_step4)));

        RelativeLayout btnReplacementProcess = view.findViewById(R.id.btnReplacementProcess);
        RelativeLayout btnServiceCenter = view.findViewById(R.id.btnServiceCenter);
        RelativeLayout btnChatWithUs = view.findViewById(R.id.btnChatWithUs);


        btnReplacementProcess.setOnClickListener(v -> {
            switchFragment(new FragmentReplacementProcess());

        });

        btnServiceCenter.setOnClickListener(v -> {
            switchFragment(new FragmentServiceCenter());
        });
        btnChatWithUs.setOnClickListener(v -> {
                // To DO: Open chat/support screen
                GeneralCommands.logEvent(getContext(), "support_hub_opened_resource_beyond_repair", "support_hub_opened_resource_beyond_repair");
//            MainActivity.openWhatsAppWithMessage(getContext().getResources().getString(R.string.hello));
                goToChatPage("resources_beyond_repair");
        });
    }

    private void switchFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}