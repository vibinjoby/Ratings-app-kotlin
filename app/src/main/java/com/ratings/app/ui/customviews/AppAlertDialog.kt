package com.ratings.app.ui.customviews

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.ratings.app.R

class AppAlertDialog
    (context: Context,
     private val alertTitleText: String,
     private val positiveBtnCLickHandler: () -> Unit)
    : Dialog(context), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.alert_dialog_view)
        findViewById<TextView>(R.id.alert_title_tv).text = alertTitleText
        findViewById<AppCompatButton>(R.id.alert_yes_btn).setOnClickListener(this)
        findViewById<AppCompatButton>(R.id.alert_cancel_btn).setOnClickListener(this)
    }
    override fun onClick(view: View) {
        when(view.id) {
            R.id.alert_yes_btn -> {
                dismiss()
                positiveBtnCLickHandler()
            }
            R.id.alert_cancel_btn -> dismiss()
            else -> dismiss()
        }
    }
}