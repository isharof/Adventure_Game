package com.example.adventuregame

import android.graphics.*
import java.time.Clock.offset

class Parois(var x1: Float, var y1: Float, var x2: Float, var y2: Float, var view: DrawingView) {
    val r = RectF(x1,y1,x2,y2)
    val paroisPaint = Paint()
    var dx = -1
    val dy = 0
    val paroisVitesse = 10

    fun draw(canvas: Canvas) {
        paroisPaint.color = Color.GREEN
        canvas.drawRect(r, paroisPaint)
    }

    fun setRect() {
        r.set(x1, y1, x2, y2)
    }

  /*  fun update(interval: Double) {
        var longueur = (interval * paroiVitesse).toFloat()
        r.offset(longueur, 0f)
    }*/



    /*fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0
        Personnage.update(interval)
        Parois.update(interval)
        Petitsmonstres.update(interval)

    }*/





}