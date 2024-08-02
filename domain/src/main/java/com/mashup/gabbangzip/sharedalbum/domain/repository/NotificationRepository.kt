package com.mashup.gabbangzip.sharedalbum.domain.repository

import com.mashup.gabbangzip.sharedalbum.domain.model.notification.FcmTokenDomainModel
import com.mashup.gabbangzip.sharedalbum.domain.model.notification.FcmTokenParamDomainModel

interface NotificationRepository {
    suspend fun registerFcmToken(token: FcmTokenParamDomainModel): FcmTokenDomainModel
}
