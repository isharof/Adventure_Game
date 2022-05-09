package com.example.adventuregame

import android.content.res.Resources
import android.graphics.*
import java.util.*
import kotlin.concurrent.schedule


class Personnage(var x1: Float, var y1: Float, var x2: Float, var y2: Float,var view: DrawingView,var life : Int) {

    val r = RectF(x1, y1, x2, y2)
    private val PersonnagePaint = Paint()
    var dx = 1
    var dy = 0
    private var random = Random()
    var color = Color.argb(255, random.nextInt(256),
        random.nextInt(256), random.nextInt(256))
    lateinit var drawingView: DrawingView
    lateinit var monstres : Monstres
    var dead = false
    lateinit var mainActivity: MainActivity
    var res: Resources = Resources.getSystem()
    var bitmap = BitmapFactory.decodeResource(res, R.drawable.background_jeu)


    /* Dessin du personnage représenté par un rectangle*/
    fun draw(canvas: Canvas,red : Int,green : Int, blue : Int) {
        PersonnagePaint.color = Color.rgb(red,green,blue)
        canvas.drawRect(r, PersonnagePaint)

    }

    fun setRect() {
        r.set(x1, y1, x2, y2)
    }


    /* le personnage se déplace vers le haut et passé un certain délai il redescend. Ce délai est
    * choisi de manière à rendre le déplacement fluide */
    fun saute() {
        dy = -2
        r.offset(0F*dx, 100.0F*dy)
        Timer("SettingUp", false).schedule(300) {r.offset(0F*dx, -100.0F*dy) }

    }

    fun droite() {
        dx = 1
        r.offset(10.0F*dx,0.0F*dy)
    }


}

