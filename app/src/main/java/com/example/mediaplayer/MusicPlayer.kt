package com.example.mediaplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import com.example.mediaplayer.databinding.ActivityMusicPlayerBinding

class MusicPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityMusicPlayerBinding
    private lateinit var mediaPlayer: MediaPlayer
    var totalTime: Int= 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mediaPlayer= MediaPlayer.create(this, R.raw.song)
        totalTime= mediaPlayer.duration

        binding.pauseId.setOnClickListener {
            mediaPlayer.pause()
        }

        binding.playId.setOnClickListener {
            mediaPlayer.start()
        }

        binding.stopId.setOnClickListener {
            mediaPlayer.stop()

        }
//this is the code for seekbar
        binding.seekBar.max= totalTime
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        //change the seekbar position based on the music
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress= mediaPlayer.currentPosition
                    Handler().postDelayed(this, 1000)
                } catch (exception: java.lang.Exception){
                    binding.seekBar.progress= 0
                }

            }

        }, 0)
    }
}