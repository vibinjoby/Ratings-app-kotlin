package com.ratings.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.ratings.app.R
import com.ratings.app.helper.fetchUserType
import com.ratings.app.helper.isErrors
import com.ratings.app.helper.toggleProgressBarOnNetworkState
import com.ratings.app.repository.Status
import com.ratings.app.type.CreateUserInput
import com.ratings.app.ui.customviews.RadioGridGroup
import com.ratings.app.ui.viewmodels.AuthViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SignupFragment : DaggerFragment(R.layout.fragment_signup) {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val authViewModel: AuthViewModel by viewModels{
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_signup, container, false)

        val signUpBtn = view.findViewById<AppCompatButton>(R.id.signup_btn)
        val userTypeRadioGroup = view.findViewById<RadioGridGroup>(R.id.user_type)
        val progressBar = view.findViewById<ProgressBar>(R.id.loading_progress_bar)

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
                var usernameValue = ""
                var emailValue = ""
                var passwordValue = ""
                result["username_field"]?.let {
                    usernameValue = it.value.toString()
                }
                result["email_field"]?.let {
                    emailValue = it.value.toString()
                }
                result["password_field"]?.let {
                    passwordValue = it.value.toString()
                }

                val userType = view.findViewById<RadioButton>(userTypeRadioGroup.checkedCheckableImageButtonId)
                val createUserInput = CreateUserInput(emailValue,
                    usernameValue, passwordValue, fetchUserType(userType.text.toString()))

                authViewModel.registerUser(createUserInput).observe(viewLifecycleOwner, {
                    println("created token is $it")
                    // Save the access token
                    authViewModel.saveAccessToken(it)
                    // Navigate to homePage
                    val action = SignupFragmentDirections.actionSignupFragmentToHomeFragment()
                    navigateTo(action)
                })

                authViewModel.networkState.observe(this@SignupFragment.viewLifecycleOwner, {
                    if(it.status == Status.FAILED) {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    // Show progress bar based on network status
                    toggleProgressBarOnNetworkState(it, progressBar)
                })
            }
        }

        view.findViewById<AppCompatButton>(R.id.signin_btn).setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        
        return view
    }

    private fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }
}