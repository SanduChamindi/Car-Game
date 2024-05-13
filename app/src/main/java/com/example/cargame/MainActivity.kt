package com.example.cargame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var score: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("HighScore", Context.MODE_PRIVATE)
        val highScore = sharedPreferences.getInt("highScore", 0)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        mGameView = GameView(this, this)

        score.text = "High Score: $highScore"

        startBtn.setOnClickListener {
            mGameView = GameView(this, this)  // Create a new GameView each time
            mGameView.setBackgroundResource(R.drawable.road)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            score.visibility = View.GONE
        }
    }

    override fun closeGame(mScore: Int) {
        val highScore = sharedPreferences.getInt("highScore", 0)
        val message = if (mScore > highScore) {
            // If the current score is higher than the high score, update the high score
            with(sharedPreferences.edit()) {
                putInt("highScore", mScore)
                apply()
            }
            "New High Score: $mScore\nYou are exellent"
        } else {
            "High Score: $highScore\nYour Score: $mScore"
        }
        // Show the message on the score TextView
        score.text = message

        // Remove the game view and make the start button and score visible again
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
    }
}
