package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Relation
import com.example.model.CartItem

@Entity(
    tableName = "cart_items",
    primaryKeys = ["product_id", "variant_id"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["product_id"]),
        Index(value = ["added_at_millis"])
    ]
)
data class CartItemEntity(
    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "variant_id")
    val variantId: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "added_at_millis")
    val addedAtMillis: Long = System.currentTimeMillis()
)

/**
 * Room relation pairing a CartItemEntity row with its referenced ProductEntity.
 */
data class CartItemWithProduct(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: ProductEntity?
)

fun CartItemEntity.toDomain(): CartItem = CartItem(
    productId = productId,
    variantId = variantId,
    quantity = quantity
)

fun CartItem.toEntity(addedAtMillis: Long = System.currentTimeMillis()): CartItemEntity =
    CartItemEntity(
        productId = productId,
        variantId = variantId,
        quantity = quantity,
        addedAtMillis = addedAtMillis
    )
