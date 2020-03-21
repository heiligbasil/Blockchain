package com.lambdaschool.blockchain_wallet.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.lambdaschool.blockchain_wallet.R
import com.lambdaschool.blockchain_wallet.ui.UserId
import kotlinx.android.synthetic.main.fragment_edit.*

class EditFragment : Fragment() {

    private lateinit var editViewModel: EditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editViewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_edit, container, false)
        val textView: TextView = root.findViewById(R.id.text_edit)
        editViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val editText: TextInputEditText = root.findViewById(R.id.edit_edit)
        editViewModel.edit.observe(viewLifecycleOwner, Observer {
            editText.setText(it)
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val refresh: ImageView = view.findViewById(R.id.refresh_edit_image)
        refresh.setOnClickListener {
            UserId.userId = edit_edit.text.toString()
        }
    }
}
