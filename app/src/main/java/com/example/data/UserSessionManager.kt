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
    val jsonStr = prefs.getString(KEY_USER_DATA, null) ?: return UserProfile()
    return try {
      val json = JSONObject(jsonStr)
      UserProfile(
        id = json.optString("id", "user_qatar_default"),
        name = json.optString("name", "Ahmad Al-Kuwari"),
        phone = json.optString("phone", "+974 5512 8844"),
        email = json.optString("email", "ahmad.alkuwari@gmail.com"),
        city = json.optString("city", "Doha"),
        zone = json.optString("zone", "Zone 66 (West Bay Lagoon)"),
        street = json.optString("street", "Street 840"),
        building = json.optString("building", "Villa 14"),
        isRegistered = json.optBoolean("isRegistered", true),
        rewardsPoints = json.optInt("rewardsPoints", 250),
        joinedDate = json.optString("joinedDate", "September 2026")
      )
    } catch (_: Exception) {
      UserProfile()
    }
  }

  fun clearSession() {
    prefs.edit().clear().apply()
  }

  companion object {
    private const val KEY_USER_DATA = "key_user_data"
  }
}
