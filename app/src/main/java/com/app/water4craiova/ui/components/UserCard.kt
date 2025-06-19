package com.app.water4craiova.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.water4craiova.R

@Composable
fun UserCard(
    isLoggedIn: Boolean, email: String, username:String, onLogin: () -> Unit, onLogout: () -> Unit
) {
    if (isLoggedIn) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(username, fontWeight = FontWeight.Bold)
                Text(email)
                OutlinedButton(onClick = onLogout, modifier = Modifier.padding(top = 10.dp)) {
                    Text(stringResource(id = R.string.logout))
                }
            }
        }

    } else {
        OutlinedButton(onClick = onLogin, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(stringResource(id = R.string.login))
        }
    }
}