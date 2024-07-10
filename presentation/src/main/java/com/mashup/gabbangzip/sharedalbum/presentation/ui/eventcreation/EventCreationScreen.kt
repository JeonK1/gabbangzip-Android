package com.mashup.gabbangzip.sharedalbum.presentation.ui.eventcreation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.gabbangzip.sharedalbum.presentation.theme.Gray0
import com.mashup.gabbangzip.sharedalbum.presentation.theme.Gray0Alpha80
import com.mashup.gabbangzip.sharedalbum.presentation.theme.Gray80
import com.mashup.gabbangzip.sharedalbum.presentation.theme.PicTypography
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.PicBackButtonTopBar
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.PicButton
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.PicDatePickerField
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.PicGallery
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.PicTextField
import com.mashup.gabbangzip.sharedalbum.presentation.utils.hideKeyboardOnOutsideClicked

@Composable
fun EventCreationScreen(
    onCompleteButtonClicked: () -> Unit,
    onGalleryButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var description by remember { mutableStateOf("") }
    val buttonEnabled by remember { derivedStateOf { description.isNotBlank() } }

    Column(
        modifier = Modifier.hideKeyboardOnOutsideClicked(),
    ) {
        PicBackButtonTopBar(
            modifier = Modifier
                .background(Gray0Alpha80)
                .padding(top = 16.dp),
            titleText = "이벤트 만들기",
            backButtonClicked = {
                focusManager.clearFocus()
                onBackButtonClicked()
            },
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
        ) {
            EventCreationTitle(text = "이벤트 한줄 요약")
            PicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                value = description,
                onValueChange = { description = it },
                hint = "이벤트를 한줄로 요약해주세요.",
                maxLength = 10,
            )
            EventCreationTitle(text = "날짜")
            PicDatePickerField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                date = "24/10/26",
            )
            EventCreationTitle(text = "사진 선택")
            PicGallery(
                modifier = Modifier.padding(top = 16.dp),
                currentCount = 0,
                totalCount = 4,
            )
        }
        PicButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, bottom = 16.dp),
            text = "완료",
            isRippleClickable = true,
            enable = buttonEnabled,
            onButtonClicked = {
                focusManager.clearFocus()
                onCompleteButtonClicked()
            },
        )
    }
}

@Composable
private fun EventCreationTitle(text: String) {
    Text(
        text = text,
        style = PicTypography.headBold18,
        color = Gray80,
    )
}

@Preview(showBackground = true)
@Composable
private fun EventCreationScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray0),
    ) {
        EventCreationScreen(
            onCompleteButtonClicked = {},
            onGalleryButtonClicked = {},
            onBackButtonClicked = {},
        )
    }
}
