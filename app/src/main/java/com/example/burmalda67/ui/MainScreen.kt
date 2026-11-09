package com.example.burmalda67.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.fragment.compose.AndroidFragment
import com.example.burmalda67.R
import com.example.burmalda67.ui.combinations.CombinationsFragment
import com.example.burmalda67.ui.lobby.LobbyFragment
import com.example.burmalda67.ui.profile.ProfileFragment
import com.example.burmalda67.ui.room.RoomFragment

private enum class Tab(@StringRes val label: Int, val icon: ImageVector) {
    LOBBY(R.string.nav_lobby, Icons.Filled.Home),
    COMBINATIONS(R.string.nav_combinations, Icons.Filled.Style),
    PROFILE(R.string.nav_profile, Icons.Filled.Person),
}

@Composable
fun MainScreen(deepLinkRoom: String?, onDeepLinkHandled: () -> Unit) {
    var tab by rememberSaveable { mutableStateOf(Tab.LOBBY) }
    var roomCode by rememberSaveable { mutableStateOf<String?>(null) }
    val tabStates = rememberSaveableStateHolder()

    LaunchedEffect(deepLinkRoom) {
        if (deepLinkRoom != null) {
            roomCode = deepLinkRoom
            onDeepLinkHandled()
        }
    }

    BackHandler(enabled = roomCode != null) { roomCode = null }
    BackHandler(enabled = roomCode == null && tab != Tab.LOBBY) { tab = Tab.LOBBY }

    // комната выезжает справа и уезжает обратно, как раньше отдельная активити
    AnimatedContent(
        targetState = roomCode,
        transitionSpec = {
            val dir = if (targetState != null) SlideDirection.Start else SlideDirection.End
            (slideIntoContainer(dir, tween(ANIM_MS)) + fadeIn(tween(ANIM_MS))) togetherWith
                (slideOutOfContainer(dir, tween(ANIM_MS)) + fadeOut(tween(ANIM_MS)))
        },
        label = "room",
    ) { code ->
        if (code != null) {
            key(code) {
                AndroidFragment<RoomFragment>(
                    modifier = Modifier.fillMaxSize(),
                    arguments = bundleOf(RoomFragment.ARG_CODE to code),
                )
            }
        } else {
            Tabs(tab = tab, onTabChange = { tab = it }, tabStates = tabStates, onOpenRoom = { roomCode = it })
        }
    }
}

@Composable
private fun Tabs(
    tab: Tab,
    onTabChange: (Tab) -> Unit,
    tabStates: SaveableStateHolder,
    onOpenRoom: (String) -> Unit,
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { item ->
                    NavigationBarItem(
                        selected = item == tab,
                        onClick = { onTabChange(item) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.label)) },
                    )
                }
            }
        },
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())

        // плавная смена вкладок, как была у NavHost по умолчанию
        AnimatedContent(
            targetState = tab,
            transitionSpec = { fadeIn(tween(TAB_FADE_MS)) togetherWith fadeOut(tween(TAB_FADE_MS)) },
            label = "tabs",
        ) { current ->
            tabStates.SaveableStateProvider(current.name) {
                when (current) {
                    Tab.LOBBY -> AndroidFragment<LobbyFragment>(
                        modifier = modifier,
                        onUpdate = { it.onOpenRoom = onOpenRoom },
                    )
                    Tab.COMBINATIONS -> AndroidFragment<CombinationsFragment>(modifier = modifier)
                    Tab.PROFILE -> AndroidFragment<ProfileFragment>(modifier = modifier)
                }
            }
        }
    }
}

private const val TAB_FADE_MS = 700
private const val ANIM_MS = 350
