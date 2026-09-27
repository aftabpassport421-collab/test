package com.example.model

data class UserProfile(
  val id: String = "",
  val name: String = "",
  val phone: String = "+974 ",
  val email: String = "",
  val city: String = "Doha",
  val zone: String = "",
  val street: String = "",
  val building: String = "",
  val isRegistered: Boolean = false,
  val rewardsPoints: Int = 0,
  val joinedDate: String = ""
) {
  val fullQatarAddress: String
    get() = if (building.isNotBlank() && street.isNotBlank()) "$building, $street, $zone, $city" else "$city, Qatar"

  val nationalPhoneFormatted: String
    get() = if (phone.startsWith("+974")) phone else "+974 $phone"
}

enum class QatarMunicipality(val displayName: String, val deliveryTime: String) {
  DOHA("Doha (West Bay, Al Sadd, Old Airport)", "Same-Day Delivery (2-4 hrs)"),
  LUSAIL("Lusail City (Marina, Fox Hills)", "Same-Day Delivery (2-4 hrs)"),
  THE_PEARL("The Pearl-Qatar (Porto Arabia, Qanat Quartier)", "Same-Day Delivery (2-4 hrs)"),
  AL_RAYYAN("Al Rayyan (Education City, Gharrafa)", "Same-Day Delivery (3-5 hrs)"),
  AL_WAKRAH("Al Wakrah (Ezdan, Souq Waqif Al Wakra)", "Same-Day Delivery (3-5 hrs)"),
  AL_KHOR("Al Khor & Ras Laffan", "Next-Day Morning Delivery"),
  UMM_SALAL("Umm Salal & Al Daayen", "Same-Day Delivery (4-6 hrs)")
}
