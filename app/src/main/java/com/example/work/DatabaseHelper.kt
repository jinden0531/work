package com.example.work

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "ScoreDatabase.db"

        const val TABLE_HISTORY = "history"
        const val TABLE_SCORE_DETAIL = "score_detail"
        const val TABLE_TEAMS = "teams"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_HISTORY_ID = "history_id"
        const val COLUMN_SCORE = "score"
        const val COLUMN_DETAIL_TIMESTAMP = "detail_timestamp"
        const val COLUMN_TEAM_ID = "team_id"
        const val COLUMN_TEAM_NAME = "team_name"
        const val COLUMN_LEFT_TEAM_NAME = "left_team_name"
        const val COLUMN_RIGHT_TEAM_NAME = "right_team_name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createHistoryTable = """
            CREATE TABLE $TABLE_HISTORY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_TIMESTAMP TEXT,
                $COLUMN_LEFT_TEAM_NAME TEXT,
                $COLUMN_RIGHT_TEAM_NAME TEXT
            )
        """.trimIndent()

        val createScoreDetailTable = """
            CREATE TABLE $TABLE_SCORE_DETAIL (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_HISTORY_ID INTEGER,
                $COLUMN_SCORE TEXT,
                $COLUMN_DETAIL_TIMESTAMP TEXT,
                FOREIGN KEY ($COLUMN_HISTORY_ID) REFERENCES $TABLE_HISTORY($COLUMN_ID)
            )
        """.trimIndent()

        val createTeamsTable = """
            CREATE TABLE $TABLE_TEAMS (
                $COLUMN_TEAM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TEAM_NAME TEXT
            )
        """.trimIndent()

        db.execSQL(createHistoryTable)
        db.execSQL(createScoreDetailTable)
        db.execSQL(createTeamsTable)

        // 初始化隊伍資料
        val values = ContentValues().apply {
            put(COLUMN_TEAM_NAME, "隊伍一")
        }
        db.insert(TABLE_TEAMS, null, values)
        values.clear()
        values.put(COLUMN_TEAM_NAME, "隊伍二")
        db.insert(TABLE_TEAMS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            try {
                // 備份舊資料
                val oldHistory = mutableListOf<Pair<String, String>>()
                val cursor = db.query(
                    TABLE_HISTORY,
                    arrayOf(COLUMN_TITLE, COLUMN_TIMESTAMP),
                    null,
                    null,
                    null,
                    null,
                    null
                )
                cursor.use {
                    while (it.moveToNext()) {
                        val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                        val timestamp = it.getString(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                        oldHistory.add(Pair(title, timestamp))
                    }
                }

                // 刪除舊表
                db.execSQL("DROP TABLE IF EXISTS $TABLE_SCORE_DETAIL")
                db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
                db.execSQL("DROP TABLE IF EXISTS $TABLE_TEAMS")

                // 創建新表
                onCreate(db)

                // 恢復資料
                for ((title, timestamp) in oldHistory) {
                    val values = ContentValues().apply {
                        put(COLUMN_TITLE, title)
                        put(COLUMN_TIMESTAMP, timestamp)
                        put(COLUMN_LEFT_TEAM_NAME, "隊伍一")
                        put(COLUMN_RIGHT_TEAM_NAME, "隊伍二")
                    }
                    db.insert(TABLE_HISTORY, null, values)
                }
            } catch (e: Exception) {
                android.util.Log.e("DatabaseHelper", "升級數據庫時出錯: ${e.message}")
                e.printStackTrace()
                // 如果升級失敗，重新創建數據庫
                db.execSQL("DROP TABLE IF EXISTS $TABLE_SCORE_DETAIL")
                db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
                db.execSQL("DROP TABLE IF EXISTS $TABLE_TEAMS")
                onCreate(db)
            }
        }
    }

    fun getTeamName(teamId: Int): String? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TEAMS,
            arrayOf(COLUMN_TEAM_NAME),
            "$COLUMN_TEAM_ID = ?",
            arrayOf(teamId.toString()),
            null,
            null,
            null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndexOrThrow(COLUMN_TEAM_NAME))
            } else {
                null
            }
        }
    }

    fun updateTeamName(teamId: Int, teamName: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEAM_NAME, teamName)
        }
        db.update(
            TABLE_TEAMS,
            values,
            "$COLUMN_TEAM_ID = ?",
            arrayOf(teamId.toString())
        )
    }

    fun insertHistory(title: String, timestamp: String, leftTeamName: String, rightTeamName: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_TIMESTAMP, timestamp)
            put(COLUMN_LEFT_TEAM_NAME, leftTeamName)
            put(COLUMN_RIGHT_TEAM_NAME, rightTeamName)
        }
        return writableDatabase.insert(TABLE_HISTORY, null, values)
    }

    fun insertScoreDetail(historyId: Long, score: String, timestamp: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_HISTORY_ID, historyId)
            put(COLUMN_SCORE, score)
            put(COLUMN_DETAIL_TIMESTAMP, timestamp)
        }
        return writableDatabase.insert(TABLE_SCORE_DETAIL, null, values)
    }

    fun getHistoryList(): List<HistoryItem> {
        val historyList = mutableListOf<HistoryItem>()
        val db = readableDatabase
        try {
            android.util.Log.d("DatabaseHelper", "開始獲取歷史記錄")
            val cursor = db.query(
                TABLE_HISTORY,
                arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_TIMESTAMP, COLUMN_LEFT_TEAM_NAME, COLUMN_RIGHT_TEAM_NAME),
                null,
                null,
                null,
                null,
                "$COLUMN_TIMESTAMP DESC"
            )

            android.util.Log.d("DatabaseHelper", "查詢完成，開始處理結果")
            cursor.use {
                while (it.moveToNext()) {
                    try {
                        val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                        val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)) ?: "無標題"
                        val timestamp = it.getString(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP)) ?: System.currentTimeMillis().toString()
                        val leftTeamName = it.getString(it.getColumnIndexOrThrow(COLUMN_LEFT_TEAM_NAME)) ?: "隊伍一"
                        val rightTeamName = it.getString(it.getColumnIndexOrThrow(COLUMN_RIGHT_TEAM_NAME)) ?: "隊伍二"
                        
                        android.util.Log.d("DatabaseHelper", "處理記錄: ID=$id, 標題=$title, 時間=$timestamp")
                        historyList.add(HistoryItem(id, title, timestamp, leftTeamName, rightTeamName))
                    } catch (e: Exception) {
                        android.util.Log.e("DatabaseHelper", "處理記錄時出錯: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
            android.util.Log.d("DatabaseHelper", "總共獲取到 ${historyList.size} 條記錄")
        } catch (e: Exception) {
            android.util.Log.e("DatabaseHelper", "獲取歷史記錄時出錯: ${e.message}")
            e.printStackTrace()
        }
        return historyList
    }

    fun getScoreDetails(historyId: Long): List<ScoreDetail> {
        val scoreDetails = mutableListOf<ScoreDetail>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SCORE_DETAIL,
            arrayOf(COLUMN_ID, COLUMN_SCORE, COLUMN_DETAIL_TIMESTAMP),
            "$COLUMN_HISTORY_ID = ?",
            arrayOf(historyId.toString()),
            null,
            null,
            "$COLUMN_DETAIL_TIMESTAMP ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val score = it.getString(it.getColumnIndexOrThrow(COLUMN_SCORE))
                val timestamp = it.getString(it.getColumnIndexOrThrow(COLUMN_DETAIL_TIMESTAMP))
                scoreDetails.add(ScoreDetail(id, score, timestamp))
            }
        }
        return scoreDetails
    }

    private fun hasScoreDetails(historyId: Long): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SCORE_DETAIL,
            arrayOf(COLUMN_ID),
            "$COLUMN_HISTORY_ID = ?",
            arrayOf(historyId.toString()),
            null,
            null,
            null,
            "1"
        )
        return cursor.use { it.count > 0 }
    }

    fun deleteHistory(historyId: Long) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // 先刪除相關的逐球記錄
            db.delete(
                TABLE_SCORE_DETAIL,
                "$COLUMN_HISTORY_ID = ?",
                arrayOf(historyId.toString())
            )
            // 再刪除歷史記錄
            db.delete(
                TABLE_HISTORY,
                "$COLUMN_ID = ?",
                arrayOf(historyId.toString())
            )
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun updateHistory(historyId: Long, title: String, timestamp: String, leftTeamName: String, rightTeamName: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_TIMESTAMP, timestamp)
            put(COLUMN_LEFT_TEAM_NAME, leftTeamName)
            put(COLUMN_RIGHT_TEAM_NAME, rightTeamName)
        }
        db.update(
            TABLE_HISTORY,
            values,
            "$COLUMN_ID = ?",
            arrayOf(historyId.toString())
        )
    }
}
