package com.youssef.navigationdrawer1.ui.fluctuation_graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.youssef.navigationdrawer1.R

class FluctuationGraphFragment : Fragment() {

    private lateinit var fluctuationGraphViewModel: FluctuationGraphViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fluctuationGraphViewModel =
                ViewModelProvider(this).get(FluctuationGraphViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_fluctuation_graph, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        fluctuationGraphViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}