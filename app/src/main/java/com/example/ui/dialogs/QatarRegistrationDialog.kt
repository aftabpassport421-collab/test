package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.QatarMunicipality
import com.example.model.UserProfile
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent
import com.example.ui.theme.StarGold

enum class AuthStep {
  FORM,
  OTP_VERIFY,
  SUCCESS
}

@Composable
fun QatarRegistrationDialog(
  initialUser: UserProfile? = null,
  onDismiss: () -> Unit,
  onRegistrationComplete: (UserProfile) -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(if (initialUser?.isRegistered == true) 0 else 1) } // 0: Login, 1: Register
  var authStep by remember { mutableStateOf(AuthStep.FORM) }

  // Form Fields
  var fullName by remember { mutableStateOf(initialUser?.name ?: "") }
  var qatarPhoneDigits by remember {
    val clean = initialUser?.phone?.replace("+974", "")?.replace(" ", "") ?: ""
    mutableStateOf(clean)
  }
  var email by remember { mutableStateOf(initialUser?.email ?: "") }
  var selectedMunicipality by remember {
    mutableStateOf(
      QatarMunicipality.values().find { it.name.equals(initialUser?.city, ignoreCase = true) }
        ?: QatarMunicipality.DOHA
    )
  }
  var zoneNumber by remember { mutableStateOf(initialUser?.zone ?: "Zone 66 (West Bay Lagoon)") }
  var streetNumber by remember { mutableStateOf(initialUser?.street ?: "Street 840") }
  var buildingNumber by remember { mutableStateOf(initialUser?.building ?: "Villa 14") }

  // OTP State
  var enteredOtp by remember { mutableStateOf("") }
  var municipalityDropdownExpanded by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  fun isValidQatarPhone(digits: String): Boolean {
    val trimmed = digits.trim()
    return trimmed.length == 8 && (trimmed.startsWith("3") || trimmed.startsWith("5") || trimmed.startsWith("6") || trimmed.startsWith("7"))
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .padding(vertical = 24.dp)
        .testTag("qatar_auth_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Top Bar with Close button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = IndigoPrimary.copy(alpha = 0.12f),
              modifier = Modifier.size(36.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(text = "🇶🇦", fontSize = 18.sp)
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Wonder Toy Qatar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "YallaToys-Style Qatar Mobile Access",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_auth_dialog_btn")
          ) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (authStep) {
          AuthStep.FORM -> {
            // Tabs: Sign In / Register (Same as Yalla Toys Qatar)
            TabRow(
              selectedTabIndex = selectedTab,
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              contentColor = IndigoPrimary,
              indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                  Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                  color = IndigoPrimary
                )
              },
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
            ) {
              Tab(
                selected = selectedTab == 0,
                onClick = {
                  selectedTab = 0
                  errorMessage = null
                },
                text = {
                  Text(
                    text = "Sign In",
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp
                  )
                }
              )
              Tab(
                selected = selectedTab == 1,
                onClick = {
                  selectedTab = 1
                  errorMessage = null
                },
                text = {
                  Text(
                    text = "Register (+100 🎁)",
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp
                  )
                }
              )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // In Registration mode: Full Name
            if (selectedTab == 1) {
              OutlinedTextField(
                value = fullName,
                onValueChange = {
                  fullName = it
                  errorMessage = null
                },
                label = { Text("Full Name (as per QID/Qatar ID)") },
                leadingIcon = {
                  Icon(Icons.Filled.Person, contentDescription = null, tint = IndigoPrimary)
                },
                placeholder = { Text("e.g. Ahmad Al-Kuwari") },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_name_input"),
                shape = RoundedCornerShape(12.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
            }

            // Qatar Phone Number Field (Fixed +974 Country Code)
            Text(
              text = "Qatar Mobile Number (Ooredoo / Vodafone)",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Fixed +974 Qatar Badge
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.height(56.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                  Text(text = "🇶🇦", fontSize = 18.sp)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "+974",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }

              Spacer(modifier = Modifier.width(8.dp))

              OutlinedTextField(
                value = qatarPhoneDigits,
                onValueChange = { input ->
                  val filtered = input.filter { it.isDigit() }
                  if (filtered.length <= 8) {
                    qatarPhoneDigits = filtered
                    errorMessage = null
                  }
                },
                placeholder = { Text("XX XXX XXX (e.g. 5512 8844)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = qatarPhoneDigits.isNotEmpty() && !isValidQatarPhone(qatarPhoneDigits),
                modifier = Modifier
                  .weight(1f)
                  .testTag("auth_qatar_phone_input"),
                shape = RoundedCornerShape(12.dp)
              )
            }

            Text(
              text = "Qatar valid numbers start with 3, 5, 6, or 7 (8 digits total)",
              fontSize = 10.sp,
              color = if (qatarPhoneDigits.isNotEmpty() && !isValidQatarPhone(qatarPhoneDigits)) {
                MaterialTheme.colorScheme.error
              } else {
                MaterialTheme.colorScheme.onSurfaceVariant
              },
              modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )

            // Registration Extra Fields
            if (selectedTab == 1) {
              Spacer(modifier = Modifier.height(12.dp))

              // Email
              OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (Optional for receipts)") },
                placeholder = { Text("name@example.qa") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("auth_email_input"),
                shape = RoundedCornerShape(12.dp)
              )

              Spacer(modifier = Modifier.height(14.dp))

              // Qatar Municipality Selector
              Text(
                text = "Qatar Delivery Municipality / Area",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(4.dp))

              Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                  border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { municipalityDropdownExpanded = true }
                    .padding(vertical = 12.dp, horizontal = 14.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Filled.LocationOn, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(18.dp))
                      Spacer(modifier = Modifier.width(8.dp))
                      Column {
                        Text(
                          text = selectedMunicipality.displayName,
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Bold,
                          color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                          text = selectedMunicipality.deliveryTime,
                          fontSize = 10.sp,
                          color = MintAccent
                        )
                      }
                    }
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                  }
                }

                DropdownMenu(
                  expanded = municipalityDropdownExpanded,
                  onDismissRequest = { municipalityDropdownExpanded = false }
                ) {
                  QatarMunicipality.values().forEach { mun ->
                    DropdownMenuItem(
                      text = {
                        Column {
                          Text(mun.displayName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                          Text(mun.deliveryTime, fontSize = 10.sp, color = MintAccent)
                        }
                      },
                      onClick = {
                        selectedMunicipality = mun
                        municipalityDropdownExpanded = false
                      }
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Qatar Blue Plate Address Format (Zone, Street, Building)
              Text(
                text = "Qatar Blue Plate Address 🏠",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(4.dp))

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                  value = zoneNumber,
                  onValueChange = { zoneNumber = it },
                  label = { Text("Zone", fontSize = 11.sp) },
                  placeholder = { Text("e.g. 66") },
                  singleLine = true,
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                  value = streetNumber,
                  onValueChange = { streetNumber = it },
                  label = { Text("Street", fontSize = 11.sp) },
                  placeholder = { Text("e.g. 840") },
                  singleLine = true,
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                  value = buildingNumber,
                  onValueChange = { buildingNumber = it },
                  label = { Text("Bldg/Villa", fontSize = 11.sp) },
                  placeholder = { Text("e.g. 14") },
                  singleLine = true,
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(10.dp)
                )
              }
            }

            // Error display
            errorMessage?.let { err ->
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = err,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Button: Send OTP
            Button(
              onClick = {
                if (!isValidQatarPhone(qatarPhoneDigits)) {
                  errorMessage = "Please enter a valid 8-digit Qatar mobile number starting with 3, 5, 6, or 7."
                  return@Button
                }
                if (selectedTab == 1 && fullName.isBlank()) {
                  errorMessage = "Please enter your full name as per Qatar ID."
                  return@Button
                }
                // Transition to OTP verification step
                errorMessage = null
                authStep = AuthStep.OTP_VERIFY
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("auth_submit_btn")
            ) {
              Icon(Icons.Filled.Sms, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (selectedTab == 1) "Send Qatar SMS OTP 📲" else "Sign In with Mobile OTP 📲",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // YallaToys feature banner
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MintAccent.copy(alpha = 0.1f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
              ) {
                Text(text = "🎁", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "Yalla Rewards Qatar • 100 Welcome Points",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintAccent
                  )
                  Text(
                    text = "Enjoy instant 10 QAR discount on your first Wonder Toy checkout!",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }

          AuthStep.OTP_VERIFY -> {
            // OTP Verification Step
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .size(60.dp)
                  .background(IndigoPrimary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Lock,
                  contentDescription = null,
                  tint = IndigoPrimary,
                  modifier = Modifier.size(32.dp)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "Verify Qatar Mobile 🇶🇦",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "We sent a 4-digit code to +974 $qatarPhoneDigits",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(20.dp))

              // 4 digit OTP Input
              OutlinedTextField(
                value = enteredOtp,
                onValueChange = {
                  if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    enteredOtp = it
                    errorMessage = null
                  }
                },
                placeholder = { Text("Enter 4-digit OTP (e.g. 1234)", textAlign = TextAlign.Center) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth(0.85f)
                  .testTag("otp_code_input")
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Quick Fill Demo button for instant testing
              OutlinedButton(
                onClick = { enteredOtp = "1234" },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("quick_fill_otp_btn")
              ) {
                Text("⚡ Quick-Fill Demo Code (1234)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }

              errorMessage?.let { err ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = err, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Verify Button
              Button(
                onClick = {
                  if (enteredOtp.length < 4) {
                    errorMessage = "Please enter the 4-digit code."
                    return@Button
                  }
                  // Success
                  val updatedProfile = UserProfile(
                    id = "user_qatar_${qatarPhoneDigits}",
                    name = if (fullName.isNotBlank()) fullName else "Qatar Toy Customer",
                    phone = "+974 $qatarPhoneDigits",
                    email = email.ifBlank { "customer.${qatarPhoneDigits}@wondertoy.qa" },
                    city = selectedMunicipality.displayName.split(" ").first(),
                    zone = zoneNumber,
                    street = streetNumber,
                    building = buildingNumber,
                    isRegistered = true,
                    rewardsPoints = (initialUser?.rewardsPoints ?: 150) + 100,
                    joinedDate = "September 2026"
                  )
                  authStep = AuthStep.SUCCESS
                  onRegistrationComplete(updatedProfile)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("verify_otp_confirm_btn")
              ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Verify & Access Account", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              }

              Spacer(modifier = Modifier.height(10.dp))

              TextButton(onClick = { authStep = AuthStep.FORM }) {
                Text("← Change Mobile Number", fontSize = 12.sp, color = IndigoPrimary)
              }
            }
          }

          AuthStep.SUCCESS -> {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .background(MintAccent.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Check,
                  contentDescription = null,
                  tint = MintAccent,
                  modifier = Modifier.size(36.dp)
                )
              }
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = "Welcome to Wonder Toy Qatar! 🇶🇦",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Your Qatar mobile account is verified & active.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(16.dp))
              Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MintAccent),
                modifier = Modifier.fillMaxWidth().testTag("auth_done_btn")
              ) {
                Text("Start Shopping Wonder Toy 🛍️", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}
