package com.example.dotodo.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.dotodo.R;
import com.example.dotodo.data.model.Task;
import com.example.dotodo.viewmodel.TaskViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDetailDialog extends Dialog {
    private Task task;
    private TaskViewModel viewModel;
    private EditText descriptionEdit;
    private RadioGroup priorityGroup;
    private Button deadlineButton;
    private TextView deadlineText;
    private Context context;
    private final LifecycleOwner lifecycleOwner;  // 추가
    private Date selectedDate;  // 선택된 날짜 저장
    private TextView saveButton;
    private TextView cancelButton;

    public TaskDetailDialog(Context context, Task task, TaskViewModel viewModel) {
        super(context);
        this.context = context;
        this.task = task;
        this.viewModel = viewModel;
        if (context instanceof LifecycleOwner) {
            this.lifecycleOwner = (LifecycleOwner) context;
        } else {
            throw new IllegalArgumentException("Context must be a LifecycleOwner");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_task_detail);

        // Dialog 창 스타일 설정
        Window window = getWindow();
        if (window != null) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.WHITE);
            shape.setCornerRadius(50f); // radius 크기 조절

            window.setBackgroundDrawable(shape);
        }


        // UI 요소 초기화
        descriptionEdit = findViewById(R.id.edit_description);
        priorityGroup = findViewById(R.id.priority_group);
        deadlineButton = findViewById(R.id.btn_set_deadline);
        deadlineText = findViewById(R.id.text_deadline);
        saveButton = findViewById(R.id.btn_save);
         cancelButton = findViewById(R.id.btn_cancel);

        // 현재 데이터 표시
        if (task.getDescription() != null) {
            descriptionEdit.setText(task.getDescription());
        }

        // 우선순위 설정
        RadioButton priorityButton = (RadioButton) priorityGroup.getChildAt(task.getPriority());
        if (priorityButton != null) {
            priorityButton.setChecked(true);
        }

        // 기존 기한 표시
        selectedDate = task.getDeadline();
        updateDeadlineText();

        // 기한 설정 버튼 클릭 리스너
        deadlineButton.setOnClickListener(v -> showDatePicker());

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> {
            saveTask();
            dismiss();
        });

        // 취소 버튼 클릭 리스너
        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) {
            calendar.setTime(selectedDate);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                new ContextThemeWrapper(context, R.style.CustomDatePickerDialog),
                (view, year, month, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, month, dayOfMonth);
                    selectedDate = newDate.getTime();
                    updateDeadlineText();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );


        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "save", datePickerDialog);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", (dialog, which) -> dialog.dismiss());


        // 다이얼로그가 표시되기 전에 배경 설정
        datePickerDialog.setOnShowListener(dialog -> {
            // 배경 drawable 생성
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.WHITE);
            shape.setCornerRadius(80f);

            Window window = datePickerDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(shape);
            }


            // 버튼 색상 변경
            Button positiveButton = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            if (positiveButton != null) {
                positiveButton.setTextColor(Color.BLACK);
            }
            if (negativeButton != null) {
                negativeButton.setTextColor(Color.BLACK);
            }
        });

        datePickerDialog.show();
    }


    private void updateDeadlineText() {
        if (selectedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            deadlineText.setText(dateFormat.format(selectedDate));
            deadlineText.setVisibility(View.VISIBLE);
        } else {
            deadlineText.setVisibility(View.GONE);
        }
    }

    private void saveTask() {
        // 설명 업데이트
        task.setDescription(descriptionEdit.getText().toString().trim());

        // 우선순위 업데이트
        int selectedPriorityId = priorityGroup.getCheckedRadioButtonId();
        int priorityIndex = 0; // 기본값: 낮음
        if (selectedPriorityId == R.id.radio_medium) {
            priorityIndex = 1;
        } else if (selectedPriorityId == R.id.radio_high) {
            priorityIndex = 2;
        }
        task.setPriority(priorityIndex);

        // 기한 업데이트
        task.setDeadline(selectedDate);

        // 데이터베이스 업데이트
        viewModel.update(task);

        // UI 업데이트 확인 후 다이얼로그 닫기
        viewModel.updatedTask.observe(lifecycleOwner, updatedTask -> {
            if (updatedTask != null && updatedTask.getId() == task.getId()) {
                dismiss();
            }
        });

    }
}