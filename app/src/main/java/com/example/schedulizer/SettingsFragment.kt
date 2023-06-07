package com.example.schedulizer

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val frameLayout: FrameLayout = view.findViewById(R.id.SettingPage)
        val switchDarkMode: Switch = view.findViewById(R.id.switchDarkMode)

        switchDarkMode.setOnClickListener {
            frameLayout.setBackgroundColor(Color.DKGRAY)
        }
    }
}