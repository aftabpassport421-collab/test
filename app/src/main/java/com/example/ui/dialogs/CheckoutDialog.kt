package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeliveryType
import com.example.model.StoreBranch
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutDialog(
  subtotalQar: Double,
  deliveryFeeQar: Double,
  discountQar: Double,
  finalTotalQar: Double,
  currentDeliveryType: DeliveryType,
  selectedStore: StoreBranch? = null,
  onDeliveryTypeChange: (DeliveryType) -> Unit,
  onStoreChange: ((StoreBranch) -> Unit)? = null,
  onDismiss: () -> Unit,
  onConfirmOrder: (name: String, phone: String, address: String, city: String, payment: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var customerName by remember { mutableStateOf("Mohammed Al-Kuwari") }
  var phone by remember { mutableStateOf("+974 5512 3456") }
  var city by remember { mutableStateOf("Doha") }
  var streetAddress by remember { mutableStateOf("Villa 28, Street 902, West Bay Lagoon") }
  var paymentMethod by remember { mutableStateOf("Credit / Debit Card") }

  // Card details state
  var cardNumber by remember { mutableStateOf("4508 •••• •••• 9124") }
  var cardExpiry by remember { mutableStateOf("08/28") }
  var cardCvv by remember { mutableStateOf("•••") }
  var cardHolder by remember { mutableStateOf("M. AL-KUWARI") }

  var nameError by remember { mutableStateOf(false) }

  val qatarCities = listOf("Doha", "Lusail", "The Pearl", "Al Wakrah", "Al Rayyan", "Al Khor")
  val paymentOptions = listOf("Credit / Debit Card", "Apple Pay", "QNB QPay", "Cash on Delivery")

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    modifier = modifier.testTag("checkout_modal_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Qatar Checkout 🇶🇦",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Express delivery across Doha & all Qatar regions",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("close_checkout_dialog_btn")
        ) {
          Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Secure Gateway Guarantee Banner
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Filled.Shield,
            contentDescription = null,
            tint = IndigoPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "100% Secure Qatar Payment Gateway",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = IndigoPrimary
            )
            Text(
              text = "Encrypted transactions via QCB compliant payment channels.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Section 1: Customer Contact
      Text(
        text = "Recipient Information",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))

      OutlinedTextField(
        value = customerName,
        onValueChange = {
          customerName = it
          nameError = it.isBlank()
        },
        label = { Text("Full Name") },
        isError = nameError,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("checkout_name_input")
      )

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Qatar Mobile (+974)") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("checkout_phone_input")
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Section 2: Delivery Option (Direct Home Delivery across Qatar)
      Text(
        text = "Delivery Method",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))

      DeliveryType.values().forEach { dType ->
        val isSelected = currentDeliveryType == dType
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onDeliveryTypeChange(dType) },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              RadioButton(
                selected = isSelected,
                onClick = { onDeliveryTypeChange(dType) },
                colors = RadioButtonDefaults.colors(selectedColor = IndigoPrimary)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Column {
                Text(
                  text = dType.title,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = dType.eta,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
            Text(
              text = "${dType.priceQar.toInt()} QAR",
              fontSize = 13.sp,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Delivery Address
      Text(
        text = "Delivery Address in Qatar",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))

      // City chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        qatarCities.forEach { c ->
          val isCity = city == c
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isCity) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.clickable { city = c }
          ) {
            Text(
              text = c,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (isCity) Color.White else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = streetAddress,
        onValueChange = { streetAddress = it },
        label = { Text("Zone / Street / Building / Villa") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("checkout_address_input")
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Section 3: Payment Method Selection & Details
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Payment Method",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "QNB • CBQ • Visa • MC",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
      }
      Spacer(modifier = Modifier.height(6.dp))

      // Payment option selector chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        paymentOptions.forEach { pay ->
          val isPaySelected = paymentMethod == pay
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isPaySelected) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier
              .weight(1f)
              .clickable { paymentMethod = pay }
          ) {
            Box(
              modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = when (pay) {
                  "Credit / Debit Card" -> "Card 💳"
                  "Apple Pay" -> "Apple Pay 🍏"
                  "QNB QPay" -> "QPay 🇶🇦"
                  else -> "COD 💵"
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPaySelected) Color.White else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Payment Details Form per selected method
      AnimatedVisibility(visible = paymentMethod == "Credit / Debit Card") {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Filled.CreditCard,
                  contentDescription = null,
                  tint = IndigoPrimary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Card Details",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Text(
                text = "Visa / Mastercard / NAPS",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = cardNumber,
              onValueChange = { cardNumber = it },
              label = { Text("Card Number", fontSize = 11.sp) },
              singleLine = true,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedTextField(
                value = cardExpiry,
                onValueChange = { cardExpiry = it },
                label = { Text("Expiry (MM/YY)", fontSize = 11.sp) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              )
              OutlinedTextField(
                value = cardCvv,
                onValueChange = { cardCvv = it },
                label = { Text("CVV", fontSize = 11.sp) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
              value = cardHolder,
              onValueChange = { cardHolder = it },
              label = { Text("Cardholder Name", fontSize = 11.sp) },
              singleLine = true,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }

      AnimatedVisibility(visible = paymentMethod == "Apple Pay") {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.Black,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "🍏 Pay with Apple Pay (Face ID / Touch ID)",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      AnimatedVisibility(visible = paymentMethod == "QNB QPay") {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🏦", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Qatar National Payment Gateway (QPay)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Seamless verification with any Qatar debit card (NAPS).",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      AnimatedVisibility(visible = paymentMethod == "Cash on Delivery") {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = CoralSecondary.copy(alpha = 0.1f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "💵", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Cash or Card on Delivery",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CoralSecondary
              )
              Text(
                text = "Pay Qatar Riyals to the courier upon delivery at your doorstep.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Section 4: Order Summary
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
            Text("Subtotal", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${subtotalQar.toInt()} QAR", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }

          if (discountQar > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Discount Promo", fontSize = 13.sp, color = CoralSecondary)
              Text("-${discountQar.toInt()} QAR", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoralSecondary)
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Qatar Shipping", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              "${deliveryFeeQar.toInt()} QAR",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Divider(modifier = Modifier.padding(vertical = 8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Total Amount", fontSize = 15.sp, fontWeight = FontWeight.Black)
            Text(
              "${finalTotalQar.toInt()} QAR",
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Confirm Order Button
      Button(
        onClick = {
          if (customerName.isBlank()) {
            nameError = true
            return@Button
          }
          onConfirmOrder(customerName, phone, streetAddress, city, paymentMethod)
        },
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("confirm_order_btn")
      ) {
        Icon(
          imageVector = Icons.Filled.CheckCircle,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Pay & Confirm • ${finalTotalQar.toInt()} QAR",
          fontSize = 16.sp,
          fontWeight = FontWeight.Black
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
