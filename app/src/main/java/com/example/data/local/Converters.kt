package com.example.data.local

import androidx.room.TypeConverter
import com.example.model.OrderLineItem
import com.example.model.OrderStatus
import com.example.model.ProductReview
import com.example.model.ProductVariant
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val variantListType = Types.newParameterizedType(List::class.java, ProductVariant::class.java)
    private val variantAdapter = moshi.adapter<List<ProductVariant>>(variantListType)

    private val reviewListType = Types.newParameterizedType(List::class.java, ProductReview::class.java)
    private val reviewAdapter = moshi.adapter<List<ProductReview>>(reviewListType)

    private val orderItemListType = Types.newParameterizedType(List::class.java, OrderLineItem::class.java)
    private val orderItemAdapter = moshi.adapter<List<OrderLineItem>>(orderItemListType)

    @TypeConverter
    fun fromVariantList(value: List<ProductVariant>): String = variantAdapter.toJson(value)

    @TypeConverter
    fun toVariantList(json: String): List<ProductVariant> = variantAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromReviewList(value: List<ProductReview>): String = reviewAdapter.toJson(value)

    @TypeConverter
    fun toReviewList(json: String): List<ProductReview> = reviewAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromOrderItemList(value: List<OrderLineItem>): String = orderItemAdapter.toJson(value)

    @TypeConverter
    fun toOrderItemList(json: String): List<OrderLineItem> = orderItemAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String = status.name

    @TypeConverter
    fun toOrderStatus(name: String): OrderStatus =
        runCatching { OrderStatus.valueOf(name) }.getOrDefault(OrderStatus.PLACED)
}
