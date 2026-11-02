package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.data.AppCurrency
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.data.ListItemEntity
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.util.NumberFormatUtils
import com.example.ui.components.CategoryBadge
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.ManageColumnsDialog
import com.example.ui.components.QuantityStepper
import com.example.ui.components.SharePreviewDialog
import com.example.ui.components.getCategoryInfo
import com.example.ui.theme.AccentSuccess
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.ShareUtils
import com.example.viewmodel.ListViewModel
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailListScreen(
    viewModel: ListViewModel,
    listId: Long,
    initialEditMode: Boolean = false,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val listWithItems by viewModel.currentListWithItems.collectAsState()

    var isEditMode by remember { mutableStateOf(initialEditMode) }
    var showTopMenu by remember { mutableStateOf(false) }
    var showDeleteListDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<ListItemEntity?>(null) }
    var showSharePreviewDialog by remember { mutableStateOf(false) }
    var showManageColumnsDialog by remember { mutableStateOf(false) }
    
    // Bottom Sheet for Add/Edit Item
    var showItemBottomSheet by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ListItemEntity?>(null) }

    BackHandler {
        onBack()
    }

    if (listWithItems == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...")
        }
        return
    }

    val currentList = listWithItems!!.list
    val items = listWithItems!!.sortedItems
    val catInfo = getCategoryInfo(currentList.category)
    val localizedCat = Strings.get(catInfo.nameKey, language)

    // Parse custom columns if any
    val customColumns = remember(currentList.customColumnsJson) {
        val list = mutableListOf<String>()
        try {
            val arr = JSONArray(currentList.customColumnsJson)
            for (i in 0 until arr.length()) {
                list.add(arr.getString(i))
            }
        } catch (e: Exception) {
            // ignore
        }
        list
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        // Title
                        Text(
                            text = currentList.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        // Subtitle: Kategori • Jumlah Item
                        Text(
                            text = Strings.get("detail_info_subtitle", language, localizedCat, items.size),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Prominent Share Button
                    IconButton(
                        onClick = { showSharePreviewDialog = true },
                        modifier = Modifier.testTag("detail_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = Strings.get("btn_share", language),
                            tint = PrimaryBlue
                        )
                    }

                    // Overflow Menu
                    Box {
                        IconButton(
                            onClick = { showTopMenu = true },
                            modifier = Modifier.testTag("detail_more_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More"
                            )
                        }

                        DropdownMenu(
                            expanded = showTopMenu,
                            onDismissRequest = { showTopMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (isEditMode) Strings.get("mode_view", language) else Strings.get("mode_edit", language)
                                    )
                                },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    showTopMenu = false
                                    isEditMode = !isEditMode
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(Strings.get("manage_columns", language)) },
                                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    showTopMenu = false
                                    showManageColumnsDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(Strings.get("save_as_template", language)) },
                                leadingIcon = { Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    showTopMenu = false
                                    viewModel.saveAsTemplate(currentList.id) {
                                        Toast.makeText(
                                            context,
                                            Strings.get("saved_as_template_toast", language),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        Strings.get("btn_delete", language),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.DeleteOutline,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showTopMenu = false
                                    showDeleteListDialog = true
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingItem = null
                    showItemBottomSheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = Strings.get("btn_add_item", language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag("fab_add_item")
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Mode Banner (when in Edit Mode)
            if (isEditMode) {
                Surface(
                    color = PrimaryBlue.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✏️ " + Strings.get("mode_edit", language) + ": Ubah nama, jumlah, atau urutan item",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue
                        )
                        TextButton(
                            onClick = { isEditMode = false },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = Strings.get("btn_save", language),
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Items List
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "📝", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Belum ada item di daftar ini",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tekan tombol Tambah Item untuk mulai mengisi daftar ini.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                editingItem = null
                                showItemBottomSheet = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("empty_add_item_btn")
                        ) {
                            Text(
                                text = Strings.get("btn_add_item", language),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(items, key = { _, itm -> itm.id }) { index, item ->
                        ModernItemRowCard(
                            item = item,
                            index = index,
                            totalCount = items.size,
                            isEditMode = isEditMode,
                            language = language,
                            onToggleComplete = { viewModel.toggleItemCompleted(item) },
                            onEdit = {
                                editingItem = item
                                showItemBottomSheet = true
                            },
                            onDelete = { itemToDelete = item },
                            onMoveUp = {
                                if (index > 0) {
                                    viewModel.moveItem(currentList.id, index, index - 1)
                                }
                            },
                            onMoveDown = {
                                if (index < items.size - 1) {
                                    viewModel.moveItem(currentList.id, index, index + 1)
                                }
                            }
                        )
                    }

                    // Bottom Action Bar: Bagikan
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { showSharePreviewDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("detail_bottom_share_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = Strings.get("btn_share", language),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        ShareUtils.copyToClipboard(context, listWithItems!!, language)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("detail_bottom_copy_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(Strings.get("btn_copy", language))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Share Preview Dialog (Text Preview + WhatsApp + Other Apps + Copy)
    if (showSharePreviewDialog) {
        val text = ShareUtils.formatListAsText(listWithItems!!, language)
        SharePreviewDialog(
            formattedText = text,
            language = language,
            onShareWhatsApp = {
                ShareUtils.shareViaSystem(context, listWithItems!!, language)
            },
            onShareOther = {
                ShareUtils.shareViaSystem(context, listWithItems!!, language)
            },
            onCopyText = {
                ShareUtils.copyToClipboard(context, listWithItems!!, language)
            },
            onDismiss = { showSharePreviewDialog = false }
        )
    }

    // Modal Bottom Sheet for Add / Edit Item (with QuantityStepper and Validation)
    if (showItemBottomSheet) {
        ModernItemBottomSheet(
            language = language,
            currency = currency,
            item = editingItem,
            customColumns = customColumns,
            onDismiss = { showItemBottomSheet = false },
            onSave = { name, quantity, note, customValues ->
                if (editingItem != null) {
                    val jsonMap = JSONObject()
                    customValues.forEach { (k, v) -> jsonMap.put(k, v) }
                    viewModel.updateItem(
                        editingItem!!.copy(
                            name = name,
                            quantity = quantity,
                            note = note,
                            customValuesJson = jsonMap.toString()
                        )
                    )
                    Toast.makeText(context, Strings.get("toast_item_updated", language), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addItem(
                        listId = currentList.id,
                        name = name,
                        quantity = quantity,
                        note = note,
                        customValues = customValues
                    )
                    Toast.makeText(context, Strings.get("toast_item_added", language), Toast.LENGTH_SHORT).show()
                }
                showItemBottomSheet = false
            }
        )
    }

    // Manage Columns Dialog (⚙️ Atur Kolom)
    if (showManageColumnsDialog) {
        ManageColumnsDialog(
            columns = customColumns,
            language = language,
            onAddColumn = { newCol ->
                val updated = customColumns.toMutableList().apply { add(newCol) }
                viewModel.updateCustomColumns(currentList.id, updated)
            },
            onRemoveColumn = { colToRemove ->
                val updated = customColumns.toMutableList().apply { remove(colToRemove) }
                viewModel.updateCustomColumns(currentList.id, updated)
            },
            onDismiss = { showManageColumnsDialog = false }
        )
    }

    // Delete Item Confirmation
    itemToDelete?.let { target ->
        ConfirmDeleteDialog(
            title = Strings.get("delete_item_title", language),
            message = Strings.get("delete_item_msg", language, target.name),
            language = language,
            onConfirm = {
                viewModel.deleteItem(target)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }

    // Delete List Confirmation
    if (showDeleteListDialog) {
        ConfirmDeleteDialog(
            title = Strings.get("delete_list_title", language),
            message = Strings.get("delete_list_msg", language),
            language = language,
            onConfirm = {
                viewModel.deleteList(currentList.id)
                showDeleteListDialog = false
                onBack()
            },
            onDismiss = { showDeleteListDialog = false }
        )
    }
}

@Composable
fun ModernItemRowCard(
    item: ListItemEntity,
    index: Int,
    totalCount: Int,
    isEditMode: Boolean,
    language: AppLanguage,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customKeyValues = remember(item.customValuesJson) {
        val list = mutableListOf<Pair<String, String>>()
        if (item.customValuesJson.isNotBlank() && item.customValuesJson != "{}") {
            try {
                val json = JSONObject(item.customValuesJson)
                val keys = json.keys()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val v = json.optString(k)
                    if (v.isNotBlank()) {
                        list.add(k to v)
                    }
                }
            } catch (e: Exception) {
                // ignore
            }
        }
        list
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) Color(0xFFF8FAFC) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (item.isCompleted) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.outline
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("item_card_${item.id}")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item Number Circle
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            if (item.isCompleted) MaterialTheme.colorScheme.outlineVariant else PrimaryBlue.copy(alpha = 0.12f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isCompleted) TextMuted else PrimaryBlue
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Checkbox for completed status
                Checkbox(
                    checked = item.isCompleted,
                    onCheckedChange = { onToggleComplete() },
                    colors = CheckboxDefaults.colors(checkedColor = AccentSuccess),
                    modifier = Modifier.testTag("item_checkbox_${item.id}")
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Item Name, Quantity, and Note
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { if (isEditMode) onEdit() else onToggleComplete() }
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isCompleted) TextMuted else TextPrimary,
                        textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Detail text: Jumlah: 2
                    Text(
                        text = "${Strings.get("item_qty_label", language)}: ${item.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = if (item.isCompleted) TextMuted else TextSecondary
                    )

                    // Catatan: Pedas
                    if (item.note.isNotBlank()) {
                        Text(
                            text = "${Strings.get("item_note_label", language)}: ${item.note}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (item.isCompleted) TextMuted else TextSecondary
                        )
                    }

                    // Custom columns values
                    if (customKeyValues.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            customKeyValues.forEach { (k, v) ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    val formattedVal = if (NumberFormatUtils.isNominalColumn(k)) {
                                        NumberFormatUtils.formatNominal(v)
                                    } else {
                                        v
                                    }
                                    Text(
                                        text = "$k: $formattedVal",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Quick Edit Button (always accessible as requested)
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("item_edit_btn_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Item",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Reorder and Delete in Edit Mode
                if (isEditMode) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Move Up
                        IconButton(
                            onClick = onMoveUp,
                            enabled = index > 0,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = Strings.get("move_up", language),
                                tint = if (index > 0) PrimaryBlue else TextMuted
                            )
                        }

                        // Move Down
                        IconButton(
                            onClick = onMoveDown,
                            enabled = index < totalCount - 1,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = Strings.get("move_down", language),
                                tint = if (index < totalCount - 1) PrimaryBlue else TextMuted
                            )
                        }

                        // Delete
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernItemBottomSheet(
    language: AppLanguage,
    currency: AppCurrency,
    item: ListItemEntity?,
    customColumns: List<String>,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Map<String, String>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf(item?.name ?: "") }
    var quantity by remember { mutableStateOf(item?.quantity ?: "1") }
    var note by remember { mutableStateOf(item?.note ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }

    val customMap = remember {
        val map = mutableStateMapOf<String, String>()
        if (item != null && item.customValuesJson.isNotBlank() && item.customValuesJson != "{}") {
            try {
                val json = JSONObject(item.customValuesJson)
                val it = json.keys()
                while (it.hasNext()) {
                    val k = it.next()
                    map[k] = json.optString(k)
                }
            } catch (e: Exception) {
                // ignore
            }
        }
        map
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = if (item != null) Strings.get("btn_edit", language) + " Item" else Strings.get("btn_add_item", language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Nama Item
            Text(
                text = Strings.get("item_name_label", language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.isNotBlank()) nameError = null
                },
                placeholder = { Text(Strings.get("item_name_hint", language)) },
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) {
                        Text(
                            text = nameError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("item_form_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Jumlah with Stepper: [-]  1  [+]
            Text(
                text = Strings.get("item_qty_label", language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            QuantityStepper(
                value = quantity,
                onValueChange = { quantity = it },
                language = language,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Keterangan / Catatan
            Text(
                text = Strings.get("item_note_label", language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text(Strings.get("item_note_hint", language)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("item_form_note_input")
            )

            // Dynamic Custom Columns
            if (customColumns.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = Strings.get("optional_columns_title", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
                customColumns.forEach { colName ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = colName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val isPriceCol = NumberFormatUtils.isNominalColumn(colName)
                    OutlinedTextField(
                        value = customMap[colName] ?: "",
                        onValueChange = { input ->
                            customMap[colName] = if (isPriceCol) {
                                NumberFormatUtils.formatNominal(input)
                            } else {
                                input
                            }
                        },
                        keyboardOptions = if (isPriceCol) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
                        placeholder = { Text(if (isPriceCol) "cth: 25.000" else "Isi $colName...") },
                        prefix = if (isPriceCol) {
                            {
                                Text(
                                    text = "${currency.symbol} ",
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                            }
                        } else null,
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Button: Simpan Item
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = Strings.get("item_name_validation_error", language)
                    } else {
                        onSave(name.trim(), quantity.trim(), note.trim(), customMap.toMap())
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("item_form_save_btn")
            ) {
                Text(
                    text = Strings.get("btn_save_item", language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
