package com.example.schedulizer

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.fragment_settings)
            val frameLayout: FrameLayout = findViewById(R.id.SettingPage)
            frameLayout.setBackgroundColor(Color.DKGRAY)
        }

        fun onSwitchClick(view: View) {
            //    enable Dark Mode - disabled since it gives errors
            //    ConstraintLayout someLayout=(ConstraintLayout)view.findViewById(R.id.someLayout);
            //    someLayout.getBackground().setColorFilter(Color.parseColor("#1C1B1A"), PorterDuff.Mode.SRC_ATOP);
            setContentView(R.layout.fragment_settings)
            val frameLayout: FrameLayout = findViewById(R.id.SettingPage)
            frameLayout.setBackgroundColor(Color.DKGRAY)
        }

    }
}