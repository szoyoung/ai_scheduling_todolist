package com.example.dotodo.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dotodo.data.model.DaySchedule;
import com.example.dotodo.data.model.Schedule;
import com.example.dotodo.data.model.Task;
import com.example.dotodo.data.repository.ScheduleRepository;
import com.example.dotodo.data.repository.TaskRepository;
import com.example.dotodo.network.GeminiClient;
import com.example.dotodo.network.GenerateContentRequest;
import com.example.dotodo.network.GenerateContentResponse;
import com.example.dotodo.util.ScheduleParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleViewModel extends AndroidViewModel {
    private final GeminiClient geminiClient;
    private final LiveData<List<Task>> tasks;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<DaySchedule>> weeklySchedule = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSaveButtonVisible = new MutableLiveData<>(false);
    private final ScheduleRepository scheduleRepository;


    @Override
    protected void onCleared() {
        super.onCleared();
    }

    // 앱 시작시 저장된 스케줄 로드
    public void loadLastSchedule() {
        scheduleRepository.getCurrentSchedule().observeForever(schedule -> {
            if (schedule != null) {
                weeklySchedule.setValue(schedule.getWeeklySchedule());
                isSaveButtonVisible.setValue(false);  // 이미 저장된 스케줄이므로 저장 버튼 숨김
            }
        });
    }
    public ScheduleViewModel(Application application) {
        super(application);
        scheduleRepository = new ScheduleRepository(application);
        Log.d("ScheduleViewModel", "Constructor started");

        geminiClient = GeminiClient.getInstance(application);
        Log.d("ScheduleViewModel", "GeminiClient initialized");

        TaskRepository repository = new TaskRepository(application);
        tasks = repository.getAllTasks();
        Log.d("ScheduleViewModel", "Tasks LiveData retrieved from repository");
    }

    // 저장 기능 추가
    public void saveCurrentSchedule() {
        List<DaySchedule> currentSchedule = weeklySchedule.getValue();
        if (currentSchedule != null) {
            Schedule schedule = new Schedule(
                    new Date(),  // 현재 시간을 생성 시간으로
                    "Schedule " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()),
                    currentSchedule
            );

            scheduleRepository.insert(schedule);
            Log.d("ScheduleViewModel", "Schedule saved to database");
            isSaveButtonVisible.setValue(false);  // 저장 후 버튼 숨기기
        } else {
            Log.e("ScheduleViewModel", "No schedule to save");
            error.setValue("저장할 스케줄이 없습니다.");
        }
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<DaySchedule>> getWeeklySchedule() {
        return weeklySchedule;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsSaveButtonVisible() {
        return isSaveButtonVisible;
    }

    public void generateSchedule() {
        isLoading.setValue(true);
        isSaveButtonVisible.setValue(false);  // 새로운 스케줄 생성 시작할 때 저장 버튼 숨김

        List<Task> currentTasks = tasks.getValue();
        if (currentTasks != null && !currentTasks.isEmpty()) {
            // 현재 날짜 기준으로 미래의 task만 필터링
            Date today = new Date();
            List<Task> validTasks = currentTasks.stream()
                    .filter(task -> task.getDeadline().after(today))
                    .collect(Collectors.toList());

            if (validTasks.isEmpty()) {
                error.setValue("유효한 할 일이 없습니다.");
                isLoading.setValue(false);
                return;
            }

            String fixedSchedule = getFixedSchedule();
            String prompt = ScheduleParser.buildPrompt(validTasks, fixedSchedule, today);
            GenerateContentRequest request = new GenerateContentRequest(prompt);

            geminiClient.getApiService().generateContent(geminiClient.getApiKey(), request)
                    .enqueue(new Callback<GenerateContentResponse>() {
                        @Override
                        public void onResponse(Call<GenerateContentResponse> call,
                                               Response<GenerateContentResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String generatedText = response.body().getGeneratedText();
                                if (generatedText != null) {
                                    List<DaySchedule> schedules = ScheduleParser
                                            .parseResponse(generatedText);
                                    weeklySchedule.postValue(schedules);
                                    isSaveButtonVisible.postValue(true);  // 스케줄 생성 성공시 저장 버튼 표시
                                } else {
                                    error.postValue("스케줄 생성에 실패했습니다.");
                                }
                            } else {
                                error.postValue("API 호출 실패: " + response.message());
                            }
                            isLoading.postValue(false);
                        }

                        @Override
                        public void onFailure(Call<GenerateContentResponse> call, Throwable t) {
                            error.postValue("네트워크 오류: " + t.getMessage());
                            isLoading.postValue(false);
                        }
                    });
        } else {
            Log.d("ScheduleViewModel", "Tasks is null or empty");
            isLoading.setValue(false);
            error.setValue("할 일 목록이 비어있습니다.");
        }
    }

    private String getFixedSchedule() {
        return getApplication().getSharedPreferences("settings", Application.MODE_PRIVATE)
                .getString("fixed_schedule", "");
    }
}