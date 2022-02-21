package com.ratings.app.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ratings.app.R
import com.ratings.app.model.AdminHomeInfo

class AdminViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private val imageView: ImageView = view.findViewById(R.id.pic_iv)
    private val titleTv: TextView = view.findViewById(R.id.title_tv)
    private val descriptionTv: TextView = view.findViewById(R.id.description_tv)

    fun bind(adminHomeInfo: AdminHomeInfo) {
        Glide.with(view.context)
            .load(adminHomeInfo.imageView)
            .into(imageView)

        titleTv.text = adminHomeInfo.title
        descriptionTv.text = adminHomeInfo.description
    }
}

class AdminHomeListAdapter(private val clickHandler: (adminInfo: AdminHomeInfo) -> Unit): ListAdapter<AdminHomeInfo,AdminViewHolder>(DIFF_CONFIG) {
    companion object {
        val DIFF_CONFIG = object: DiffUtil.ItemCallback<AdminHomeInfo>() {
            override fun areItemsTheSame(oldItem: AdminHomeInfo, newItem: AdminHomeInfo): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: AdminHomeInfo,
                newItem: AdminHomeInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_home, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}