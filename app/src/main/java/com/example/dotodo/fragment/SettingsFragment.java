package com.example.dotodo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dotodo.MainActivity;
import com.example.dotodo.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsFragment extends Fragment {
    private TextInputEditText scheduleEdit;
    private SwitchMaterial notificationSwitch;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        scheduleEdit = view.findViewById(R.id.edit_schedule);
        notificationSwitch = view.findViewById(R.id.switch_notification);

        // 저장된 설정 불러오기
        scheduleEdit.setText(sharedPreferences.getString("fixed_schedule", ""));
        notificationSwitch.setChecked(sharedPreferences.getBoolean("notifications_enabled", true));

        // 스케줄 저장
        scheduleEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                saveSchedule();
            }
        });

        // 알림 설정 변경
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit()
                    .putBoolean("notifications_enabled", isChecked)
                    .apply();

            ((MainActivity) requireActivity()).toggleAlarm(isChecked);
        });
    }

    private void saveSchedule() {
        String schedule = scheduleEdit.getText().toString().trim();
        sharedPreferences.edit()
                .putString("fixed_schedule", schedule)
                .apply();
    }
}