package com.ratings.app.views.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.android.material.textfield.TextInputLayout
import com.ratings.app.R
import com.ratings.app.helper.isErrors

class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        val loginButton = view.findViewById<AppCompatButton>(R.id.login_btn)
        val emailField = view.findViewById<TextInputLayout>(R.id.email_field)
        val passwordField = view.findViewById<TextInputLayout>(R.id.password_field)

        form {
            useRealTimeValidation(disableSubmit = true)

            inputLayout(emailField) {
                isNotEmpty()
                isEmail()
                onErrors{ _, errors ->
                    loginButton.isEnabled = !isErrors(errors)
                }
            }
            inputLayout(passwordField) {
                isNotEmpty()
                length().greaterThan(8)
                onErrors{ _, errors ->
                    loginButton.isEnabled = !isErrors(errors)
                }
            }
            submitWith(loginButton) { result ->
                // this block is only called if form is valid.
                // do something with a valid form state.
            }
        }

        view.findViewById<Button>(R.id.signup_btn).setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }
        return view
    }


}