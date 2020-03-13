package com.lambdaschool.blockchain_wallet.ui.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambdaschool.blockchain_wallet.ui.UserId

class BalanceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "The current balance for '${UserId.userId}'"
    }
    val text: LiveData<String> = _text
}