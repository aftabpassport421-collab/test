package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ToyItem
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.StarGold

@Composable
fun ToyCard(
  toy: ToyItem,
  isWishlisted: Boolean,
  onToyClick: () -> Unit,
  onWishlistToggle: () -> Unit,
  onAddToCart: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .clickable { onToyClick() }
      .testTag("toy_card_${toy.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Top visual block
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .background(
            Brush.verticalGradient(
              listOf(
                Color(toy.accentColorHex).copy(alpha = 0.18f),
                Color(toy.accentColorHex).copy(alpha = 0.05f)
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        // Main Visual Emoji / Icon
        Text(
          text = toy.iconEmoji,
          fontSize = 54.sp,
          modifier = Modifier.testTag("toy_emoji_${toy.id}")
        )

        // Badge if available (e.g. Bestseller, -20%)
        if (toy.badge != null) {
          Surface(
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            color = if (toy.badge.contains("%") || toy.badge.contains("Deal")) CoralSecondary else IndigoPrimary
          ) {
            Text(
              text = toy.badge,
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
          }
        }

        // Wishlist button
        IconButton(
          onClick = onWishlistToggle,
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(4.dp)
            .size(36.dp)
            .background(Color.White.copy(alpha = 0.85f), CircleShape)
            .testTag("wishlist_btn_${toy.id}")
        ) {
          Icon(
            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Wishlist",
            tint = if (isWishlisted) Color(0xFFEF4444) else Color(0xFF64748B),
            modifier = Modifier.size(20.dp)
          )
        }

        // Age tag at bottom right
        Surface(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp),
          shape = RoundedCornerShape(6.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        ) {
          Text(
            text = toy.ageRange,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      // Details section
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp)
      ) {
        // Brand & Rating row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = toy.brand.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(toy.accentColorHex),
            letterSpacing = 0.5.sp
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Star,
              contentDescription = "Rating",
              tint = StarGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "${toy.rating}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Product Name
        Text(
          text = toy.name,
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 18.sp,
          modifier = Modifier.height(36.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Price & Add to Cart row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = "${toy.priceQar.toInt()} ",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "QAR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
            if (toy.originalPriceQar != null) {
              Text(
                text = "${toy.originalPriceQar.toInt()} QAR",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                textDecoration = TextDecoration.LineThrough
              )
            }
          }

          Button(
            onClick = onAddToCart,
            shape = RoundedCornerShape(10.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
              .height(34.dp)
              .testTag("add_to_cart_${toy.id}")
          ) {
            Icon(
              imageVector = Icons.Filled.ShoppingCart,
              contentDescription = "Add to cart",
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Add",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
