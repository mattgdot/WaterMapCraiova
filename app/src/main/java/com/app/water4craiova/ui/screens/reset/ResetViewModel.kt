package com.app.water4craiova.ui.screens.reset

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.app.water4craiova.R
import com.app.water4craiova.model.service.AccountService
import com.app.water4craiova.utils.Format.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(
    private val accountService: AccountService,
    private val application: Application
) : ViewModel() {

    var email by mutableStateOf("")
    var emailError by mutableStateOf("")

    var recoverEmail by mutableStateOf(false)
    var isLoading by mutableStateOf(false)


    fun onEmailChange(newEmail: String) {
        email = newEmail
        if (email.isNotBlank()) {
            emailError = ""
        }
    }

    fun sendRecoverEmail() {
        if (email.isNotBlank()) {
            if (!isValidEmail(email)) {
                emailError = application.getString(R.string.emailInvalid)
                return
            }
            isLoading = true
            accountService.sendRecoveryEmail(email = email, onResult = { exception ->
                if (exception != null) {
                    Toast.makeText(
                        application, application.getString(R.string.error), Toast.LENGTH_SHORT

                    ).show()
                } else {
                    Toast.makeText(
                        application, application.getString(R.string.followReset), Toast.LENGTH_LONG
                    ).show()
                    recoverEmail = true
                }
                isLoading = false
            })
        }
        if (email.isBlank()) {
            emailError = application.getString(R.string.emailMissing)
        }
    }

}