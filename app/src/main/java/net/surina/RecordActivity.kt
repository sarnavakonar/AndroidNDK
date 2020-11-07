package net.surina

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.squti.androidwaverecorder.WaveRecorder
import com.sarnava.chipmunk.R
import kotlinx.android.synthetic.main.activity_record.*
import net.surina.soundtouch.SoundTouch

class RecordActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private lateinit var waveRecorder: WaveRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        startTimer()
        startRecording()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
    }

    private fun startTimer(){
        timer = object : CountDownTimer(10000, 25) {
            override fun onTick(millisUntilFinished: Long) {
                rv.updateTimeText(millisUntilFinished)
            }
            override fun onFinish() {
                waveRecorder.stopRecording()
                process()
            }
        }
        timer.start()
    }

    private fun startRecording(){
        waveRecorder = WaveRecorder(input)
        waveRecorder.startRecording()
    }

    /// process a file with SoundTouch. Do the processing using a background processing
    /// task to avoid hanging of the UI
    private fun process() {
        try {
            //start SoundTouch processing in a background thread
            ProcessTask().execute()
        } catch (exp: java.lang.Exception) {
            exp.printStackTrace()
        }
    }

    inner class ProcessTask() : AsyncTask<Void, Void, Long>() {

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            if(result == 0L){
                startActivity(Intent(this@RecordActivity, PlayActivity::class.java))
                finish()
            }
        }

        /// Function that does the SoundTouch processing
        private fun doSoundTouchProcessing(): Long {
            val st = SoundTouch()
            st.setTempo(1f)
            st.setPitchSemiTones(6f)
            Log.e("lalalala", "process file " + input)
            val startTime = System.currentTimeMillis()
            val res = st.processFile(input, output)
            val endTime = System.currentTimeMillis()
            val duration = (endTime - startTime) * 0.001f
            Log.e("lalalala","Processing done, duration $duration sec.")
            if (res != 0) {
                val err = SoundTouch.getErrorString()
                Log.e("lalalala","Failure: $err")
                return -1L
            }
            return 0L
        }

        override fun doInBackground(vararg params: Void?): Long {
            return doSoundTouchProcessing()
        }
    }

    companion object{
        var input: String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording_input.wav"
        var output: String = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording_output.mp3"
    }
}