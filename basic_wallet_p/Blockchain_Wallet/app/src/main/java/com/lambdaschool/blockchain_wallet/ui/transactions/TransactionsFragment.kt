package com.lambdaschool.blockchain_wallet.ui.transactions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambdaschool.blockchain_wallet.R
import com.lambdaschool.blockchain_wallet.ui.Blockchain
import com.lambdaschool.blockchain_wallet.ui.Interface
import com.lambdaschool.blockchain_wallet.ui.ListAdapter
import com.lambdaschool.blockchain_wallet.ui.UserId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TransactionsFragment : Fragment() {

    private lateinit var transactionsViewModel: TransactionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionsViewModel = ViewModelProviders.of(this).get(TransactionsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_transactions, container, false)
        val textView: TextView = root.findViewById(R.id.text_transactions)
        transactionsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refresh: ImageView = view.findViewById(R.id.refresh_transactions_image)
        refresh.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(Interface::class.java)
            val call = service.getData()
            call.enqueue(object : Callback<Blockchain> {
                override fun onResponse(call: Call<Blockchain>, response: Response<Blockchain>) {
                    if (response.code() == 200) {
                        val textBlock = response.body()
                        val transactionList: MutableList<String> = mutableListOf<String>()
                        textBlock?.chain?.forEach { block ->
                            block.transactions?.forEach { transaction ->
                                if (transaction.recipient.equals(UserId.userId))
                                    transactionList.add("Received from ${transaction.sender}, amount ${transaction.amount}")
                                else if (transaction.sender.equals(UserId.userId))
                                    transactionList.add("Sent to ${transaction.recipient}, amount ${transaction.amount}")
                            }
                        }
                        val rv: RecyclerView = view.findViewById(R.id.recycler_view)
                        rv.setHasFixedSize(true)
                        val manager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                        rv.layoutManager = manager
                        val adapter = ListAdapter(transactionList)
                        rv.adapter = adapter
                    } else {
                        Log.w("Code: ${response.code()}", "${response.message()} - ${response.raw()}")
                    }
                }

                override fun onFailure(call: Call<Blockchain>, t: Throwable) {
                    Log.e("Error", t.message ?: "Unknown Error")
                }
            })
        }
    }
}
