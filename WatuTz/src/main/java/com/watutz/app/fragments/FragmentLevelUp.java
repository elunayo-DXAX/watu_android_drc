package com.watutz.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.watutz.app.DocumentsActivity;
import com.watutz.app.GeneralCommands;
import com.watutz.app.LevelUpActivity;
import com.watutz.app.R;

import static com.watutz.app.constant.IWhereMyLocationConstants.KEY_START_FROM;

public class FragmentLevelUp extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_up, container, false);

        View btnViewStatus = view.findViewById(R.id.btnViewStatus);
        View btnCheckProgress = view.findViewById(R.id.btnCheckProgress);
        View btnUpgradeNow = view.findViewById(R.id.btnUpgradeNow);


        btnViewStatus.setOnClickListener(v -> {
            // TODO: Implement navigation or action for View Status
            goToDocumentsPage();
        });
        btnCheckProgress.setOnClickListener(v -> {
            // TODO: Implement navigation or action for Check Progress
        });
        btnUpgradeNow.setOnClickListener(v -> {
            // TODO: Implement navigation or action for Upgrade Now
        });

        return view;
    }


    public void goToDocumentsPage()
    {
        Intent mIntent = new Intent(getContext(), DocumentsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        GeneralCommands.goToPage(getContext(), mIntent,true);

    }
} 