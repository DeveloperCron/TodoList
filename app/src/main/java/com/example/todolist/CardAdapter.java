package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {
    private final List<Card> cardList;
    private final Context context;


    public CardAdapter(List<Card> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardHolder(LayoutInflater.from(context).inflate(R.layout.card, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        holder.tvTime.setText(cardList.get(position).makeTime());
        holder.tvTaskName.setText(cardList.get(position).getTaskName());
        holder.tvMonth.setText(cardList.get(position).makeMonth());
        // @TODO Rename the methods to makeDay()
        holder.tvDay.setText(cardList.get(position).makeDate());
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateData(Card card){
        cardList.add(card);

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteByPosition(int pos){
        cardList.remove(pos);

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteAll(){
        cardList.clear();

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeItem(int position){
        cardList.remove(position);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }
}
