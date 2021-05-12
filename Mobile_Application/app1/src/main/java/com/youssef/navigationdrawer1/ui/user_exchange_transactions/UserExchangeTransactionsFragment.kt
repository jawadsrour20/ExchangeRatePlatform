package com.youssef.navigationdrawer1.ui.user_exchange_transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.youssef.navigationdrawer1.R

class UserExchangeTransactionsFragment : Fragment() {

    private lateinit var userExchangeTransactionsViewModel: UserExchangeTransactionsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        userExchangeTransactionsViewModel =
                ViewModelProvider(this).get(UserExchangeTransactionsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_user_exchange_transactions, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        userExchangeTransactionsViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        return root
    }
}