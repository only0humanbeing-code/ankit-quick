package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY is_featured DESC, name ASC")
    fun observeAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category_id = :categoryId ORDER BY is_featured DESC, name ASC")
    fun observeByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE brand_id = :brandId ORDER BY is_featured DESC, name ASC")
    fun observeByBrand(brandId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE is_featured = 1 ORDER BY rating DESC, name ASC")
    fun observeFeatured(): Flow<List<ProductEntity>>

    @Query(
        "SELECT * FROM products " +
            "WHERE name LIKE '%' || :query || '%' " +
            "OR description LIKE '%' || :query || '%' " +
            "ORDER BY is_featured DESC, rating DESC"
    )
    fun search(query: String): Flow<List<ProductEntity>>

    @Transaction
    @Query("SELECT * FROM products ORDER BY is_featured DESC, name ASC")
    fun observeAllWithDetails(): Flow<List<ProductWithDetails>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ProductEntity?

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIgnore(items: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ProductEntity)

    @Query("UPDATE products SET shop_category = :shopCategory, sub_category = :subCategory WHERE id = :id")
    suspend fun updateTaxonomy(id: String, shopCategory: String, subCategory: String)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: String)
}
