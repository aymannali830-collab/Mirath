package com.example.mirath.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mirath.data.AchievementManager
import com.example.mirath.data.GameRepository
import com.example.mirath.data.LeaderboardRepository
import com.example.mirath.util.GameSessionManager
import com.example.mirath.util.MirathAudioEngine
import kotlinx.coroutines.launch

@Composable
fun SimulatorScreen(gameId: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val leaderboardRepo = remember { LeaderboardRepository(context) }
    val isMuted by MirathAudioEngine.isMuted.collectAsState()

    val game = GameRepository.games.find { it.id == gameId } ?: GameRepository.games[0]
    val matchCounts by GameRepository.matchCounts.collectAsState()
    val currentGameMatches = matchCounts[gameId] ?: 0

    // Load saved session if exists
    val savedSession = remember { GameSessionManager.loadSession(context, gameId) }

    var playerPieces by remember { mutableIntStateOf(savedSession?.playerPieces ?: 7) }
    var aiPieces by remember { mutableIntStateOf(savedSession?.aiPieces ?: 7) }
    var diceResult by remember { mutableIntStateOf(0) }
    var isPlayerTurn by remember { mutableStateOf(true) }
    var gameMessage by remember {
        mutableStateOf(
            if (savedSession != null) "تم استئناف جلستك السابقة بنجاح. استمر في اللعب!"
            else "اضغط على نرد الأهرامات الأربعة للبدء بالرمي."
        )
    }
    var hasRecordedMatch by remember { mutableStateOf(false) }
    var moveHistory by remember { mutableStateOf(savedSession?.moveHistory ?: listOf("بدء جلسة اللعبة الجديدة")) }

    fun addHistory(entry: String) {
        moveHistory = listOf(entry) + moveHistory
        GameSessionManager.saveSession(context, gameId, playerPieces, aiPieces, moveHistory)
    }

    fun rollDice() {
        MirathAudioEngine.playDiceRoll()
        val roll = (0..4).random()
        diceResult = roll
        if (roll == 0) {
            gameMessage = "حالفك الحظ السيء! صفر نقلات. الدور ينتقل للخصم."
            addHistory("رمي النرد: 0 (لا نقلات)")
            isPlayerTurn = !isPlayerTurn
        } else {
            gameMessage = "تم رمي النرد بنجاح! لديك $roll نقلات متاحة."
            addHistory("رمي النرد: $roll نقلات متاحة")
        }
    }

    fun makeMove() {
        if (diceResult > 0) {
            MirathAudioEngine.playMove()
            if (isPlayerTurn) {
                playerPieces = (playerPieces - 1).coerceAtLeast(0)
                addHistory("تقدم اللاعب قطعة (المتبقي: $playerPieces)")
                if (playerPieces == 0) {
                    gameMessage = "تهانينا! لقد فزت بملحمة أور الملكية وتفوقت على الذكاء الأثري (+100 نقطة)."
                    MirathAudioEngine.playWin()
                    if (!hasRecordedMatch) {
                        GameRepository.recordMatchPlayed(gameId)
                        AchievementManager.init(context)
                        AchievementManager.recordWin(context)
                        coroutineScope.launch {
                            leaderboardRepo.submitScore(gameId, 100L)
                        }
                        hasRecordedMatch = true
                        GameSessionManager.clearSession(context, gameId)
                    }
                } else {
                    gameMessage = "خطوة موفقة! تم تقدم القطعة. دور الذكاء الاصطناعي..."
                    isPlayerTurn = false
                }
            } else {
                aiPieces = (aiPieces - 1).coerceAtLeast(0)
                MirathAudioEngine.playCapture()
                addHistory("تحرك الذكاء الاصطناعي (المتبقي: $aiPieces)")
                if (aiPieces == 0) {
                    gameMessage = "فاز الذكاء الاصطناعي الأثري بهذه الجولة. حاول مرة أخرى!"
                    if (!hasRecordedMatch) {
                        GameRepository.recordMatchPlayed(gameId)
                        hasRecordedMatch = true
                        GameSessionManager.clearSession(context, gameId)
                    }
                } else {
                    gameMessage = "قام الذكاء الاصطناعي بالتحرك. دورك الآن!"
                    isPlayerTurn = true
                }
            }
            diceResult = 0
            GameSessionManager.saveSession(context, gameId, playerPieces, aiPieces, moveHistory)
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
        moveHistory = listOf("إعادة ضبط الجولة وبدء من جديد")
        GameSessionManager.clearSession(context, gameId)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "المحاكي (مبارياتك الحقيقية: $currentGameMatches)",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { MirathAudioEngine.toggleMute() },
                    modifier = Modifier.testTag("audio_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.Star else Icons.Default.Refresh,
                        contentDescription = "تبديل الصوت"
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
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
                        Text("إعادة ضبط الجولة وحذف الجلسة المحفوظة")
                    }
                }
            }
        }

        // Move History Viewer Section
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "سجل الحركات السابقة (Move History)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "تتبع خطواتك وتسلسل الحركات السابقة في هذه الجلسة (محفوظة تلقائياً):",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (moveHistory.isEmpty()) {
                        Text("لا توجد حركات مسجلة بعد.", style = MaterialTheme.typography.bodySmall)
                    } else {
                        moveHistory.take(8).forEachIndexed { index, move ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "•",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = move,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
