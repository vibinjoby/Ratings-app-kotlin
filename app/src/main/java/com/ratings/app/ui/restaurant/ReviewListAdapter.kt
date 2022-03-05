package com.ratings.app.ui.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ratings.app.R
import com.ratings.app.RestaurantDetailsQuery
import com.ratings.app.type.UserType
import com.ratings.app.ui.customviews.AppAlertDialog
import com.ratings.app.ui.customviews.EditReviewDialog

class ReviewViewHolder(
    val userType: String,
    val view: View,
    private val onSaveEditResponse: ((reviewText: String, ratingBar: Double, ownerResponse: String, reviewId: Int) -> Unit)?,
    private val onDeleteReview: ((reviewId: Int) -> Unit)?,
    private val onSaveReply: ((reviewId: Int, reply: String)-> Unit)?): RecyclerView.ViewHolder(view) {
    fun bind(review: RestaurantDetailsQuery.Review) {
        val ratingBar = view.findViewById<AppCompatRatingBar>(R.id.rating_bar)
        val reviewTv = view.findViewById<TextView>(R.id.review_tv)
        val reviewDateTv = view.findViewById<TextView>(R.id.review_date_tv)
        val customerNameTv = view.findViewById<TextView>(R.id.customer_name_tv)

        val businessOwnerLayout = view.findViewById<ConstraintLayout>(R.id.business_owner_reply_layout)
        val ownerReplyTv = view.findViewById<TextView>(R.id.owner_reply_tv)

        val ownerReplyEt = view.findViewById<EditText>(R.id.owner_reply_et)
        val sendReplyBtn = view.findViewById<AppCompatButton>(R.id.review_send_btn)
        val replyButton = view.findViewById<AppCompatButton>(R.id.review_reply_btn)
        val moreBtn = view.findViewById<AppCompatButton>(R.id.more_button)

        val ownerReplyLayout = view.findViewById<LinearLayout>(R.id.owner_reply_layout)
        if (userType === UserType.owner.toString() && review.ownerReply.isNullOrEmpty()) {
            replyButton.visibility = View.VISIBLE
            replyButton.setOnClickListener {
                replyButton.visibility = View.GONE
                ownerReplyLayout.visibility = View.VISIBLE
            }
            sendReplyBtn.setOnClickListener {
                replyButton.visibility = View.GONE
                onSaveReply?.let {
                    it(review.id, ownerReplyEt.text.toString())
                }
            }
        }

        if (userType === UserType.admin.toString()) {
            moreBtn.visibility = View.VISIBLE
            moreBtn.setOnClickListener {
                val popupMenu = PopupMenu(view.context, it)
                popupMenu.menuInflater.inflate(R.menu.admin_more_menu, popupMenu.menu)
                popupMenu.show()
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit_review -> {
                            val dialog = EditReviewDialog(
                                it.context,
                            ) { reviewText, ratingBar, ownerResponse ->
                                onSaveEditResponse?.let {
                                    it(reviewText, ratingBar, ownerResponse, review.id)
                                }
                            }
                            dialog.show()
                            true
                        }
                        R.id.delete_review -> {
                            val dialog = AppAlertDialog(view.context,
                                "Are you sure you want to delete this review?") {
                                onDeleteReview?.let {
                                    it(review.id)
                                }
                            }
                            dialog.show()
                            true
                        }
                        else -> false
                    }
                }
            }
            ratingBar.rating = review.ratings.toFloat()
            reviewTv.text = review.comments
            reviewDateTv.text = review.visitDate.toString()
            customerNameTv.text = review.user.fullName

            review.ownerReply?.let {
                businessOwnerLayout.visibility = View.VISIBLE
                ownerReplyTv.text = review.ownerReply
            }
        }
    }
}

class ReviewListAdapter(
        val userType: String,
        private val onSaveEditResponse: ((reviewText: String, ratingBar: Double, ownerResponse: String,reviewId: Int)-> Unit)? = null,
        private val onDeleteReview: ((reviewId: Int) -> Unit)? = null,
        private val onSaveReply: ((reviewId: Int, reply: String) -> Unit)? = null
    ) : ListAdapter<RestaurantDetailsQuery.Review, ReviewViewHolder>(DIFF_CONFIG) {
    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<RestaurantDetailsQuery.Review>() {
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(userType, view,onSaveEditResponse, onDeleteReview, onSaveReply)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}