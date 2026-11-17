package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {
    @Query("SELECT * FROM brands ORDER BY rowid ASC")
    fun observeAll(): Flow<List<BrandEntity>>

    @Query("SELECT * FROM brands WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BrandEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BrandEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIgnore(items: List<BrandEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: BrandEntity)

    @Query("DELETE FROM brands WHERE id = :id")
    suspend fun deleteById(id: String)
}
