package com.example.dotodo.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.dotodo.data.model.Task;
import com.example.dotodo.data.repository.TaskRepository;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;
    private MutableLiveData<Task> updatedTask;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        updatedTask = new MutableLiveData<>();  // 초기화 추가
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getUpdatedTask() {
        return updatedTask;
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
        updatedTask.setValue(task);  // 업데이트된 task를 즉시 알림
    }

    public void delete(Task task) {
        repository.delete(task);
    }
}