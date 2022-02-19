package com.ratings.app.ui.auth

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.android.material.textfield.TextInputLayout
import com.ratings.app.R
import com.ratings.app.helper.fetchUserType
import com.ratings.app.helper.isErrors
import com.ratings.app.helper.toggleProgressBarOnNetworkState
import com.ratings.app.repository.Status
import com.ratings.app.type.LoginInput
import com.ratings.app.ui.customviews.RadioGridGroup
import com.ratings.app.ui.viewmodels.AuthViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LoginFragment : DaggerFragment(R.layout.fragment_login) {
    private val TAG = "LoginFragment"

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val authViewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
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

                    authViewModel.login(loginInput).observe(this@LoginFragment.viewLifecycleOwner, {
                        // Save the access token
                        authViewModel.saveAccessToken(it)
                        // Navigate to homePage
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        navigateTo(action)
                    })

                    authViewModel.networkState.observe(this@LoginFragment.viewLifecycleOwner, {
                        if(it.status == Status.FAILED) {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        // Show progress bar based on network status
                        toggleProgressBarOnNetworkState(it, progressBar)
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
}