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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeliveryType
import com.example.model.StoreBranch
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
  val scope = rememberCoroutineScope()

  var customerName by remember { mutableStateOf("Mohammed Al-Kuwari") }
  var phone by remember { mutableStateOf("+974 5512 3456") }
  var city by remember { mutableStateOf("Doha") }
  var streetAddress by remember { mutableStateOf("Villa 28, Street 902, West Bay Lagoon") }
  var paymentMethod by remember { mutableStateOf("Credit / Debit Card") }

  // Card details state
  var cardNumber by remember { mutableStateOf("4508 2384 9102 5519") }
  var cardExpiry by remember { mutableStateOf("12/28") }
  var cardCvv by remember { mutableStateOf("382") }
  var cardHolder by remember { mutableStateOf("M. AL-KUWARI") }

  // QPay state
  var selectedBank by remember { mutableStateOf("QNB") }
  var qpayOtp by remember { mutableStateOf("8491") }

  // Processing state
  var isProcessingPayment by remember { mutableStateOf(false) }
  var processingStepText by remember { mutableStateOf("Connecting to gateway...") }
  var nameError by remember { mutableStateOf(false) }
  var cardError by remember { mutableStateOf(false) }

  val qatarCities = listOf("Doha", "Lusail", "The Pearl", "Al Wakrah", "Al Rayyan", "Al Khor")
  val paymentOptions = listOf("Credit / Debit Card", "Apple Pay", "QNB QPay", "Cash on Delivery")
  val qatarBanks = listOf("QNB", "CBQ", "QIB", "Dukhan", "Al Rayan")

  ModalBottomSheet(
    onDismissRequest = {
      if (!isProcessingPayment) onDismiss()
    },
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    modifier = modifier.testTag("checkout_modal_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .imePadding()
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Wonder Toy Checkout",
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "🇶🇦", fontSize = 18.sp)
          }
          Text(
            text = "Fast delivery across Doha & all Qatar regions",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        IconButton(
          onClick = { if (!isProcessingPayment) onDismiss() },
          modifier = Modifier.testTag("close_checkout_dialog_btn")
        ) {
          Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Gateway Security Guarantee Banner
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
              text = "Encrypted transaction channel authorized for Qatar.",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Section 1: Customer Contact
      Text(
        text = "1. Recipient Details",
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("checkout_phone_input")
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Section 2: Delivery Option (Direct Home Delivery across Qatar)
      Text(
        text = "2. Qatar Delivery Option",
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
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
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

      Spacer(modifier = Modifier.height(12.dp))

      // Delivery Address & City
      Text(
        text = "Delivery Address",
        fontSize = 13.sp,
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
              modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = streetAddress,
        onValueChange = { streetAddress = it },
        label = { Text("Zone / Street / Villa / Building") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("checkout_address_input")
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Section 3: Payment Method Selection & Live Interactive Form
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "3. Payment Method",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Live Gateway Ready",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = MintAccent
        )
      }
      Spacer(modifier = Modifier.height(8.dp))

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

      Spacer(modifier = Modifier.height(12.dp))

      // 1) Credit / Debit Card Form
      AnimatedVisibility(visible = paymentMethod == "Credit / Debit Card") {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
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
                  text = "Card Information",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Text(
                text = "Visa / MC / NAPS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = IndigoPrimary
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Test Card Fill Buttons for easy live testing on phone
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedButton(
                onClick = {
                  cardNumber = "4508 2384 9102 5519"
                  cardExpiry = "12/28"
                  cardCvv = "382"
                  cardHolder = "M. AL-KUWARI"
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(imageVector = Icons.Filled.FlashOn, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Test Visa", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }

              OutlinedButton(
                onClick = {
                  cardNumber = "5200 8192 3840 9124"
                  cardExpiry = "06/29"
                  cardCvv = "714"
                  cardHolder = "QNB NAPS CARD"
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(imageVector = Icons.Filled.FlashOn, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Test NAPS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = cardNumber,
              onValueChange = { input ->
                val digits = input.filter { it.isDigit() }.take(16)
                cardNumber = digits.chunked(4).joinToString(" ")
                cardError = false
              },
              label = { Text("16-Digit Card Number", fontSize = 11.sp) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              isError = cardError,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedTextField(
                value = cardExpiry,
                onValueChange = { input ->
                  val clean = input.filter { it.isDigit() }.take(4)
                  cardExpiry = if (clean.length > 2) "${clean.take(2)}/${clean.drop(2)}" else clean
                },
                label = { Text("Expiry (MM/YY)", fontSize = 11.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              )
              OutlinedTextField(
                value = cardCvv,
                onValueChange = { input ->
                  cardCvv = input.filter { it.isDigit() }.take(4)
                },
                label = { Text("CVV", fontSize = 11.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

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

      // 2) Apple Pay / Google Pay
      AnimatedVisibility(visible = paymentMethod == "Apple Pay") {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFF1A1A1A),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Filled.Fingerprint,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Pay with Apple Pay  / Biometric",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "1-Tap instant authorization linked to Qatar bank cards",
              color = Color.LightGray,
              fontSize = 11.sp
            )
          }
        }
      }

      // 3) QNB QPay
      AnimatedVisibility(visible = paymentMethod == "QNB QPay") {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Filled.AccountBalance,
                contentDescription = null,
                tint = IndigoPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Qatar Central Bank Gateway (QPay)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = IndigoPrimary
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Select your Qatar banking institution:",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bank selectors
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              qatarBanks.forEach { b ->
                val isB = selectedBank == b
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = if (isB) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant,
                  modifier = Modifier
                    .weight(1f)
                    .clickable { selectedBank = b }
                ) {
                  Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                    Text(
                      text = b,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (isB) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = qpayOtp,
                onValueChange = { qpayOtp = it.filter { ch -> ch.isDigit() }.take(6) },
                label = { Text("QNB 3D-Secure OTP", fontSize = 11.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
              )
              OutlinedButton(
                onClick = { qpayOtp = "8491" },
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Autofill OTP", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // 4) Cash on Delivery
      AnimatedVisibility(visible = paymentMethod == "Cash on Delivery") {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = CoralSecondary.copy(alpha = 0.1f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "💵", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Cash on Delivery (COD)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CoralSecondary
              )
              Text(
                text = "Pay exact ${finalTotalQar.toInt()} QAR in cash to the courier. Card POS terminal is also carried upon request.",
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
            Text("Qatar Delivery Fee", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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

      // Live Payment Processing Overlay or Action Button
      if (isProcessingPayment) {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(20.dp),
              strokeWidth = 2.5.dp,
              color = IndigoPrimary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = processingStepText,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = IndigoPrimary
            )
          }
        }
      } else {
        Button(
          onClick = {
            if (customerName.isBlank()) {
              nameError = true
              return@Button
            }
            if (paymentMethod == "Credit / Debit Card" && cardNumber.replace(" ", "").length < 15) {
              cardError = true
              return@Button
            }

            isProcessingPayment = true
            scope.launch {
              processingStepText = "Connecting to Qatar Payment Gateway..."
              delay(350)
              processingStepText = when (paymentMethod) {
                "Apple Pay" -> "Authorizing biometric token with Apple Pay..."
                "QNB QPay" -> "Verifying $selectedBank NAPS debit with QCB..."
                "Credit / Debit Card" -> "Authorizing ${cardNumber.takeLast(4)} with Visa/MC..."
                else -> "Booking Doha cash on delivery slot..."
              }
              delay(450)
              processingStepText = "Payment Verified & Approved ✓"
              delay(300)

              val formattedPayment = when (paymentMethod) {
                "Credit / Debit Card" -> "Card (•••• ${cardNumber.filter { it.isDigit() }.takeLast(4)})"
                "Apple Pay" -> "Apple Pay  (Biometric)"
                "QNB QPay" -> "QPay ($selectedBank NAPS)"
                else -> "Cash on Delivery"
              }

              isProcessingPayment = false
              onConfirmOrder(customerName, phone, streetAddress, city, formattedPayment)
            }
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("confirm_order_btn")
        ) {
          Icon(
            imageVector = Icons.Filled.Payment,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (paymentMethod) {
              "Cash on Delivery" -> "Confirm Order • ${finalTotalQar.toInt()} QAR (COD)"
              "Apple Pay" -> "Pay with Apple Pay • ${finalTotalQar.toInt()} QAR"
              "QNB QPay" -> "Pay with QPay • ${finalTotalQar.toInt()} QAR"
              else -> "Pay & Confirm • ${finalTotalQar.toInt()} QAR"
            },
            fontSize = 15.sp,
            fontWeight = FontWeight.Black
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}
