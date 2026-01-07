package com.example.domain.sputnikdata.productdata.order


import com.example.domain.sputnikdata.productdata.duration.DurationData
import com.example.domain.sputnikdata.productdata.orderline.OrderLineData


data class OrderData(
    val id: String,
    val title: String,
    val ticket_type: String,
    val is_bace: Boolean,
    val duration: DurationData,
    val order_lines: List<OrderLineData>,
)
