package com.example.mediaplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var runnable: Runnable
    private var handler = Handler()
    private var index: Int? = null
    private lateinit var mediaPlayer: MediaPlayer

    private var music: List<Int> = listOf(R.raw.btr_if_i_ruled_the_world, R.raw.lady_gaga_poker_face, R.raw.the_weeknd_blinding_lights, R.raw.ariana_grande_34_35, R.raw.intelligence_for_violence, R.raw.alice_dj_better_off_alone, R.raw.g6)
    private var names: List<String> = listOf("Big Time Rush Feat. Iyaz - If I Ruled The World", "Lady Gaga – Poker Face", "The Weeknd - Blinding Lights", "Ariana Grande - 34+35", "Intelligence For Violence", "Alice DJ-Better Off Alone", "Far East Movement feat. The Cataracs- DEV-Like A G6")
    private var text: List<Int> = listOf(R.raw.if_i_ruled_the_world, R.raw.poker_face, R.raw.blinding_lights, R.raw.ag_34_35, R.raw.ifv, R.raw.boa, R.raw.g6lyrics)

    private val CHANNEL_ID = "Channel_id_Example_01"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()


        index = 0

        mediaPlayer = MediaPlayer.create(this, music[index!!])

        musicTitle.text = names[index!!]

        seekbar.progress = 0
        seekbar.max = mediaPlayer.duration

        play.setOnClickListener {
            play.setBackgroundResource(R.drawable.play);
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()

                sendNotification(
                        "Playing",
                        "Currently playing: " + names[index!!]
                )

                play.setImageResource(R.drawable.ic_baseline_pause_24)
            }else {
                mediaPlayer.pause()

                sendNotification(
                        "Pause",
                        "Player paused"
                )

                play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }

        }

        previous.setOnClickListener {

            previousSong()
        }


        next.setOnClickListener {
            nextSong()
        }

        lyrics.setOnClickListener {
            val intent = Intent(this, TextActivity::class.java).apply {
                putExtra("LYRIC", text[index!!])
            }
            startActivity(intent)
        }

        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, pos: Int, changed: Boolean) {
                if(changed) {
                    mediaPlayer.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        runnable = Runnable {
            seekbar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)

        mediaPlayer.setOnCompletionListener {
            nextSong()
        }


    }

    private fun nextSong() {
        mediaPlayer.stop()

        if (index == music.lastIndex) {
            index = 0

            mediaPlayer = MediaPlayer.create(this, music[index!!])
            musicTitle.text = names[index!!]

        } else {
            index = index!! +1

            mediaPlayer = MediaPlayer.create(this, music[index!!])
            musicTitle.text = names[index!!]

        }

        sendNotification(
                "Playing",
                "Currently playing: " + names[index!!]
        )

        mediaPlayer.start()
        play.setImageResource(R.drawable.ic_baseline_pause_24)

        seekbar.progress = 0
        seekbar.max = mediaPlayer.duration


        mediaPlayer.setOnCompletionListener {
            nextSong()
        }
    }

    private fun previousSong() {
        mediaPlayer.stop()

        if (index == 0) {
            index = music.lastIndex

            mediaPlayer = MediaPlayer.create(this, music[index!!])
            musicTitle.text = names[index!!]

        } else {
            index = index!! -1

            mediaPlayer = MediaPlayer.create(this, music[index!!])
            musicTitle.text = names[index!!]

        }

        sendNotification(
                "Playing",
                "Currently playing: " + names[index!!]
        )

        mediaPlayer.start()
        play.setImageResource(R.drawable.ic_baseline_pause_24)

        seekbar.progress = 0
        seekbar.max = mediaPlayer.duration


        mediaPlayer.setOnCompletionListener {
            nextSong()
        }
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun sendNotification(notificationTitle: String, notificationText: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher_foreground)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

}