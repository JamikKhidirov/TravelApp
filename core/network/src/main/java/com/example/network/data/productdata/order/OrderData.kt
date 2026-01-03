package com.example.network.data.productdata.order


import com.example.network.data.productdata.duration.DurationData
import com.example.network.data.productdata.orderline.OrderLineData


data class OrderData(
    val id: String,
    val title: String,
    val ticket_type: String,
    val is_bace: Boolean,
    val duration: DurationData,
    val order_lines: List<OrderLineData>,
)
