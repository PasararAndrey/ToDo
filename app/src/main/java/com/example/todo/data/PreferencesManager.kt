package com.example.todo.data

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val DATASTORE_NAME = "tasks_filter_settings"
private const val TAG = "PreferencesManager"


enum class SortOrder {
    BY_NAME, BY_CREATION_DATE, BY_DEADLINE_DATE
}

data class FilterPreferences(val sortOrder: SortOrder, val anchorImportant: Boolean)

class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(DATASTORE_NAME)
    val preferencesFlow: Flow<FilterPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences: ", exception)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_CREATION_DATE.name
            )
            val anchorImportant = preferences[PreferencesKeys.ANCHOR_IMPORTANT] ?: false
            FilterPreferences(sortOrder, anchorImportant)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateAnchorImportant(anchorImportant: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANCHOR_IMPORTANT] = anchorImportant
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val ANCHOR_IMPORTANT = preferencesKey<Boolean>("anchor_important")
    }
}
