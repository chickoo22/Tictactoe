package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game_matches ORDER BY timestamp DESC LIMIT 50")
    fun getAllMatches(): Flow<List<GameMatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: GameMatchEntity)

    @Query("DELETE FROM game_matches")
    suspend fun clearHistory()
}
