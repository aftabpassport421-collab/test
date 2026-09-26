package com.example

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Wonder Toy", appName)
  }

  @Test
  fun `launch MainActivity successfully`() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity { activity ->
        assertNotNull(activity)
      }
    }
  }

  @Test
  fun `create dummy order and verify record creation`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = com.example.data.FirestoreOrderRepository.getInstance(context)
    val dummyOrder = com.example.model.FirestoreOrder(
      orderId = "TEST-DUMMY-999",
      userId = "test_user_qatar",
      items = listOf(
        com.example.model.FirestoreOrderItem(
          toyId = "toy_01",
          name = "Wonder Lego Lusail Stadium",
          quantity = 1,
          priceQar = 340.0,
          iconEmoji = "🏟️"
        )
      ),
      totalAmount = 340.0,
      status = "pending",
      timestamp = System.currentTimeMillis(),
      customerName = "Test Buyer",
      phone = "+974 5555 0000",
      address = "Doha Corniche",
      city = "Doha",
      paymentMethod = "Credit Card"
    )

    val latch = java.util.concurrent.CountDownLatch(1)
    var isSuccess = false

    repository.saveOrder(dummyOrder) { success ->
      isSuccess = success
      latch.countDown()
    }

    val completed = latch.await(5, java.util.concurrent.TimeUnit.SECONDS)
    org.junit.Assert.assertTrue("Save order timed out", completed)
    org.junit.Assert.assertTrue("Save order failed", isSuccess)

    val orders = repository.getOrdersSync()
    val found = orders.find { it.orderId == "TEST-DUMMY-999" }
    assertNotNull(found)
    assertEquals(340.0, found?.totalAmount ?: 0.0, 0.01)
    assertEquals("pending", found?.status)
  }
}

