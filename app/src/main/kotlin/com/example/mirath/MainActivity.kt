package com.example.mirath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mirath.data.GameRepository
import com.example.mirath.ui.screens.*
import com.example.mirath.ui.theme.MirathTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "الرئيسية", Icons.Default.Home)
    object Catalog : Screen("catalog", "الكتالوج", Icons.Default.List)
    object Leaderboard : Screen("leaderboard", "المتصدرين", Icons.Default.Star)
    object Achievements : Screen("achievements", "الإنجازات", Icons.Default.CheckCircle)
    object Map : Screen("map", "الخريطة", Icons.Default.Place)
    object Articles : Screen("articles", "المقالات", Icons.Default.List)
    object Stats : Screen("stats", "الإحصائيات", Icons.Default.Star)
    object About : Screen("about", "عن مِرث", Icons.Default.Info)
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MirathTheme {
                val navController = rememberNavController()
                val items = listOf(
                    Screen.Home,
                    Screen.Catalog,
                    Screen.Leaderboard,
                    Screen.Achievements,
                    Screen.Map,
                    Screen.Articles,
                    Screen.Stats,
                    Screen.About
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("مِرث | Mirath", fontWeight = FontWeight.Bold) },
                            actions = {
                                IconButton(
                                    onClick = {
                                        val randomGame = GameRepository.games.random()
                                        navController.navigate("simulator/${randomGame.id}")
                                    },
                                    modifier = Modifier.testTag("surprise_me_button")
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "فاجئني")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        if (currentRoute != screen.route) {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    modifier = Modifier.testTag("nav_${screen.route}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onNavigateToCatalog = { navController.navigate(Screen.Catalog.route) },
                                onNavigateToSimulator = { gameId -> navController.navigate("simulator/$gameId") },
                                onNavigateToStats = { navController.navigate(Screen.Stats.route) }
                            )
                        }
                        composable(Screen.Catalog.route) {
                            CatalogScreen(
                                onNavigateToSimulator = { gameId -> navController.navigate("simulator/$gameId") }
                            )
                        }
                        composable(Screen.Leaderboard.route) {
                            LeaderboardScreen()
                        }
                        composable(Screen.Achievements.route) {
                            AchievementsScreen()
                        }
                        composable(
                            route = "simulator/{gameId}",
                            arguments = listOf(navArgument("gameId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val gameId = backStackEntry.arguments?.getString("gameId") ?: "ur"
                            SimulatorScreen(gameId = gameId)
                        }
                        composable(Screen.Map.route) {
                            MapScreen()
                        }
                        composable(Screen.Articles.route) {
                            ArticlesScreen()
                        }
                        composable(Screen.Stats.route) {
                            StatsScreen()
                        }
                        composable(Screen.About.route) {
                            AboutScreen()
                        }
                    }
                }
            }
        }
    }
}
