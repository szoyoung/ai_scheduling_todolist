package com.example.dotodo.adapter;

import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dotodo.R;
import com.example.dotodo.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ListAdapter<Task, TaskViewHolder> {
    private OnTaskClickListener listener;
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    public TaskAdapter(OnTaskClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        setupSwipeToDelete();
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private float currentDx = 0f;
            private View currentForeground = null;
            private View currentDeleteButton = null;
            private boolean isDeleteButtonVisible = false;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 스와이프 완료 시 아무 동작도 하지 않음 (삭제는 버튼 클릭으로만)
                // 원래 위치로 아이템을 되돌림
                notifyItemChanged(viewHolder.getAdapterPosition());
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                currentDx = 0f;
                if (currentForeground != null) {
                    currentForeground.setTranslationX(0f);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState,
                                    boolean isCurrentlyActive) {

                View foregroundView = viewHolder.itemView.findViewById(R.id.foreground_view);
                View deleteBackground = viewHolder.itemView.findViewById(R.id.delete_background);
                View deleteButton = viewHolder.itemView.findViewById(R.id.btn_delete);

                // 현재 뷰 저장
                currentForeground = foregroundView;
                currentDeleteButton = deleteButton;
                currentDx = dX;

                // 삭제 버튼 너비
                int deleteButtonWidth = deleteButton.getWidth();

                // 스와이프 제한 (삭제 버튼 너비만큼만)
                float newDx = Math.max(Math.min(dX, 0), -deleteButtonWidth);

                foregroundView.setTranslationX(newDx);

                // 스와이프 정도에 따라 삭제 버튼 표시/숨김
                if (Math.abs(newDx) >= deleteButtonWidth / 2) {
                    if (!isDeleteButtonVisible) {
                        deleteButton.setVisibility(View.VISIBLE);
                        isDeleteButtonVisible = true;
                    }
                } else {
                    if (isDeleteButtonVisible) {
                        deleteButton.setVisibility(View.GONE);
                        isDeleteButtonVisible = false;
                    }
                }
            }
        };

        itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateTask(Task updatedTask) {
        List<Task> currentList = new ArrayList<>(getCurrentList());
        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getId() == updatedTask.getId()) {
                currentList.set(i, updatedTask);
                submitList(currentList);
                notifyItemChanged(i);
                break;
            }
        }
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.isCompleted() == newItem.isCompleted() &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position);
        holder.bind(task, listener);

        // 삭제 버튼 클릭 리스너 설정
        holder.itemView.findViewById(R.id.btn_delete).setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskDelete(task);
            }
        });
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskLongClick(Task task);
        void onTaskDelete(Task task); // 삭제 메서드 추가
    }
}