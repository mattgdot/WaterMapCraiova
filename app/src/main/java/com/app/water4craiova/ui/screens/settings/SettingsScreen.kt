package com.app.water4craiova.ui.screens.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.water4craiova.R
import com.app.water4craiova.ui.components.CustomAlertDialog
import com.app.water4craiova.ui.components.ThemeSelectionDialog
import com.app.water4craiova.ui.components.UserCard
import com.app.water4craiova.ui.navigation.NavItem
import com.app.water4craiova.utils.Constants.ABOUT_LINK
import com.app.water4craiova.utils.Constants.EMAIL
import com.app.water4craiova.utils.Constants.INSTAGRAM_LINK
import com.app.water4craiova.utils.Constants.LOGIN_ROUTE
import com.app.water4craiova.utils.Constants.PLAYSTORE_LINK
import com.app.water4craiova.utils.Constants.PRIVACY_LINK
import com.app.water4craiova.utils.Constants.TERMS_LINK
import com.app.water4craiova.utils.Constants.WEBVIEW_ROUTE
import com.app.water4craiova.utils.ThemeMode
import java.net.URLEncoder

@Composable
fun SettingsScreen(
    openScreen: (String) -> Unit,
    appTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    var openThemeDialog by remember { mutableStateOf(false) }
    val themeOptions = listOf(ThemeMode.AUTO, ThemeMode.DARK, ThemeMode.LIGHT)

    val user = viewModel.user

    var showMySources by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .padding(top = 15.dp)
    ) {
        item {
            if(user!=null){
                UserCard(isLoggedIn = user.id.isNotBlank(), onLogin = {
                    openScreen(LOGIN_ROUTE)
                }, email = user.email, username = user.username, onLogout = viewModel::signOut)

                ListItem(headlineContent = {
                    Text(text = stringResource(id = R.string.mySources))
                }, leadingContent = {
                    Icon(Icons.Outlined.WaterDrop, contentDescription = null)
                }, modifier = Modifier.clickable {
                    showMySources = true
                })

            }

            ListItem(headlineContent = {
                Text(text = stringResource(id = R.string.setTheme))
            }, leadingContent = {
                Icon(Icons.Outlined.Settings, contentDescription = null)
            }, modifier = Modifier.clickable {
                openThemeDialog = true
            })

            ListItem(headlineContent = {
                Text(text = stringResource(id = R.string.about))
            }, leadingContent = {
                Icon(Icons.Outlined.Info, contentDescription = null)
            }, modifier = Modifier.clickable {
                openScreen(WEBVIEW_ROUTE.replace("{url}", URLEncoder.encode(ABOUT_LINK, "UTF-8")))
            })

            ListItem(headlineContent = {
                Text(text = stringResource(R.string.share))
            }, leadingContent = {
                Icon(Icons.Outlined.Share, contentDescription = null)
            }, modifier = Modifier.clickable {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, PLAYSTORE_LINK + context.packageName
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                try {
                    context.startActivity(shareIntent)
                } catch (_: Exception) {
                    Toast.makeText(
                        context, context.getString(R.string.cantOpenLink), Toast.LENGTH_SHORT
                    ).show()
                }
            })

            ListItem(headlineContent = {
                Text(text = stringResource(id = R.string.review))
            }, leadingContent = {
                Icon(Icons.Outlined.ThumbUp, contentDescription = null)
            }, modifier = Modifier.clickable {
                try {
                    uriHandler.openUri(PLAYSTORE_LINK + context.packageName)
                } catch (e: Exception) {
                    Toast.makeText(
                        context, context.getString(R.string.cantOpenLink), Toast.LENGTH_SHORT
                    ).show()
                }
            })

            ListItem(headlineContent = {
                Text(text = stringResource(id = R.string.contact))
            }, leadingContent = {
                Icon(Icons.Outlined.Email, contentDescription = null)
            }, modifier = Modifier.clickable {
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$EMAIL"))
                try {
                    context.startActivity(emailIntent)
                } catch (_: ActivityNotFoundException) {
                    Toast.makeText(
                        context, context.getString(R.string.cantOpenLink), Toast.LENGTH_SHORT
                    ).show()
                }
            })

            ListItem(headlineContent = {
                Text(text = stringResource(id = R.string.terms))
            }, leadingContent = {
                Icon(Icons.Outlined.VerifiedUser, contentDescription = null)
            }, modifier = Modifier.clickable {
                openScreen(WEBVIEW_ROUTE.replace("{url}", URLEncoder.encode(TERMS_LINK, "UTF-8")))
            })

            ListItem(headlineContent = {
                Text(text = stringResource(id = R.string.privacy))
            }, leadingContent = {
                Icon(Icons.Outlined.Lock, contentDescription = null)
            }, modifier = Modifier.clickable {
                openScreen(WEBVIEW_ROUTE.replace("{url}", URLEncoder.encode(PRIVACY_LINK, "UTF-8")))
            })

            ListItem(headlineContent = {
                Text(text = stringResource(id = R.string.instagram))
            }, leadingContent = {
                Icon(painterResource(id = R.drawable.ic_instagram), contentDescription = null)
            }, modifier = Modifier.clickable {
                try {
                    uriHandler.openUri(INSTAGRAM_LINK)
                } catch (e: Exception) {
                    Toast.makeText(
                        context, context.getString(R.string.cantOpenLink), Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    if (openThemeDialog) {
        ThemeSelectionDialog(onDismiss = { openThemeDialog = false }, onSubmit = {
            onThemeChange(it)
        }, themeOptions = themeOptions, initialTheme = appTheme
        )
    }

    if(showMySources){
        CustomAlertDialog(
            title = stringResource(id = R.string.mySources),
            confirmText = stringResource(id = R.string.done),
            dismissText = stringResource(id = R.string.cancel),
            icon = Icons.Outlined.WaterDrop,
            onDismiss = { showMySources = false },
            onConfirm = { showMySources = false },
            content = {
                LazyColumn {
                    items(viewModel.userSources){
                        ListItem(headlineContent = {
                            Text(text = it.name)
                        }, supportingContent = {
                            Text(text = if(it.approved) stringResource(id = R.string.approved) else stringResource(id = R.string.inReview))
                        })
                    }
                }
            }
        )
    }
}