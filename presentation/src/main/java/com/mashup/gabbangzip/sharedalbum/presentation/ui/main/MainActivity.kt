package com.mashup.gabbangzip.sharedalbum.presentation.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mashup.gabbangzip.sharedalbum.presentation.R
import com.mashup.gabbangzip.sharedalbum.presentation.theme.SharedAlbumTheme
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.PicSnackbarHost
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.model.PicSnackbarType
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.showPicSnackbar
import com.mashup.gabbangzip.sharedalbum.presentation.ui.groupcreation.GroupCreationActivity
import com.mashup.gabbangzip.sharedalbum.presentation.ui.login.LoginActivity
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.navigation.MainNavHost
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.navigation.MainRoute
import com.mashup.gabbangzip.sharedalbum.presentation.ui.model.MainEvent
import com.mashup.gabbangzip.sharedalbum.presentation.utils.FileUtil
import com.mashup.gabbangzip.sharedalbum.presentation.utils.PicPhotoPicker
import com.mashup.gabbangzip.sharedalbum.presentation.utils.shareBitmap
import com.mashup.gabbangzip.sharedalbum.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var photoPicker: PicPhotoPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.registerFcmToken()
        initPhotoPicker()

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

            SharedAlbumTheme {
                ObserveEvent(snackbarHostState)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { PicSnackbarHost(state = snackbarHostState) },
                ) { innerPadding ->
                    MainNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navController = rememberNavController(),
                        startDestination = MainRoute.initRoute,
                        navigateLoginAndFinish = {
                            LoginActivity.openActivity(this)
                            finish()
                        },
                        navigateToGroupCreationAndFinish = {
                            GroupCreationActivity.openActivity(this)
                            finish()
                        },
                        onClickOpenPhotoPickerButton = {
                            photoPicker.open()
                        },
                        onClickSendFcmButton = { eventId ->
                            viewModel.sendKookNotification(eventId)
                        },
                        onClickShareButton = { bitmap ->
                            shareBitmap(bitmap)
                        },
                        onSnackbarRequired = { type, message ->
                            coroutineScope.launch {
                                snackbarHostState.showPicSnackbar(type, message)
                            }
                        },
                        onErrorEvent = { showToast(R.string.error_retry) },
                    )
                }
            }
        }
    }

    private fun initPhotoPicker() {
        photoPicker = PicPhotoPicker.create(
            activity = this,
            max = PICTURES_MAX_COUNT,
        ) { uriList ->
            uriList.mapNotNull { uri ->
                FileUtil.getFileFromUri(this, uri)
            }.also {
                viewModel.uploadMyPic(0, it)
            }
        }
    }

    @Composable
    private fun ObserveEvent(snackbarHostState: SnackbarHostState) {
        LaunchedEffect(null) {
            viewModel.mainEvent.collect { event ->
                when (event) {
                    MainEvent.SuccessNotification -> snackbarHostState.showPicSnackbar(
                        type = PicSnackbarType.NORMAL,
                        message = getString(R.string.kook_snackbar),
                    )

                    MainEvent.FailNotification -> showToast(R.string.error_retry)

                    MainEvent.SuccessUploadMyPic -> {
                        snackbarHostState.showPicSnackbar(
                            type = PicSnackbarType.NORMAL,
                            message = getString(R.string.my_pic_upload_complete),
                        )
                    }

                    MainEvent.FailUploadMyPic -> showToast(R.string.error_retry)
                }
            }
        }
    }

    companion object {
        const val PICTURES_MAX_COUNT = 4

        fun openActivity(context: Activity) {
            context.startActivity(
                Intent(context, MainActivity::class.java),
            )
        }

        fun openActivity(context: Activity, flags: Int) {
            context.startActivity(
                Intent(context, MainActivity::class.java).apply {
                    addFlags(flags)
                },
            )
        }
    }
}
