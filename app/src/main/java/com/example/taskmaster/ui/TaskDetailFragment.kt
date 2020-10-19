package com.example.taskmaster.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaster.R
import com.example.taskmaster.databinding.ActivityTaskDetailBinding
import com.example.taskmaster.databinding.FragmentTaskDetailsBinding
import com.example.taskmaster.models.Task
import com.example.taskmaster.viewmodels.MainViewModel

/**
 * A fragment representing a single Task detail screen.
 * This fragment is either contained in a [TaskListActivity]
 * in two-pane mode (on tablets) or a [TaskDetailActivity]
 * on handsets.
 */
class TaskDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private lateinit var selectedTask: Task
    private lateinit var viewModel: MainViewModel
    private lateinit var activityBinding: ActivityTaskDetailBinding
    private lateinit var detailsBinding: FragmentTaskDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_TASK_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                //item = getUser()
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title =
                    it.getString(ARG_TASK_ID)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityBinding = DataBindingUtil.inflate(
            inflater, R.layout.activity_task_detail, container, false);

        detailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_details, container, false);

        viewModel = activity?.run { ViewModelProvider(activity!!)[MainViewModel::class.java] }
            ?: throw Exception("DEBUG: Invalid Activity")


        viewModel.task.observe(viewLifecycleOwner, Observer {
            println("debug: onChanged ${it.title}")
            selectedTask = it
            setDetails(selectedTask)
            activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = selectedTask.title
        })

        viewModel.getTaskById(arguments!!.getString(ARG_TASK_ID)!!.toInt())

        detailsBinding.description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                selectedTask.description = s.toString()
                viewModel.updateTask(selectedTask)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        detailsBinding.description.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                selectedTask.title = detailsBinding.title.text.toString()
                viewModel.updateTask(selectedTask)

                println("debug: focus !hasFocus")
            }

            println("debug: focus $hasFocus")
        }
        
        detailsBinding.saveTask.setOnClickListener {
            viewModel.updateTask(selectedTask)
        }

        return detailsBinding.root
    }

    private fun setDetails(task: Task) {
        detailsBinding.description.setText(task.description)
        detailsBinding.title.text = task.title
        /*detailsBinding.location.text = String
            .format("${task.address.suite}, ${task.address.street}, ${task.address.city}" )*/
    }


    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_TASK_ID = "task_id"
    }
}