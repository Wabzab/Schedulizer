package com.example.schedulizer

import android.graphics.Color
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Switch
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import android.content.Context
import android.widget.Button
import android.widget.TextView
import kotlin.math.min


class SettingsFragment : Fragment(R.layout.fragment_settings) {
lateinit var btnMinPlus: Button
lateinit var btnMinMinus: Button
lateinit var btnMaxPlus: Button
lateinit var btnMaxMinus: Button
lateinit var txtMinText: TextView
lateinit var txtMaxText: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val frameLayout: FrameLayout = view.findViewById(R.id.SettingPage)
        val switchDarkMode: Switch = view.findViewById(R.id.switchDarkMode)
        val switchMuteSound: Switch = view.findViewById(R.id.switchMuteSound)
        var isButtonMute: Boolean = true
        var minHours = 2
        var maxHours = 4
        btnMinPlus = view.findViewById(R.id.btnMinPlus)
        btnMinMinus = view.findViewById(R.id.btnMinMinus)
        btnMaxPlus = view.findViewById(R.id.btnMaxPlus)
        btnMaxMinus = view.findViewById(R.id.btnMaxMinus)
        txtMinText = view.findViewById(R.id.txtMinText)
        txtMaxText = view.findViewById(R.id.txtMaxText)
        lateinit var audioManager: AudioManager

        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager


        switchDarkMode.setOnClickListener {
            frameLayout.setBackgroundColor(Color.DKGRAY)
        }

        btnMinPlus.setOnClickListener {
            minHours = minHours + 1
           txtMinText.text = "Min: $minHours"
        }
        btnMinMinus.setOnClickListener {
            minHours = minHours - 1
            txtMinText.text = "Min: $minHours"
        }
        btnMaxPlus.setOnClickListener {
            maxHours = maxHours + 1
            txtMaxText.text = "Max: $maxHours"
        }
        btnMaxMinus.setOnClickListener {
            maxHours = maxHours - 1
            txtMaxText.text = "Max: $maxHours"
        }

        switchMuteSound.setOnClickListener {
            if (isButtonMute) {
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_NOTIFICATION,
                    AudioManager.ADJUST_MUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_ALARM,
                    AudioManager.ADJUST_MUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_MUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_RING,
                    AudioManager.ADJUST_MUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_SYSTEM,
                    AudioManager.ADJUST_MUTE,
                    0
                );
                isButtonMute = false
            } else {
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_NOTIFICATION,
                    AudioManager.ADJUST_UNMUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_ALARM,
                    AudioManager.ADJUST_UNMUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_UNMUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_RING,
                    AudioManager.ADJUST_UNMUTE,
                    0
                );
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_SYSTEM,
                    AudioManager.ADJUST_UNMUTE,
                    0
                );
                isButtonMute = true
            }
        }

    }
}