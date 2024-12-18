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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskAdapter extends ListAdapter<Task, TaskViewHolder> {
    private OnTaskClickListener listener;
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private Set<Integer> swipedPositions = new HashSet<>();

    public TaskAdapter(OnTaskClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        setupSwipeToDelete();
    }

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
        View foregroundView = holder.itemView.findViewById(R.id.foreground_view);
        View deleteButton = holder.itemView.findViewById(R.id.btn_delete);

        // 스와이프 상태에 따른 뷰 처리
        if (swipedPositions.contains(position)) {
            foregroundView.setTranslationX(-holder.itemView.getWidth());
            deleteButton.setVisibility(View.VISIBLE);
            // 스와이프 상태에서는 할 일 관련 클릭 리스너를 비활성화
            holder.bind(task, null);
        } else {
            foregroundView.setTranslationX(0f);
            deleteButton.setVisibility(View.GONE);
            // 일반 상태에서는 원래의 리스너 활성화
            holder.bind(task, listener);
        }

        // 전체 아이템 뷰에 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            if (swipedPositions.contains(position)) {
                // 스와이프 상태일 때는 원래 상태로 복구
                swipedPositions.remove(position);
                notifyItemChanged(position);
            } else if (listener != null) {
                // 일반 상태일 때는 task 클릭 이벤트 발생
                listener.onTaskClick(task);
            }
        });

        // 삭제 버튼 클릭 리스너는 항상 유지
        deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskDelete(task);
            }
            swipedPositions.remove(position);
        });
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                swipedPositions.add(position);
                notifyItemChanged(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState,
                                    boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                View foregroundView = itemView.findViewById(R.id.foreground_view);
                View deleteButton = itemView.findViewById(R.id.btn_delete);

                int position = viewHolder.getAdapterPosition();
                int itemWidth = itemView.getWidth();

                if (isCurrentlyActive) {
                    // 스와이프 중일 때
                    float newDx = Math.max(dX, -itemWidth);
                    foregroundView.setTranslationX(newDx);
                    deleteButton.setTranslationX(itemWidth + newDx);
                    deleteButton.setVisibility(Math.abs(newDx) >= itemWidth * 0.5 ? View.VISIBLE : View.GONE);
                } else if (swipedPositions.contains(position)) {
                    // 스와이프 상태 유지
                    foregroundView.setTranslationX(-itemWidth);
                    deleteButton.setTranslationX(0);
                    deleteButton.setVisibility(View.VISIBLE);
                } else {
                    // 원래 상태
                    foregroundView.setTranslationX(0f);
                    deleteButton.setVisibility(View.GONE);
                }
            }
        };

        itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // 리스트 업데이트 시 스와이프 상태 초기화
    @Override
    public void submitList(List<Task> list) {
        swipedPositions.clear();
        super.submitList(list);
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

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskLongClick(Task task);
        void onTaskDelete(Task task);
    }
}