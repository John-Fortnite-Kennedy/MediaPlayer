package com.example.mediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_text.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

class TextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        val lyric = intent.getIntExtra("LYRIC", 0)


        val inputStream: InputStream = this.resources.openRawResource(lyric)
        val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(inputStream))

        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null

        while ({text = bufferedReader.readLine(); text}() != null) {
            stringBuilder.append(text).append("\n")
        }

        place.setText(stringBuilder.toString()).toString()
    }
}