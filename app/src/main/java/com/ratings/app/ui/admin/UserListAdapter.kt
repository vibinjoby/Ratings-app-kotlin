package com.ratings.app.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ratings.app.R
import com.ratings.app.UsersListQuery

class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val usernameTv = view.findViewById<TextView>(R.id.user_name_tv)
    private val usertypeTv = view.findViewById<TextView>(R.id.user_type_tv)
    private val deleteIcIv = view.findViewById<ImageView>(R.id.delete_user_iv)

    fun bind(user: UsersListQuery.User) {
        usernameTv.text = user.fullName
        usertypeTv.text = user.userType.rawValue
        deleteIcIv.setOnClickListener {
            // click handler
        }
    }
}

class UserListAdapter: ListAdapter<UsersListQuery.User, UserViewHolder>(DIFF_CONFIG) {
    companion object {
        val DIFF_CONFIG = object: DiffUtil.ItemCallback<UsersListQuery.User>() {
            override fun areItemsTheSame(
                oldItem: UsersListQuery.User,
                newItem: UsersListQuery.User
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: UsersListQuery.User,
                newItem: UsersListQuery.User
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}