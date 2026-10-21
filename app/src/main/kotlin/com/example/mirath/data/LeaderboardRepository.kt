package com.example.mirath.data

import android.content.Context
import android.util.Log
import com.example.mirath.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

data class LeaderboardEntry(
    @DocumentId val id: String = "",
    val gameId: String = "",
    val userId: String = "",
    val userName: String = "لاعب أثري",
    val score: Long = 0L,
    val timestamp: Timestamp? = null
)

class LeaderboardRepository(private val db: FirebaseFirestore) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    fun observeLeaderboard(gameId: String): Flow<List<LeaderboardEntry>> {
        return db.collection("leaderboard_scores")
            .whereEqualTo("gameId", gameId)
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(20)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(LeaderboardEntry::class.java)?.copy(id = doc.id)
                }
            }
            .catch { e ->
                Log.e("Leaderboard", "Error observing leaderboard", e)
                emit(emptyList())
            }
    }

    suspend fun submitScore(gameId: String, score: Long) {
        val user = auth.currentUser ?: return
        val uid = user.uid
        val name = user.displayName ?: user.email ?: "لاعب أثري"

        val scoreData = mapOf(
            "gameId" to gameId,
            "userId" to uid,
            "userName" to name,
            "score" to score,
            "timestamp" to FieldValue.serverTimestamp()
        )

        try {
            db.collection("leaderboard_scores").add(scoreData).await()
        } catch (e: Exception) {
            Log.e("Leaderboard", "Failed to submit score", e)
        }
    }
}
