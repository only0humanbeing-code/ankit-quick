package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        CategoryEntity::class,
        BrandEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        AddressEntity::class,
        OrderEntity::class,
        DeliverySlotEntity::class,
        StoreConfigEntity::class,
        UserProfileEntity::class,
        NotifySubscriptionEntity::class,
        SavedForLaterEntity::class
    ],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuickKartDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun brandDao(): BrandDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun storeOpsDao(): StoreOpsDao

    companion object {
        @Volatile
        private var INSTANCE: QuickKartDatabase? = null

        fun getInstance(context: Context): QuickKartDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuickKartDatabase::class.java,
                    "quickkart_local.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
