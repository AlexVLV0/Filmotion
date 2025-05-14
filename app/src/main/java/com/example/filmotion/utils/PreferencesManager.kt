package com.example.filmotion.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    companion object {
        private const val PREF_NAME = "filmotion_prefs"
        private const val KEY_TOKEN = "token"
        private const val KEY_EMAIL = "email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_OLVIDADA_ID = "olvidada_id"
        private const val KEY_OLVIDADA_TIMESTAMP = "olvidada_timestamp"
        private const val KEY_MODO_OSCURO = "modo_oscuro"
        private const val PREF_FELIZ = "pref_feliz"
        private const val PREF_TRISTE = "pref_triste"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun hasToken(): Boolean = getToken() != null

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    fun saveEmail(email: String) {
        prefs.edit().putString(KEY_EMAIL, email).apply()
    }

    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    fun saveUserId(id: Int) {
        prefs.edit().putInt(KEY_USER_ID, id).apply()
    }

    fun getUserId(): Int? {
        val id = prefs.getInt(KEY_USER_ID, -1)
        return if (id != -1) id else null
    }

    fun saveOlvidada(peliculaId: Int) {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putInt(KEY_OLVIDADA_ID, peliculaId)
            .putLong(KEY_OLVIDADA_TIMESTAMP, now)
            .apply()
    }

    fun getOlvidadaIdSiValida(): Int? {
        val lastId = prefs.getInt(KEY_OLVIDADA_ID, -1)
        val timestamp = prefs.getLong(KEY_OLVIDADA_TIMESTAMP, 0L)
        val ahora = System.currentTimeMillis()
        return if (lastId != -1 && (ahora - timestamp) < 24 * 60 * 60 * 1000) {
            lastId
        } else {
            null
        }
    }

    fun clearOlvidadaId() {
        prefs.edit().remove(KEY_OLVIDADA_ID).remove(KEY_OLVIDADA_TIMESTAMP).apply()
    }

    fun savePreferenciaFeliz(valor: String) {
        prefs.edit().putString(PREF_FELIZ, valor).apply()
    }

    fun savePreferenciaTriste(valor: String) {
        prefs.edit().putString(PREF_TRISTE, valor).apply()
    }

    fun getPreferenciaFeliz(): String? = prefs.getString(PREF_FELIZ, null)

    fun getPreferenciaTriste(): String? = prefs.getString(PREF_TRISTE, null)

    fun setModoOscuro(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_MODO_OSCURO, enabled).apply()
    }

    fun isModoOscuro(): Boolean = prefs.getBoolean(KEY_MODO_OSCURO, false)

    fun saveEmocionDiaria(emocion: String) {
        prefs.edit()
            .putString("emocion_dia", emocion)
            .putLong("emocion_timestamp", System.currentTimeMillis())
            .apply()
    }

    fun getEmocionActualSiValida(): String? {
        val ts = prefs.getLong("emocion_timestamp", 0L)
        val ahora = System.currentTimeMillis()
        return if ((ahora - ts) < 24 * 60 * 60 * 1000) {
            prefs.getString("emocion_actual", null)
        } else null
    }

    // Devuelve la emoción del día solo si es válida (menos de 24h)
    fun getEmocionDelDia(): String? {
        val guardado = prefs.getLong("emocion_timestamp", 0L)
        return if (System.currentTimeMillis() - guardado < 24 * 60 * 60 * 1000) {
            prefs.getString("emocion_dia", null)
        } else {
            null
        }
    }

    // Verifica si la emoción diaria aún es válida
    fun isEmocionDelDiaValida(): Boolean {
        return getEmocionDelDia() != null
    }


}
