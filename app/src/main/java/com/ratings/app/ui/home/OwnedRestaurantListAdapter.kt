package com.ratings.app.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ratings.app.MyRestaurantsQuery
import com.ratings.app.R
import com.ratings.app.RestaurantListQuery
import com.ratings.app.helper.RESTAURANT_IMG_URL

class OwnedRestaurantViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private val restaurantName = view.findViewById<TextView>(R.id.restaurant_name_tv)
    private val restaurantRatingText = view.findViewById<TextView>(R.id.rating_text)
    private val restaurantRatingBar = view.findViewById<RatingBar>(R.id.rating_bar)
    private val restaurantNumOfReviews = view.findViewById<TextView>(R.id.reviews_tv)
    private val restaurantImage = view.findViewById<ImageView>(R.id.restaurant_iv)

    fun bind(restaurant: MyRestaurantsQuery.GetOwnedrestaurant) {
        restaurantName.text = restaurant.restaurantName
        restaurantRatingText.text = restaurant.averageRatings.toString()
        restaurantNumOfReviews.text = "${restaurant.reviews?.size ?: 0} Reviews"
        restaurantRatingBar.rating = restaurant.averageRatings.toFloat()
        restaurantImage.clipToOutline = true

        Glide.with(view.context)
            .load(RESTAURANT_IMG_URL)
            .into(restaurantImage)
    }
}

class OwnedRestaurantListAdapter: ListAdapter<MyRestaurantsQuery.GetOwnedrestaurant, OwnedRestaurantViewHolder>(DIFF_CONFIG) {
    companion object {
        val DIFF_CONFIG  = object: DiffUtil.ItemCallback<MyRestaurantsQuery.GetOwnedrestaurant>() {
            override fun areItemsTheSame(
                oldItem: MyRestaurantsQuery.GetOwnedrestaurant,
                newItem: MyRestaurantsQuery.GetOwnedrestaurant
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: MyRestaurantsQuery.GetOwnedrestaurant,
                newItem: MyRestaurantsQuery.GetOwnedrestaurant
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnedRestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return OwnedRestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: OwnedRestaurantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}