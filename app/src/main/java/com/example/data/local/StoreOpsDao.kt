package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreOpsDao {
    // Addresses
    @Query("SELECT * FROM addresses ORDER BY isDefault DESC, label ASC")
    fun observeAddresses(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddresses(items: List<AddressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAddress(item: AddressEntity)

    @Query("UPDATE addresses SET isDefault = 0")
    suspend fun clearDefaultAddresses()

    @Query("UPDATE addresses SET isDefault = 1 WHERE id = :id")
    suspend fun setDefaultAddress(id: String)

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun deleteAddress(id: String)

    // Orders
    @Query("SELECT * FROM orders ORDER BY createdAtMillis DESC")
    fun observeOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(items: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrder(item: OrderEntity)

    // Delivery Slots
    @Query("SELECT * FROM delivery_slots ORDER BY isExpress DESC, id ASC")
    fun observeDeliverySlots(): Flow<List<DeliverySlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliverySlots(items: List<DeliverySlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDeliverySlot(item: DeliverySlotEntity)

    @Query("DELETE FROM delivery_slots WHERE id = :id")
    suspend fun deleteDeliverySlot(id: String)

    // Store Config
    @Query("SELECT * FROM store_config WHERE id = 1")
    fun observeStoreConfig(): Flow<StoreConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStoreConfig(config: StoreConfigEntity)

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserProfile(profile: UserProfileEntity)

    // Notify Subscriptions
    @Query("SELECT productId FROM notify_subscriptions")
    fun observeNotifyProductIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotifySubscription(item: NotifySubscriptionEntity)

    @Query("DELETE FROM notify_subscriptions WHERE productId = :productId")
    suspend fun removeNotifySubscription(productId: String)

    // Save For Later
    @Query("SELECT productId FROM saved_for_later ORDER BY createdAtMillis DESC")
    fun observeSavedProductIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSavedForLater(item: SavedForLaterEntity)

    @Query("DELETE FROM saved_for_later WHERE productId = :productId")
    suspend fun removeSavedForLater(productId: String)
}
