package com.robolancers.lancerscoutkotlin.adapters

import android.content.Intent
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.scouting.TeamChooserActivity
import com.robolancers.lancerscoutkotlin.activities.template.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.viewmodels.ScoutDataViewModel
import com.robolancers.lancerscoutkotlin.utilities.adapters.Deletable
import com.robolancers.lancerscoutkotlin.utilities.callback.LancerTextWatcher

class TeamTemplateAdapter(
    private val teamChooserActivity: TeamChooserActivity,
    private var listItems: MutableList<ScoutData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Deletable {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val itemTitle: TextView = itemView.findViewById(R.id.item_selector_item_title)
        private val itemDelete: ImageButton = itemView.findViewById(R.id.item_selector_item_delete)

        fun bind(listItem: ScoutData) {
            itemTitle.text = listItem.scoutDataName

            itemTitle.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    listItem.scoutDataName = s.toString()
                }
            })

            itemView.setOnClickListener {
                val intent = Intent(teamChooserActivity, TemplateEditingActivity::class.java)
                intent.putExtra("ScoutData", listItem)
                teamChooserActivity.startActivity(intent)
            }

            itemDelete.setOnClickListener {
                deleteItem(adapterPosition)
            }
        }
    }

    private val scoutViewModel = ViewModelProvider(
        teamChooserActivity,
        ViewModelProvider.AndroidViewModelFactory(teamChooserActivity.application)
    ).get(ScoutDataViewModel::class.java)
    private lateinit var recentlyDeletedItem: ScoutData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_team_template, parent, false)
        )
    }

    fun setTeamTemplates(teamTemplates: MutableList<ScoutData>) {
        this.listItems = teamTemplates
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(listItems[position])
        }
    }

    override fun deleteItem(position: Int) {
        recentlyDeletedItem = listItems[position]
        scoutViewModel.delete(listItems[position])
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    override fun undoDelete() {
        scoutViewModel.insert(recentlyDeletedItem)
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
}