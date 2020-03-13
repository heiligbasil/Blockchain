package com.lambdaschool.blockchain_wallet.ui.balance

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
import com.lambdaschool.blockchain_wallet.R
import com.lambdaschool.blockchain_wallet.ui.Blockchain
import com.lambdaschool.blockchain_wallet.ui.Interface
import com.lambdaschool.blockchain_wallet.ui.UserId
import kotlinx.android.synthetic.main.fragment_balance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val textView: TextView = root.findViewById(R.id.text_balance)
        balanceViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refresh: ImageView = view.findViewById(R.id.refresh_balance_image)
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
                        UserId.balance = 0f
                        textBlock?.chain?.forEach { block ->
                            block.transactions?.forEach { transaction ->
                                if (transaction.recipient.equals(UserId.userId))
                                    UserId.balance = UserId.balance?.plus(transaction.amount ?: 0f)
                                else if (transaction.sender.equals(UserId.userId))
                                    UserId.balance = UserId.balance?.minus(transaction.amount ?: 0f)
                            }
                        }
                        val display: String = "${UserId.balance}¢"
                        balance_balance.setText(display)
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
