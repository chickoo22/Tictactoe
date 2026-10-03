package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_matches")
data class GameMatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val mode: String, // "PVP" or "AI"
    val difficulty: String, // "EASY", "MEDIUM", "HARD", "NONE"
    val winner: String, // "X", "O", "TIE"
    val timestamp: Long = System.currentTimeMillis()
)
