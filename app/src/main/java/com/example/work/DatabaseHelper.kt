package com.example.work

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createHistoryTable = ("CREATE TABLE " + TABLE_HISTORY + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT NOT NULL,"
                + COLUMN_TIMESTAMP + " TEXT NOT NULL"
                + ")")

        val createTeamsTable = ("CREATE TABLE " + TABLE_TEAMS + " ("
                + COLUMN_TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEAM_NAME + " TEXT NOT NULL"
                + ")")

        db.execSQL(createHistoryTable)
        db.execSQL(createTeamsTable)

        // 初始化隊伍資料
        val values = ContentValues()
        values.put(COLUMN_TEAM_NAME, "Team A")
        db.insert(TABLE_TEAMS, null, values)
        values.put(COLUMN_TEAM_NAME, "Team B")
        db.insert(TABLE_TEAMS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS)
        onCreate(db)
    }

    fun getTeamName(teamId: Int): String? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TEAMS, arrayOf(COLUMN_TEAM_NAME), COLUMN_TEAM_ID + " = ?",
            arrayOf(teamId.toString()), null, null, null
        )

        var teamName: String? = null
        if (cursor.moveToFirst()) {
            teamName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEAM_NAME))
        }
        cursor.close()
        return teamName
    }

    fun updateTeamName(teamId: Int, teamName: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TEAM_NAME, teamName)
        db.update(TABLE_TEAMS, values, COLUMN_TEAM_ID + " = ?", arrayOf(teamId.toString()))
    }

    fun getHistoryList(): List<HistoryItem> {
        val historyList = mutableListOf<HistoryItem>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_HISTORY,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_TIMESTAMP),
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val timestamp = getString(getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                historyList.add(HistoryItem(id, title, timestamp))
            }
        }
        cursor.close()
        return historyList
    }

    fun deleteHistory(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_HISTORY, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    companion object {
        private const val DATABASE_NAME = "history.db"
        private const val DATABASE_VERSION = 4
        const val TABLE_HISTORY: String = "history"
        const val TABLE_TEAMS: String = "teams"
        const val COLUMN_ID: String = "_id"
        const val COLUMN_TITLE: String = "title"
        const val COLUMN_TIMESTAMP: String = "timestamp"
        const val COLUMN_TEAM_NAME: String = "team_name"
        const val COLUMN_TEAM_ID: String = "team_id"
    }
}
