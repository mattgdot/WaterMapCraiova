package com.app.water4craiova.ui.screens.reset

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.water4craiova.R
import com.app.water4craiova.ui.components.AuthField
import com.app.water4craiova.ui.components.HeadingTextComponent
import com.app.water4craiova.ui.components.NormalTextComponent
import com.app.water4craiova.ui.screens.login.LoginViewModel

@Composable
fun ResetScreen(
    viewModel: ResetViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    LaunchedEffect(viewModel.recoverEmail) {
        if (viewModel.recoverEmail) onFinish()
    }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            NormalTextComponent(value = stringResource(id = R.string.app_name))
            HeadingTextComponent(value = stringResource(id = R.string.resetPassword))
            Spacer(modifier = Modifier.height(15.dp))
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 20.dp)
                )
            } else {
                AuthField(
                    label = stringResource(id = R.string.email),
                    onUpdate = viewModel::onEmailChange,
                    icon = Icons.Outlined.Email,
                    error = viewModel.emailError,
                    singleLine = true,
                    maxLines = 1
                )

                Button(
                    onClick = viewModel::sendRecoverEmail, modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.sendReset))
                }
            }
        }
    }
}