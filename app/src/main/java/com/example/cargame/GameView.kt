package com.example.cargame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c: Context, var gameTask: GameTask) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myCarPosition = 0
    private val otherCars = ArrayList<HashMap<String, Any>>()  // List to store information about other cars

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    // Method called when the view needs to be drawn
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        // Generating random other cars
        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..3).random() // Generate random lane from 0 to 3
            map["startTime"] = time
            otherCars.add(map)
        }
        time += 10 + speed
        val carWidth = viewWidth / 6 // Car width
        val carHeight = carWidth + 10 // Car height
        myPaint!!.style = Paint.Style.FILL

        // Drawing player's car
        val d = resources.getDrawable(R.drawable.car2, null)

        d.setBounds(
            myCarPosition * viewWidth / 4 + viewWidth / 20 + 25, // Adjusted for four lanes
            viewHeight - 2 - carHeight,
            myCarPosition * viewWidth / 4 + viewWidth / 20 + carWidth - 25, // Adjusted for four lanes
            viewHeight - 2
        )
        d.draw(canvas!!)

        // Drawing other cars and checking for collisions
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for (i in otherCars.indices) {
            try {
                val carX =
                    otherCars[i]["lane"] as Int * viewWidth / 4 + viewWidth / 20 // Adjusted for four lanes
                var carY = time - otherCars[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.car1, null)

                d2.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                d2.draw(canvas)

                // Checking for collisions with player's car
                if (otherCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > viewHeight - 2 - carHeight
                        && carY < viewHeight - 2
                    ) {
                        gameTask.closeGame(score)
                    }
                }

                // Removing cars that have passed the player's car
                if (carY > viewHeight + carHeight) {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highScore) {
                        highScore = score
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 3) { // Update condition to 3 for four lanes
                        myCarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }
}
