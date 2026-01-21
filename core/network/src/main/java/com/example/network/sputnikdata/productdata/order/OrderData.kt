package com.example.network.sputnikdata.productdata.order



import com.example.network.sputnikdata.productdata.duration.DurationData
import com.example.network.sputnikdata.productdata.orderline.OrderLineData


data class OrderData(
    val id: String,
    val title: String,
    val ticket_type: String,
    val is_bace: Boolean,
    val duration: DurationData,
    val order_lines: List<OrderLineData>,
)
