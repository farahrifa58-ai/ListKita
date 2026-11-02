package com.example.data

import android.content.Context
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BackupModel(
    val appName: String,
    val version: Int,
    val exportedAt: Long,
    val lists: List<BackupListModel>
)

data class BackupListModel(
    val title: String,
    val category: String,
    val customColumnsJson: String,
    val isTemplate: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val items: List<BackupItemModel>
)

data class BackupItemModel(
    val name: String,
    val quantity: String,
    val note: String,
    val customValuesJson: String,
    val orderIndex: Int,
    val isCompleted: Boolean
)

class BackupRepository(
    private val context: Context,
    private val listDao: ListDao,
    private val preferencesManager: PreferencesManager
) {
    suspend fun createBackupJson(): String {
        val allLists = listDao.getAllListsIncludingTemplates()
        val root = JSONObject()
        root.put("appName", "ListKita")
        root.put("version", 1)
        root.put("exportedAt", System.currentTimeMillis())

        val listsArray = JSONArray()
        for (listWithItems in allLists) {
            val listObj = JSONObject()
            listObj.put("title", listWithItems.list.title)
            listObj.put("category", listWithItems.list.category)
            listObj.put("customColumnsJson", listWithItems.list.customColumnsJson)
            listObj.put("isTemplate", listWithItems.list.isTemplate)
            listObj.put("createdAt", listWithItems.list.createdAt)
            listObj.put("updatedAt", listWithItems.list.updatedAt)

            val itemsArray = JSONArray()
            for (item in listWithItems.sortedItems) {
                val itemObj = JSONObject()
                itemObj.put("name", item.name)
                itemObj.put("quantity", item.quantity)
                itemObj.put("note", item.note)
                itemObj.put("customValuesJson", item.customValuesJson)
                itemObj.put("orderIndex", item.orderIndex)
                itemObj.put("isCompleted", item.isCompleted)
                itemsArray.put(itemObj)
            }
            listObj.put("items", itemsArray)
            listsArray.put(listObj)
        }

        root.put("lists", listsArray)
        return root.toString(2)
    }

    fun parseBackupJson(jsonString: String): BackupModel {
        val root = JSONObject(jsonString)
        val appName = root.optString("appName", "ListKita")
        val version = root.optInt("version", 1)
        val exportedAt = root.optLong("exportedAt", System.currentTimeMillis())

        val listsList = mutableListOf<BackupListModel>()
        val listsArray = root.getJSONArray("lists")

        for (i in 0 until listsArray.length()) {
            val listObj = listsArray.getJSONObject(i)
            val itemsList = mutableListOf<BackupItemModel>()
            val itemsArray = listObj.optJSONArray("items") ?: JSONArray()

            for (j in 0 until itemsArray.length()) {
                val itemObj = itemsArray.getJSONObject(j)
                itemsList.add(
                    BackupItemModel(
                        name = itemObj.optString("name", ""),
                        quantity = itemObj.optString("quantity", "1"),
                        note = itemObj.optString("note", ""),
                        customValuesJson = itemObj.optString("customValuesJson", "{}"),
                        orderIndex = itemObj.optInt("orderIndex", j),
                        isCompleted = itemObj.optBoolean("isCompleted", false)
                    )
                )
            }

            listsList.add(
                BackupListModel(
                    title = listObj.optString("title", "Untitled List"),
                    category = listObj.optString("category", "Umum"),
                    customColumnsJson = listObj.optString("customColumnsJson", "[]"),
                    isTemplate = listObj.optBoolean("isTemplate", false),
                    createdAt = listObj.optLong("createdAt", System.currentTimeMillis()),
                    updatedAt = listObj.optLong("updatedAt", System.currentTimeMillis()),
                    items = itemsList
                )
            )
        }

        return BackupModel(appName, version, exportedAt, listsList)
    }

    suspend fun restoreBackup(backup: BackupModel, replaceAll: Boolean) {
        if (replaceAll) {
            listDao.deleteAllItems()
            listDao.deleteAllLists()
        }

        for (listModel in backup.lists) {
            val listEntity = ListEntity(
                title = listModel.title,
                category = listModel.category,
                customColumnsJson = listModel.customColumnsJson,
                isTemplate = listModel.isTemplate,
                createdAt = listModel.createdAt,
                updatedAt = listModel.updatedAt
            )
            val insertedListId = listDao.insertList(listEntity)

            val items = listModel.items.map { itemModel ->
                ListItemEntity(
                    listId = insertedListId,
                    name = itemModel.name,
                    quantity = itemModel.quantity,
                    note = itemModel.note,
                    customValuesJson = itemModel.customValuesJson,
                    orderIndex = itemModel.orderIndex,
                    isCompleted = itemModel.isCompleted
                )
            }
            listDao.insertItems(items)
        }

        preferencesManager.setLastBackupTime(System.currentTimeMillis())
    }

    suspend fun writeBackupToUri(uri: Uri): Boolean {
        return try {
            val json = createBackupJson()
            context.contentResolver.openOutputStream(uri)?.use { os ->
                os.write(json.toByteArray(Charsets.UTF_8))
                os.flush()
            }
            preferencesManager.setLastBackupTime(System.currentTimeMillis())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun readBackupFromUri(uri: Uri, replaceAll: Boolean): Boolean {
        return try {
            val stringBuilder = StringBuilder()
            context.contentResolver.openInputStream(uri)?.use { isStream ->
                BufferedReader(InputStreamReader(isStream, Charsets.UTF_8)).use { reader ->
                    var line = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = reader.readLine()
                    }
                }
            }
            val backup = parseBackupJson(stringBuilder.toString())
            restoreBackup(backup, replaceAll)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun generateBackupFileName(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HHmm", Locale.getDefault())
        return "ListKita_Backup_${dateFormat.format(Date())}.json"
    }
}
