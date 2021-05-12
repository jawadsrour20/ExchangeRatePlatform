package com.youssef.navigationdrawer1.ui.fluctuation_graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FluctuationGraphViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //value = "This is Fluctuation Graph Fragment"
    }
    val text: LiveData<String> = _text
}