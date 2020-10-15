package com.example.taskmaster.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaster.R
import com.example.taskmaster.adapters.RecyclerAdapter
import com.example.taskmaster.databinding.ActivityMainBinding
import com.example.taskmaster.models.User
import com.example.taskmaster.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var viewModel: MainViewModel


    private lateinit var mainBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        viewModel.getUsers()?.observe(this,
            Observer<MutableList<User>> {
                initializeRecyclerView(it)
            })

        viewModel.getIsUpdating()?.observe(this,
            Observer {
                if(it!!){
                    showProgressBar()
                } else{
                hideProgressBar()
                    mainBinding.customRecyclerView
                        .smoothScrollToPosition(
                            viewModel
                                .getUsers()!!.value!!.size -1)
                }
            })


        mainBinding.fab.setOnClickListener {
            viewModel.addNewValue(User())
        }
    }

    private fun initializeRecyclerView(users: MutableList<User>) {
        /*mAdapter = RecyclerAdapter(this, users)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@MainActivity)
        mainBinding.root.customRecyclerView.layoutManager = layoutManager
        mainBinding.root.customRecyclerView.adapter = mAdapter*/
    }

    private fun showProgressBar() {
        mainBinding.progressBar.visibility = VISIBLE
    }

    private fun hideProgressBar() {
        mainBinding.progressBar.visibility = GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJobs()
    }
}