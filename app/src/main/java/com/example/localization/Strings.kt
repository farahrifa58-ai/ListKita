package com.example.localization

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val flag: String,
    val region: String
) {
    // Asia
    INDONESIAN("in", "Bahasa Indonesia", "🇮🇩", "Asia"),
    MALAY("ms", "Bahasa Melayu", "🇲🇾", "Asia"),
    VIETNAMESE("vi", "Tiếng Việt", "🇻🇳", "Asia"),
    THAI("th", "ไทย (Thai)", "🇹🇭", "Asia"),
    CHINESE("zh", "中文 (Mandarin)", "🇨🇳", "Asia"),
    JAPANESE("ja", "日本語 (Jepang)", "🇯🇵", "Asia"),
    KOREAN("ko", "한국어 (Korea)", "🇰🇷", "Asia"),
    ARABIC("ar", "العربية (Arab)", "🇸🇦", "Asia"),
    HINDI("hi", "हिन्दी (Hindi)", "🇮🇳", "Asia"),

    // Eropa
    ENGLISH("en", "English", "🇬🇧", "Eropa"),
    FRENCH("fr", "Français (Prancis)", "🇫🇷", "Eropa"),
    GERMAN("de", "Deutsch (Jerman)", "🇩🇪", "Eropa"),
    SPANISH("es", "Español (Spanyol)", "🇪🇸", "Eropa"),
    ITALIAN("it", "Italiano (Italia)", "🇮🇹", "Eropa"),
    DUTCH("nl", "Nederlands (Belanda)", "🇳🇱", "Eropa"),

    // Rusia
    RUSSIAN("ru", "Русский (Rusia)", "🇷🇺", "Rusia");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) }
                ?: if (code.startsWith("en", ignoreCase = true)) ENGLISH else INDONESIAN
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
            "btn_create_new_list" to "Buat List Baru",
            "btn_create_new_fab" to "Buat Baru",
            "btn_open" to "Buka",
            "btn_edit" to "Edit",
            "btn_duplicate" to "Duplikat",
            "btn_delete" to "Hapus",
            "btn_share" to "Bagikan",
            "btn_copy" to "Salin Teks",
            "btn_save" to "Simpan",
            "btn_cancel" to "Batal",
            "btn_confirm" to "Konfirmasi",
            "btn_add_item" to "Tambah Item",
            "btn_quick_add" to "Tambah",
            "btn_close" to "Tutup",
            
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
            "manage_columns" to "Atur Kolom",
            "manage_columns_title" to "Atur Kolom Tambahan",
            "manage_columns_desc" to "Tambahkan atau hapus kolom khusus untuk daftar ini (misal: Harga, Lokasi/Rak).",
            "add_column_label" to "Nama Kolom Baru",
            "add_column_hint" to "cth: Harga, Satuan, Vendor",
            "btn_add_column" to "Tambah Kolom",
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
            "search_items_hint" to "Cari item dalam daftar...",
            "empty_items_title" to "Belum ada item",
            "empty_items_desc" to "Tekan tombol Tambah Item untuk mulai mengisi daftar ini.",
            "save_as_template" to "Simpan sebagai Template",
            "saved_as_template_toast" to "✓ Disimpan sebagai template!",
            "toast_item_updated" to "Item berhasil diperbarui",
            "toast_item_added" to "Item berhasil ditambahkan",
            "move_up" to "Pindah ke atas",
            "move_down" to "Pindah ke bawah",
            
            // Share Sheet & Formatting
            "share_dialog_title" to "Bagikan Daftar",
            "share_whatsapp" to "Kirim ke WhatsApp",
            "share_other_apps" to "Aplikasi Lainnya",
            "share_copy_clipboard" to "Salin Teks ke Clipboard",
            "copied_toast" to "✓ Teks daftar disalin ke clipboard!",
            "share_category" to "Kategori: %s",
            "share_note" to "   Catatan: %s",
            "share_created" to "📅 Dibuat: %s",
            "share_watermark" to "Dibagikan via ListKita",
            "share_chooser_title" to "Bagikan daftar via",
            
            // Templates
            "templates_title" to "Template List",
            "templates_desc" to "Gunakan format daftar siap pakai untuk mempercepat pembuatan list.",
            "templates_header" to "Pilih Template",
            "template_use_btn" to "Gunakan Template",
            "template_save_as_new" to "Simpan sebagai Template",
            "use_template" to "Gunakan Template Ini",
            
            // Backup & Restore
            "backup_restore_title" to "Cadangkan & Pulihkan",
            "backup_section" to "Cadangkan Data (Backup)",
            "backup_desc" to "Ekspor semua daftar dan item Anda ke file JSON aman yang dapat disimpan di Google Drive atau memori internal.",
            "last_backup_info" to "Backup terakhir: %s",
            "no_backup_yet" to "Belum pernah melakukan backup",
            "btn_backup_now" to "Cadangkan Sekarang",
            "backup_success" to "✓ Backup berhasil disimpan!",
            "restore_section" to "Pulihkan Data (Restore)",
            "restore_desc" to "Impor file backup JSON ListKita untuk mengembalikan daftar yang telah dicadangkan sebelumnya.",
            "btn_restore_now" to "Pilih File & Pulihkan",
            "restore_choice_title" to "Metode Pemulihan",
            "restore_merge" to "Gabungkan (Merge)",
            "restore_merge_desc" to "Tambahkan daftar dari file backup tanpa menghapus daftar yang ada sekarang.",
            "restore_replace" to "Gantikan Semua (Replace)",
            "restore_replace_desc" to "Hapus semua daftar yang ada saat ini dan gantikan dengan isi file backup.",
            "restore_success" to "✓ Data berhasil dipulihkan!",
            "restore_error" to "Gagal memulihkan file backup. Format tidak valid.",
            
            // Settings Screen
            "settings_title" to "Pengaturan",
            "settings_section_general" to "Preferensi",
            "settings_section_data" to "Penyimpanan & Data",
            "settings_section_about" to "Tentang",
            "settings_language" to "Bahasa Aplikasi",
            "settings_language_desc" to "Pilih bahasa tampilan aplikasi",
            "settings_currency" to "Mata Uang",
            "settings_currency_desc" to "Atur mata uang untuk kolom harga",
            "settings_theme" to "Tema Tampilan",
            "settings_theme_desc" to "Pilih tema warna aplikasi",
            "theme_light" to "Terang",
            "theme_dark" to "Gelap",
            "theme_system" to "Ikuti Sistem",
            "settings_backup" to "Cadangkan & Pulihkan",
            "settings_backup_sub" to "Backup dan restore database Anda",
            "settings_storage" to "Penyimpanan Lokal",
            "settings_storage_sub" to "Data disimpan di perangkat Anda",
            "storage_desc" to "Aplikasi bekerja secara Offline-First. Semua data tersimpan aman di penyimpanan lokal HP Anda tanpa memerlukan koneksi internet.",
            "storage_stats" to "Tersimpan: %d Daftar • %d Item",
            "settings_about" to "Tentang ListKita",
            "about_app_title" to "Tentang Aplikasi",
            "about_app_subtitle" to "Versi 1.0.0 • Informasi aplikasi & pengembang",
            "about_desc" to "Aplikasi untuk membuat, mengatur, dan membagikan berbagai jenis daftar dengan mudah.",
            "about_author" to "by Rafline Dev",
            "app_version" to "Versi 1.0.0",
            "about_app_info" to "ListKita adalah aplikasi sederhana, fleksibel, dan cepat untuk membuat, mengatur, menyimpan, dan membagikan berbagai jenis daftar keperluan Anda secara offline.",
            "about_storage_tag" to "Penyimpanan 100% Offline & Privat",
            "select_language" to "Pilih Bahasa",
            "select_currency" to "Pilih Mata Uang",
            "filter_all" to "Semua",
            "search_currency_hint" to "Cari mata uang, negara, simbol...",
            
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
            "nav_create" to "Create New",
            "nav_settings" to "Settings",
            
            // Home / Beranda
            "welcome_title" to "Welcome 👋",
            "my_lists_title" to "My Lists",
            "search_hint_modern" to "Search lists...",
            "search_placeholder" to "Search list name, category, or item...",
            "all_categories" to "All",
            "sort_by" to "Sort by",
            "sort_updated" to "Last Updated",
            "sort_newest" to "Newest Created",
            "sort_oldest" to "Oldest",
            "sort_az" to "Name A–Z",
            
            // Categories
            "cat_product" to "Product",
            "cat_food" to "Food",
            "cat_shopping" to "Shopping",
            "cat_stock" to "Stock",
            "cat_event" to "Event Needs",
            "cat_general" to "Other",
            "cat_other" to "Other",
            
            // List Card & Actions
            "items_count" to "%d Items",
            "updated_today" to "Last updated today",
            "updated_yesterday" to "Last updated yesterday",
            "updated_on" to "Updated: %s",
            "btn_create_new_list" to "Create New List",
            "btn_create_new_fab" to "Create New",
            "btn_open" to "Open",
            "btn_edit" to "Edit",
            "btn_duplicate" to "Duplicate",
            "btn_delete" to "Delete",
            "btn_share" to "Share",
            "btn_copy" to "Copy Text",
            "btn_save" to "Save",
            "btn_cancel" to "Cancel",
            "btn_confirm" to "Confirm",
            "btn_add_item" to "Add Item",
            "btn_quick_add" to "Add",
            "btn_close" to "Close",
            
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
            "manage_columns" to "Manage Columns",
            "manage_columns_title" to "Manage Extra Columns",
            "manage_columns_desc" to "Add or remove specific columns for this list (e.g. Price, Location/Shelf).",
            "add_column_label" to "New Column Name",
            "add_column_hint" to "e.g.: Price, Unit, Vendor",
            "btn_add_column" to "Add Column",
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
            "search_items_hint" to "Search items in list...",
            "empty_items_title" to "No items yet",
            "empty_items_desc" to "Press Add Item button to start filling this list.",
            "save_as_template" to "Save as Template",
            "saved_as_template_toast" to "✓ Saved as template!",
            "toast_item_updated" to "Item updated successfully",
            "toast_item_added" to "Item added successfully",
            "move_up" to "Move up",
            "move_down" to "Move down",
            
            // Share Sheet & Formatting
            "share_dialog_title" to "Share List",
            "share_whatsapp" to "Send via WhatsApp",
            "share_other_apps" to "Other Apps",
            "share_copy_clipboard" to "Copy Text to Clipboard",
            "copied_toast" to "✓ List copied to clipboard!",
            "share_category" to "Category: %s",
            "share_note" to "   Catatan: %s",
            "share_created" to "📅 Created: %s",
            "share_watermark" to "Dibagikan via ListKita",
            "share_chooser_title" to "Share list via",
            
            // Templates
            "templates_title" to "List Templates",
            "templates_desc" to "Use ready-made formats to create lists faster.",
            "templates_header" to "Select Template",
            "template_use_btn" to "Use Template",
            "template_save_as_new" to "Save as Template",
            "use_template" to "Use This Template",
            
            // Backup & Restore
            "backup_restore_title" to "Backup & Restore",
            "backup_section" to "Backup Data",
            "backup_desc" to "Export all your lists and items into a safe JSON file that can be stored on Google Drive or internal storage.",
            "last_backup_info" to "Last backup: %s",
            "no_backup_yet" to "No backup yet",
            "btn_backup_now" to "Backup Now",
            "backup_success" to "✓ Backup saved successfully!",
            "restore_section" to "Restore Data",
            "restore_desc" to "Import a ListKita JSON backup file to restore previously backed up lists.",
            "btn_restore_now" to "Choose File & Restore",
            "restore_choice_title" to "Restore Method",
            "restore_merge" to "Merge",
            "restore_merge_desc" to "Add lists from backup without deleting existing lists.",
            "restore_replace" to "Replace All",
            "restore_replace_desc" to "Delete all current lists and replace with the backup file contents.",
            "restore_success" to "✓ Data restored successfully!",
            "restore_error" to "Failed to restore backup file. Invalid format.",
            
            // Settings Screen
            "settings_title" to "Settings",
            "settings_section_general" to "Preferences",
            "settings_section_data" to "Storage & Data",
            "settings_section_about" to "About",
            "settings_language" to "App Language",
            "settings_language_desc" to "Select app display language",
            "settings_currency" to "Currency",
            "settings_currency_desc" to "Set currency for price column",
            "settings_theme" to "App Theme",
            "settings_theme_desc" to "Select app color theme",
            "theme_light" to "Light",
            "theme_dark" to "Dark",
            "theme_system" to "Follow System",
            "settings_backup" to "Backup & Restore",
            "settings_backup_sub" to "Backup and restore your database",
            "settings_storage" to "Local Storage",
            "settings_storage_sub" to "Data is stored on your device",
            "storage_desc" to "Offline-first application. All data is securely stored locally on your device without requiring an active internet connection.",
            "storage_stats" to "Stored: %d Lists • %d Items",
            "settings_about" to "About ListKita",
            "about_app_title" to "About Application",
            "about_app_subtitle" to "Version 1.0.0 • Application & developer information",
            "about_desc" to "Application to create, organize, and share various lists easily.",
            "about_author" to "by Rafline Dev",
            "app_version" to "Version 1.0.0",
            "about_app_info" to "ListKita is a simple, flexible, and fast application to create, organize, store, and share your lists offline.",
            "about_storage_tag" to "100% Offline & Private Storage",
            "select_language" to "Select Language",
            "select_currency" to "Select Currency",
            "filter_all" to "All",
            "search_currency_hint" to "Search currency, country, symbol...",
            
            // Empty States
            "empty_lists_title" to "No lists yet",
            "empty_lists_desc" to "Create your first list and organize everything easily.",
            "empty_search_title" to "No lists found",
            "empty_search_desc" to "No lists matched \"%s\"."
        ),

        // Russian (Rusia)
        AppLanguage.RUSSIAN to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "Мои списки",
            "nav_create" to "Создать",
            "nav_settings" to "Настройки",
            "welcome_title" to "Добро пожаловать 👋",
            "my_lists_title" to "Мои списки",
            "search_hint_modern" to "Поиск списков...",
            "all_categories" to "Все",
            "btn_create_new_list" to "Создать новый список",
            "btn_create_new_fab" to "Создать",
            "btn_add_item" to "Добавить элемент",
            "btn_save" to "Сохранить",
            "btn_cancel" to "Отмена",
            "btn_delete" to "Удалить",
            "btn_close" to "Закрыть",
            "manage_columns" to "Настроить колонки",
            "settings_title" to "Настройки",
            "settings_language" to "Язык приложения",
            "settings_currency" to "Валюта",
            "settings_theme" to "Тема оформления",
            "about_app_title" to "О приложении",
            "about_app_subtitle" to "Версия 1.0.0 • Информация о разработчике",
            "about_app_info" to "ListKita — это простое и гибкое приложение для создания, организации и хранения списков офлайн."
        ),

        // French (Eropa)
        AppLanguage.FRENCH to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "Mes listes",
            "nav_create" to "Créer",
            "nav_settings" to "Paramètres",
            "welcome_title" to "Bienvenue 👋",
            "my_lists_title" to "Mes listes",
            "search_hint_modern" to "Rechercher...",
            "all_categories" to "Tout",
            "btn_create_new_list" to "Créer une liste",
            "btn_create_new_fab" to "Créer",
            "btn_add_item" to "Ajouter un élément",
            "btn_save" to "Enregistrer",
            "btn_cancel" to "Annuler",
            "btn_delete" to "Supprimer",
            "btn_close" to "Fermer",
            "manage_columns" to "Gérer les colonnes",
            "settings_title" to "Paramètres",
            "settings_language" to "Langue",
            "settings_currency" to "Devise",
            "settings_theme" to "Thème",
            "about_app_title" to "À propos",
            "about_app_subtitle" to "Version 1.0.0 • Informations sur l'application",
            "about_app_info" to "ListKita est une application simple et flexible pour créer et gérer des listes hors ligne."
        ),

        // German (Eropa)
        AppLanguage.GERMAN to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "Meine Listen",
            "nav_create" to "Erstellen",
            "nav_settings" to "Einstellungen",
            "welcome_title" to "Willkommen 👋",
            "my_lists_title" to "Meine Listen",
            "search_hint_modern" to "Listen suchen...",
            "all_categories" to "Alle",
            "btn_create_new_list" to "Neue Liste erstellen",
            "btn_create_new_fab" to "Neu erstellen",
            "btn_add_item" to "Element hinzufügen",
            "btn_save" to "Speichern",
            "btn_cancel" to "Abbrechen",
            "btn_delete" to "Löschen",
            "btn_close" to "Schließen",
            "manage_columns" to "Spalten verwalten",
            "settings_title" to "Einstellungen",
            "settings_language" to "App-Sprache",
            "settings_currency" to "Währung",
            "settings_theme" to "Design",
            "about_app_title" to "Über die App",
            "about_app_subtitle" to "Version 1.0.0 • Entwickler-Info",
            "about_app_info" to "ListKita ist eine übersichtliche und flexible App zum Verwalten und Teilen Ihrer Listen."
        ),

        // Spanish (Eropa)
        AppLanguage.SPANISH to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "Mis Listas",
            "nav_create" to "Crear",
            "nav_settings" to "Ajustes",
            "welcome_title" to "Bienvenido 👋",
            "my_lists_title" to "Mis Listas",
            "search_hint_modern" to "Buscar listas...",
            "all_categories" to "Todas",
            "btn_create_new_list" to "Crear nueva lista",
            "btn_create_new_fab" to "Crear",
            "btn_add_item" to "Añadir elemento",
            "btn_save" to "Guardar",
            "btn_cancel" to "Cancelar",
            "btn_delete" to "Eliminar",
            "btn_close" to "Cerrar",
            "manage_columns" to "Organizar columnas",
            "settings_title" to "Ajustes",
            "settings_language" to "Idioma",
            "settings_currency" to "Moneda",
            "settings_theme" to "Tema",
            "about_app_title" to "Acerca de",
            "about_app_subtitle" to "Versión 1.0.0 • Información de la aplicación",
            "about_app_info" to "ListKita es una aplicación práctica para organizar y gestionar tus listas sin conexión."
        ),

        // Japanese (Asia)
        AppLanguage.JAPANESE to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "マイリスト",
            "nav_create" to "新規作成",
            "nav_settings" to "設定",
            "welcome_title" to "ようこそ 👋",
            "my_lists_title" to "マイリスト",
            "search_hint_modern" to "リストを検索...",
            "all_categories" to "すべて",
            "btn_create_new_list" to "リストを作成",
            "btn_create_new_fab" to "新規作成",
            "btn_add_item" to "アイテムを追加",
            "btn_save" to "保存",
            "btn_cancel" to "キャンセル",
            "btn_delete" to "削除",
            "btn_close" to "閉じる",
            "manage_columns" to "列を管理",
            "settings_title" to "設定",
            "settings_language" to "言語",
            "settings_currency" to "通貨",
            "settings_theme" to "テーマ",
            "about_app_title" to "アプリについて",
            "about_app_subtitle" to "バージョン 1.0.0",
            "about_app_info" to "ListKitaは、オフラインで様々なリストを便利に作成・管理できるアプリです。"
        ),

        // Korean (Asia)
        AppLanguage.KOREAN to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "내 목록",
            "nav_create" to "새로 만들기",
            "nav_settings" to "설정",
            "welcome_title" to "환영합니다 👋",
            "my_lists_title" to "내 목록",
            "search_hint_modern" to "목록 검색...",
            "all_categories" to "전체",
            "btn_create_new_list" to "새 목록 만들기",
            "btn_create_new_fab" to "새로 만들기",
            "btn_add_item" to "항목 추가",
            "btn_save" to "저장",
            "btn_cancel" to "취소",
            "btn_delete" to "삭제",
            "btn_close" to "닫기",
            "manage_columns" to "열 설정",
            "settings_title" to "설정",
            "settings_language" to "언어",
            "settings_currency" to "통화",
            "settings_theme" to "테마",
            "about_app_title" to "앱 정보",
            "about_app_subtitle" to "버전 1.0.0",
            "about_app_info" to "ListKita는 다양한 목록을 오프라인으로 간편하게 관리하는 앱입니다."
        ),

        // Chinese (Asia)
        AppLanguage.CHINESE to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "我的清单",
            "nav_create" to "新建",
            "nav_settings" to "设置",
            "welcome_title" to "欢迎 👋",
            "my_lists_title" to "我的清单",
            "search_hint_modern" to "搜索清单...",
            "all_categories" to "全部",
            "btn_create_new_list" to "新建清单",
            "btn_create_new_fab" to "新建",
            "btn_add_item" to "添加项目",
            "btn_save" to "保存",
            "btn_cancel" to "取消",
            "btn_delete" to "删除",
            "btn_close" to "关闭",
            "manage_columns" to "管理列",
            "settings_title" to "设置",
            "settings_language" to "应用语言",
            "settings_currency" to "货币",
            "settings_theme" to "外观主题",
            "about_app_title" to "关于应用",
            "about_app_subtitle" to "版本 1.0.0",
            "about_app_info" to "ListKita 是一款简洁高效的离线清单制作与管理工具。"
        ),

        // Arabic (Asia)
        AppLanguage.ARABIC to mapOf(
            "app_name" to "ListKita",
            "nav_my_lists" to "قوائمي",
            "nav_create" to "إنشاء جديد",
            "nav_settings" to "الإعدادات",
            "welcome_title" to "مرحباً 👋",
            "my_lists_title" to "قوائمي",
            "search_hint_modern" to "بحث في القوائم...",
            "all_categories" to "الكل",
            "btn_create_new_list" to "إنشاء قائمة جديدة",
            "btn_create_new_fab" to "إنشاء",
            "btn_add_item" to "إضافة عنصر",
            "btn_save" to "حفظ",
            "btn_cancel" to "إلغاء",
            "btn_delete" to "حذف",
            "btn_close" to "إغلاق",
            "manage_columns" to "إدارة الأعمدة",
            "settings_title" to "الإعدادات",
            "settings_language" to "اللغة",
            "settings_currency" to "العملة",
            "settings_theme" to "المظهر",
            "about_app_title" to "حول التطبيق",
            "about_app_subtitle" to "الإصدار 1.0.0",
            "about_app_info" to "ListKita هو تطبيق سهل وبسيط لتنظيم وإدارة القوائم بدون إنترنت."
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
