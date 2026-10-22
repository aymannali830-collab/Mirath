package com.example.mirath.util

import android.content.Context
import android.content.SharedPreferences

data class SavedGameSession(
    val playerPieces: Int,
    val aiPieces: Int,
    val moveHistory: List<String>
)

object GameSessionManager {
    private const val PREF_NAME = "mirath_game_sessions"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(context: Context, gameId: String, playerPieces: Int, aiPieces: Int, history: List<String>) {
        val prefs = getPrefs(context)
        val historyStr = history.joinToString("|||")
        prefs.edit()
            .putInt("${gameId}_player", playerPieces)
            .putInt("${gameId}_ai", aiPieces)
            .putString("${gameId}_history", historyStr)
            .apply()
    }

    fun loadSession(context: Context, gameId: String): SavedGameSession? {
        val prefs = getPrefs(context)
        if (!prefs.contains("${gameId}_player")) return null
        val playerPieces = prefs.getInt("${gameId}_player", 7)
        val aiPieces = prefs.getInt("${gameId}_ai", 7)
        val historyStr = prefs.getString("${gameId}_history", "") ?: ""
        val history = if (historyStr.isBlank()) emptyList() else historyStr.split("|||")
        return SavedGameSession(playerPieces, aiPieces, history)
    }

    fun clearSession(context: Context, gameId: String) {
        val prefs = getPrefs(context)
        prefs.edit()
            .remove("${gameId}_player")
            .remove("${gameId}_ai")
            .remove("${gameId}_history")
            .apply()
    }
}
