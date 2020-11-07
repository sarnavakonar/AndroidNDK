package net.surina

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sarnava.chipmunk.R
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        lav.setAnimation("lottie_playback.json")
        lav.playAnimation()
        lav.loop(true)
    }

    private fun playWavFile(fileName: String?) {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer!!.setDataSource(fileName)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            Toast.makeText(this,
                "Playing...",
                Toast.LENGTH_SHORT
            ).show()
            mediaPlayer!!.setOnCompletionListener {
                val i = Intent(this, StartActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
            }
        } catch (e: Exception) {
            Log.e("lalalala", e.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        playWavFile(RecordActivity.output)
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.stop()
        mediaPlayer = null
    }
}