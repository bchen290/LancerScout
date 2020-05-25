package com.robolancers.lancerscoutkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.scouting.TeamChooserActivity
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.viewmodels.ScoutDataViewModel
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import com.robolancers.lancerscoutkotlin.utilities.adapters.Deletable
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallbackReorderable

class TeamAdapter(
    private var teamChooserActivity: TeamChooserActivity,
    private var scoutDataViewModel: ScoutDataViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Deletable, Filterable {
    inner class TeamHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var teamNumber: TextView = itemView.findViewById(R.id.team_number)
        private var teamTemplateRecyclerView = itemView.findViewById<RecyclerView>(R.id.team_template_list)

        private var teamTemplateItems = mutableListOf<ScoutData>()

        private var teamTemplateAdapter = TeamTemplateAdapter(
            teamTemplateRecyclerView.context,
            teamTemplateItems
        )
        private var teamTemplateItemTouchHelper = ItemTouchHelper(
            ItemTouchHelperSimpleCallbackReorderable(
                teamTemplateAdapter
            ).simpleItemCallback)
        private val teamTemplateLinearLayoutManager = LinearLayoutManager(teamTemplateRecyclerView.context, RecyclerView.VERTICAL, false)

        private var recyclerViewVisible = true

        fun bind(team: Team) {
            teamNumber.text = team.teamNumber.toString()

            scoutDataViewModel.getAllScoutData(team.teamNumber ?: 0).observe(teamChooserActivity, Observer { data ->
                teamTemplateAdapter.setTeamTemplates(data.toMutableList())
            })

            teamTemplateRecyclerView.apply {
                layoutManager = teamTemplateLinearLayoutManager
                adapter = teamTemplateAdapter
                setRecycledViewPool(viewPool)
                visibility = View.GONE
            }

            teamTemplateItemTouchHelper.attachToRecyclerView(teamTemplateRecyclerView)

            itemView.setOnClickListener {
                if (recyclerViewVisible) {
                    teamTemplateRecyclerView.visibility = View.VISIBLE
                } else {
                    teamTemplateRecyclerView.visibility = View.GONE
                }

                recyclerViewVisible = !recyclerViewVisible
            }
        }
    }

    private lateinit var recentlyDeletedItem: Team
    private var teams = emptyList<Team>()
    private var teamsFiltered = emptyList<Team>()
    private val teamViewModel = ViewModelProvider(teamChooserActivity, ViewModelProvider.AndroidViewModelFactory(teamChooserActivity.application)).get(TeamViewModel::class.java)

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TeamHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_team, parent, false)
        )
    }

    override fun getItemCount(): Int = teamsFiltered.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TeamHolder) {
            holder.bind(teamsFiltered[position])
        }
    }

    fun setTeams(teams: List<Team>) {
        this.teams = teams
        this.teamsFiltered = teams
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
        recentlyDeletedItem = teamsFiltered[position]
        teamViewModel.delete(teamsFiltered[position])
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    override fun undoDelete() {
        teamViewModel.insert(recentlyDeletedItem)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint.toString()
                teamsFiltered = if (filterString.isEmpty()) {
                    teams
                } else {
                    teams.filter { it.teamNumber.toString().contains(filterString) }
                }

                return FilterResults().apply {
                    values = teamsFiltered
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                teamsFiltered = results?.values as? List<Team> ?: teams
                notifyDataSetChanged()
            }
        }
    }
}