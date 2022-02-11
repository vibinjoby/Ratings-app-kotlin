package com.ratings.app.views.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ratings.app.R

class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        /*view.findViewById<Button>(R.id.user_button).setOnClickListener {
           // it.background = resources.getDrawable(R.drawable.tab_left_button_focused)
        }*/
        return view
    }
}