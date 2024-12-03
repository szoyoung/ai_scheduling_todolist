package com.example.dotodo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotodo.R;
import com.example.dotodo.adapter.TaskAdapter;
import com.example.dotodo.data.model.Task;
import com.example.dotodo.dialog.TaskDetailDialog;
import com.example.dotodo.viewmodel.TaskViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class ToDoListFragment extends Fragment implements TaskAdapter.OnTaskClickListener {
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private TextInputEditText editNewTask;
    private ImageButton btnAddTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupRecyclerView(view);
        setupViewModel();
        setupTaskInput();
    }

    private void initializeViews(View view) {
        editNewTask = view.findViewById(R.id.edit_new_task);
        btnAddTask = view.findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener(v -> addNewTask());
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks ->
                adapter.submitList(tasks));

        taskViewModel.getUpdatedTask().observe(getViewLifecycleOwner(), task -> {
            if (task != null) {
                adapter.updateTask(task);
            }
        });
    }

    private void setupTaskInput() {
        editNewTask.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addNewTask();
                return true;
            }
            return false;
        });
    }

    private void addNewTask() {
        String title = editNewTask.getText().toString().trim();
        if (!title.isEmpty()) {
            Task task = new Task(title);
            task.setDeadline(new Date());
            taskViewModel.insert(task);
            editNewTask.setText("");

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editNewTask.getWindowToken(), 0);
        }
    }

    @Override
    public void onTaskClick(Task task) {
        task.setCompleted(!task.isCompleted());
        taskViewModel.update(task);
    }

    @Override
    public void onTaskLongClick(Task task) {
        TaskDetailDialog dialog = new TaskDetailDialog(requireContext(), task, taskViewModel);
        dialog.show();
    }
}