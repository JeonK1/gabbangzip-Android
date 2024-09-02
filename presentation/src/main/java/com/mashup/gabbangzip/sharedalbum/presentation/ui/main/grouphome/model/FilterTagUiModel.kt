package com.mashup.gabbangzip.sharedalbum.presentation.ui.main.grouphome.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.mashup.gabbangzip.sharedalbum.presentation.ui.model.GroupKeyword

data class FilterTagUiModel(
    val name: String,
    @DrawableRes val symbolResId: Int?,
    @DrawableRes val symbolColor: Color,
    @StringRes val tagNameResId: Int,
    val isSelected: Boolean,
)

fun GroupKeyword.toFilterTagUiModel() = FilterTagUiModel(
    name = name,
    symbolResId = symbolResId,
    symbolColor = symbolColor,
    tagNameResId = tagNameResId,
    isSelected = false,
)
