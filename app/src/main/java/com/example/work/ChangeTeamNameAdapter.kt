package com.example.work

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class ChangeTeamNameAdapter(
    private var teamItems: List<TeamItem>,
    private val onSaveClick: (Int, String) -> Unit
) : RecyclerView.Adapter<ChangeTeamNameAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamNameEditText: EditText = view.findViewById(R.id.teamNameEditText)
        val saveButton: Button = view.findViewById(R.id.saveButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teamItem = teamItems[position]

        // 設定隊伍名稱
        holder.teamNameEditText.setText(teamItem.name)

        // 儲存按鈕點擊事件
        holder.saveButton.setOnClickListener {
            val updatedName = holder.teamNameEditText.text.toString()
            onSaveClick(teamItem.id, updatedName)
        }
    }

    override fun getItemCount(): Int {
        return teamItems.size
    }

    // 更新資料的方法
    fun updateData(newTeamItems: List<TeamItem>) {
        teamItems = newTeamItems
        notifyDataSetChanged()
    }
}

// 隊伍項目的資料結構
data class TeamItem(
    val id: Int,
    val name: String
)
