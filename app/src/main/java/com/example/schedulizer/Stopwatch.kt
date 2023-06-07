package com.example.schedulizer

import android.os.Handler

class Stopwatch(private val onUpdate: (Long) -> Unit) {

    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var handler: Handler = Handler()
    private var isRunning = false

    private val tickRunnable = object : Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            elapsedTime += currentTime - startTime
            startTime = currentTime
            onUpdate(elapsedTime)
            handler.postDelayed(this, INTERVAL)
        }
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            startTime = System.currentTimeMillis()
            handler.postDelayed(tickRunnable, INTERVAL)
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(tickRunnable)
        }
    }

    fun reset() {
        elapsedTime = 0
        onUpdate(elapsedTime)
    }

    companion object {
        private const val INTERVAL: Long = 100 // update interval in milliseconds
    }
}