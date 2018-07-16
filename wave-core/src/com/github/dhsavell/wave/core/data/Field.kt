package com.github.dhsavell.wave.core.data

import com.sxtanna.database.Kedis

class Field<T : Storable<T>> {
    fun updateValue(newValue: T, db: Kedis) {
        newValue.saveToDatabase(db)
    }


}