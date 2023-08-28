package com.example.aplicacionprueba2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Products::class], version = 1)

abstract class appDataBase : RoomDatabase() {
    abstract fun daoProducts(): DaoProducts

    companion object{
        @Volatile
        private var DATA_BASE: appDataBase? = null

        fun getInstance(context : Context) : appDataBase {

            return DATA_BASE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    appDataBase::class.java,
                    "Products.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { DATA_BASE = it }
            }
        }
    }
}