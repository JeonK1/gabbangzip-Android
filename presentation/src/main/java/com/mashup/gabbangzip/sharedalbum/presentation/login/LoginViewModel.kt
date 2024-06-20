package com.mashup.gabbangzip.sharedalbum.presentation.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.gabbangzip.sharedalbum.domain.model.LoginParam
import com.mashup.gabbangzip.sharedalbum.domain.usecase.LoginUseCase
import com.mashup.gabbangzip.sharedalbum.presentation.auth.KakaoUserSdkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    fun login() {
        KakaoUserSdkUtil.loginWithKakao(
            context = context,
            onSuccess = { idToken, profile ->
                if (profile.nickname != null && profile.profileImageUrl != null) {
                    picLogin(
                        idToken = idToken,
                        nickname = profile.nickname!!,
                        profileImage = profile.profileImageUrl!!,
                    )
                } else {
                    Log.d(TAG, "정보 조회 실패")
                }
            },
            onFailure = {
                Log.d(TAG, "카카오 로그인 실패 또는 정보 조회 실패 $it")
            },
        )
    }

    private fun picLogin(idToken: String, nickname: String, profileImage: String) {
        val param = LoginParam(
            idToken = idToken,
            nickname = nickname,
            profileImage = profileImage,
        )
        viewModelScope.launch {
            loginUseCase(param)
                .onSuccess {
                    Log.d(TAG, "로그인 성공")
                }.onFailure {
                    Log.d(TAG, "로그인 실패 $it")
                }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
