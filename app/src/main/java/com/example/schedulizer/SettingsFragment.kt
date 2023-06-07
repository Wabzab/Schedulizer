package com.example.schedulizer

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    fun onSwitchClick(view: View) {
        //    enable Dark Mode - disabled since it gives errors
        //    ConstraintLayout someLayout=(ConstraintLayout)view.findViewById(R.id.someLayout);
        //    someLayout.getBackground().setColorFilter(Color.parseColor("#1C1B1A"), PorterDuff.Mode.SRC_ATOP);
    }

}