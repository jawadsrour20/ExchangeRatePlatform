package com.youssef.currencyexchange

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.youssef.currencyexchange.api.ExchangeService
import com.youssef.currencyexchange.api.model.ExchangeRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExchangeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRates()

    }

    override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle? ): View? {

        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_exchange,
                container, false)

        buyUsdTextView = view.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view.findViewById(R.id.txtSellUsdRate)
        return view
    }


    private fun fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
                Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>) {
                Log.d("API", response.toString())
                val responseBody: ExchangeRates? = response.body(); // necessary code to extract from the responseBody
                                                                    // object the exchange rates, and then using them to update the two TextViews in our UI
                if (responseBody != null) {
                    buyUsdTextView?.text = responseBody.lbpToUsd.toString()
                    sellUsdTextView?.text = responseBody.usdToLbp.toString()
                };
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                Log.d("API", t.toString())
                return;
                TODO("Not yet implemented")
            }
        })
    }
}