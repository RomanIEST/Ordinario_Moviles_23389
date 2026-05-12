package com.example.examen_ordinario.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "chuck_prefs")

object JokeDataStore {
    private val JOKE_KEY = stringPreferencesKey("frase_guardada")

    fun obtenerFrase(context: Context): Flow<String> =
        context.dataStore.data.map { prefs -> prefs[JOKE_KEY] ?: "" }

    suspend fun guardarFrase(context: Context, frase: String) {
        context.dataStore.edit { prefs -> prefs[JOKE_KEY] = frase }
    }
}
