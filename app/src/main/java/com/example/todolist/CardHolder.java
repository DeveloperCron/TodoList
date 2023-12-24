package com.example.todolist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardHolder extends RecyclerView.ViewHolder {
    TextView tvDay,tvMonth,tvTaskName,tvTime;
    public CardHolder(@NonNull View itemView) {
        super(itemView);

        tvDay = itemView.findViewById(R.id.tvDay);
        tvMonth = itemView.findViewById(R.id.tvMonth);
        tvTaskName = itemView.findViewById(R.id.tvTaskName);
        tvTime = itemView.findViewById(R.id.tvTime);
    }
}
