package com.example.mirath.util

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

enum class MatchSyncState {
    PENDING,
    LOCAL_ONLY,
    SYNCED,
    FAILED
}

data class MatchResultRecord(
    val matchId: String,
    val gameId: String,
    val outcome: String,
    val score: Long,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
    val syncState: MatchSyncState,
    val lastError: String? = null
)

object MatchIntegrityManager {
    private const val PREF_NAME = "mirath_match_integrity"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun generateMatchId(): String = UUID.randomUUID().toString()

    fun saveCompletedMatch(
        context: Context,
        gameId: String,
        outcome: String,
        score: Long,
        matchId: String = generateMatchId()
    ): String {
        val finalMatchId = if (matchId.isBlank()) generateMatchId() else matchId
        val now = System.currentTimeMillis()
        val record = listOf(
            finalMatchId,
            gameId,
            outcome,
            score.toString(),
            now.toString(),
            now.toString(),
            MatchSyncState.LOCAL_ONLY.name,
            ""
        ).joinToString("|")

        val current = prefs(context)
        val existingIds = current.getStringSet("completed_match_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
        existingIds.add(finalMatchId)
        current.edit()
            .putStringSet("completed_match_ids", existingIds)
            .putString("match_$finalMatchId", record)
            .apply()
        return finalMatchId
    }

    fun markPendingSync(context: Context, matchId: String) {
        updateRecord(context, matchId) { record ->
            record.copy(syncState = MatchSyncState.PENDING, updatedAtEpochMillis = System.currentTimeMillis())
        }
    }

    fun markSynced(context: Context, matchId: String) {
        updateRecord(context, matchId) { record ->
            record.copy(syncState = MatchSyncState.SYNCED, updatedAtEpochMillis = System.currentTimeMillis(), lastError = null)
        }
    }

    fun markFailed(context: Context, matchId: String, reason: String) {
        updateRecord(context, matchId) { record ->
            record.copy(syncState = MatchSyncState.FAILED, updatedAtEpochMillis = System.currentTimeMillis(), lastError = reason)
        }
    }

    fun hasSubmissionBeenRecorded(context: Context, dedupeKey: String): Boolean {
        val key = "dedupe_$dedupeKey"
        return prefs(context).contains(key)
    }

    fun recordSubmission(context: Context, dedupeKey: String) {
        prefs(context).edit().putBoolean("dedupe_$dedupeKey", true).apply()
    }

    fun getRecord(context: Context, matchId: String): MatchResultRecord? {
        val raw = prefs(context).getString("match_$matchId", null) ?: return null
        val parts = raw.split("|")
        if (parts.size < 8) return null
        return try {
            MatchResultRecord(
                matchId = parts[0],
                gameId = parts[1],
                outcome = parts[2],
                score = parts[3].toLong(),
                createdAtEpochMillis = parts[4].toLong(),
                updatedAtEpochMillis = parts[5].toLong(),
                syncState = MatchSyncState.valueOf(parts[6]),
                lastError = if (parts[7].isBlank()) null else parts[7]
            )
        } catch (_: Exception) {
            null
        }
    }

    fun listRecords(context: Context): List<MatchResultRecord> {
        val ids = prefs(context).getStringSet("completed_match_ids", emptySet()) ?: emptySet()
        return ids.mapNotNull { id -> getRecord(context, id) }
    }

    private fun updateRecord(
        context: Context,
        matchId: String,
        updater: (MatchResultRecord) -> MatchResultRecord
    ) {
        val current = getRecord(context, matchId) ?: return
        val updated = updater(current)
        prefs(context).edit()
            .putString(
                "match_$matchId",
                listOf(
                    updated.matchId,
                    updated.gameId,
                    updated.outcome,
                    updated.score.toString(),
                    updated.createdAtEpochMillis.toString(),
                    updated.updatedAtEpochMillis.toString(),
                    updated.syncState.name,
                    updated.lastError.orEmpty()
                ).joinToString("|")
            )
            .apply()
    }
}
