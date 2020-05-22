package com.robolancers.lancerscoutkotlin.adapters

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.scouting.ScoutDataActivity
import com.robolancers.lancerscoutkotlin.activities.scouting.TeamChooserActivity
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import com.robolancers.lancerscoutkotlin.utilities.adapters.Deletable

class TeamAdapter(private var teamChooserActivity: TeamChooserActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Deletable {
    inner class TeamListener {
        fun onItemClicked(viewHolder: RecyclerView.ViewHolder, itemClicked: Team) {
            val intent = Intent(teamChooserActivity, ScoutDataActivity::class.java)
            intent.putExtra("TeamNumber", itemClicked.teamNumber)
            val options = ActivityOptions.makeSceneTransitionAnimation(teamChooserActivity, (viewHolder as ViewHolder).textView, "title")
            teamChooserActivity.startActivity(intent, options.toBundle())
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)
    }

    private lateinit var recentlyDeletedItem: Team
    private var teams = emptyList<Team>()
    private val listener = TeamListener()
    private val teamViewModel = ViewModelProvider(teamChooserActivity, ViewModelProvider.AndroidViewModelFactory(teamChooserActivity.application)).get(TeamViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_text_no_handle, parent, false)
        )
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.textView.text = teams[position].teamNumber.toString()
            holder.itemView.setOnClickListener {
                listener.onItemClicked(holder, teams[position])
            }
        }
    }

    fun setTeams(teams: List<Team>) {
        this.teams = teams
        notifyDataSetChanged()
    }

    override fun showUndoSnackbar() {
        val view = teamChooserActivity.findViewById<CoordinatorLayout>(R.id.team_coordinator_layout)
        val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete()
        }
        snackbar.show()
    }

    override fun deleteItem(position: Int) {
        recentlyDeletedItem = teams[position]
        teamViewModel.delete(teams[position])
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    override fun undoDelete() {
        teamViewModel.insert(recentlyDeletedItem)
        notifyDataSetChanged()
    }
}