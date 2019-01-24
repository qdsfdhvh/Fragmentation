package me.yokeyword.sample.zhihu.listener

import android.view.View

import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(position: Int, view: View, vh: RecyclerView.ViewHolder)
}