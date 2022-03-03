package com.ratings.app.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ratings.app.R
import com.ratings.app.UsersListQuery
import com.ratings.app.helper.RESTAURANT_IMG_URL

class UserViewHolder(private val view: View, private val deleteClickHandler: (user: UsersListQuery.User) -> Unit): RecyclerView.ViewHolder(view) {
    private val usernameTv = view.findViewById<TextView>(R.id.user_name_tv)
    private val usertypeTv = view.findViewById<TextView>(R.id.user_type_tv)
    private val deleteIcIv = view.findViewById<AppCompatButton>(R.id.delete_user_iv)
    private val userPicIv = view.findViewById<ImageView>(R.id.user_pic_iv)

    fun bind(user: UsersListQuery.User) {
        usernameTv.text = user.fullName
        usertypeTv.text = user.userType.rawValue
        deleteIcIv.setOnClickListener {
            deleteClickHandler(user)
        }
        userPicIv.clipToOutline = true
        Glide.with(view.context)
            .load(RESTAURANT_IMG_URL)
            .into(userPicIv)
    }
}

class UserListAdapter(private val deleteClickHandler: (user: UsersListQuery.User) -> Unit): ListAdapter<UsersListQuery.User, UserViewHolder>(DIFF_CONFIG) {
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
        return UserViewHolder(view, deleteClickHandler)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}