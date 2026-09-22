package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ToyItem
import com.example.ui.components.ToyCard
import com.example.ui.theme.IndigoPrimary

@Composable
fun WishlistScreen(
  wishlistToys: List<ToyItem>,
  onToyClick: (ToyItem) -> Unit,
  onWishlistToggle: (String) -> Unit,
  onAddToCart: (ToyItem) -> Unit,
  onBrowseClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (wishlistToys.isEmpty()) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(32.dp)
        .testTag("wishlist_empty_view"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = Icons.Outlined.FavoriteBorder,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.size(72.dp)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Your Wishlist is Empty",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "Tap the heart on any toy to save it for birthday surprises and holidays!",
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
      Spacer(modifier = Modifier.height(20.dp))
      Button(
        onClick = onBrowseClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        modifier = Modifier.testTag("wishlist_browse_btn")
      ) {
        Text("Explore Wonder Toy Catalog")
      }
    }
  } else {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .testTag("wishlist_screen_list"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Saved Toys (${wishlistToys.size})",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Spacer(modifier = Modifier.height(12.dp))
      }

      items(wishlistToys.chunked(2)) { pair ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          pair.forEach { toy ->
            Box(modifier = Modifier.weight(1f)) {
              ToyCard(
                toy = toy,
                isWishlisted = true,
                onToyClick = { onToyClick(toy) },
                onWishlistToggle = { onWishlistToggle(toy.id) },
                onAddToCart = { onAddToCart(toy) }
              )
            }
          }
          if (pair.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}
