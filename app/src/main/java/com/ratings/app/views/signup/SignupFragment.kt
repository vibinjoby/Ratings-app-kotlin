package com.ratings.app.views.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.ratings.app.R
import com.ratings.app.utils.isErrors

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_signup, container, false)

        val signUpBtn = view.findViewById<AppCompatButton>(R.id.signup_btn)

        form {
            useRealTimeValidation(disableSubmit = true)

            inputLayout(view.findViewById(R.id.username_field)) {
                isNotEmpty().description("Enter your username")
                length().greaterThan(5)
                onErrors{ _, errors ->
                    signUpBtn.isEnabled = !isErrors(errors)
                }
            }
            inputLayout(view.findViewById(R.id.email_field)) {
                isNotEmpty()
                isEmail()
                onErrors{ _, errors ->
                    signUpBtn.isEnabled = !isErrors(errors)
                }
            }
            inputLayout(view.findViewById(R.id.password_field)) {
                isNotEmpty()
                length().greaterThan(8)
                onErrors{ _, errors ->
                    signUpBtn.isEnabled = !isErrors(errors)
                }
            }
            submitWith(view.findViewById(R.id.signup_btn)) { result ->
                // this block is only called if form is valid.
                // do something with a valid form state.
            }
        }

        view.findViewById<AppCompatButton>(R.id.signin_btn).setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        
        return view
    }
}