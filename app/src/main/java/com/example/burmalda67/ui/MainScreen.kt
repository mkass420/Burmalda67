package com.example.burmalda67.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    val topBar = topBarState(tab, roomCode)

    Scaffold(
        topBar = {
            AppTopBar(state = topBar, onNavigateUp = { roomCode = null })
        },
    ) { innerPadding ->
        // Навбар живёт внутри экрана вкладок и уезжает вместе с ним. Если прятать его
        // в Scaffold, высота контента меняется и всё дёргается при входе в комнату.
        // комната выезжает справа и уезжает обратно, как раньше отдельная активити
        AnimatedContent(
            targetState = roomCode,
            transitionSpec = {
                val dir = if (targetState != null) SlideDirection.Start else SlideDirection.End
                (slideIntoContainer(dir, tween(ANIM_MS)) + fadeIn(tween(ANIM_MS))) togetherWith
                    (slideOutOfContainer(dir, tween(ANIM_MS)) + fadeOut(tween(ANIM_MS)))
            },
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            label = "room",
        ) { code ->
            if (code != null) {
                key(code) {
                    AndroidFragment<RoomFragment>(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding(),
                        arguments = bundleOf(RoomFragment.ARG_CODE to code),
                    )
                }
            } else {
                Tabs(tab = tab, onTabChange = { tab = it }, tabStates = tabStates, onOpenRoom = { roomCode = it })
            }
        }
    }
}

// Что показывает тулбар: заголовок и нужна ли стрелка «назад».
// Тот же принцип, что у AppBarConfiguration из Navigation: на экранах верхнего
// уровня (вкладках) стрелки нет, на вложенных (комната) она есть.
private data class TopBarState(@StringRes val title: Int, val canNavigateUp: Boolean)

private fun topBarState(tab: Tab, roomCode: String?): TopBarState =
    if (roomCode != null) TopBarState(R.string.room_title, canNavigateUp = true)
    else TopBarState(tab.label, canNavigateUp = false)

// Стрелка и заголовок меняются одним блоком (старый гаснет, новый проявляется),
// иначе при исчезновении стрелки заголовок съезжает влево
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(state: TopBarState, onNavigateUp: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Crossfade(
            targetState = state,
            modifier = Modifier
                .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                .fillMaxWidth()
                .height(64.dp),
            label = "topBar",
        ) { current ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (current.canNavigateUp) {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.room_back),
                        )
                    }
                } else {
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    stringResource(current.title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
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
    Column(Modifier.fillMaxSize()) {
        // плавная смена вкладок, как была у NavHost по умолчанию
        AnimatedContent(
            targetState = tab,
            transitionSpec = { fadeIn(tween(TAB_FADE_MS)) togetherWith fadeOut(tween(TAB_FADE_MS)) },
            modifier = Modifier.weight(1f),
            label = "tabs",
        ) { current ->
            val modifier = Modifier.fillMaxSize()
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
    }
}

private const val TAB_FADE_MS = 700
private const val ANIM_MS = 350
