package com.app.water4craiova.ui.screens.login

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
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val application: Application,
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf("")
    var passwordError by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var isLoggedIn by mutableStateOf(false)

    fun onEmailChange(newEmail: String) {
        email = newEmail
        if (email.isNotBlank()) {
            emailError = ""
        }
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        if (password.isNotBlank()) {
            passwordError = ""
        }
    }

    fun onLogIn() {
        if (email.isNotBlank() && password.isNotBlank()) {
            if (!isValidEmail(email)) {
                emailError = application.getString(R.string.emailInvalid)
                return
            }
            isLoading = true
            accountService.authenticate(email = email,
                password = password,
                onResult = { exception ->
                    if (exception != null) {
                        Toast.makeText(
                            application,
                            application.getString(R.string.loginFailed),
                            Toast.LENGTH_SHORT

                        ).show()
                    } else {
                        Toast.makeText(
                            application,
                            application.getString(R.string.loginSuccess),
                            Toast.LENGTH_SHORT
                        ).show()
                        isLoggedIn = true
                    }
                    isLoading = false
                })
        }
        if (email.isBlank()) {
            emailError = application.getString(R.string.emailMissing)
        }
        if (password.isBlank()) {
            passwordError = application.getString(R.string.passwordMissing)
        }
    }
}