package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.UserProfile
import org.json.JSONObject

class UserSessionManager(context: Context) {
  private val prefs: SharedPreferences = context.getSharedPreferences("wonder_toy_user_session", Context.MODE_PRIVATE)

  fun saveProfile(profile: UserProfile) {
    val json = JSONObject().apply {
      put("id", profile.id)
      put("name", profile.name)
      put("phone", profile.phone)
      put("email", profile.email)
      put("city", profile.city)
      put("zone", profile.zone)
      put("street", profile.street)
      put("building", profile.building)
      put("isRegistered", profile.isRegistered)
      put("rewardsPoints", profile.rewardsPoints)
      put("joinedDate", profile.joinedDate)
    }
    prefs.edit().putString(KEY_USER_DATA, json.toString()).apply()
  }

  fun getProfile(): UserProfile {
    val jsonStr = prefs.getString(KEY_USER_DATA, null) ?: return UserProfile(isRegistered = false)
    return try {
      val json = JSONObject(jsonStr)
      UserProfile(
        id = json.optString("id", ""),
        name = json.optString("name", ""),
        phone = json.optString("phone", "+974 "),
        email = json.optString("email", ""),
        city = json.optString("city", "Doha"),
        zone = json.optString("zone", ""),
        street = json.optString("street", ""),
        building = json.optString("building", ""),
        isRegistered = json.optBoolean("isRegistered", false),
        rewardsPoints = json.optInt("rewardsPoints", 0),
        joinedDate = json.optString("joinedDate", "")
      )
    } catch (_: Exception) {
      UserProfile(isRegistered = false)
    }
  }

  fun clearSession() {
    prefs.edit().clear().apply()
  }

  companion object {
    private const val KEY_USER_DATA = "key_user_data"
  }
}
