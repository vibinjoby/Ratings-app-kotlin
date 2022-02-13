package com.ratings.app.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.android.material.textfield.TextInputLayout
import com.ratings.app.R
import com.ratings.app.api.AuthorizationInterceptor
import com.ratings.app.api.RatingsApiClient
import com.ratings.app.helper.isErrors
import com.ratings.app.repository.AuthRepository
import com.ratings.app.repository.NetworkState
import com.ratings.app.type.LoginInput
import com.ratings.app.type.UserType
import com.ratings.app.ui.customviews.RadioGridGroup
import okhttp3.OkHttpClient

class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    private lateinit var authViewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        val loginButton = view.findViewById<AppCompatButton>(R.id.login_btn)
        val emailField = view.findViewById<TextInputLayout>(R.id.email_field)
        val passwordField = view.findViewById<TextInputLayout>(R.id.password_field)
        val userTypeRadioGroup = view.findViewById<RadioGridGroup>(R.id.user_type)

        val progressBar = view.findViewById<ProgressBar>(R.id.loading_progress_bar)

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
                var emailValue = ""
                var passwordValue = ""
                result["email_field"]?.let {
                    emailValue = it.value.toString()
                }
                result["password_field"]?.let {
                    passwordValue = it.value.toString()
                }

                val userType = view.findViewById<RadioButton>(userTypeRadioGroup.checkedCheckableImageButtonId)

                if(emailValue.isNotBlank() && passwordValue.isNotBlank()) {
                    val loginInput = LoginInput(emailValue, passwordValue, fetchUserType(userType.text.toString()))
                    val okHttpClient = OkHttpClient.Builder()
                        .build()
                    val apiService = RatingsApiClient(okHttpClient)
                    authRepository = AuthRepository(apiService,requireContext())
                    authViewModel = ViewModelProvider(this@LoginFragment, object: ViewModelProvider.Factory {
                        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                            return LoginViewModel(authRepository,loginInput) as T
                        }
                    })[LoginViewModel::class.java]

                    authViewModel.accessToken.observe(this@LoginFragment.viewLifecycleOwner, {
                        // Save the access token
                        authViewModel.saveAccessToken(it)
                        // Navigate to homePage
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        navigateTo(action)
                    })

                    authViewModel.networkState.observe(this@LoginFragment.viewLifecycleOwner, {
                        // Show progress bar based on network status
                        if(it == NetworkState.LOADING ) {
                            progressBar.visibility = View.VISIBLE
                        } else {
                            progressBar.visibility = View.GONE
                        }
                    })
                }
            }
        }

        view.findViewById<Button>(R.id.signup_btn).setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            navigateTo(action)
        }
        return view
    }

    private fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun fetchUserType(userType: String): UserType {
         return when(userType) {
            "Admin" -> UserType.admin
             "User" -> UserType.customer
             "Owner" -> UserType.owner

             else -> UserType.UNKNOWN__
         }
    }


}