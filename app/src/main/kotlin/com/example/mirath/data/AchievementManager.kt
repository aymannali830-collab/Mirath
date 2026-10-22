package com.example.mirath.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val iconName: String
)

object AchievementManager {
    private const val PREF_NAME = "mirath_achievements"

    val achievementsList = listOf(
        Achievement("first_win", "انتصارك الأول (First Win)", "أكمل فوزك الأول في أي لعبة تاريخية على المحاكي.", false, "🏆"),
        Achievement("winning_streak", "سلسلة الانتصارات (Winning Streak)", "حقق انتصارين متتاليين في جولات المحاكي.", false, "🔥"),
        Achievement("expert_strategist", "الاستراتيجي الخبير (Expert Strategist)", "أكمل 5 جولات ناجحة أو أكثر في المحاكي.", false, "🧠")
    )

    private val _unlockedAchievements = MutableStateFlow<Set<String>>(emptySet())
    val unlockedAchievements: StateFlow<Set<String>> = _unlockedAchievements.asStateFlow()

    private var winsCount = 0

    fun init(context: Context) {
        val prefs = getPrefs(context)
        val unlocked = prefs.getStringSet("unlocked", emptySet()) ?: emptySet()
        _unlockedAchievements.value = unlocked
        winsCount = prefs.getInt("wins_count", 0)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun recordWin(context: Context) {
        winsCount++
        val prefs = getPrefs(context)
        prefs.edit().putInt("wins_count", winsCount).apply()

        val currentUnlocked = _unlockedAchievements.value.toMutableSet()
        var updated = false

        // First Win
        if (winsCount >= 1 && !currentUnlocked.contains("first_win")) {
            currentUnlocked.add("first_win")
            updated = true
        }
        // Winning Streak (2 wins)
        if (winsCount >= 2 && !currentUnlocked.contains("winning_streak")) {
            currentUnlocked.add("winning_streak")
            updated = true
        }
        // Expert Strategist (5 wins)
        if (winsCount >= 5 && !currentUnlocked.contains("expert_strategist")) {
            currentUnlocked.add("expert_strategist")
            updated = true
        }

        if (updated) {
            _unlockedAchievements.value = currentUnlocked
            prefs.edit().putStringSet("unlocked", currentUnlocked).apply()
        }
    }
}
