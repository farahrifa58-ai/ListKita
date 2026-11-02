package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.data.ListWithItems
import com.example.localization.AppLanguage
import com.example.localization.Strings
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ShareUtils {

    fun formatListAsText(listWithItems: ListWithItems, language: AppLanguage): String {
        val list = listWithItems.list
        val items = listWithItems.sortedItems
        val sb = StringBuilder()

        // Title
        sb.append("📋 *").append(list.title.trim().uppercase(Locale.getDefault())).append("*\n")
        
        // Category
        val catKey = when (list.category) {
            "Produk" -> "cat_product"
            "Makanan" -> "cat_food"
            "Belanja" -> "cat_shopping"
            "Stok" -> "cat_stock"
            "Kebutuhan Acara" -> "cat_event"
            else -> "cat_general"
        }
        val localizedCat = Strings.get(catKey, language)
        sb.append("🏷️ ").append(Strings.get("share_category", language, localizedCat)).append("\n\n")

        if (items.isEmpty()) {
            sb.append("(Daftar kosong / Empty list)\n\n")
        } else {
            items.forEachIndexed { index, item ->
                val num = index + 1
                sb.append("$num. ").append(item.name.trim())
                if (item.quantity.isNotBlank()) {
                    sb.append(" — ").append(item.quantity.trim())
                }
                sb.append("\n")

                if (item.note.isNotBlank()) {
                    sb.append(Strings.get("share_note", language, item.note.trim())).append("\n")
                }

                // Check custom columns
                if (item.customValuesJson.isNotBlank() && item.customValuesJson != "{}") {
                    try {
                        val json = JSONObject(item.customValuesJson)
                        val keys = json.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            val value = json.optString(key)
                            if (value.isNotBlank()) {
                                sb.append("   • $key: $value\n")
                            }
                        }
                    } catch (e: Exception) {
                        // ignore JSON parse
                    }
                }
                sb.append("\n")
            }
        }

        // Summary & Date
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", if (language == AppLanguage.INDONESIAN) Locale("id", "ID") else Locale.ENGLISH)
        val dateStr = dateFormat.format(Date(list.createdAt))

        sb.append("📊 ").append(Strings.get("total_items", language)).append(": ").append(items.size).append("\n")
        sb.append(Strings.get("share_created", language, dateStr)).append("\n")
        sb.append("✨ ").append(Strings.get("share_watermark", language))

        return sb.toString()
    }

    fun shareViaSystem(context: Context, listWithItems: ListWithItems, language: AppLanguage) {
        val text = formatListAsText(listWithItems, language)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, listWithItems.list.title)
            type = "text/plain"
        }
        val chooserTitle = Strings.get("share_chooser_title", language)
        val shareIntent = Intent.createChooser(sendIntent, chooserTitle)
        context.startActivity(shareIntent)
    }

    fun copyToClipboard(context: Context, listWithItems: ListWithItems, language: AppLanguage) {
        val text = formatListAsText(listWithItems, language)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(listWithItems.list.title, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, Strings.get("copied_toast", language), Toast.LENGTH_SHORT).show()
    }
}
