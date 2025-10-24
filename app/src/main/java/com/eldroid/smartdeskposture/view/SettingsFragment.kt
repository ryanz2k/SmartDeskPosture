package com.eldroid.smartdeskposture.view

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.eldroid.smartdeskposture.EditProfileActivity
import com.eldroid.smartdeskposture.LoginActivity
import com.eldroid.smartdeskposture.R
import com.eldroid.smartdeskposture.UpdatePasswordActivity
import com.eldroid.smartdeskposture.WelcomePageActivity
import com.eldroid.smartdeskposture.data.UserDataManager

class SettingsFragment : Fragment() {

    private lateinit var switchPostureAlerts: Switch
    private lateinit var switchSoundAlert: Switch
    private lateinit var switchVibrationAlert: Switch
    private lateinit var switchDarkMode: Switch
    private lateinit var spinnerReminderTime: Spinner
    private lateinit var spinnerThemeColor: Spinner
    private lateinit var btnSaveSettings: Button
    private lateinit var btnResetSettings: Button
    private lateinit var btnConnectSensor: Button
    private lateinit var btnCalibrateSensor: Button
    private lateinit var tvEditProfile: TextView
    private lateinit var tvUpdatePassword: TextView
    private lateinit var btnLogout: Button

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize SharedPreferences
        sharedPref = requireActivity().getSharedPreferences("AppSettingsPrefs", 0)

        // Initialize views
        switchPostureAlerts = view.findViewById(R.id.switchPostureAlerts)
        switchSoundAlert = view.findViewById(R.id.switchSoundAlert)
        switchVibrationAlert = view.findViewById(R.id.switchVibrationAlert)
        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        spinnerReminderTime = view.findViewById(R.id.spinnerReminderTime)
        spinnerThemeColor = view.findViewById(R.id.spinnerThemeColor)
        btnSaveSettings = view.findViewById(R.id.btnSaveSettings)
        btnResetSettings = view.findViewById(R.id.btnResetSettings)
        btnConnectSensor = view.findViewById(R.id.btnConnectSensor)
        btnCalibrateSensor = view.findViewById(R.id.btnCalibrateSensor)
        tvEditProfile = view.findViewById(R.id.tvEditProfile)
        tvUpdatePassword = view.findViewById(R.id.tvUpdatePassword)
        btnLogout = view.findViewById(R.id.btnLogout)

        setupSpinners()
        setupButtons()
        setupDarkModeSwitch()

        return view
    }

    private fun setupSpinners() {
        val reminderOptions = arrayOf("15 minutes", "30 minutes", "1 hour", "2 hours")
        spinnerReminderTime.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, reminderOptions)

        val colorOptions = arrayOf("Default Blue", "Purple", "Teal", "Orange")
        spinnerThemeColor.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, colorOptions)
    }

    private fun setupButtons() {
        btnConnectSensor.setOnClickListener {
            Toast.makeText(requireContext(), "Connecting to Ultrasonic Sensor...", Toast.LENGTH_SHORT).show()
        }

        btnCalibrateSensor.setOnClickListener {
            Toast.makeText(requireContext(), "Calibrating sensor. Please wait...", Toast.LENGTH_SHORT).show()
        }

        btnSaveSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings saved successfully!", Toast.LENGTH_SHORT).show()
        }

        btnResetSettings.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Reset Settings")
                .setMessage("Are you sure you want to reset all settings to default?")
                .setPositiveButton("Yes") { _, _ ->
                    resetSettings()
                    Toast.makeText(requireContext(), "Settings reset to default.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        tvEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        tvUpdatePassword.setOnClickListener {
            startActivity(Intent(requireContext(), UpdatePasswordActivity::class.java))
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupDarkModeSwitch() {
        // Load saved preference
        val isDarkModeEnabled = sharedPref.getBoolean("DarkMode", false)
        switchDarkMode.isChecked = isDarkModeEnabled

        // Apply theme when fragment loads
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        // Listen for switch changes
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            editor.putBoolean("DarkMode", isChecked)
            editor.apply()

            // Apply dark or light theme immediately
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun resetSettings() {
        switchPostureAlerts.isChecked = false
        switchSoundAlert.isChecked = false
        switchVibrationAlert.isChecked = false
        switchDarkMode.isChecked = false
        spinnerReminderTime.setSelection(0)
        spinnerThemeColor.setSelection(0)

        val editor = sharedPref.edit()
        editor.clear().apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun showLogoutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnLogoutDialog = dialogView.findViewById<Button>(R.id.btnLogout)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnLogoutDialog.setOnClickListener {
            // Clear user session using UserDataManager
            UserDataManager.clearUser()

            // Navigate to WelcomePageActivity
            val intent = Intent(requireActivity(), WelcomePageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            alertDialog.dismiss()
        }
    }
}
