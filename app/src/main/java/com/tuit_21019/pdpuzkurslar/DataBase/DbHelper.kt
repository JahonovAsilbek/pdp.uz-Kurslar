package com.tuit_21019.pdpuzkurslar.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tuit_21019.pdpuzkurslar.models.Kurs

class DbHelper(context: Context) : SQLiteOpenHelper(context, "pdpuz_kurslar", null, 1),DbMethods {
    override fun onCreate(db: SQLiteDatabase?) {
        val create_barcha_kurslar="CREATE TABLE `barcha_kurslar` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`kurs_nomi` TEXT NOT NULL)"
        val create_mentorlar = "CREATE TABLE `mentorlar` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`mentor_nomi` TEXT NOT NULL,`mentor_familyasi` TEXT NOT NULL,`mentor_otasining_ismi` INTEGER NOT NULL, `kurs_id` INTEGER NOT NULL,FOREIGN KEY(`kurs_id`) REFERENCES `barcha_kurslar`(`id`))"
        val create_guruhlar = "CREATE TABLE `guruhlar` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`guruh_nomi` TEXT NOT NULL,`mentor_id` INTEGER NOT NULL,`ochilganligi` TEXT NOT NULL,`kurs_id` INTEGER NOT NULL,`dars_vaqti` TEXT NOT NULL,FOREIGN KEY(`kurs_id`) REFERENCES `barcha_kurslar`(`id`),FOREIGN KEY(`mentor_id`) REFERENCES `mentorlar`(`id`))"
        val create_talabalar = "CREATE TABLE `talabalar` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`talaba_ismi` TEXT NOT NULL,`talaba_familyasi` TEXT NOT NULL,`talaba_otasining_ismi` TEXT NOT NULL,`dars_boshlash_vaqti` TEXT NOT NULL,`mentor_id` INTEGER NOT NULL,`kunlar` INTEGER NOT NULL,`dars_vaqti` INTEGER NOT NULL,`guruh_id` INTEGER NOT NULL,FOREIGN KEY(`guruh_id`) REFERENCES `guruhlar`(`id`),FOREIGN KEY(`mentor_id`) REFERENCES `mentorlar`(`id`))"


        db?.execSQL(create_barcha_kurslar)
        db?.execSQL(create_mentorlar)
        db?.execSQL(create_guruhlar)
        db?.execSQL(create_talabalar)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table talabalar")
        db?.execSQL("drop table guruhlar")
        db?.execSQL("drop table mentorlar")
        db?.execSQL("drop table barcha_kurslar")
    }

    override fun insertKurs(kurs: Kurs) {
        val contentValues = ContentValues()
        val db = this.writableDatabase
        contentValues.put("kurs_nomi", kurs.kurs_nomi)
        db.insert("barcha_kurslar", null, contentValues)
        db.close()
    }

    override fun getAllKurs(): ArrayList<Kurs> {
        val db = this.readableDatabase
        val kursList = ArrayList<Kurs>()
        val cursor = db.rawQuery("select * from barcha_kurslar", null)
        if (cursor.moveToFirst()) {
            do {
                kursList.add(Kurs(cursor.getInt(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return kursList
    }

}