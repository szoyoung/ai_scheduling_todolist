package com.example.dotodo.adapter;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dotodo.R;
import com.example.dotodo.data.model.Task;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private CheckBox checkBox;
    private TextView titleView;
    private TextView deadlineView;
    private SimpleDateFormat dateFormat;
    private EditText editText;   // 추가

    public TaskViewHolder(View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkbox_task);
        titleView = itemView.findViewById(R.id.text_title);
        deadlineView = itemView.findViewById(R.id.text_deadline);
        editText = itemView.findViewById(R.id.edit_title);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public void bind(Task task, TaskAdapter.OnTaskClickListener listener) {
        // 기존 코드 유지
        titleView.setText(task.getTitle());
        checkBox.setChecked(task.isCompleted());


        if (task.getDeadline() != null) {
            deadlineView.setVisibility(View.VISIBLE);
            deadlineView.setText(dateFormat.format(task.getDeadline()));
        } else {
            deadlineView.setVisibility(View.GONE);
        }

        // 체크박스 클릭 리스너
        checkBox.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });

        // 길게 누르기 리스너
        itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onTaskLongClick(task);
                return true;
            }
            return false;
        });

        // 제목 클릭 시 수정 모드로 전환 (추가)
        titleView.setOnClickListener(v -> {
            titleView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            editText.setText(task.getTitle());
            editText.requestFocus();
            editText.setSelection(editText.length());

            InputMethodManager imm = (InputMethodManager) itemView.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        });

        // 수정 완료 (Enter 키) 처리 (추가)
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String newTitle = editText.getText().toString().trim();
                if (!newTitle.isEmpty()) {
                    task.setTitle(newTitle);
                    titleView.setText(newTitle);
                    listener.onTaskClick(task);  // 기존 onTaskClick 활용
                }

                editText.setVisibility(View.GONE);
                titleView.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) itemView.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                return true;
            }
            return false;
        });

    }
}