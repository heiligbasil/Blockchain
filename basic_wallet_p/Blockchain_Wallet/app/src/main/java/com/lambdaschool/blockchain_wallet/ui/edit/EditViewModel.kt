package com.lambdaschool.blockchain_wallet.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambdaschool.blockchain_wallet.ui.UserId

class EditViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Your Blockchain Transaction ID"
    }
    val text: LiveData<String> = _text

    private val _edit = MutableLiveData<String>().apply {
        value = UserId.userId
    }
    val edit: LiveData<String> = _edit
}