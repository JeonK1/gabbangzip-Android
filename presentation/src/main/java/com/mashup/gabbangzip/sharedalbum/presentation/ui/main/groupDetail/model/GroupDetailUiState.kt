package com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupDetail.model

import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.grouphome.model.GroupInfo
import com.mashup.gabbangzip.sharedalbum.presentation.utils.ImmutableList

data class GroupDetailUiState(
    val groupInfo: GroupInfo,
    val recentEvent: GroupEvent? = null,
    val history: ImmutableList<HistoryItem>,
)
