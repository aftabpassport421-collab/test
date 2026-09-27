package com.example.data

import com.example.model.StoreBranch
import com.example.model.ToyItem

object ToyCatalog {
  val branches = listOf(
    StoreBranch(
      id = "moq",
      name = "Wonder Toy - Mall of Qatar",
      mall = "Mall of Qatar",
      zone = "Al Rayyan, Doha",
      timing = "10:00 AM - 11:00 PM Daily",
      phone = "+974 4499 1234",
      pickupReady = true
    ),
    StoreBranch(
      id = "vil",
      name = "Wonder Toy - Villaggio Mall",
      mall = "Villaggio Mall",
      zone = "Aspire Zone, Al Waab",
      timing = "10:00 AM - 11:00 PM Daily",
      phone = "+974 4450 5678",
      pickupReady = true
    ),
    StoreBranch(
      id = "dfc",
      name = "Wonder Toy - Doha Festival City",
      mall = "Doha Festival City",
      zone = "Umm Salal Mohammed",
      timing = "10:00 AM - 12:00 AM (Midnight)",
      phone = "+974 4035 8890",
      pickupReady = true
    ),
    StoreBranch(
      id = "pvl",
      name = "Wonder Toy - Place Vendôme",
      mall = "Place Vendôme Mall",
      zone = "Lusail City, Qatar",
      timing = "10:00 AM - 11:00 PM Daily",
      phone = "+974 4412 3344",
      pickupReady = true
    )
  )

  val categories = listOf(
    "All Categories",
    "Building Sets",
    "Vehicles & RC",
    "Dolls & Plush",
    "Action Figures",
    "Outdoor & Nerf",
    "Creative & Arts",
    "Board Games & STEM"
  )

  val ageGroups = listOf(
    "All Ages",
    "0-2 Years",
    "3-5 Years",
    "6-8 Years",
    "9-12 Years",
    "12+ Years"
  )

  val brands = listOf(
    "All Brands",
    "LEGO",
    "Barbie",
    "Hot Wheels",
    "Marvel",
    "Disney",
    "Nerf",
    "Play-Doh",
    "Pokémon",
    "Hasbro",
    "Transformers"
  )

  // Empty by default for Play Store release — toys are added dynamically via Admin Portal / Firestore database
  val toys = listOf<ToyItem>()
}
