package com.example.taskmaster.ui

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.R
import com.example.taskmaster.adapters.RecyclerAdapter
import com.example.taskmaster.databinding.ActivityMainBinding
import com.example.taskmaster.databinding.ActivityUserListBinding
import com.example.taskmaster.models.Address
import com.example.taskmaster.models.Company

import com.example.taskmaster.models.User
import com.example.taskmaster.utils.hide
import com.example.taskmaster.utils.invokeInputDialog
import com.example.taskmaster.utils.show
import com.example.taskmaster.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.user_list.view.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [UserDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

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
                mAdapter.updateList(it)
                viewModel.setIsUpdating(false)
            })


        viewModel.getIsUpdating()?.observe(this,
            Observer {
                if(it!!){
                    showProgressBar()
                } else{
                    hideProgressBar()
                    binding.userList
                        .smoothScrollToPosition(
                            mAdapter.itemCount -1)
                }
            })


        binding.fab.setOnClickListener {
            invokeInputDialog()
        }

        setupRecyclerView()
    }

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
        binding.userList.layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerAdapter(this, listOf(), twoPane)
        binding.userList.adapter = mAdapter
    }


    private fun invokeInputDialog() {

        // Set up the input
        val input = EditText(this)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setMessage("")
            .setView(input)
            .setPositiveButton("OK") { dialog, which ->
                viewModel.addNewTask(User(
                    null,
                    Address(),
                    Company(),
                    "sample@gmail.com",
                    input.text.toString(),
                    "",
                    "",
                    ""))
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}