package com.youssef.currencyexchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.youssef.currencyexchange.api.Authentication
import com.youssef.currencyexchange.api.ExchangeService
import com.youssef.currencyexchange.api.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var listview: ListView? = null
    private var transactions: ArrayList<Transaction>? = ArrayList()
    private var adapter: TransactionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        fetchTransactions()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_transactions,
                container, false)
        listview = view.findViewById(R.id.listview)
        adapter =
                TransactionAdapter(layoutInflater, transactions!!)
        listview?.adapter = adapter
        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                TransactionsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun fetchTransactions() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                    .getTransactions("Bearer${"Token " + Authentication.getToken()}")
                    .enqueue(object : Callback<List<Transaction>> {
                        override fun onFailure(call: Call<List<Transaction>>,
                                               t: Throwable) {
                            return
                        }

                        override fun onResponse(
                                call: Call<List<Transaction>>,
                                response: Response<List<Transaction>>
                        ) {
                            transactions?.addAll(response.body()!!)
                            adapter?.notifyDataSetChanged()
                        }
                    })
        }
    }

}
    class TransactionAdapter(
            private val inflater: LayoutInflater,
            private val dataSource: List<Transaction>
    ) : BaseAdapter() {
        override fun getView(
                position: Int, convertView: View?, parent:
                ViewGroup?
        ): View {
            val view: View = inflater.inflate(
                    R.layout.item_transaction,
                    parent, false
            )
            view.findViewById<TextView>(R.id.txtView).text =
                    dataSource[position].userId.toString()
            return view
        }

        override fun getItem(position: Int): Any {
            return dataSource[position]
        }

        override fun getItemId(position: Int): Long {
            return dataSource[position].userId?.toLong() ?: 0
        }

        override fun getCount(): Int {
            return dataSource.size
        }

    }

