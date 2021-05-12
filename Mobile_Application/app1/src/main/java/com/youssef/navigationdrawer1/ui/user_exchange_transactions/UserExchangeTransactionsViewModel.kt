package com.youssef.navigationdrawer1.ui.user_exchange_transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserExchangeTransactionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //value = "This is User Exchange Transactions Fragment"
    }
    val text: LiveData<String> = _text
}