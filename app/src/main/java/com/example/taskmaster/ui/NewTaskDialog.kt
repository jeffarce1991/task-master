package com.example.taskmaster.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.taskmaster.R


class NewTaskDialog : DialogFragment(), View.OnClickListener {
    //widgets
    private var mTitle: EditText? = null
    private var mContent: EditText? = null
    private var mCreate: TextView? = null
    private var mCancel: TextView? = null

    //vars
    private var userListView: UserListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userListView = activity as UserListView?
        val style: Int = STYLE_NORMAL
        val theme = R.style.Theme_AppCompat_DayNight_Dialog
        setStyle(style, theme)
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_new_task, container, false)
        mTitle = view.findViewById(R.id.note_title)
        mContent = view.findViewById(R.id.note_content)
        mCreate = view.findViewById(R.id.create)
        mCancel = view.findViewById(R.id.cancel)
        mCancel!!.setOnClickListener(this)
        mCreate!!.setOnClickListener(this)
        dialog!!.setTitle("New Note")
        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.create -> {
                // insert the new note
                val title = mTitle!!.text.toString()
                val content = mContent!!.text.toString()
                if (title != "") {
                    userListView!!.createNewTask(title, content)
                    dialog!!.dismiss()
                } else {
                    Toast.makeText(activity, "Enter a title", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.cancel -> {
                dialog!!.dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "NewTaskDialog"
    }
}