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



class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val frameLayout: FrameLayout = view.findViewById(R.id.SettingPage)
        val switchDarkMode: Switch = view.findViewById(R.id.switchDarkMode)
        val switchMuteSound: Switch = view.findViewById(R.id.switchMuteSound)
        var isButtonMute: Boolean = true
        lateinit var audioManager: AudioManager
        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager


        switchDarkMode.setOnClickListener {
            frameLayout.setBackgroundColor(Color.DKGRAY)
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