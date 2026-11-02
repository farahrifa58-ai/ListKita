package com.example.ui.screens

import android.text.format.DateUtils
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ListWithItems
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.components.ALL_CATEGORIES
import com.example.ui.components.CategoryBadge
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.ModernHomeHeader
import com.example.ui.components.ModernSearchBar
import com.example.ui.components.SharePreviewDialog
import com.example.ui.components.getCategoryInfo
import com.example.ui.theme.PrimaryBlue
import com.example.util.ShareUtils
import com.example.viewmodel.ListViewModel
import com.example.viewmodel.SortMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyListsScreen(
    viewModel: ListViewModel,
    onNavigateToDetail: (Long, Boolean) -> Unit, // listId, isEditMode
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val lists by viewModel.filteredLists.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortMode by viewModel.sortMode.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<ListWithItems?>(null) }
    var listToPreviewShare by remember { mutableStateOf<ListWithItems?>(null) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCreate,
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
                        text = Strings.get("btn_create_new_fab", language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag("fab_create_new_list")
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
            // Header: Selamat datang 👋 & Daftar Saya
            ModernHomeHeader(language = language)

            // Modern Search Bar: Cari daftar...
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                ModernSearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.searchQuery.value = it },
                    placeholderText = Strings.get("search_hint_modern", language)
                )
            }

            // Categories Filter & Sort Bar: Semua | Produk | Makanan | Belanja | Stok | Lainnya
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val isAllSelected = selectedCategory == null || selectedCategory == "Semua" || selectedCategory == "All"
                    FilterChip(
                        selected = isAllSelected,
                        onClick = { viewModel.selectedCategory.value = null },
                        label = {
                            Text(
                                text = Strings.get("all_categories", language),
                                fontSize = 13.sp,
                                fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isAllSelected,
                            borderColor = if (isAllSelected) PrimaryBlue else MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.testTag("filter_chip_all")
                    )

                    ALL_CATEGORIES.forEach { cat ->
                        val catInfo = getCategoryInfo(cat)
                        val localizedName = Strings.get(catInfo.nameKey, language)
                        val isSelected = selectedCategory.equals(cat, ignoreCase = true)

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.selectedCategory.value = if (isSelected) null else cat
                            },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = catInfo.emoji, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = localizedName,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White,
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.testTag("filter_chip_$cat")
                        )
                    }
                }

                // Sort Dropdown Button
                Box(modifier = Modifier.padding(end = 16.dp)) {
                    IconButton(
                        onClick = { showSortMenu = true },
                        modifier = Modifier.testTag("sort_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = Strings.get("sort_by", language),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(Strings.get("sort_updated", language)) },
                            onClick = {
                                viewModel.sortMode.value = SortMode.LAST_UPDATED
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(Strings.get("sort_newest", language)) },
                            onClick = {
                                viewModel.sortMode.value = SortMode.NEWEST_CREATED
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(Strings.get("sort_oldest", language)) },
                            onClick = {
                                viewModel.sortMode.value = SortMode.OLDEST_CREATED
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(Strings.get("sort_az", language)) },
                            onClick = {
                                viewModel.sortMode.value = SortMode.NAME_ASC
                                showSortMenu = false
                            }
                        )
                    }
                }
            }

            // List Content / Empty State
            if (lists.isEmpty()) {
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
                        Surface(
                            shape = CircleShape,
                            color = PrimaryBlue.copy(alpha = 0.08f),
                            modifier = Modifier.size(96.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (searchQuery.isNotEmpty()) "🔍" else "📋",
                                    fontSize = 44.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) {
                                Strings.get("empty_search_title", language)
                            } else {
                                Strings.get("empty_lists_title", language)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) {
                                Strings.get("empty_search_desc", language, searchQuery)
                            } else {
                                Strings.get("empty_lists_desc", language)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            lineHeight = 20.sp
                        )
                        if (searchQuery.isEmpty()) {
                            Spacer(modifier = Modifier.height(22.dp))
                            OutlinedButton(
                                onClick = onNavigateToCreate,
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                                modifier = Modifier.testTag("empty_state_create_button")
                            ) {
                                Text(
                                    text = Strings.get("btn_create_new_list", language),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lists, key = { it.list.id }) { item ->
                        ModernListCard(
                            listWithItems = item,
                            language = language,
                            onOpen = { onNavigateToDetail(item.list.id, false) },
                            onEdit = { onNavigateToDetail(item.list.id, true) },
                            onShare = { listToPreviewShare = item },
                            onDuplicate = {
                                val suffix = Strings.get("duplicate_suffix", language)
                                viewModel.duplicateList(item.list.id, suffix) {
                                    Toast.makeText(
                                        context,
                                        Strings.get("duplicated_toast", language),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onDelete = { listToDelete = item }
                        )
                    }
                }
            }
        }
    }

    // Share Text Preview Dialog
    listToPreviewShare?.let { item ->
        val text = ShareUtils.formatListAsText(item, language)
        SharePreviewDialog(
            formattedText = text,
            language = language,
            onShareWhatsApp = {
                ShareUtils.shareViaSystem(context, item, language)
            },
            onShareOther = {
                ShareUtils.shareViaSystem(context, item, language)
            },
            onCopyText = {
                ShareUtils.copyToClipboard(context, item, language)
            },
            onDismiss = { listToPreviewShare = null }
        )
    }

    // Accidental Delete Protection Dialog
    listToDelete?.let { target ->
        ConfirmDeleteDialog(
            title = Strings.get("delete_list_title", language),
            message = Strings.get("delete_list_msg", language),
            language = language,
            onConfirm = {
                viewModel.deleteList(target.list.id)
                listToDelete = null
            },
            onDismiss = {
                listToDelete = null
            }
        )
    }
}

@Composable
fun ModernListCard(
    listWithItems: ListWithItems,
    language: AppLanguage,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedMenu by remember { mutableStateOf(false) }
    val itemCount = listWithItems.items.size
    val updatedRelative = formatUpdatedDate(listWithItems.list.updatedAt, language)
    val catInfo = getCategoryInfo(listWithItems.list.category)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("list_card_${listWithItems.list.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon on the Left (inside soft rounded container)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = catInfo.color.copy(alpha = 0.12f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = catInfo.emoji,
                        fontSize = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Main Content: Title & Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = listWithItems.list.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle: 15 Item • Terakhir diubah hari ini
                Text(
                    text = "${Strings.get("items_count", language, itemCount)} • $updatedRelative",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 3-dot Menu Icon on Right
            Box {
                IconButton(
                    onClick = { expandedMenu = true },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("card_menu_button_${listWithItems.list.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(Strings.get("btn_edit", language)) },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            expandedMenu = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(Strings.get("btn_duplicate", language)) },
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            expandedMenu = false
                            onDuplicate()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(Strings.get("btn_share", language)) },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        onClick = {
                            expandedMenu = false
                            onShare()
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
                            expandedMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

private fun formatUpdatedDate(timestamp: Long, language: AppLanguage): String {
    val isToday = DateUtils.isToday(timestamp)
    val isYesterday = DateUtils.isToday(timestamp + DateUtils.DAY_IN_MILLIS)

    return when {
        isToday -> Strings.get("updated_today", language)
        isYesterday -> Strings.get("updated_yesterday", language)
        else -> {
            val format = SimpleDateFormat("dd MMM", if (language == AppLanguage.INDONESIAN) Locale("id", "ID") else Locale.ENGLISH)
            Strings.get("updated_on", language, format.format(Date(timestamp)))
        }
    }
}
