package com.robolancers.lancerscoutkotlin.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.scouting.TeamChooserActivity
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import kotlinx.android.synthetic.main.list_item_white_text.view.*
import java.util.*

class TeamAdapter(private var teamChooserActivity: TeamChooserActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), LancerAdapter {
    inner class TeamListener {
        fun onItemClicked(viewHolder: RecyclerView.ViewHolder, itemClicked: Team) {

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)
    }

    private lateinit var recentlyDeletedItem: Team
    private var teams = emptyList<Team>()
    private val listener = TeamListener()
    private val viewModel = ViewModelProvider(teamChooserActivity, ViewModelProvider.AndroidViewModelFactory(teamChooserActivity.application)).get(TeamViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_white_text, parent, false)
            )

        viewHolder.itemView.handle_view.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                teamChooserActivity.startDrag(viewHolder)
            }

            return@setOnTouchListener true
        }

        return viewHolder
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
        val view = teamChooserActivity.findViewById<CoordinatorLayout>(R.id.template_coordinator_layout)
        val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete()
        }
        snackbar.show()
    }

    override fun moveItem(from: Int, to: Int){
        Collections.swap(teams, from, to)
    }

    override fun deleteItem(position: Int) {
        recentlyDeletedItem = teams[position]
        viewModel.delete(teams[position])
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    override fun undoDelete() {
        viewModel.insert(recentlyDeletedItem)
        notifyItemInserted(teams.size - 1)
    }
}