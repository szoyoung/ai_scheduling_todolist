package com.example.dotodo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dotodo.R;
import com.example.dotodo.data.model.ScheduleEvent;

import java.util.ArrayList;
import java.util.List;

public class DailyEventAdapter extends RecyclerView.Adapter<DailyEventAdapter.ViewHolder> {
    private List<ScheduleEvent> events = new ArrayList<>();

    public void setEvents(List<ScheduleEvent> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleEvent event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView timeText;
        private final TextView titleText;

        ViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.text_time);
            titleText = itemView.findViewById(R.id.text_title);
        }

        void bind(ScheduleEvent event) {
            // 시간 표시
            timeText.setText(String.format("%s-%s", event.getStartTime(), event.getEndTime()));

            // 제목 표시
            titleText.setText(event.getTitle());

        }
    }
}