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
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.template.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.adapters.TeamAdapter
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.viewmodels.ScoutDataViewModel
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import com.robolancers.lancerscoutkotlin.room.viewmodels.TemplateViewModel
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
    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var scoutDataViewModel: ScoutDataViewModel

    private lateinit var templateList: List<Template>
    private lateinit var chosenTemplate: Template

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_chooser)

        setupToolbar()

        templateViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            TemplateViewModel::class.java)

        scoutDataViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            ScoutDataViewModel::class.java)

        templateViewModel.allTemplates.observe(this, Observer { templates ->
            templateList = templates
        })

        val teamLayoutManager = LinearLayoutManager(this)
        teamAdapter = TeamAdapter(this, scoutDataViewModel)

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

        fab.setOnClickListener {
            if (templateList.isNotEmpty()) {
                MaterialDialog(this).show {
                    input(
                        hint = "Enter team number",
                        inputType = InputType.TYPE_CLASS_NUMBER,
                        waitForPositiveButton = true
                    ) { _, text ->
                        if (teamViewModel.findTeamByNumber(text.toString().toInt()) == null) {
                            teamViewModel.insert(Team(teamNumber = text.toString().toInt()))
                        }

                        scoutDataViewModel.insert(ScoutData(text.toString().toInt(), chosenTemplate.name, chosenTemplate.data))

                        startActivity(
                            Intent(
                                this@TeamChooserActivity,
                                TemplateEditingActivity::class.java
                            ).putExtra("TeamNumber", text.toString().toInt())
                                .putExtra("Template", chosenTemplate)
                        )
                    }
                    listItemsSingleChoice(items = templateList.map { it.name.orEmpty() }, waitForPositiveButton = false) { _, index, _ ->
                        chosenTemplate = templateList[index]
                    }
                    positiveButton(text = "Submit")
                }
            } else {
                Snackbar.make(team_coordinator_layout, "Create a template first!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        templateItemTouchHelper.startDrag(viewHolder)
    }
}
