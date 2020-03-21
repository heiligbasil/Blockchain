package com.lambdaschool.blockchain_wallet.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambdaschool.blockchain_wallet.ui.UserId

class TransactionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "List of all '${UserId.userId}'s transactions"
    }
    val text: LiveData<String> = _text
}