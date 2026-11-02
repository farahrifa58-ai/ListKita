package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BackupRepository
import com.example.data.ListEntity
import com.example.data.ListItemEntity
import com.example.data.ListKitaDatabase
import com.example.data.ListWithItems
import com.example.data.PreferencesManager
import com.example.data.ThemeMode
import com.example.localization.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

enum class SortMode {
    LAST_UPDATED,
    NEWEST_CREATED,
    OLDEST_CREATED,
    NAME_ASC
}

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val db = ListKitaDatabase.getDatabase(application, viewModelScope)
    val listDao = db.listDao()
    val preferencesManager = PreferencesManager(application)
    val backupRepository = BackupRepository(application, listDao, preferencesManager)

    // Language & Theme
    val language: StateFlow<AppLanguage> = preferencesManager.language
    val themeMode: StateFlow<ThemeMode> = preferencesManager.themeMode
    val lastBackupTime: StateFlow<Long> = preferencesManager.lastBackupTime

    // Filter & Search
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<String?>(null)
    val sortMode = MutableStateFlow(SortMode.LAST_UPDATED)

    // Raw Lists Flow
    private val rawLists = listDao.getAllListsWithItems(isTemplate = false)
    val rawTemplates = listDao.getAllListsWithItems(isTemplate = true)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered & Sorted Lists
    val filteredLists: StateFlow<List<ListWithItems>> = combine(
        rawLists,
        searchQuery,
        selectedCategory,
        sortMode
    ) { lists, query, category, sort ->
        var result = lists

        // Category filter
        if (category != null && category.isNotBlank() && category != "Semua" && category != "All") {
            result = result.filter { it.list.category.equals(category, ignoreCase = true) }
        }

        // Query filter (matches list title, category, or any item name/note)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            result = result.filter { listWithItems ->
                listWithItems.list.title.lowercase().contains(q) ||
                listWithItems.list.category.lowercase().contains(q) ||
                listWithItems.items.any { item ->
                    item.name.lowercase().contains(q) ||
                    item.note.lowercase().contains(q) ||
                    item.customValuesJson.lowercase().contains(q)
                }
            }
        }

        // Sorting
        when (sort) {
            SortMode.LAST_UPDATED -> result.sortedByDescending { it.list.updatedAt }
            SortMode.NEWEST_CREATED -> result.sortedByDescending { it.list.createdAt }
            SortMode.OLDEST_CREATED -> result.sortedBy { it.list.createdAt }
            SortMode.NAME_ASC -> result.sortedBy { it.list.title.lowercase() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently Selected List for Detail
    private val _selectedListId = MutableStateFlow<Long?>(null)
    val selectedListId: StateFlow<Long?> = _selectedListId.asStateFlow()

    private val _currentListWithItems = MutableStateFlow<ListWithItems?>(null)
    val currentListWithItems: StateFlow<ListWithItems?> = _currentListWithItems.asStateFlow()

    // Storage Statistics
    private val _statsListsCount = MutableStateFlow(0)
    val statsListsCount: StateFlow<Int> = _statsListsCount.asStateFlow()

    private val _statsItemsCount = MutableStateFlow(0)
    val statsItemsCount: StateFlow<Int> = _statsItemsCount.asStateFlow()

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch(Dispatchers.IO) {
            _statsListsCount.value = listDao.getListsCount()
            _statsItemsCount.value = listDao.getItemsCount()
        }
    }

    fun selectList(listId: Long) {
        _selectedListId.value = listId
        viewModelScope.launch(Dispatchers.IO) {
            listDao.getListWithItemsById(listId).collect {
                _currentListWithItems.value = it
            }
        }
    }

    fun clearSelectedList() {
        _selectedListId.value = null
        _currentListWithItems.value = null
    }

    fun setLanguage(lang: AppLanguage) {
        preferencesManager.setLanguage(lang)
    }

    fun setThemeMode(mode: ThemeMode) {
        preferencesManager.setThemeMode(mode)
    }

    fun createList(
        title: String,
        category: String,
        customColumns: List<String>,
        isTemplate: Boolean = false,
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonArray = JSONArray()
            customColumns.forEach { jsonArray.put(it) }
            val now = System.currentTimeMillis()
            val list = ListEntity(
                title = title.ifBlank { "Daftar Baru" },
                category = category,
                customColumnsJson = jsonArray.toString(),
                isTemplate = isTemplate,
                createdAt = now,
                updatedAt = now
            )
            val newId = listDao.insertList(list)
            refreshStats()
            launch(Dispatchers.Main) {
                onCreated(newId)
            }
        }
    }

    fun updateListDetails(listId: Long, title: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = listDao.getListWithItemsByIdOnce(listId) ?: return@launch
            val updated = existing.list.copy(
                title = title.ifBlank { existing.list.title },
                category = category,
                updatedAt = System.currentTimeMillis()
            )
            listDao.updateList(updated)
        }
    }

    fun updateCustomColumns(listId: Long, customColumns: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = listDao.getListWithItemsByIdOnce(listId) ?: return@launch
            val jsonArray = JSONArray()
            customColumns.forEach { jsonArray.put(it) }
            val updated = existing.list.copy(
                customColumnsJson = jsonArray.toString(),
                updatedAt = System.currentTimeMillis()
            )
            listDao.updateList(updated)
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            listDao.deleteListById(listId)
            refreshStats()
            if (_selectedListId.value == listId) {
                _selectedListId.value = null
                _currentListWithItems.value = null
            }
        }
    }

    fun duplicateList(listId: Long, suffix: String, onDuplicated: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val original = listDao.getListWithItemsByIdOnce(listId) ?: return@launch
            val newTitle = "${original.list.title} $suffix"
            val newId = listDao.duplicateList(listId, newTitle)
            refreshStats()
            launch(Dispatchers.Main) {
                onDuplicated(newId)
            }
        }
    }

    fun saveAsTemplate(listId: Long, onSaved: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val original = listDao.getListWithItemsByIdOnce(listId) ?: return@launch
            val templateList = original.list.copy(
                id = 0,
                isTemplate = true,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val templateId = listDao.insertList(templateList)
            val templateItems = original.sortedItems.map {
                it.copy(id = 0, listId = templateId, isCompleted = false)
            }
            listDao.insertItems(templateItems)
            refreshStats()
            launch(Dispatchers.Main) {
                onSaved()
            }
        }
    }

    fun createFromTemplate(templateId: Long, onCreated: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = listDao.createFromTemplate(templateId)
            refreshStats()
            launch(Dispatchers.Main) {
                onCreated(newId)
            }
        }
    }

    // Item Actions
    fun addItem(
        listId: Long,
        name: String,
        quantity: String,
        note: String,
        customValues: Map<String, String> = emptyMap()
    ) {
        if (name.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val current = listDao.getListWithItemsByIdOnce(listId)
            val nextOrder = (current?.items?.size ?: 0)
            val jsonMap = JSONObject()
            customValues.forEach { (k, v) -> jsonMap.put(k, v) }

            val item = ListItemEntity(
                listId = listId,
                name = name.trim(),
                quantity = quantity.trim().ifBlank { "1" },
                note = note.trim(),
                customValuesJson = jsonMap.toString(),
                orderIndex = nextOrder,
                isCompleted = false
            )
            listDao.insertItem(item)

            // Touch list updatedAt
            current?.let {
                listDao.updateList(it.list.copy(updatedAt = System.currentTimeMillis()))
            }
            refreshStats()
        }
    }

    fun updateItem(item: ListItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            listDao.updateItem(item)
            val list = listDao.getListWithItemsByIdOnce(item.listId)?.list
            list?.let {
                listDao.updateList(it.copy(updatedAt = System.currentTimeMillis()))
            }
        }
    }

    fun toggleItemCompleted(item: ListItemEntity) {
        updateItem(item.copy(isCompleted = !item.isCompleted))
    }

    fun deleteItem(item: ListItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            listDao.deleteItem(item)
            val list = listDao.getListWithItemsByIdOnce(item.listId)?.list
            list?.let {
                listDao.updateList(it.copy(updatedAt = System.currentTimeMillis()))
            }
            refreshStats()
        }
    }

    fun moveItem(listId: Long, fromIndex: Int, toIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val listWithItems = listDao.getListWithItemsByIdOnce(listId) ?: return@launch
            val items = listWithItems.sortedItems.toMutableList()
            if (fromIndex !in items.indices || toIndex !in items.indices) return@launch

            val moved = items.removeAt(fromIndex)
            items.add(toIndex, moved)

            items.forEachIndexed { idx, itm ->
                listDao.updateItem(itm.copy(orderIndex = idx))
            }
            listDao.updateList(listWithItems.list.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    // Backup & Restore
    fun backupToUri(uri: Uri, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = backupRepository.writeBackupToUri(uri)
            refreshStats()
            launch(Dispatchers.Main) {
                onResult(success)
            }
        }
    }

    fun restoreFromUri(uri: Uri, replaceAll: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = backupRepository.readBackupFromUri(uri, replaceAll)
            refreshStats()
            launch(Dispatchers.Main) {
                onResult(success)
            }
        }
    }
}
