package com.example.dotodo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dotodo.R;
import com.example.dotodo.data.model.DaySchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeeklyScheduleAdapter extends RecyclerView.Adapter<WeeklyScheduleAdapter.ViewHolder> {
    private List<DaySchedule> schedules = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 (E)", Locale.KOREA);

    public void setSchedules(List<DaySchedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DaySchedule daySchedule = schedules.get(position);
        holder.bind(daySchedule);
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;
        private final RecyclerView eventList;
        private final DailyEventAdapter eventAdapter;

        ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_date);
            eventList = itemView.findViewById(R.id.event_list);

            // 이벤트 리스트 설정
            eventAdapter = new DailyEventAdapter();
            eventList.setAdapter(eventAdapter);
            eventList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        void bind(DaySchedule schedule) {
            // 영어 요일 표시
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
            dateText.setText(dayFormat.format(schedule.getDate()));
            // 일정 목록 설정
            eventAdapter.setEvents(schedule.getEvents());
        }
    }
}