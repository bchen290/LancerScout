package com.robolancers.lancerscoutkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.scouting.TeamChooserActivity
import com.robolancers.lancerscoutkotlin.models.scouting.TeamTemplateItem
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import com.robolancers.lancerscoutkotlin.utilities.adapters.Deletable
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallbackReorderable

class TeamAdapter(private var teamChooserActivity: TeamChooserActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Deletable {
    inner class TeamHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var teamNumber: TextView = itemView.findViewById(R.id.team_number)
        private var teamTemplateRecyclerView = itemView.findViewById<RecyclerView>(R.id.team_template_list)

        private var teamTemplateItems = mutableListOf<TeamTemplateItem>()

        private var itemSelectorAdapter = TeamTemplateAdapter(teamTemplateRecyclerView.context, teamTemplateItems, this)
        private var itemSelectorItemTouchHelper = ItemTouchHelper(
            ItemTouchHelperSimpleCallbackReorderable(
                itemSelectorAdapter
            ).simpleItemCallback)
        private val itemSelectorLinearLayoutManager = LinearLayoutManager(teamTemplateRecyclerView.context, RecyclerView.VERTICAL, false)

        fun bind(team: Team) {
            teamNumber.text = team.teamNumber.toString()

            if (!team.templates.isNullOrBlank()) {
                teamTemplateItems.addAll(
                    team.templates.orEmpty().split(",").map { TeamTemplateItem(it) }.toMutableList()
                )
                itemSelectorAdapter.notifyDataSetChanged()
            }

            teamTemplateRecyclerView.apply {
                layoutManager = itemSelectorLinearLayoutManager
                adapter = itemSelectorAdapter
                setRecycledViewPool(viewPool)
            }

            itemSelectorItemTouchHelper.attachToRecyclerView(teamTemplateRecyclerView)
        }

        fun startDragging(viewHolder: RecyclerView.ViewHolder) {
            itemSelectorItemTouchHelper.startDrag(viewHolder)
        }
    }

    private lateinit var recentlyDeletedItem: Team
    private var teams = emptyList<Team>()
    private val teamViewModel = ViewModelProvider(teamChooserActivity, ViewModelProvider.AndroidViewModelFactory(teamChooserActivity.application)).get(TeamViewModel::class.java)

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TeamHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_team, parent, false)
        )
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TeamHolder) {
            holder.bind(teams[position])
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