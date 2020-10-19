package com.example.taskmaster.ui

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.R
import com.example.taskmaster.adapters.RecyclerAdapter
import com.example.taskmaster.databinding.ActivityUserListBinding
import com.example.taskmaster.models.Address
import com.example.taskmaster.models.Company
import com.example.taskmaster.models.User
import com.example.taskmaster.utils.hide
import com.example.taskmaster.utils.show
import com.example.taskmaster.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [UserDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@AndroidEntryPoint
class UserListActivity : AppCompatActivity(), UserListView, View.OnClickListener {

    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var viewModel: MainViewModel
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var binding : ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list)


        setupToolbar()

        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/


        if (findViewById<NestedScrollView>(R.id.user_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.getUsers()?.observe(this,
            Observer<MutableList<User>> {
                println("debug: onChanged ${it.size} ")
                mAdapter.updateList(it)
                viewModel.setIsUpdating(false)
            })


        viewModel.getIsUpdating()?.observe(this,
            Observer {
                if(it!!){
                    showProgressBar()
                } else{
                    hideProgressBar()
                    if(mAdapter.itemCount > 0){
                        println("debug: adapter count is ${mAdapter.itemCount}")
                        binding.recyclerviewUsers
                            .smoothScrollToPosition(
                                mAdapter.itemCount -1)
                    }
                }
            })



        binding.fab.setOnClickListener(this);

        setupRecyclerView()
        setUpItemTouchHelper()

        println("debug: TIMESTAMP: ${getCurrentTimestamp()}")
        println("debug: DATE: ${getCurrentDateNumeric()}")
    }

    private fun getCurrentDate(): String {
        return DateUtils.formatDateTime(this, getCurrentTimestamp(), DateUtils.FORMAT_SHOW_YEAR)
    }

    private fun getCurrentDateNumeric(): String {
        return DateUtils.formatDateTime(this, getCurrentTimestamp(), DateUtils.FORMAT_NUMERIC_DATE)
    }

    private fun getCurrentTimestamp(): Long = (System.currentTimeMillis() / 1000) * 1000



    private fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    private fun setupRecyclerView() {
        binding.recyclerviewUsers.layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerAdapter(this, listOf(), twoPane)
        binding.recyclerviewUsers.adapter = mAdapter
    }

    private fun setUpItemTouchHelper() {
        val simpleCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    /*val swipedPokemonPosition = viewHolder.adapterPosition
                    val pokemon: Pokemon = adapter.getPokemonAt(swipedPokemonPosition)
                    viewModel.insertPokemon(pokemon)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(getContext(), "Pokemon added to favorites.", Toast.LENGTH_SHORT)
                        .show()*/
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewUsers)

        val simpleCallback2: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    /*val swipedPokemonPosition = viewHolder.adapterPosition
                    val pokemon: Pokemon = adapter.getPokemonAt(swipedPokemonPosition)
                    viewModel.insertPokemon(pokemon)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(getContext(), "Pokemon added to favorites.", Toast.LENGTH_SHORT)
                        .show()*/
                }
            }
        val itemTouchHelper2 = ItemTouchHelper(simpleCallback2)
        itemTouchHelper2.attachToRecyclerView(binding.recyclerviewUsers)
    }

    override fun createNewTask(title: String?, content: String?) {
        viewModel.addNewTask(User(
            null,
            Address(),
            Company(),
            "sample@gmail.com",
            content!!,
            "",
            "",
            ""))
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.fab -> {

                //create a new note
                val dialog = NewTaskDialog()
                dialog.show(supportFragmentManager, getString(R.string.dialog_new_task))
            }
        }
    }
}