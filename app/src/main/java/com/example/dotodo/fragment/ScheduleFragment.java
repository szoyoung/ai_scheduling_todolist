package com.example.dotodo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotodo.R;
import com.example.dotodo.adapter.WeeklyScheduleAdapter;
import com.example.dotodo.dialog.ScheduleConfirmDialog;
import com.example.dotodo.viewmodel.ScheduleViewModel;
import com.example.dotodo.viewmodel.TaskViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleFragment extends Fragment {
    private ScheduleViewModel viewModel;
    private ProgressBar progressBar;
    private RecyclerView scheduleList;
    private WeeklyScheduleAdapter adapter;
    private boolean isTasksLoaded = false;
    private TaskViewModel taskViewModel;
    private ScheduleViewModel scheduleViewModel;
    private ImageButton createButton;
    private MaterialButton saveButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 현재 날짜 설정
        TextView dateText = view.findViewById(R.id.text_current_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.US);
        dateText.setText(dateFormat.format(new Date()));

        // 뷰 초기화
        initViews(view);
        // 뷰모델 설정
        setupViewModel();
        // 리사이클러뷰 설정
        setupRecyclerView();
        // 버튼 클릭 리스너 설정
        setupCreateButton();
        setupSaveButton();  // 저장 버튼 설정 추가

        viewModel.loadLastSchedule();
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        scheduleList = view.findViewById(R.id.schedule_list);
        createButton = view.findViewById(R.id.btn_create_schedule);
        saveButton = view.findViewById(R.id.btn_save_schedule);

    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
//        scheduleViewModel = new ViewModelProvider(requireActivity()).get(ScheduleViewModel.class);
//        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Task 데이터 변화 관찰
        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            isTasksLoaded = true;  // 데이터 로드 완료
            if(tasks != null) {
                Log.d("ScheduleFragment", "Tasks loaded: " + tasks.size());
            }

        });

        // 저장 버튼 표시 여부 관찰
        viewModel.getIsSaveButtonVisible().observe(getViewLifecycleOwner(), isVisible -> {
            saveButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        });

        // 로딩 상태 관찰
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), this::setLoading);

        // 스케줄 데이터 관찰
        viewModel.getWeeklySchedule().observe(getViewLifecycleOwner(), schedules -> {
            adapter.setSchedules(schedules);
        });

        // 에러 메시지 관찰
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });


        // 저장 버튼 표시 여부 관찰 추가
        viewModel.getIsSaveButtonVisible().observe(getViewLifecycleOwner(), isVisible -> {
            saveButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        });
    }

    // 저장 버튼 클릭 리스너 설정 추가
    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            viewModel.saveCurrentSchedule();
            Toast.makeText(requireContext(), "스케줄이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            saveButton.setVisibility(View.GONE);  // 저장 후 버튼 숨기기
        });
    }

    private void setupCreateButton() {
        createButton.setOnClickListener(v -> {
            // 현재 스케줄이 있을 때만 확인 다이얼로그 표시
            if (adapter.getItemCount() > 0) {
                showConfirmDialog();
            } else {
                // 현재 스케줄이 없으면 바로 생성
                viewModel.generateSchedule();
            }
        });
    }

    private void showConfirmDialog() {
        ScheduleConfirmDialog dialog = new ScheduleConfirmDialog(requireContext(),
                new ScheduleConfirmDialog.DialogListener() {
                    @Override
                    public void onConfirm() {
                        viewModel.generateSchedule();
                    }

                    @Override
                    public void onCancel() {
                        // 취소 시 아무 동작 하지 않음
                    }
                });
        dialog.show();
    }

    private void setupRecyclerView() {
        adapter = new WeeklyScheduleAdapter();
        scheduleList.setAdapter(adapter);
        scheduleList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }


    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        createButton.setEnabled(!isLoading);
    }
}