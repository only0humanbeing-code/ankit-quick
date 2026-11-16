package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY added_at_millis ASC")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    @Transaction
    @Query("SELECT * FROM cart_items ORDER BY added_at_millis ASC")
    fun observeCartWithProducts(): Flow<List<CartItemWithProduct>>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun observeTotalQuantity(): Flow<Int>

    @Query("SELECT * FROM cart_items WHERE product_id = :productId AND variant_id = :variantId LIMIT 1")
    suspend fun getItem(productId: String, variantId: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE product_id = :productId AND variant_id = :variantId")
    suspend fun updateQuantity(productId: String, variantId: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE product_id = :productId AND variant_id = :variantId")
    suspend fun deleteItem(productId: String, variantId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
