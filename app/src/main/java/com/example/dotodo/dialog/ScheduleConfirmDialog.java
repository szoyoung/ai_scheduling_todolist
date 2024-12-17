package com.example.dotodo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.dotodo.R;

public class ScheduleConfirmDialog extends Dialog {
    private final DialogListener listener;

    public interface DialogListener {
        void onConfirm();
        void onCancel();
    }

    public ScheduleConfirmDialog(@NonNull Context context, DialogListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schedule_confirm);

        Window window = getWindow();
        if (window != null) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.WHITE);
            shape.setCornerRadius(50f); // radius 크기 조절 가능

            window.setBackgroundDrawable(shape);

            // 다이얼로그의 가로 크기 설정
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85); // 화면 너비의 85%로 설정
            window.setAttributes(layoutParams);
        }

        Button confirmButton = findViewById(R.id.btn_confirm);
        Button cancelButton = findViewById(R.id.btn_cancel);

        confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm();
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        });
    }
}