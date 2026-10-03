package com.example.data

import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao) {
    val allMatches: Flow<List<GameMatchEntity>> = gameDao.getAllMatches()

    suspend fun insertMatch(match: GameMatchEntity) {
        gameDao.insertMatch(match)
    }

    suspend fun clearHistory() {
        gameDao.clearHistory()
    }
}
