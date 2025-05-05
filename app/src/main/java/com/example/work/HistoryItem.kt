package com.example.work

data class HistoryItem(
    val id: Long,
    val title: String,
    val timestamp: String,
    val leftTeamName: String,
    val rightTeamName: String
)