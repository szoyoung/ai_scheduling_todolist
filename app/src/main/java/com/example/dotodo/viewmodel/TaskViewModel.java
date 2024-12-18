package com.example.dotodo.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.dotodo.data.model.Task;
import com.example.dotodo.data.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private final MutableLiveData<List<Task>> _allTasks = new MutableLiveData<>();
    public final LiveData<List<Task>> allTasks = _allTasks;
    private final MutableLiveData<Task> _updatedTask = new MutableLiveData<>();
    public final LiveData<Task> updatedTask = _updatedTask;


    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        // Repository의 LiveData를 관찰하여 MutableLiveData 업데이트
        repository.getAllTasks().observeForever(tasks -> {
            _allTasks.setValue(tasks);
        });

    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getUpdatedTask() {
        return updatedTask;
    }

    public void insert(Task task) {
        repository.insert(task);
        // 현재 리스트에 즉시 추가
        List<Task> currentList = _allTasks.getValue();
        if (currentList != null) {
            List<Task> newList = new ArrayList<>(currentList);
            newList.add(task);
            _allTasks.setValue(newList);
        }
    }


    public void update(Task task) {
        repository.update(task);
        _updatedTask.setValue(task);

        // 현재 리스트에서 해당 task 업데이트
        List<Task> currentList = _allTasks.getValue();
        if (currentList != null) {
            List<Task> newList = new ArrayList<>(currentList);
            for (int i = 0; i < newList.size(); i++) {
                if (newList.get(i).getId() == task.getId()) {
                    newList.set(i, task);
                    break;
                }
            }
            _allTasks.setValue(newList);
        }
    }

    public void delete(Task task) {
        repository.delete(task);
        // 현재 리스트에서 즉시 제거
        List<Task> currentList = _allTasks.getValue();
        if (currentList != null) {
            List<Task> newList = new ArrayList<>(currentList);
            newList.removeIf(t -> t.getId() == task.getId());
            _allTasks.setValue(newList);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // observeForever 제거
        repository.getAllTasks().removeObserver(tasks -> {});
    }
}

