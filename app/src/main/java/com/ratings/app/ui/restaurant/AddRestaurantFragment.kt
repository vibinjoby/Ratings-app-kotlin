package com.ratings.app.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.android.material.textfield.TextInputLayout
import com.ratings.app.R
import com.ratings.app.helper.isErrors
import com.ratings.app.helper.toggleProgressBarOnNetworkState
import com.ratings.app.repository.NetworkState
import com.ratings.app.type.CreateRestaurantInput
import com.ratings.app.ui.viewmodels.RestaurantViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AddRestaurantFragment : DaggerFragment(R.layout.fragment_add_restaurant) {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val restaurantViewModel: RestaurantViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_restaurant, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_add_restaurant)
        val restaurantNameField = view.findViewById<TextInputLayout>(R.id.restaurant_name_field)
        val contactNumberField = view.findViewById<TextInputLayout>(R.id.contact_number_field)
        val addressField = view.findViewById<TextInputLayout>(R.id.address_field)
        val emailField = view.findViewById<TextInputLayout>(R.id.email_field)
        val saveButton = view.findViewById<AppCompatButton>(R.id.save_btn)


        form {
            useRealTimeValidation(disableSubmit = true)

            inputLayout(emailField) {
                isNotEmpty()
                isEmail()
                onErrors { _, errors ->
                    saveButton.isEnabled = !isErrors(errors)
                }
            }
            inputLayout(restaurantNameField) {
                isNotEmpty()
                length().greaterThan(2)
                onErrors { _, errors ->
                    saveButton.isEnabled = !isErrors(errors)
                }
            }
            inputLayout(addressField) {
                isNotEmpty()
                onErrors { _, errors ->
                    saveButton.isEnabled = !isErrors(errors)
                }
            }
            inputLayout(contactNumberField) {
                isNotEmpty()
                isNumber()
                onErrors { _, errors ->
                    saveButton.isEnabled = !isErrors(errors)
                }
            }
            submitWith(saveButton) { result ->
                var restaurantName = ""
                var address = ""
                var email = ""
                var contactNumber = ""
                result["restaurant_name_field"]?.let {
                    restaurantName = it.value.toString()
                }
                result["address_field"]?.let {
                    address = it.value.toString()
                }
                result["email_field"]?.let {
                    email = it.value.toString()
                }
                result["contact_number_field"]?.let {
                    contactNumber = it.value.toString()
                }

                if(restaurantName.isNotBlank() && address.isNotBlank()
                    && email.isNotBlank() && contactNumber.isNotBlank()) {
                        val createRestaurantInput = CreateRestaurantInput(restaurantName, address, contactNumber.toDouble())
                    restaurantViewModel.saveRestaurant(createRestaurantInput)

                    restaurantViewModel.networkState.observe(viewLifecycleOwner, {
                        if(it == NetworkState.LOADED) {
                            findNavController().navigate(AddRestaurantFragmentDirections.actionAddRestaurantFragmentToHomeFragment())
                        }
                        // Show progress bar based on network status
                        toggleProgressBarOnNetworkState(it, progressBar)
                    })
                }
            }
        }
        return view
    }
}