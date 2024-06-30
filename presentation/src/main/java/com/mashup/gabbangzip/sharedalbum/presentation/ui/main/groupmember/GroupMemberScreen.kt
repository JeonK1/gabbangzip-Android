package com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupmember

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mashup.gabbangzip.sharedalbum.presentation.R
import com.mashup.gabbangzip.sharedalbum.presentation.theme.Gray0
import com.mashup.gabbangzip.sharedalbum.presentation.theme.Gray60
import com.mashup.gabbangzip.sharedalbum.presentation.theme.Gray80
import com.mashup.gabbangzip.sharedalbum.presentation.theme.PicTypography
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.PicNormalButton
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.TopBar
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.TopBarIcon
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.TopBarTitleAlign
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupmember.model.Member
import com.mashup.gabbangzip.sharedalbum.presentation.ui.model.GroupKeyWord
import com.mashup.gabbangzip.sharedalbum.presentation.utils.ImmutableList

private val topBarHeight = 56.dp

@Composable
fun GroupMemberScreen(
    onClickBackButton: () -> Unit,
    viewModel: GroupMemberViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GroupMemberScreen(
        state = state,
        onClickBackButton = onClickBackButton,
        onClickCopyButton = { /* TODO */ },
    )
}

@Composable
private fun GroupMemberScreen(
    state: GroupMemberUiState,
    onClickBackButton: () -> Unit,
    onClickCopyButton: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray0),
    ) {
        TopBar(
            modifier = Modifier.height(topBarHeight),
            leftIcon = TopBarIcon(
                size = 26.dp,
                leftPadding = 16.dp,
                description = "뒤로가기",
                resId = R.drawable.ic_back,
                iconClickListener = onClickBackButton,
            ),
            titleText = stringResource(id = R.string.group_member_list_title),
            titleAlign = TopBarTitleAlign.CENTER,
            topPadding = 16.dp,
            bottomPadding = 16.dp,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topBarHeight),
        ) {
            GroupMemberList(
                type = state.keyWord,
                list = state.members,
            )
            InvitationSection(
                isEnabled = state.isFull.not(),
                onButtonClick = onClickCopyButton,
            )
        }
    }
}

@Composable
private fun GroupMemberList(
    modifier: Modifier = Modifier,
    type: GroupKeyWord,
    list: ImmutableList<Member>,
) {
    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 280.dp),
    ) {
        list.forEach { member ->
            GroupMemberItem(
                type = type,
                member = member,
            )
        }
    }
}

@Composable
private fun GroupMemberItem(
    modifier: Modifier = Modifier,
    type: GroupKeyWord,
    member: Member,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        Image(
            modifier = Modifier
                .size(24.dp),
            painter = painterResource(id = type.symbolResId),
            contentDescription = type.name,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.name,
                style = PicTypography.bodyMedium16,
                color = Gray80,
            )
            if (member.isMaster) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    text = "그룹장",
                    style = PicTypography.textNormal14,
                    color = Gray80,
                )
            }
        }
    }
}

@Composable
private fun InvitationSection(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(
                if (isEnabled) {
                    R.string.group_add_more_crew
                } else {
                    R.string.group_maximum_count_error
                },
            ),
            style = PicTypography.bodyMedium14,
            color = Gray60,
        )
        Spacer(modifier = Modifier.height(8.dp))
        PicNormalButton(
            text = "링크 복사",
            iconRes = R.drawable.ic_link,
            enable = isEnabled,
            onButtonClicked = onButtonClick,
        )
    }
}

@Composable
@Preview
private fun GroupMemberScreenPreview(
    @PreviewParameter(GroupMemberProvider::class) state: GroupMemberUiState,
) {
    GroupMemberScreen(
        state = state,
        onClickBackButton = {},
        onClickCopyButton = {},
    )
}
