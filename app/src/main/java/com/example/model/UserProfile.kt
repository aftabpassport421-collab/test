package com.example.model

data class UserProfile(
  val id: String = "user_qatar_default",
  val name: String = "Ahmad Al-Kuwari",
  val phone: String = "+974 5512 8844",
  val email: String = "ahmad.alkuwari@gmail.com",
  val city: String = "Doha",
  val zone: String = "Zone 66 (West Bay Lagoon)",
  val street: String = "Street 840",
  val building: String = "Villa 14",
  val isRegistered: Boolean = true,
  val rewardsPoints: Int = 250,
  val joinedDate: String = "September 2026"
) {
  val fullQatarAddress: String
    get() = "$building, $street, $zone, $city"

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
