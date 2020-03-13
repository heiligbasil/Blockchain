package com.lambdaschool.blockchain_wallet

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lambdaschool.blockchain_wallet.ui.Blockchain
import com.lambdaschool.blockchain_wallet.ui.Interface
import com.lambdaschool.blockchain_wallet.ui.UserId
import com.lambdaschool.blockchain_wallet.ui.edit.EditViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    val lastIndex: Int = textBlock?.chain?.lastIndex ?: 0
                    UserId.userId = textBlock?.chain?.get(lastIndex)?.transactions?.get(0)?.recipient
                } else {
                    Log.w("Code: ${response.code()}", "${response.message()} - ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<Blockchain>, t: Throwable) {
                Log.e("Error", t.message ?: "Unknown Error")
            }
        })

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_edit, R.id.navigation_balance, R.id.navigation_transactions
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
