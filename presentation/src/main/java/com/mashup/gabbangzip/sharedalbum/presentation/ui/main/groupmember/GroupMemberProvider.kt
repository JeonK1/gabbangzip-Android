package com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupmember

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupmember.model.Member
import com.mashup.gabbangzip.sharedalbum.presentation.ui.model.GroupKeyWord
import com.mashup.gabbangzip.sharedalbum.presentation.utils.ImmutableList

class GroupMemberProvider : PreviewParameterProvider<GroupMemberUiState> {
    override val values: Sequence<GroupMemberUiState>
        get() = sequenceOf(
            GroupMemberUiState(
                keyWord = GroupKeyWord.EXERCISE,
                members = ImmutableList(
                    listOf(
                        Member(id = 0, name = "연규", isLeader = true),
                        Member(id = 1, name = "윤서", isLeader = false),
                        Member(id = 1, name = "윤서", isLeader = false),
                        Member(id = 1, name = "윤서", isLeader = false),
                        Member(id = 1, name = "윤서", isLeader = false),
                        Member(id = 1, name = "윤서", isLeader = false),
                    ),
                ),
            ),
            GroupMemberUiState(
                keyWord = GroupKeyWord.SCHOOL,
                members = ImmutableList(
                    listOf(
                        Member(id = 0, name = "연규", isLeader = true),
                    ),
                ),
            ),
        )
}
