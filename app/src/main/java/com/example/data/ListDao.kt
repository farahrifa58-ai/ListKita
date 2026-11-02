package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {
    @Transaction
    @Query("SELECT * FROM lists WHERE isTemplate = :isTemplate ORDER BY updatedAt DESC")
    fun getAllListsWithItems(isTemplate: Boolean = false): Flow<List<ListWithItems>>

    @Transaction
    @Query("SELECT * FROM lists WHERE isTemplate = :isTemplate")
    suspend fun getAllListsSnapshot(isTemplate: Boolean): List<ListWithItems>

    @Transaction
    @Query("SELECT * FROM lists")
    suspend fun getAllListsIncludingTemplates(): List<ListWithItems>

    @Transaction
    @Query("SELECT * FROM lists WHERE id = :listId")
    fun getListWithItemsById(listId: Long): Flow<ListWithItems?>

    @Transaction
    @Query("SELECT * FROM lists WHERE id = :listId")
    suspend fun getListWithItemsByIdOnce(listId: Long): ListWithItems?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity): Long

    @Update
    suspend fun updateList(list: ListEntity)

    @Delete
    suspend fun deleteList(list: ListEntity)

    @Query("DELETE FROM lists WHERE id = :listId")
    suspend fun deleteListById(listId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ListItemEntity>)

    @Update
    suspend fun updateItem(item: ListItemEntity)

    @Delete
    suspend fun deleteItem(item: ListItemEntity)

    @Query("DELETE FROM list_items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Long)

    @Query("DELETE FROM lists")
    suspend fun deleteAllLists()

    @Query("DELETE FROM list_items")
    suspend fun deleteAllItems()

    @Query("SELECT COUNT(*) FROM lists WHERE isTemplate = 0")
    suspend fun getListsCount(): Int

    @Query("SELECT COUNT(*) FROM list_items")
    suspend fun getItemsCount(): Int

    @Transaction
    suspend fun duplicateList(sourceListId: Long, copyTitle: String): Long {
        val original = getListWithItemsByIdOnce(sourceListId) ?: return -1
        val now = System.currentTimeMillis()
        val newList = original.list.copy(
            id = 0,
            title = copyTitle,
            isTemplate = false,
            createdAt = now,
            updatedAt = now
        )
        val newListId = insertList(newList)
        val newItems = original.sortedItems.map { item ->
            item.copy(
                id = 0,
                listId = newListId
            )
        }
        insertItems(newItems)
        return newListId
    }

    @Transaction
    suspend fun createFromTemplate(templateId: Long, customTitle: String? = null): Long {
        val template = getListWithItemsByIdOnce(templateId) ?: return -1
        val now = System.currentTimeMillis()
        val newList = template.list.copy(
            id = 0,
            title = customTitle ?: template.list.title,
            isTemplate = false,
            createdAt = now,
            updatedAt = now
        )
        val newListId = insertList(newList)
        val newItems = template.sortedItems.map { item ->
            item.copy(
                id = 0,
                listId = newListId,
                isCompleted = false
            )
        }
        insertItems(newItems)
        return newListId
    }
}
