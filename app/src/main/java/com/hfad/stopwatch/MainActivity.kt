package com.hfad.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.hfad.stopwatch.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
    private var running = false  // is the stopwatch running?
    private var offset: Long = 0 // the base offset for the stopwatch

    //Add key strings for use with the bundle
    private val OFFSET_KEY = "offset"
    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        //Restore the previous state
        if (savedInstanceState !=null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running){
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else {
                setBaseTime()
            }
        }

        // the start button start the stopwatch if it's not running

       binding.startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                binding.stopwatch.start()
                running = true
            }
        }
          // the pause button pauses  the stopwatch if it's running

        binding.pauseButton.setOnClickListener {
            if (running) {
                saveOffset()
                binding.stopwatch.stop()
                running = false
            }
        }

        //The reset sets the offset and stopwatch to 0
        binding.resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    //Update the stopwatch.base time allowing for any offset
    private fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }
    //record the offset
    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        if (running){
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (running) {
            setBaseTime()
            binding.stopwatch.start()
            offset = 0
        }
    }

}