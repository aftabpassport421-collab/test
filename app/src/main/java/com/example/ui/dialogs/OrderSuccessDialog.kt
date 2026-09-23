package com.example.ui.dialogs

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent

@Composable
fun OrderSuccessDialog(
  order: Order,
  onDismiss: () -> Unit,
  onViewOrders: () -> Unit,
  modifier: Modifier = Modifier
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(24.dp),
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier.testTag("order_success_dialog"),
    title = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(64.dp)
            .background(MintAccent.copy(alpha = 0.15f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Success",
            tint = MintAccent,
            modifier = Modifier.size(36.dp)
          )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Order Confirmed! 🎉",
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Wonder Toy Qatar is packing your toys",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Order ID", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(order.id, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Paid", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${order.totalQar.toInt()} QAR", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Payment", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(
                order.paymentMethod,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (order.isPaid) MintAccent else CoralSecondary
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Payment Status", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(
                if (order.isPaid) "PAID & VERIFIED ✓" else "PAY ON DELIVERY",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (order.isPaid) MintAccent else CoralSecondary
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Reference #", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(order.transactionRef, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Delivery ETA", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(order.estimatedArrival, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CoralSecondary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Destination", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(order.city, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Filled.LocalShipping,
            contentDescription = null,
            tint = MintAccent,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Doha warehouse dispatch within 30 minutes",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onViewOrders,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        modifier = Modifier.testTag("success_view_orders_btn")
      ) {
        Text("Track Order 📦")
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("success_continue_btn")
      ) {
        Text("Continue Shopping")
      }
    }
  )
}
