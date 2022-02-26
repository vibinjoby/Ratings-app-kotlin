package com.ratings.app.ui.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ratings.app.R
import com.ratings.app.RestaurantDetailsQuery
import com.ratings.app.type.UserType
import java.text.SimpleDateFormat

class ReviewViewHolder(val userType: String, val view: View, val onSaveReply: (reviewId: Int, reply: String)-> Unit): RecyclerView.ViewHolder(view) {
    fun bind(review: RestaurantDetailsQuery.Review) {
        val ratingBar = view.findViewById<AppCompatRatingBar>(R.id.rating_bar)
        val reviewTv = view.findViewById<TextView>(R.id.review_tv)
        val reviewDateTv = view.findViewById<TextView>(R.id.review_date_tv)
        val customerNameTv = view.findViewById<TextView>(R.id.customer_name_tv)

        val businessOwnerLayout = view.findViewById<ConstraintLayout>(R.id.business_owner_reply_layout)
        val ownerReplyTv = view.findViewById<TextView>(R.id.owner_reply_tv)

        val replyButton = view.findViewById<AppCompatButton>(R.id.review_reply_btn)

        val ownerReplyLayout = view.findViewById<LinearLayout>(R.id.owner_reply_layout)
        val ownerReplyEt = view.findViewById<EditText>(R.id.owner_reply_et)
        val sendReplyBtn = view.findViewById<AppCompatButton>(R.id.review_send_btn)

        ratingBar.rating = review.ratings.toFloat()
        reviewTv.text = review.comments
        reviewDateTv.text = review.visitDate.toString()
        customerNameTv.text = review.user.fullName

        if(userType == UserType.owner.toString() && review.ownerReply.isNullOrEmpty()) {
            replyButton.visibility = View.VISIBLE
        }

        review.ownerReply?.let {
            businessOwnerLayout.visibility = View.VISIBLE
            ownerReplyTv.text = review.ownerReply
        }

        replyButton.setOnClickListener {
            replyButton.visibility = View.GONE
            ownerReplyLayout.visibility = View.VISIBLE
        }
        sendReplyBtn.setOnClickListener {
            replyButton.visibility = View.GONE
            onSaveReply(review.id, ownerReplyEt.text.toString())
        }
    }
}

class ReviewListAdapter(val userType: String, private val onSaveReply: (reviewId: Int, reply: String)-> Unit ): ListAdapter<RestaurantDetailsQuery.Review,ReviewViewHolder>(DIFF_CONFIG) {
    companion object {
        val DIFF_CONFIG = object: DiffUtil.ItemCallback<RestaurantDetailsQuery.Review>() {
            override fun areItemsTheSame(
                oldItem: RestaurantDetailsQuery.Review,
                newItem: RestaurantDetailsQuery.Review
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: RestaurantDetailsQuery.Review,
                newItem: RestaurantDetailsQuery.Review
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review,parent, false)
        return ReviewViewHolder(userType,view, onSaveReply)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}