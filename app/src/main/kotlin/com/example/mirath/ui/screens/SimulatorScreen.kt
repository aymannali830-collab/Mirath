package com.example.mirath.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mirath.data.GameRepository
import com.example.mirath.data.LeaderboardRepository
import kotlinx.coroutines.launch

@Composable
fun SimulatorScreen(gameId: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val leaderboardRepo = remember { LeaderboardRepository(context) }

    val game = GameRepository.games.find { it.id == gameId } ?: GameRepository.games[0]
    val matchCounts by GameRepository.matchCounts.collectAsState()
    val currentGameMatches = matchCounts[gameId] ?: 0

    var playerPieces by remember { mutableIntStateOf(7) }
    var aiPieces by remember { mutableIntStateOf(7) }
    var diceResult by remember { mutableIntStateOf(0) }
    var isPlayerTurn by remember { mutableStateOf(true) }
    var gameMessage by remember { mutableStateOf("اضغط على نرد الأهرامات الأربعة للبدء بالرمي.") }
    var hasRecordedMatch by remember { mutableStateOf(false) }

    fun rollDice() {
        val roll = (0..4).random()
        diceResult = roll
        if (roll == 0) {
            gameMessage = "حالفك الحظ السيء! صفر نقلات. الدور ينتقل للخصم."
            isPlayerTurn = !isPlayerTurn
        } else {
            gameMessage = "تم رمي النرد بنجاح! لديك $roll نقلات متاحة."
        }
    }

    fun makeMove() {
        if (diceResult > 0) {
            if (isPlayerTurn) {
                playerPieces = (playerPieces - 1).coerceAtLeast(0)
                if (playerPieces == 0) {
                    gameMessage = "تهانينا! لقد فزت بملحمة أور الملكية وتفوقت على الذكاء الأثري (+100 نقطة)."
                    if (!hasRecordedMatch) {
                        GameRepository.recordMatchPlayed(gameId)
                        coroutineScope.launch {
                            leaderboardRepo.submitScore(gameId, 100L)
                        }
                        hasRecordedMatch = true
                    }
                } else {
                    gameMessage = "خطوة موفقة! تم تقدم القطعة. دور الذكاء الاصطناعي..."
                    isPlayerTurn = false
                }
            } else {
                aiPieces = (aiPieces - 1).coerceAtLeast(0)
                if (aiPieces == 0) {
                    gameMessage = "فاز الذكاء الاصطناعي الأثري بهذه الجولة. حاول مرة أخرى!"
                    if (!hasRecordedMatch) {
                        GameRepository.recordMatchPlayed(gameId)
                        hasRecordedMatch = true
                    }
                } else {
                    gameMessage = "قام الذكاء الاصطناعي بالتحرك. دورك الآن!"
                    isPlayerTurn = true
                }
            }
            diceResult = 0
        } else {
            gameMessage = "يرجى رمي النرد أولاً قبل محاولة التحريك."
        }
    }

    fun resetGame() {
        playerPieces = 7
        aiPieces = 7
        diceResult = 0
        isPlayerTurn = true
        gameMessage = "تم إعادة ضبط اللوحة. ابدأ برمي النرد."
        hasRecordedMatch = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "المحاكي التفاعلي الحي (مبارياتك الحقيقية: $currentGameMatches)",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${game.region} • ${game.dateStr}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Score & Status Box
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("قطع اللاعب (أنت)", style = MaterialTheme.typography.labelMedium)
                            Text("$playerPieces / 7", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                        Divider(modifier = Modifier.height(40.dp).width(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("قطع الخصم (الذكاء الأثري)", style = MaterialTheme.typography.labelMedium)
                            Text("$aiPieces / 7", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = gameMessage,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Dice Rolling Mechanism & Board Simulation
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "نرد الأهرامات الأربعة (معيار المتحف البريطاني)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { rollDice() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("roll_dice_button")
                        ) {
                            Text("رمي النرد الهرمي")
                        }

                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "النتيجة: $diceResult",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = { makeMove() },
                        enabled = diceResult > 0 && playerPieces > 0 && aiPieces > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        modifier = Modifier.testTag("make_move_button")
                    ) {
                        Text("التحرك على اللوح (مربع الروزيت ✿)")
                    }

                    OutlinedButton(
                        onClick = { resetGame() },
                        modifier = Modifier.testTag("reset_game_button")
                    ) {
                        Text("إعادة ضبط الجولة")
                    }
                }
            }
        }
    }
}
