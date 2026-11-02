package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    INDONESIAN("in", "Bahasa Indonesia", "🇮🇩"),
    ENGLISH("en", "English", "🇬🇧");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return if (code.startsWith("en", ignoreCase = true)) ENGLISH else INDONESIAN
        }
    }
}

object Strings {
    private val translations = mapOf(
        AppLanguage.INDONESIAN to mapOf(
            "app_name" to "ListKita",
            "app_tagline" to "Aplikasi untuk membuat, mengatur, dan membagikan berbagai jenis daftar dengan mudah.",
            "splash_tagline" to "Buat. Atur. Bagikan.",
            "splash_author" to "by Rafline Dev",
            
            // Navigation
            "nav_my_lists" to "List Saya",
            "nav_create" to "Buat Baru",
            "nav_settings" to "Pengaturan",
            
            // Home / Beranda
            "welcome_title" to "Selamat datang 👋",
            "my_lists_title" to "Daftar Saya",
            "search_hint_modern" to "Cari daftar...",
            "search_placeholder" to "Cari nama list, kategori, atau item...",
            "all_categories" to "Semua",
            "sort_by" to "Urutkan",
            "sort_updated" to "Terakhir Diubah",
            "sort_newest" to "Terbaru Dibuat",
            "sort_oldest" to "Terlama",
            "sort_az" to "Nama A–Z",
            
            // Categories
            "cat_product" to "Produk",
            "cat_food" to "Makanan",
            "cat_shopping" to "Belanja",
            "cat_stock" to "Stok",
            "cat_event" to "Kebutuhan Acara",
            "cat_general" to "Lainnya",
            "cat_other" to "Lainnya",
            
            // List Card & Actions
            "items_count" to "%d Item",
            "updated_today" to "Terakhir diubah hari ini",
            "updated_yesterday" to "Terakhir diubah kemarin",
            "updated_on" to "Diubah: %s",
            "btn_create_new_list" to "+ Buat List Baru",
            "btn_create_new_fab" to "＋ Buat Baru",
            "btn_open" to "Buka",
            "btn_edit" to "Edit",
            "btn_duplicate" to "Duplikat",
            "btn_delete" to "Hapus",
            "btn_share" to "Bagikan",
            "btn_copy" to "Salin Teks",
            "btn_save" to "Simpan",
            "btn_cancel" to "Batal",
            "btn_confirm" to "Konfirmasi",
            "btn_add_item" to "+ Tambah Item",
            "btn_quick_add" to "Tambah",
            
            // Confirmations
            "delete_list_title" to "Hapus List?",
            "delete_list_msg" to "List dan semua item di dalamnya akan dihapus.",
            "delete_item_title" to "Hapus Item?",
            "delete_item_msg" to "Hapus item \"%s\" dari daftar?",
            "duplicate_suffix" to "(Salinan)",
            "duplicated_toast" to "✓ Daftar berhasil diduplikat!",
            
            // Create List Screen
            "create_title" to "Buat List Baru",
            "list_name_label" to "Nama List",
            "list_name_hint" to "Contoh: Daftar Belanja Mingguan",
            "select_category_label" to "Pilih Kategori",
            "category_desc" to "Kategori membantu pengelompokan daftar Anda.",
            "optional_columns_title" to "Kolom Tambahan (Opsional)",
            "optional_columns_desc" to "Format standar: Nama, Jumlah, Catatan. Tambahkan kolom lain jika dibutuhkan:",
            "col_price" to "Harga",
            "col_location" to "Lokasi/Rak",
            "col_custom" to "Kolom Kustom Lainnya",
            "add_custom_col_hint" to "Nama kolom (misal: Warna, Vendor)",
            "use_template_shortcut" to "Atau mulai dari Template siap pakai:",
            "btn_create_list" to "Buat List",
            "btn_create_and_add_items" to "Buat List",
            
            // Custom Columns (Atur Kolom)
            "manage_columns" to "⚙️ Atur Kolom",
            "manage_columns_title" to "Atur Kolom Tambahan",
            "manage_columns_desc" to "Tambahkan atau hapus kolom khusus untuk daftar ini (misal: Harga, Lokasi/Rak).",
            "add_column_label" to "Nama Kolom Baru",
            "add_column_hint" to "cth: Harga, Satuan, Vendor",
            "btn_add_column" to "+ Tambah Kolom",
            "no_extra_columns" to "Format standar digunakan: Nama, Jumlah, Keterangan.",
            "delete_column" to "Hapus Kolom",
            
            // Detail Screen
            "detail_info_subtitle" to "%s • %d Item",
            "created_on" to "Dibuat pada: %s",
            "mode_view" to "Mode Lihat",
            "mode_edit" to "Mode Edit",
            "item_name_label" to "Nama Item",
            "item_name_hint" to "Nama barang / makanan / produk",
            "item_name_validation_error" to "Nama item belum diisi.",
            "item_qty_label" to "Jumlah",
            "item_qty_hint" to "cth: 1, 2, 500 gr",
            "item_note_label" to "Catatan",
            "item_note_hint" to "cth: Pedas, Ukuran L, Tanpa gula",
            "btn_save_item" to "Simpan Item",
            "total_items" to "Total Item",
            "completed_items" to "Selesai",
            "reorder_items" to "Ubah Urutan",
            "move_up" to "Geser Naik",
            "move_down" to "Geser Turun",
            "save_as_template" to "Simpan sebagai Template",
            "saved_as_template_toast" to "✓ Disimpan sebagai template baru!",
            
            // Share & Preview
            "share_preview_title" to "Pratinjau Pesan yang Dibagikan",
            "share_preview_desc" to "Berikut teks yang akan dikirim ke WhatsApp atau aplikasi lainnya:",
            "btn_share_whatsapp" to "Bagikan ke WhatsApp",
            "btn_share_other_apps" to "Bagikan ke Aplikasi Lain",
            "btn_copy_text" to "Salin Teks",
            "share_header" to "📋 *%s*",
            "share_category" to "🏷️ Kategori: %s",
            "share_note" to "   Catatan: %s",
            "share_created" to "📅 Dibuat pada: %s",
            "share_watermark" to "Dibagikan via ListKita",
            "share_chooser_title" to "Bagikan Daftar via",
            "copied_toast" to "✓ Teks berhasil disalin ke clipboard!",
            
            // Feedback Toasts
            "toast_list_saved" to "✓ List berhasil disimpan",
            "toast_item_added" to "✓ Item berhasil ditambahkan",
            "toast_item_updated" to "✓ Item berhasil diperbarui",
            
            // Templates
            "templates_title" to "Template List",
            "templates_desc" to "Gunakan atau buat template untuk daftar yang sering digunakan.",
            "use_template" to "Gunakan Template",
            "create_template" to "+ Buat Template Baru",
            "template_badge" to "Template",
            
            // Backup & Restore
            "backup_restore_title" to "Backup & Restore",
            "backup_section" to "Cadangkan Data",
            "backup_desc" to "Simpan seluruh daftar, item, dan pengaturan ke file backup JSON (dapat disimpan langsung ke Google Drive atau memori perangkat).",
            "btn_backup_now" to "☁️ Backup Sekarang ke Google Drive / File",
            "last_backup_info" to "Backup terakhir: %s",
            "no_backup_yet" to "Belum pernah melakukan backup",
            "restore_section" to "Pulihkan Data (Restore)",
            "restore_desc" to "Pulihkan data dari file backup JSON yang sebelumnya disimpan di Google Drive atau perangkat.",
            "btn_restore_now" to "📥 Restore dari Backup",
            "restore_choice_title" to "Pilih Metode Restore",
            "restore_merge" to "Gabungkan dengan data yang ada",
            "restore_merge_desc" to "Daftar baru akan ditambahkan tanpa menghapus daftar yang saat ini ada.",
            "restore_replace" to "Ganti semua data dengan data backup",
            "restore_replace_desc" to "PERINGATAN: Semua daftar dan item saat ini akan digantikan seluruhnya oleh data cadangan.",
            "backup_success" to "✓ Backup berhasil disimpan!",
            "restore_success" to "✓ Data berhasil dipulihkan!",
            "restore_error" to "Gagal memulihkan file backup. Format tidak valid.",
            
            // Settings
            "settings_section_general" to "Umum",
            "settings_section_data" to "Data",
            "settings_section_about" to "Tentang",
            "settings_language" to "🌐 Bahasa",
            "settings_language_desc" to "Pilih bahasa tampilan aplikasi",
            "settings_theme" to "🎨 Tema Tampilan",
            "settings_theme_desc" to "Pilih tema warna aplikasi",
            "theme_light" to "Terang",
            "theme_dark" to "Gelap",
            "theme_system" to "Ikuti Sistem",
            "settings_backup" to "☁️ Backup & Restore",
            "settings_backup_sub" to "Cadangkan dan pulihkan data Anda",
            "settings_storage" to "📱 Penyimpanan Lokal",
            "settings_storage_sub" to "Data disimpan di perangkat Anda",
            "storage_desc" to "Aplikasi bekerja secara Offline-First. Semua data tersimpan aman di penyimpanan lokal HP Anda tanpa memerlukan koneksi internet.",
            "storage_stats" to "Tersimpan: %d Daftar • %d Item",
            "settings_about" to "ℹ️ Tentang ListKita",
            "about_desc" to "Aplikasi untuk membuat, mengatur, dan membagikan berbagai jenis daftar dengan mudah.",
            "about_author" to "by Rafline Dev",
            "app_version" to "Versi 1.0.0",
            
            // Empty States
            "empty_lists_title" to "Belum ada daftar",
            "empty_lists_desc" to "Buat daftar pertamamu dan atur semuanya dengan lebih mudah.",
            "empty_search_title" to "Tidak ditemukan",
            "empty_search_desc" to "Tidak ada daftar yang cocok dengan kata kunci \"%s\"."
        ),
        
        AppLanguage.ENGLISH to mapOf(
            "app_name" to "ListKita",
            "app_tagline" to "Application to create, organize, and share various lists easily.",
            "splash_tagline" to "Create. Organize. Share.",
            "splash_author" to "by Rafline Dev",
            
            // Navigation
            "nav_my_lists" to "My Lists",
            "nav_create" to "Create",
            "nav_settings" to "Settings",
            
            // Home / Beranda
            "welcome_title" to "Welcome 👋",
            "my_lists_title" to "My Lists",
            "search_hint_modern" to "Search lists...",
            "search_placeholder" to "Search list title, category, or items...",
            "all_categories" to "All",
            "sort_by" to "Sort by",
            "sort_updated" to "Recently Updated",
            "sort_newest" to "Newest Created",
            "sort_oldest" to "Oldest",
            "sort_az" to "Name A–Z",
            
            // Categories
            "cat_product" to "Product",
            "cat_food" to "Food",
            "cat_shopping" to "Shopping",
            "cat_stock" to "Stock",
            "cat_event" to "Event Prep",
            "cat_general" to "Other",
            "cat_other" to "Other",
            
            // List Card & Actions
            "items_count" to "%d Items",
            "updated_today" to "Last updated today",
            "updated_yesterday" to "Last updated yesterday",
            "updated_on" to "Updated: %s",
            "btn_create_new_list" to "+ Create New List",
            "btn_create_new_fab" to "＋ Create New",
            "btn_open" to "Open",
            "btn_edit" to "Edit",
            "btn_duplicate" to "Duplicate",
            "btn_delete" to "Delete",
            "btn_share" to "Share",
            "btn_copy" to "Copy Text",
            "btn_save" to "Save",
            "btn_cancel" to "Cancel",
            "btn_confirm" to "Confirm",
            "btn_add_item" to "+ Add Item",
            "btn_quick_add" to "Add",
            
            // Confirmations
            "delete_list_title" to "Delete List?",
            "delete_list_msg" to "List and all items inside will be deleted.",
            "delete_item_title" to "Delete Item?",
            "delete_item_msg" to "Delete item \"%s\" from this list?",
            "duplicate_suffix" to "(Copy)",
            "duplicated_toast" to "✓ List duplicated successfully!",
            
            // Create List Screen
            "create_title" to "Create New List",
            "list_name_label" to "List Name",
            "list_name_hint" to "e.g.: Weekly Grocery List",
            "select_category_label" to "Select Category",
            "category_desc" to "Categories help group your lists.",
            "optional_columns_title" to "Additional Columns (Optional)",
            "optional_columns_desc" to "Standard format: Name, Quantity, Note. Add more columns if needed:",
            "col_price" to "Price",
            "col_location" to "Location/Shelf",
            "col_custom" to "Other Custom Column",
            "add_custom_col_hint" to "Column name (e.g. Color, Vendor)",
            "use_template_shortcut" to "Or start from ready-to-use Templates:",
            "btn_create_list" to "Create List",
            "btn_create_and_add_items" to "Create List",
            
            // Custom Columns (Atur Kolom)
            "manage_columns" to "⚙️ Manage Columns",
            "manage_columns_title" to "Manage Extra Columns",
            "manage_columns_desc" to "Add or remove specific columns for this list (e.g. Price, Location/Shelf).",
            "add_column_label" to "New Column Name",
            "add_column_hint" to "e.g.: Price, Unit, Vendor",
            "btn_add_column" to "+ Add Column",
            "no_extra_columns" to "Standard format used: Name, Quantity, Note.",
            "delete_column" to "Delete Column",
            
            // Detail Screen
            "detail_info_subtitle" to "%s • %d Items",
            "created_on" to "Created on: %s",
            "mode_view" to "View Mode",
            "mode_edit" to "Edit Mode",
            "item_name_label" to "Item Name",
            "item_name_hint" to "Product name / food / item",
            "item_name_validation_error" to "Item name cannot be empty.",
            "item_qty_label" to "Quantity",
            "item_qty_hint" to "e.g.: 1, 2, 500g",
            "item_note_label" to "Note",
            "item_note_hint" to "e.g.: Spicy, Size L, No sugar",
            "btn_save_item" to "Save Item",
            "total_items" to "Total Items",
            "completed_items" to "Completed",
            "reorder_items" to "Change Order",
            "move_up" to "Move Up",
            "move_down" to "Move Down",
            "save_as_template" to "Save as Template",
            "saved_as_template_toast" to "✓ Saved as a new template!",
            
            // Share & Preview
            "share_preview_title" to "Share Text Preview",
            "share_preview_desc" to "Here is the text that will be sent to WhatsApp or other apps:",
            "btn_share_whatsapp" to "Share to WhatsApp",
            "btn_share_other_apps" to "Share to Other Apps",
            "btn_copy_text" to "Copy Text",
            "share_header" to "📋 *%s*",
            "share_category" to "🏷️ Category: %s",
            "share_note" to "   Note: %s",
            "share_created" to "📅 Created on: %s",
            "share_watermark" to "Shared via ListKita",
            "share_chooser_title" to "Share List via",
            "copied_toast" to "✓ Text copied to clipboard!",
            
            // Feedback Toasts
            "toast_list_saved" to "✓ List saved successfully",
            "toast_item_added" to "✓ Item added successfully",
            "toast_item_updated" to "✓ Item updated successfully",
            
            // Templates
            "templates_title" to "List Templates",
            "templates_desc" to "Use or create templates for lists you create frequently.",
            "use_template" to "Use Template",
            "create_template" to "+ Create New Template",
            "template_badge" to "Template",
            
            // Backup & Restore
            "backup_restore_title" to "Backup & Restore",
            "backup_section" to "Backup Data",
            "backup_desc" to "Save all lists, items, and settings into a JSON backup file (can be saved directly to Google Drive or device storage).",
            "btn_backup_now" to "☁️ Backup Now to Google Drive / File",
            "last_backup_info" to "Last backup: %s",
            "no_backup_yet" to "No backups performed yet",
            "restore_section" to "Restore Data",
            "restore_desc" to "Restore data from a JSON backup file previously saved on Google Drive or device storage.",
            "btn_restore_now" to "📥 Restore from Backup",
            "restore_choice_title" to "Select Restore Method",
            "restore_merge" to "Merge with existing data",
            "restore_merge_desc" to "New lists will be added without deleting your current lists.",
            "restore_replace" to "Replace all data with backup",
            "restore_replace_desc" to "WARNING: All current lists and items will be replaced completely by the backup.",
            "backup_success" to "✓ Backup saved successfully!",
            "restore_success" to "✓ Data restored successfully!",
            "restore_error" to "Failed to restore backup file. Invalid format.",
            
            // Settings
            "settings_section_general" to "General",
            "settings_section_data" to "Data",
            "settings_section_about" to "About",
            "settings_language" to "🌐 Language",
            "settings_language_desc" to "Select application display language",
            "settings_theme" to "🎨 Display Theme",
            "settings_theme_desc" to "Choose light or dark app appearance",
            "theme_light" to "Light",
            "theme_dark" to "Dark",
            "theme_system" to "System Default",
            "settings_backup" to "☁️ Backup & Restore",
            "settings_backup_sub" to "Backup and restore your data",
            "settings_storage" to "📱 Local Storage",
            "settings_storage_sub" to "Data is stored on your device",
            "storage_desc" to "Offline-first application. All data is securely stored locally on your device without requiring an active internet connection.",
            "storage_stats" to "Stored: %d Lists • %d Items",
            "settings_about" to "ℹ️ About ListKita",
            "about_desc" to "Application to create, organize, and share various lists easily.",
            "about_author" to "by Rafline Dev",
            "app_version" to "Version 1.0.0",
            
            // Empty States
            "empty_lists_title" to "No lists yet",
            "empty_lists_desc" to "Create your first list and organize everything easily.",
            "empty_search_title" to "No lists found",
            "empty_search_desc" to "No lists matched \"%s\"."
        )
    )

    fun get(key: String, language: AppLanguage, vararg args: Any): String {
        val langMap = translations[language] ?: translations[AppLanguage.INDONESIAN]!!
        val template = langMap[key] 
            ?: translations[AppLanguage.INDONESIAN]?.get(key) 
            ?: key
        return if (args.isNotEmpty()) {
            try {
                String.format(template, *args)
            } catch (e: Exception) {
                template
            }
        } else {
            template
        }
    }
}
