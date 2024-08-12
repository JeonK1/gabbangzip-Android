package com.mashup.gabbangzip.sharedalbum.presentation.ui.main.navigation

import android.content.Intent
import android.graphics.Bitmap
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mashup.gabbangzip.sharedalbum.presentation.ui.common.model.PicSnackbarType
import com.mashup.gabbangzip.sharedalbum.presentation.ui.eventcreation.EventCreationActivity
import com.mashup.gabbangzip.sharedalbum.presentation.ui.groupcreation.GroupCreationActivity
import com.mashup.gabbangzip.sharedalbum.presentation.ui.invitation.InvitationCodeActivity
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupdetail.navigation.groupDetailNavGraph
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupdetail.navigation.navigateGroupDetail
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.grouphome.navigation.groupHomeNavGraph
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupmember.navigation.groupMemberNavGraph
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.groupmember.navigation.navigateGroupMember
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.mypage.navigation.myPageNavGraph
import com.mashup.gabbangzip.sharedalbum.presentation.ui.main.mypage.navigation.navigateMyPage
import com.mashup.gabbangzip.sharedalbum.presentation.ui.vote.VoteActivity

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    navigateLoginAndFinish: () -> Unit,
    onClickOpenPhotoPickerButton: () -> Unit,
    onClickPokeButton: () -> Unit,
    onClickShareButton: (Bitmap) -> Unit,
    onSnackbarRequired: (PicSnackbarType, String) -> Unit,
) {
    val context = LocalContext.current

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        groupHomeNavGraph(
            onClickGroupDetail = { id -> navController.navigateGroupDetail(id) },
            onClickMyPage = { navController.navigateMyPage() },
            onClickEventMake = { id -> EventCreationActivity.openActivity(context, id) },
            onClickGroupMake = { GroupCreationActivity.openActivity(context) },
            onClickGroupEnter = { InvitationCodeActivity.openActivity(context) },
            onClickSendFcm = { /* TODO : 푸시 알림 */ },
        )
        groupDetailNavGraph(
            onClickGroupMemberButton = { id, keyword ->
                navController.navigateGroupMember(id, keyword)
            },
            onClickBackButton = { navController.popBackStack() },
            onClickOpenPhotoPickerButton = onClickOpenPhotoPickerButton,
            onClickPokeButton = onClickPokeButton,
            onClickVoteButton = { VoteActivity.openActivity(context) },
            onClickEventMake = { id -> EventCreationActivity.openActivity(context, id) },
            onClickShareButton = onClickShareButton,
            onClickHistoryItem = { /* TODO */ },
        )
        groupMemberNavGraph(
            onClickBackButton = { navController.popBackStack() },
            onShowSnackbar = onSnackbarRequired,
        )
        myPageNavGraph(
            onClickBack = { navController.popBackStack() },
            onClickNotificationSetting = {
                context.startActivity(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    },
                )
            },
            navigateLoginAndFinish = navigateLoginAndFinish,
        )
    }
}
