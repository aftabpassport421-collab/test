package com.example.model

data class StoreBranch(
  val id: String,
  val name: String,
  val mall: String,
  val zone: String,
  val timing: String,
  val phone: String,
  val pickupReady: Boolean = true,
)
