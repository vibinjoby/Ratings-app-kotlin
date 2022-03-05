package com.ratings.app.ui.customviews

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import com.ratings.app.R

class EditReviewDialog
    (context: Context,
     private val positiveBtnCLickHandler: (reviewText: String, ratingBar: Double, ownerResponse: String) -> Unit)
    : Dialog(context), View.OnClickListener {

    private lateinit var reviewEt : EditText
    private lateinit var ratingBar : AppCompatRatingBar
    private lateinit var ownerResponseEt :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_edit_review)
        reviewEt = findViewById(R.id.review_et)
        ratingBar = findViewById(R.id.rating_bar)
        ownerResponseEt = findViewById(R.id.owner_response_et)

        findViewById<AppCompatButton>(R.id.alert_save_btn).setOnClickListener(this)
        findViewById<AppCompatButton>(R.id.alert_cancel_btn).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.alert_save_btn -> {
                dismiss()
                positiveBtnCLickHandler(
                    reviewEt.text.toString(),
                    ratingBar.rating.toDouble(),
                    ownerResponseEt.text.toString() )
            }
            R.id.alert_cancel_btn -> dismiss()
            else -> dismiss()
        }
    }
}