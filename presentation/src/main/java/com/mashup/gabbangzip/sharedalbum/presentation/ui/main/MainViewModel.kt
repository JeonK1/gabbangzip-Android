package com.mashup.gabbangzip.sharedalbum.presentation.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.mashup.gabbangzip.sharedalbum.domain.model.notification.FcmNotificationParamDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.usecase.UploadMyPicUseCase
import com.mashup.gabbangzip.sharedalbum.domain.usecase.notification.RegisterFcmTokenUseCase
import com.mashup.gabbangzip.sharedalbum.domain.usecase.notification.SendFcmNotificationUseCase
import com.mashup.gabbangzip.sharedalbum.presentation.ui.model.MainEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    private val uploadMyPicUseCase: UploadMyPicUseCase,
    private val sendFcmNotificationUseCase: SendFcmNotificationUseCase,
) : ViewModel() {

    private val _mainEvent = MutableSharedFlow<MainEvent>()
    val mainEvent = _mainEvent.asSharedFlow()

    fun registerFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            viewModelScope.launch {
                val token = task.result
                registerFcmTokenUseCase(token)
            }
        }
    }

    fun uploadMyPic(uri: Uri) {
        viewModelScope.launch {
            uploadMyPicUseCase()
        }
    }

    fun sendKookNotification(eventId: Long) {
        viewModelScope.launch {
            sendFcmNotificationUseCase(
                FcmNotificationParamDomainModel(
                    eventId = eventId,
                ),
            ).onSuccess {
                _mainEvent.emit(MainEvent.SuccessNotification)
            }.onFailure {
                _mainEvent.emit(MainEvent.FailNotification)
            }
        }
    }
}
