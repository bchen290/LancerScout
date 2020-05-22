package com.robolancers.lancerscoutkotlin.activities.scouting

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.adapters.TeamAdapter
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallbackDeletable
import kotlinx.android.synthetic.main.activity_team_chooser.*

class TeamChooserActivity : ToolbarActivity() {
    private lateinit var teamAdapter: TeamAdapter

    private val templateItemTouchHelper by lazy {
        ItemTouchHelper(
            ItemTouchHelperSimpleCallbackDeletable(
                applicationContext,
                teamAdapter
            ).simpleItemCallback)
    }

    private lateinit var teamViewModel: TeamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_chooser)

        setupToolbar()

        fab.setOnClickListener {
            MaterialDialog(this).show {
                input(hint = "Enter team number", inputType = InputType.TYPE_CLASS_NUMBER) { _, text ->
                    if(teamViewModel.findTeamByNumber(321) == null) {
                        teamViewModel.insert(Team(teamNumber = text.toString().toInt()))
                    }

                    startActivity(Intent(this@TeamChooserActivity, ScoutDataActivity::class.java).putExtra("TeamNumber", text.toString().toInt()))
                }
                positiveButton(text = "Submit")
            }
        }

        val teamLayoutManager = LinearLayoutManager(this)
        teamAdapter = TeamAdapter(this)

        val templateRecyclerView = teams_recycler_view.apply {
            layoutManager = teamLayoutManager
            adapter = teamAdapter
        }

        templateItemTouchHelper.attachToRecyclerView(templateRecyclerView)

        teamViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            TeamViewModel::class.java)

        teamViewModel.allTeams.observe(this, Observer { teams ->
            teamAdapter.setTeams(teams)
        })
    }

    fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        templateItemTouchHelper.startDrag(viewHolder)
    }
}
