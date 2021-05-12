package com.youssef.currencyexchange.api.model
import com.google.gson.annotations.SerializedName
import java.util.*

class Transaction {

    @SerializedName("usd_amount")
    var usdAmount : Float? = null

    @SerializedName("lbp_amount")
    var lbpAmount : Float? = null

    @SerializedName("usd_to_lbp")
    var usdToLbp : Boolean? = null

    @SerializedName("added_date")
    var addedDate : Date? = null

    @SerializedName("user_id")
    var userId : Int? = null
}