package com.mashup.gabbangzip.sharedalbum.data.service

import com.mashup.gabbangzip.sharedalbum.data.base.PicResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.request.event.CreateEventRequest
import com.mashup.gabbangzip.sharedalbum.data.dto.request.event.UploadImagesRequest
import com.mashup.gabbangzip.sharedalbum.data.dto.response.event.CreateEventResponse
import com.mashup.gabbangzip.sharedalbum.data.dto.response.event.UploadImagesResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface EventService {
    @POST("api/v1/events")
    suspend fun createEvent(
        @Body createEventRequest: CreateEventRequest,
    ): PicResponse<CreateEventResponse>

    @POST("api/v1/events/images")
    suspend fun uploadImages(
        @Body uploadImagesRequest: UploadImagesRequest,
    ): PicResponse<UploadImagesResponse>
}
