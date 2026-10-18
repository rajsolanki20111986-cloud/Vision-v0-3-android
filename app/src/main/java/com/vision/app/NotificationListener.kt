package com.vision.app

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow

/** One incoming message. id makes every message distinct, so the flow always emits. */
data class IncomingMessage(val app: String, val sender: String, val text: String, val id: Long = System.nanoTime())

/** Forwards chat notifications (WhatsApp, Telegram, Instagram, Messenger, SMS) to the voice session. */
class VisionNotificationListener : NotificationListenerService() {

    companion object {
        val newMessage = MutableStateFlow<IncomingMessage?>(null)
        private val BLOCK = listOf("paisa", "phonepe", "paytm", "paypal", "bank", "wallet", "upi", "razorpay")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        if (!Prefs.notifyAlerts) return
        val pkg = sbn.packageName ?: return
        if (BLOCK.any { pkg.contains(it, ignoreCase = true) }) return

        val extras = sbn.notification?.extras ?: return
        val text = (extras.getCharSequence(Notification.EXTRA_TEXT) ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT))
            ?.toString()?.trim()
        if (text.isNullOrBlank()) return
        val sender = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()?.trim().takeUnless { it.isNullOrBlank() } ?: pkg

        val app = when {
            pkg.contains("whatsapp", ignoreCase = true) -> "WhatsApp"
            pkg.contains("telegram", ignoreCase = true) -> "Telegram"
            pkg.contains("instagram", ignoreCase = true) -> "Instagram"
            pkg.contains("messenger", ignoreCase = true) -> "Messenger"
            pkg.contains("sms", ignoreCase = true) || pkg.contains("messaging", ignoreCase = true) -> "SMS"
            else -> return // only chat apps are announced
        }
        newMessage.value = IncomingMessage(app, sender, text)
    }
}
