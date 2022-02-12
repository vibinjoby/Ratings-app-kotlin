package com.ratings.app.views.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ratings.app.R
import com.ratings.app.RestaurantListQuery
import com.ratings.app.helper.RESTAURANT_IMG_URL

class RestaurantViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private val restaurantName = view.findViewById<TextView>(R.id.restaurant_name_tv)
    private val restaurantRatings = view.findViewById<TextView>(R.id.ratings_tv)
    private val restaurantNumOfReviews = view.findViewById<TextView>(R.id.reviews_tv)
    private val restaurantImage = view.findViewById<ImageView>(R.id.restaurant_iv)

    fun bind(restaurant: RestaurantListQuery.Node) {
        restaurantName.text = restaurant.restaurantName
        restaurantRatings.text = restaurant.averageRatings.toString()
        restaurantNumOfReviews.text = restaurant.reviewsCount.toString()

        Glide.with(view.context)
            .load(RESTAURANT_IMG_URL)
            .into(restaurantImage)
    }
}

class RestaurantListAdapter: ListAdapter<RestaurantListQuery.Node, RestaurantViewHolder>(DIFF_CONFIG) {
    companion object {
        val DIFF_CONFIG  = object: DiffUtil.ItemCallback<RestaurantListQuery.Node>() {
            override fun areItemsTheSame(
                oldItem: RestaurantListQuery.Node,
                newItem: RestaurantListQuery.Node
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: RestaurantListQuery.Node,
                newItem: RestaurantListQuery.Node
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}