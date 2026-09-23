package com.example.ui.dialogs

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent

enum class AppRoleMode(val title: String, val subtitle: String) {
  CUSTOMER("Customer App 🛍️", "Browse toys, cart, Qatar mobile registration & live tracking"),
  ADMIN("Admin / Merchant App 🛠️", "Firestore order queue, status updater dropdown & analytics")
}

@Composable
fun AppModeSelectorDialog(
  currentMode: AppRoleMode,
  onDismiss: () -> Unit,
  onSelectMode: (AppRoleMode) -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .testTag("app_mode_selector_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Choose Application Mode",
              fontSize = 17.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Separate apps for Customers & Admins 🇶🇦",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Option 1: Customer App
        val isCustomer = currentMode == AppRoleMode.CUSTOMER
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isCustomer) IndigoPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
          border = androidx.compose.foundation.BorderStroke(
            width = if (isCustomer) 2.dp else 1.dp,
            color = if (isCustomer) IndigoPrimary else MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              onSelectMode(AppRoleMode.CUSTOMER)
              onDismiss()
            }
            .testTag("select_customer_app_mode")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .background(IndigoPrimary.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Filled.ShoppingBag, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Wonder Toy (Customer App)",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "🛍️", fontSize = 12.sp)
              }
              Text(
                text = "Shopping, Qatar +974 mobile registration, cart & live order tracking",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            if (isCustomer) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .background(IndigoPrimary, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Option 2: Admin App
        val isAdmin = currentMode == AppRoleMode.ADMIN
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isAdmin) CoralSecondary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
          border = androidx.compose.foundation.BorderStroke(
            width = if (isAdmin) 2.dp else 1.dp,
            color = if (isAdmin) CoralSecondary else MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              onSelectMode(AppRoleMode.ADMIN)
              onDismiss()
            }
            .testTag("select_admin_app_mode")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .background(CoralSecondary.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Filled.Storefront, contentDescription = null, tint = CoralSecondary, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Wonder Toy Admin (Merchant Hub)",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "🛠️", fontSize = 12.sp)
              }
              Text(
                text = "Live Firestore orders queue, status updater dropdown & dispatch analytics",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            if (isAdmin) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .background(CoralSecondary, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Tip: You can switch between apps at any time using the quick switch button.",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 4.dp)
        )
      }
    }
  }
}
