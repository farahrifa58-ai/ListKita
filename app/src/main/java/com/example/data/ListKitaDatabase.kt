package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ListEntity::class, ListItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ListKitaDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao

    companion object {
        @Volatile
        private var INSTANCE: ListKitaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ListKitaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListKitaDatabase::class.java,
                    "listkita_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.listDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: ListDao) {
                val now = System.currentTimeMillis()

                // Initial Welcome Active Lists
                val welcomeListId = dao.insertList(
                    ListEntity(
                        title = "Stok Produk Toko",
                        category = "Stok",
                        customColumnsJson = "[\"Harga\"]",
                        isTemplate = false,
                        createdAt = now - 3600000,
                        updatedAt = now - 3600000
                    )
                )
                dao.insertItems(
                    listOf(
                        ListItemEntity(listId = welcomeListId, name = "Kaos Polos Cotton Combed L", quantity = "24 pcs", note = "Warna Hitam & Putih", customValuesJson = "{\"Harga\":\"Rp 45.000\"}", orderIndex = 0),
                        ListItemEntity(listId = welcomeListId, name = "Kemeja Flanel Casual M", quantity = "12 pcs", note = "Motif Kotak Navy", customValuesJson = "{\"Harga\":\"Rp 85.000\"}", orderIndex = 1),
                        ListItemEntity(listId = welcomeListId, name = "Celana Chino Slim Fit 32", quantity = "10 pcs", note = "Warna Khaki & Abu", customValuesJson = "{\"Harga\":\"Rp 120.000\"}", orderIndex = 2)
                    )
                )

                val foodListId = dao.insertList(
                    ListEntity(
                        title = "Pesanan Makanan Siang",
                        category = "Makanan",
                        customColumnsJson = "[]",
                        isTemplate = false,
                        createdAt = now - 7200000,
                        updatedAt = now - 7200000
                    )
                )
                dao.insertItems(
                    listOf(
                        ListItemEntity(listId = foodListId, name = "Nasi Goreng Spesial", quantity = "2 porsi", note = "Pedas level 2", orderIndex = 0),
                        ListItemEntity(listId = foodListId, name = "Es Teh Manis", quantity = "3 gelas", note = "Gula sedikit", orderIndex = 1),
                        ListItemEntity(listId = foodListId, name = "Mie Ayam Jamur", quantity = "1 porsi", note = "Tanpa sambal, pangsit basah", orderIndex = 2)
                    )
                )

                // Pre-made Templates
                val tmplBelanjaId = dao.insertList(
                    ListEntity(
                        title = "Daftar Belanja Mingguan",
                        category = "Belanja",
                        customColumnsJson = "[]",
                        isTemplate = true,
                        createdAt = now,
                        updatedAt = now
                    )
                )
                dao.insertItems(
                    listOf(
                        ListItemEntity(listId = tmplBelanjaId, name = "Beras Premium", quantity = "5 kg", note = "Pulen", orderIndex = 0),
                        ListItemEntity(listId = tmplBelanjaId, name = "Minyak Goreng", quantity = "2 liter", note = "Kemasan pouch", orderIndex = 1),
                        ListItemEntity(listId = tmplBelanjaId, name = "Telur Ayam", quantity = "1 kg", note = "Segar", orderIndex = 2),
                        ListItemEntity(listId = tmplBelanjaId, name = "Sayur Bayam & Kangkung", quantity = "3 ikat", note = "Segar", orderIndex = 3),
                        ListItemEntity(listId = tmplBelanjaId, name = "Bawang Merah & Putih", quantity = "250 gram", note = "Kualitas bagus", orderIndex = 4)
                    )
                )

                val tmplAcaraId = dao.insertList(
                    ListEntity(
                        title = "Kebutuhan Acara & Event",
                        category = "Kebutuhan Acara",
                        customColumnsJson = "[]",
                        isTemplate = true,
                        createdAt = now,
                        updatedAt = now
                    )
                )
                dao.insertItems(
                    listOf(
                        ListItemEntity(listId = tmplAcaraId, name = "Sewa Sound System & Mic", quantity = "1 paket", note = "2 Mic wireless", orderIndex = 0),
                        ListItemEntity(listId = tmplAcaraId, name = "Konsumsi Panitia & Tamu", quantity = "50 box", note = "Ayam bakar + buah", orderIndex = 1),
                        ListItemEntity(listId = tmplAcaraId, name = "Banner Panggung 3x2m", quantity = "1 pcs", note = "Desain final", orderIndex = 2),
                        ListItemEntity(listId = tmplAcaraId, name = "Name Tag Panitia", quantity = "15 pcs", note = "Lengkap lanyard", orderIndex = 3)
                    )
                )
            }
        }
    }
}
