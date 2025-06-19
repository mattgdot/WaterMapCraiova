package com.app.water4craiova.model.service

import com.app.water4craiova.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

    fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun createAccount(email: String, password: String, username:String, onResult: (Throwable?) -> Unit)
    fun signOut()
    fun sendRecoveryEmail(email: String, onResult: (Throwable?) -> Unit)

}