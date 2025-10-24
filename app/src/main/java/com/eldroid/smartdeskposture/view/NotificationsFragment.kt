package com.eldroid.smartdeskposture.view

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eldroid.smartdeskposture.R

class NotificationsFragment : Fragment() {

    private lateinit var notificationContainer: LinearLayout
    private lateinit var btnMarkRead: Button
    private lateinit var btnClearAll: Button

    private val notifications = mutableListOf(
        NotificationData("Bad Posture Detected",
            "You’ve been leaning forward for 5 minutes. Sit upright to avoid back strain.",
            "10m ago", false),
        NotificationData("Stretch Reminder",
            "It’s been an hour since your last stretch. Take a quick break!",
            "1h ago", false),
        NotificationData("Daily Posture Report",
            "Your posture improved by 15% compared to yesterday. Keep it up!",
            "3h ago", false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        notificationContainer = view.findViewById(R.id.notificationContainer)
        btnMarkRead = view.findViewById(R.id.btnMarkRead)
        btnClearAll = view.findViewById(R.id.btnClearAll)

        displayNotifications(inflater)

        btnMarkRead.setOnClickListener {
            notifications.forEach { it.isRead = true }
            displayNotifications(inflater)
        }

        btnClearAll.setOnClickListener {
            notifications.clear()
            notificationContainer.removeAllViews()
        }

        return view
    }

    private fun displayNotifications(inflater: LayoutInflater) {
        notificationContainer.removeAllViews()

        for (notif in notifications) {
            val card = inflater.inflate(R.layout.item_notification, notificationContainer, false)

            val icon = card.findViewById<ImageView>(R.id.icon)
            val title = card.findViewById<TextView>(R.id.title)
            val message = card.findViewById<TextView>(R.id.message)
            val time = card.findViewById<TextView>(R.id.time)

            title.text = notif.title
            message.text = notif.message
            time.text = notif.time

            if (notif.isRead) {
                title.setTypeface(null, Typeface.NORMAL)
                message.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
                icon.setImageResource(android.R.drawable.checkbox_on_background)
            } else {
                title.setTypeface(null, Typeface.BOLD)
                message.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                icon.setImageResource(android.R.drawable.ic_dialog_alert)
            }

            notificationContainer.addView(card)
        }
    }

    data class NotificationData(
        val title: String,
        val message: String,
        val time: String,
        var isRead: Boolean
    )
}
