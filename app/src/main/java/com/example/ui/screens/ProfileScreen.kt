package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent
import com.example.ui.theme.StarGold

@Composable
fun ProfileScreen(
  user: UserProfile,
  onOpenAuthDialog: () -> Unit,
  onViewOrders: () -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("customer_profile_screen"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
  ) {
    // Header Row
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "My Qatar Account 🇶🇦",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Wonder Toy Customer Profile",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MintAccent.copy(alpha = 0.12f),
          modifier = Modifier.testTag("profile_verified_badge")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MintAccent, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Qatar Verified 🇶🇦", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MintAccent)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // User Profile Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("user_profile_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .background(IndigoPrimary.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = user.name.take(1).uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = IndigoPrimary
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = user.name,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Black,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.Filled.CheckCircle,
                  contentDescription = "Verified Qatar Mobile",
                  tint = MintAccent,
                  modifier = Modifier.size(16.dp)
                )
              }

              Spacer(modifier = Modifier.height(2.dp))

              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🇶🇦", fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = user.nationalPhoneFormatted,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = IndigoPrimary
                )
              }

              Text(
                text = user.email,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            IconButton(
              onClick = onOpenAuthDialog,
              modifier = Modifier.testTag("profile_edit_user_btn")
            ) {
              Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = IndigoPrimary)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Divider()
          Spacer(modifier = Modifier.height(14.dp))

          // Yalla-style Loyalty Rewards Banner
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = CoralSecondary.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CardGiftcard, contentDescription = null, tint = CoralSecondary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Wonder Club Qatar Points",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoralSecondary
                  )
                  Text(
                    text = "${user.rewardsPoints} Points (= ${(user.rewardsPoints / 10)} QAR discount)",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = CoralSecondary
              ) {
                Text(
                  text = "ACTIVE",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Qatar Address Section (Blue Plate)
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Filled.LocationOn, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Qatar Delivery Address (Blue Plate)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            TextButton(onClick = onOpenAuthDialog) {
              Text("Change", fontSize = 12.sp, color = IndigoPrimary)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "${user.building}, ${user.street}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${user.zone}, ${user.city}, State of Qatar 🇶🇦",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "⚡ Same-Day delivery from Wonder Toy Doha Hub",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MintAccent
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // App Navigation Options
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(8.dp)) {
          // Track Orders option
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onViewOrders() }
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text("My Orders & Live Qatar Tracking", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Real-time Firestore dispatch updates", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
          }

          Divider()

          // Switch / Re-register Qatar Mobile
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onOpenAuthDialog() }
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Filled.Phone, contentDescription = null, tint = MintAccent, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text("Switch Qatar Mobile Number", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Update +974 number with SMS OTP", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))
    }

    // Logout / Reset session
    item {
      OutlinedButton(
        onClick = onLogout,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("profile_logout_btn")
      ) {
        Icon(Icons.Filled.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sign Out / Switch Account", fontSize = 13.sp, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}
