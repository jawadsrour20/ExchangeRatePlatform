package com.youssef.navigationdrawer1.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //value = "This is statistics Fragment"
    }
    val text: LiveData<String> = _text
}