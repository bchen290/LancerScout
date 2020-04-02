package com.robolancers.lancerscoutkotlin.activities.scouting

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.adapters.ScoutDataAdapter
import com.robolancers.lancerscoutkotlin.room.viewmodels.ScoutDataViewModel
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallbackDeletable
import kotlinx.android.synthetic.main.activity_scout_data.*

class ScoutDataActivity : ToolbarActivity() {
    private lateinit var scoutDataAdapter: ScoutDataAdapter

    private val templateItemTouchHelper by lazy {
        ItemTouchHelper(
            ItemTouchHelperSimpleCallbackDeletable(
                applicationContext,
                scoutDataAdapter
            ).simpleItemCallback)
    }

    private lateinit var scoutDataViewModel: ScoutDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scout_data)

        setupToolbar()

        val teamNumber = intent.getIntExtra("TeamNumber", 0)

        val templateManager = LinearLayoutManager(this)
        scoutDataAdapter = ScoutDataAdapter(this)

        scout_data_recycler_view.apply {
            layoutManager = templateManager
            adapter = scoutDataAdapter
            addItemDecoration(DividerItemDecoration(this.context, templateManager.orientation))
        }.also {
            templateItemTouchHelper.attachToRecyclerView(it)
        }

        scoutDataViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            ScoutDataViewModel::class.java)

        scoutDataViewModel.getAllScoutData(teamNumber).observe(this, Observer { teams ->
            scoutDataAdapter.setTeams(teams)
        })
    }
}
