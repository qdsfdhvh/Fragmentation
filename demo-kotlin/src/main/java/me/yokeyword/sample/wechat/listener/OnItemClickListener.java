package me.yokeyword.sample.wechat.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnItemClickListener {
    void onItemClick(int position, View view, RecyclerView.ViewHolder vh);
}