package com.app.water4craiova.ui.screens.signup

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
class SingInViewModel @Inject constructor(
    private val accountService: AccountService, private val application: Application
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var emailError by mutableStateOf("")
    var passwordError by mutableStateOf("")
    var usernameError by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isSignedIn by mutableStateOf(false)

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

    fun onUsernameChange(newUsername: String) {
        username = newUsername
        if (username.isNotBlank()) {
            usernameError = ""
        }
    }

    fun onSignIn() {
        if (email.isNotBlank() && password.isNotBlank() && username.isNotBlank()) {
            if (!isValidEmail(email)) {
                emailError = application.getString(R.string.emailInvalid)
                return
            }
            if(username.length<3) {
                usernameError = application.getString(R.string.usernameShort)
                return
            }
            if(username.length>20) {
                usernameError = application.getString(R.string.usernameLong)
                return
            }
            isLoading = true

            accountService.createAccount(
                email = email,
                password = password,
                username = username,
                onResult = { exception ->
                    if (exception != null) {
                        Toast.makeText(
                            application,
                            application.getString(R.string.signupFailed),
                            Toast.LENGTH_SHORT

                        ).show()
                    } else {
                        Toast.makeText(
                            application,
                            application.getString(R.string.signupSuccess),
                            Toast.LENGTH_SHORT
                        ).show()
                        isSignedIn = true
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
        if (username.isBlank()) {
            usernameError = application.getString(R.string.usernameMissing)
        }

    }

}