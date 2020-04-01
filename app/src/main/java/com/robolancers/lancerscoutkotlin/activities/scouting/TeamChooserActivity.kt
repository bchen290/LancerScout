package com.robolancers.lancerscoutkotlin.activities.scouting

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.adapters.TeamAdapter
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallback
import kotlinx.android.synthetic.main.activity_team_chooser.*

class TeamChooserActivity : ToolbarActivity() {
    private lateinit var teamAdapter: TeamAdapter

    private val templateItemTouchHelper by lazy {
        ItemTouchHelper(
            ItemTouchHelperSimpleCallback(
                applicationContext,
                teamAdapter
            ).simpleItemCallback)
    }

    private lateinit var teamViewModel: TeamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        setupToolbar()

        fab.setOnClickListener {
            //TODO Set up team page
        }

        val templateManager = LinearLayoutManager(this)
        teamAdapter = TeamAdapter(this)

        val templateRecyclerView = teams_recycler_view.apply {
            layoutManager = templateManager
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
