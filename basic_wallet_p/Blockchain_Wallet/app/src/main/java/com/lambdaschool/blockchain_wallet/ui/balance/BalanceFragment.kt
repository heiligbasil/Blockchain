package com.lambdaschool.blockchain_wallet.ui.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lambdaschool.blockchain_wallet.R

class BalanceFragment : Fragment() {

    private lateinit var balanceViewModel: BalanceViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        balanceViewModel =
                ViewModelProviders.of(this).get(BalanceViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_balance, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        balanceViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
