package com.daniza.simple.todolist.data.source

import androidx.room.TypeConverter
import com.daniza.simple.todolist.ui.theme.CardColor

class DBColorConverter {
    @TypeConverter
    fun toColor(value: Int) = enumValues<CardColor>()[value]

    @TypeConverter
    fun fromColor(value: CardColor) = value.ordinal
}