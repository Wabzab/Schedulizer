package com.example.schedulizer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ActivitiesFragment : Fragment(R.layout.fragment_activities)
{

    lateinit var btnAddTimer: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var mainActivity: MainActivity
   // private lateinit var adapter: TimerAdapter

    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var timerTextView: TextView

    private lateinit var stopwatch: Stopwatch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)
        timerTextView = view.findViewById(R.id.timerTextView)

        stopwatch = Stopwatch { elapsedTime ->
            timerTextView.text = formatTime(elapsedTime)
        }

        startButton.setOnClickListener {
            stopwatch.start()
        }

        stopButton.setOnClickListener {
            stopwatch.stop()
        }

        resetButton.setOnClickListener {
            stopwatch.reset()
        }
    }

    private fun formatTime(timeInMillis: Long): String {
        val seconds = timeInMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
    }



}