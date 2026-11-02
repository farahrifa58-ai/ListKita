package com.example

import com.example.data.BackupItemModel
import com.example.data.BackupListModel
import com.example.data.BackupModel
import com.example.data.ListEntity
import com.example.data.ListItemEntity
import com.example.data.ListWithItems
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.util.ShareUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareUtilsAndLocalizationTest {

    @Test
    fun testLocalizationSwitch() {
        val titleId = Strings.get("app_name", AppLanguage.INDONESIAN)
        val titleEn = Strings.get("app_name", AppLanguage.ENGLISH)
        assertEquals("ListKita", titleId)
        assertEquals("ListKita", titleEn)

        val navId = Strings.get("nav_my_lists", AppLanguage.INDONESIAN)
        val navEn = Strings.get("nav_my_lists", AppLanguage.ENGLISH)
        assertEquals("List Saya", navId)
        assertEquals("My Lists", navEn)
    }

    @Test
    fun testShareTextFormatting() {
        val list = ListEntity(
            id = 1,
            title = "Pesanan Makan Siang",
            category = "Makanan",
            createdAt = 1793582400000L
        )
        val items = listOf(
            ListItemEntity(id = 10, listId = 1, name = "Nasi Goreng", quantity = "2", note = "Pedas", orderIndex = 0),
            ListItemEntity(id = 11, listId = 1, name = "Es Teh", quantity = "3", note = "Manis", orderIndex = 1)
        )
        val listWithItems = ListWithItems(list, items)

        val text = ShareUtils.formatListAsText(listWithItems, AppLanguage.INDONESIAN)
        assertTrue(text.contains("📋 *PESANAN MAKAN SIANG*"))
        assertTrue(text.contains("1. Nasi Goreng — 2"))
        assertTrue(text.contains("Catatan: Pedas"))
        assertTrue(text.contains("2. Es Teh — 3"))
        assertTrue(text.contains("Dibagikan via ListKita"))
    }

    @Test
    fun testNominalFormatting() {
        assertEquals("1.000", com.example.util.NumberFormatUtils.formatNominal("1000"))
        assertEquals("10.000", com.example.util.NumberFormatUtils.formatNominal("10000"))
        assertEquals("100.000", com.example.util.NumberFormatUtils.formatNominal("100000"))
        assertEquals("1.000.000", com.example.util.NumberFormatUtils.formatNominal("1000000"))
        assertEquals("500", com.example.util.NumberFormatUtils.formatNominal("500"))
        assertEquals("", com.example.util.NumberFormatUtils.formatNominal(""))
        assertTrue(com.example.util.NumberFormatUtils.isNominalColumn("Harga"))
        assertTrue(com.example.util.NumberFormatUtils.isNominalColumn("Price"))
        assertTrue(com.example.util.NumberFormatUtils.isNominalColumn("Biaya Pengiriman"))
    }
}
