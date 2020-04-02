package com.robolancers.lancerscoutkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.scouting.ScoutDataActivity
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.viewmodels.ScoutDataViewModel
import com.robolancers.lancerscoutkotlin.utilities.adapters.Deletable

class ScoutDataAdapter(private var scoutDataActivity: ScoutDataActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Deletable {
    inner class TeamListener {
        fun onItemClicked(viewHolder: RecyclerView.ViewHolder, itemClicked: ScoutData) {

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)
    }

    private lateinit var recentlyDeletedItem: ScoutData
    private var teams = emptyList<ScoutData>()
    private val listener = TeamListener()
    private val viewModel = ViewModelProvider(scoutDataActivity, ViewModelProvider.AndroidViewModelFactory(scoutDataActivity.application)).get(ScoutDataViewModel::class.java)

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

    fun setTeams(teams: List<ScoutData>) {
        this.teams = teams
        notifyDataSetChanged()
    }

    override fun showUndoSnackbar() {
        val view = scoutDataActivity.findViewById<CoordinatorLayout>(R.id.scout_data_linear_layout)
        val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete()
        }
        snackbar.show()
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