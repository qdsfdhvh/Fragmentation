package me.yokeyword.sample.zhihu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView
import me.yokeyword.sample.R
import me.yokeyword.sample.zhihu.entity.Article
import me.yokeyword.sample.zhihu.listener.OnItemClickListener

/**
 * 主页HomeFragment  Adapter
 * Created by YoKeyword on 16/2/1.
 */
class HomeAdapter(context: Context) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    private val mItems = ArrayList<Article>()
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    private var mClickListener: OnItemClickListener? = null

    fun setItems(items: List<Article>) {
        mItems.clear()
        mItems.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = mInflater.inflate(R.layout.item_home, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount() = mItems.size

    fun getItem(position: Int) =  mItems[position]

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        init {
            itemView.setOnClickListener { v ->
                mClickListener?.onItemClick(adapterPosition, v, this)
            }
        }

        fun bind(position: Int) {
            val item = mItems[position]
            tvTitle.text = item.title
            tvContent.text = item.content
        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mClickListener = itemClickListener
    }
}
