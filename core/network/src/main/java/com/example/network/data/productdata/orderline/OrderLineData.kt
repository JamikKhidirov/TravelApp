package com.example.network.data.productdata.orderline


import com.example.network.data.productdata.rub.DataRub


data class OrderLineData(
    val id: String,
    val price: String,
    val all_price: DataRub,
    val netto_all_prices: DataRub,
    val all_amounts_to_pay: DataRub,
    val price_per: String,
    val title: String,
    val offer_type: String,
    val countable: Boolean,
    val start_date: String,
)
