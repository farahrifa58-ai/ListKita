package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.Strings
import com.example.ui.screens.BackupRestoreScreen
import com.example.ui.screens.CreateListScreen
import com.example.ui.screens.DetailListScreen
import com.example.ui.screens.MyListsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.ListViewModel

enum class NavTab {
    MY_LISTS,
    CREATE,
    SETTINGS
}

sealed class AppScreen {
    data class Main(val tab: NavTab) : AppScreen()
    data class Detail(val listId: Long, val isEditMode: Boolean) : AppScreen()
    object Backup : AppScreen()
    object Templates : AppScreen()
}

class MainActivity : ComponentActivity() {
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            MyApplicationTheme(themeMode = themeMode) {
                ListKitaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ListKitaApp(viewModel: ListViewModel) {
    val language by viewModel.language.collectAsState()
    
    // Splash screen state (shown once on initial launch)
    var showSplashScreen by rememberSaveable { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Main(NavTab.MY_LISTS)) }

    if (showSplashScreen) {
        SplashScreen(
            language = language,
            onSplashFinished = {
                showSplashScreen = false
            }
        )
        return
    }

    val currentTab = (currentScreen as? AppScreen.Main)?.tab ?: NavTab.MY_LISTS

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (currentScreen is AppScreen.Main) {
                Surface(
                    shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                        modifier = Modifier.padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                        )
                    ) {
                        // 1. List Saya (📋)
                        NavigationBarItem(
                            selected = currentTab == NavTab.MY_LISTS,
                            onClick = {
                                currentScreen = AppScreen.Main(NavTab.MY_LISTS)
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.FormatListBulleted,
                                    contentDescription = Strings.get("nav_my_lists", language),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = Strings.get("nav_my_lists", language),
                                    fontWeight = if (currentTab == NavTab.MY_LISTS) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryBlue,
                                selectedTextColor = PrimaryBlue,
                                indicatorColor = PrimaryBlue.copy(alpha = 0.12f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_tab_my_lists")
                        )

                        // 2. Buat Baru (➕)
                        NavigationBarItem(
                            selected = currentTab == NavTab.CREATE,
                            onClick = {
                                currentScreen = AppScreen.Main(NavTab.CREATE)
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = Strings.get("nav_create", language),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = Strings.get("nav_create", language),
                                    fontWeight = if (currentTab == NavTab.CREATE) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryBlue,
                                selectedTextColor = PrimaryBlue,
                                indicatorColor = PrimaryBlue.copy(alpha = 0.12f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_tab_create")
                        )

                        // 3. Pengaturan (⚙️)
                        NavigationBarItem(
                            selected = currentTab == NavTab.SETTINGS,
                            onClick = {
                                currentScreen = AppScreen.Main(NavTab.SETTINGS)
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = Strings.get("nav_settings", language),
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = Strings.get("nav_settings", language),
                                    fontWeight = if (currentTab == NavTab.SETTINGS) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryBlue,
                                selectedTextColor = PrimaryBlue,
                                indicatorColor = PrimaryBlue.copy(alpha = 0.12f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_tab_settings")
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Crossfade(
                targetState = currentScreen,
                animationSpec = tween(durationMillis = 200),
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    is AppScreen.Main -> {
                        when (screen.tab) {
                            NavTab.MY_LISTS -> {
                                MyListsScreen(
                                    viewModel = viewModel,
                                    onNavigateToDetail = { listId, isEdit ->
                                        viewModel.selectList(listId)
                                        currentScreen = AppScreen.Detail(listId, isEdit)
                                    },
                                    onNavigateToCreate = {
                                        currentScreen = AppScreen.Main(NavTab.CREATE)
                                    }
                                )
                            }
                            NavTab.CREATE -> {
                                CreateListScreen(
                                    viewModel = viewModel,
                                    onListCreated = { newId ->
                                        viewModel.selectList(newId)
                                        currentScreen = AppScreen.Detail(newId, true)
                                    },
                                    onBack = {
                                        currentScreen = AppScreen.Main(NavTab.MY_LISTS)
                                    }
                                )
                            }
                            NavTab.SETTINGS -> {
                                SettingsScreen(
                                    viewModel = viewModel,
                                    onNavigateToBackup = {
                                        currentScreen = AppScreen.Backup
                                    },
                                    onNavigateToTemplates = {
                                        currentScreen = AppScreen.Templates
                                    }
                                )
                            }
                        }
                    }
                    is AppScreen.Detail -> {
                        DetailListScreen(
                            viewModel = viewModel,
                            listId = screen.listId,
                            initialEditMode = screen.isEditMode,
                            onBack = {
                                viewModel.clearSelectedList()
                                currentScreen = AppScreen.Main(NavTab.MY_LISTS)
                            }
                        )
                    }
                    is AppScreen.Backup -> {
                        BackupRestoreScreen(
                            viewModel = viewModel,
                            onBack = {
                                currentScreen = AppScreen.Main(NavTab.SETTINGS)
                            }
                        )
                    }
                    is AppScreen.Templates -> {
                        TemplatesScreen(
                            viewModel = viewModel,
                            onUseTemplate = { newId ->
                                viewModel.selectList(newId)
                                currentScreen = AppScreen.Detail(newId, false)
                            },
                            onBack = {
                                currentScreen = AppScreen.Main(NavTab.SETTINGS)
                            }
                        )
                    }
                }
            }
        }
    }
}
