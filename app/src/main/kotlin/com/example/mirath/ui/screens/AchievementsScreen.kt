package com.example.mirath.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mirath.data.AchievementManager

@Composable
fun AchievementsScreen() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        AchievementManager.init(context)
    }
    val unlockedSet by AchievementManager.unlockedAchievements.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "ملف الباحث والشارات المحلية",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "الإنجازات والشارات الأثرية",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "تتبع إنجازاتك في المحاكي (انتصارك الأول، سلسلة الانتصارات، والاستراتيجي الخبير).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(AchievementManager.achievementsList) { achievement ->
            val isUnlocked = unlockedSet.contains(achievement.id)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isUnlocked) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    1.dp,
                    if (isUnlocked) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (isUnlocked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = achievement.iconName,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = achievement.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                color = if (isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = if (isUnlocked) "مكتمل ✓" else "مغلق 🔒",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isUnlocked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = achievement.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
