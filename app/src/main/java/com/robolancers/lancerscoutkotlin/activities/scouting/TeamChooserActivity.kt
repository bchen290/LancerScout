package com.robolancers.lancerscoutkotlin.activities.scouting

import android.app.SearchManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.*
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.template.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.adapters.TeamAdapter
import com.robolancers.lancerscoutkotlin.bluetooth.BluetoothService
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.viewmodels.ScoutDataViewModel
import com.robolancers.lancerscoutkotlin.room.viewmodels.TeamViewModel
import com.robolancers.lancerscoutkotlin.room.viewmodels.TemplateViewModel
import com.robolancers.lancerscoutkotlin.utilities.GsonHelper
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallbackDeletable
import com.robolancers.lancerscoutkotlin.utilities.sharedpreference.BluetoothSharedPreference
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

    private lateinit var allScoutData: List<ScoutData>

    private lateinit var templateList: List<Template>
    private lateinit var chosenTemplate: Template

    private lateinit var searchView: SearchView

    private lateinit var handler: Handler
    private lateinit var bluetoothService: BluetoothService

    private lateinit var progressBar: ProgressBar

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

        scoutDataViewModel.allScoutData.observe(this, Observer { scoutData ->
            allScoutData = scoutData
        })

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

                        val scoutData = ScoutData(
                            text.toString().toInt(),
                            chosenTemplate.name,
                            chosenTemplate.data
                        )
                        scoutDataViewModel.insert(scoutData)

                        startActivity(
                            Intent(
                                this@TeamChooserActivity,
                                TemplateEditingActivity::class.java
                            ).putExtra("TeamNumber", text.toString().toInt())
                                .putExtra("ScoutData", scoutData)
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

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
            }
        }

        bluetoothService = BluetoothService(this, handler)
        progressBar = findViewById(R.id.progress_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.team_chooser_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                teamAdapter.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                teamAdapter.filter?.filter(newText)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.team_chooser_send -> {
                BluetoothSendAsyncTask(this).execute(GsonHelper.gson.toJson(allScoutData))
                true
            }
            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }

        super.onBackPressed()
    }

    class BluetoothSendAsyncTask(private val teamChooserActivity: TeamChooserActivity) :
        AsyncTask<String, Void, Boolean>() {
        override fun onPreExecute() {
            teamChooserActivity.progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): Boolean? {
            val startTime = System.currentTimeMillis()

            teamChooserActivity.runOnUiThread {
                Snackbar.make(
                    teamChooserActivity.team_chooser_layout,
                    "Sending...",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            teamChooserActivity.bluetoothService.start()
            teamChooserActivity.bluetoothService.connect(
                BluetoothAdapter.getDefaultAdapter()
                    .getRemoteDevice(BluetoothSharedPreference.getMacAddress(teamChooserActivity)),
                false
            )

            while (teamChooserActivity.bluetoothService.serviceState != BluetoothService.STATE_CONNECTED && (System.currentTimeMillis() - startTime <= TIMEOUT)) {
            }

            return if (teamChooserActivity.bluetoothService.serviceState == BluetoothService.STATE_CONNECTED) {
                params.forEach {
                    teamChooserActivity.bluetoothService.write((it ?: "").toByteArray())
                    teamChooserActivity.runOnUiThread {
                        Snackbar.make(
                            teamChooserActivity.team_chooser_layout,
                            "Sent",
                            Snackbar.LENGTH_LONG
                        ).show()

                    }
                }
                true
            } else {
                teamChooserActivity.runOnUiThread {
                    Snackbar.make(
                        teamChooserActivity.team_chooser_layout,
                        "Timeout",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            teamChooserActivity.progressBar.visibility = View.GONE
        }

        companion object {
            const val TIMEOUT = 5000
        }
    }
}