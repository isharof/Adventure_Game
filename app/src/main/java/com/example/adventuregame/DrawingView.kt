package com.example.adventuregame

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import kotlin.random.Random

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback,Runnable {

    lateinit var canvas: Canvas
    val backgroundPaint = Paint()
    var drawing = false
    lateinit var thread: Thread
    lateinit var mainActivity: MainActivity
    var screenWidth = 0f
    var screenHeight = 0f
    var gameover = false
    private val activity = context as FragmentActivity

    val parois = arrayOf(
        Parois(0f, 0f, 0f, 0f, this), /*Sol*/
        Parois(0f, 0f, 0f, 0f, this), /*Terre*/
        Parois(0f, 0f, 0f, 0f, this),/*Nuage1*/
        Parois(0f, 0f, 0f, 0f, this),/*Nuage2*/
        Parois(0f, 0f, 0f, 0f, this),/*Nuage3*/
        Parois(0f, 0f, 0f, 0f, this))/*ScreenOut*/
    val personnage = arrayOf(Personnage(0f,0f,0f,0f,this,0), /*Dessin du perso principal*/
                            Personnage(0f,0f,0f,0f,this,0)) /*Dessin barre de vie*/
    private val recompense = Récompense(0f, 0f, 0f, 0f, this)

    private val mapview = Mapview(0f,0f,0f,0f,this)
    var lesmonstres = ArrayList<Monstres>()

    var balle = Balle(0f,0f,0f,0f,this)

    val sol = parois[0]
    private val terre = parois[1]
    private val nuage1 = parois[2]
    private val nuage2 = parois[3]
    private val nuage3 = parois[4]
    val screenout = parois[5]

    private val player = personnage[0]
    var barrevie = personnage[1]

    var life = 3
    var random = java.util.Random()
    var score = 0


    init {
        backgroundPaint.color = Color.rgb(0,255,249)
        /* couleur ciel */
    }


    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        /*la méthode onSizeChanged, qui a pour mission de réajuster les
         dimensions des graphiques en réaction aux manipulations faites sur l'application*/

        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()

        /* On génère aléatoirement les 3 types de monstres */
        val listemonstre = listOf(Grandsmonstres(2000f,screenHeight/2f + 275f,2100f,screenHeight/2f + 375f,this),
            Longsmonstres(2000f,screenHeight/2f + 210f,2050f,screenHeight/2f + 375f,this),
            Petitsmonstres(2000f,screenHeight/2f+300f,2050f,screenHeight/2f + 375f,this))
        val monstrerandom = listemonstre.random()

        /* Les valeurs ci-dessous ont été trouvé par essais-erreurs */

        mapview.drawsol(sol)     /*Dessin du sol : (épaisseur sol = 25f)*/
        mapview.drawterre(terre) /*Dessin de la Terre*/
        mapview.drawplayer(player)/* Dessin du personnage :(base personnage = 50f, hauteur = 50f)  */
        mapview.drawbarrevie(barrevie)/*Dessin de la barre de vie du personnage*/
        mapview.drawballe(balle)/*Dessin de la balle*/
        mapview.drawrecompense(recompense)/* Dessin recompense à la fin du jeu */
        mapview.drawnuage1(nuage1) /*Dessin du Nuage 1*/
        mapview.drawnuage2(nuage2)/*Dessin du Nuage 2*/
        mapview.drawnuage3(nuage3)/*Dessin du Nuage 3*/
        mapview.drawscreenout(screenout)/*Dessin Petit rectangle en dehors du téléphone*/

        /*Dessin monstre au hasard*/

        lesmonstres.add(monstrerandom)
        lesmonstres[0].setRect()


    }


    fun draw() {

        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(
                0f, 0f, canvas.width.toFloat(),
                canvas.height.toFloat(), backgroundPaint
            )
            /*var res: Resources = Resources.getSystem()
            var bitmap = BitmapFactory.decodeResource(res, R.drawable.background_jeu)*/

            sol.draw(canvas,0,255,14)
            terre.draw(canvas,103,41,11)
            nuage1.draw(canvas,255,255,255)
            nuage2.draw(canvas,255,255,255)
            nuage3.draw(canvas,255,255,255)
            screenout.draw(canvas,255,255,255)
            recompense.draw(canvas)
            /*if (!personnage.dead){}*/ player.draw(canvas,0,14,255)
            barrevie.draw(canvas,67,163,62)
            if(lesmonstres[0].MonstresOnScreen){lesmonstres[0].draw(canvas,255,0,0)}
            if(balle.BalleOnScreen){balle.draw(canvas,255,164,0)}
            holder.unlockCanvasAndPost(canvas)
        }
    }



    fun deplacementcontinue(){
        /*score()*/
        if (player.x2 < screenWidth / 2) {
            player.droite()
            barrevie.droite()
            player.x1 += 10f
            barrevie.x1 += 10f
            player.x2 += 10f
            barrevie.x2 += 10f

        }

        /*Déplacement des nuages et maps*/
        else if (player.x2 >= screenWidth / 2f ) {
            nuage1.x1 -= 20f
            nuage1.x2 -= 20f
            nuage2.x1 -= 20f
            nuage2.x2 -= 20f
            nuage3.x1 -= 20f
            nuage3.x2 -= 20f
            sol.x1 -= 10f
            sol.x2 -= 10f
            sol.deplacementmap()
            nuage1.deplacementmap()
            nuage2.deplacementmap()
            nuage3.deplacementmap()
            /*Déplacement monstres*/
            lesmonstres[0]
            lesmonstres[0].gauche()
            lesmonstres[0].x1 -= 10f
            lesmonstres[0].x2 -= 10f

                if ((lesmonstres[0].r.left == player.r.right && lesmonstres[0].r.top < player.r.top) && lesmonstres[0].MonstresOnScreen) { /*Si perso touche monstre*/
                    player.life -= 1
                    barrevie.x2 -= 50f/3
                    barrevie.setRect()
                    barrevie.draw(canvas,67,163,62)

                } else if (lesmonstres[0].r.top == player.r.bottom && lesmonstres[0].MonstresOnScreen) {
                    player.life -= 1
                    barrevie.x2 -= 50f/3
                    barrevie.setRect()
                    barrevie.draw(canvas,67,163,62)
                }
                if (player.life == 0) {
                    player.dead = true
                }}



            /*Demander AIDE assistant pour réduire code*/

            if (nuage3.x2 == 1700f) {
                nuage1.x1 = 1900f
                nuage1.y1 = 50f
                nuage1.x2 = 2600f
                nuage1.y2 = 100f
                nuage1.setRect()
                nuage1.draw(canvas, 255, 255, 255)

            } else if (nuage3.x2 == 800f) {
                nuage2.x1 = 1900f
                nuage2.y1 = 50f
                nuage2.x2 = 2600f
                nuage2.y2 = 100f
                nuage2.setRect()
                nuage2.draw(canvas, 255, 255, 255)
            } else if (nuage1.x1 == 100f) {
                nuage3.x1 = 1900f
                nuage3.y1 = 50f
                nuage3.x2 = 2600f
                nuage3.y2 = 100f
                nuage3.setRect()
            }

            if(lesmonstres[0].x2 == 0f ) {
                val listedemonstre = listOf(Grandsmonstres(screenWidth ,screenHeight/2f + 275f,screenWidth+100f,screenHeight/2f + 375f,this),
                    Longsmonstres(screenWidth,screenHeight/2f + 210f,screenWidth+50f,screenHeight/2f + 375f,this),
                    Petitsmonstres(screenWidth,screenHeight/2f+300f,screenWidth+50f,screenHeight/2f + 375f,this))
                lesmonstres[0].MonstresOnScreen = true

                lesmonstres.remove(lesmonstres[0])
                lesmonstres.add(listedemonstre.random())
            }

        }



    override fun run() {
        while (drawing) {
            draw()
            if (player.dead) {
                gameover()
                drawing = false
            }
        }

    }

    private fun score() {
        if(!lesmonstres[0].MonstresOnScreen){
            score += 2  // 1 monstre tué donne 110 point
        }
    }

    private fun gameover() {
        drawing = false
        showGameOverDialog(R.string.lose)
        gameover = true
    }

    private fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(resources.getString(messageId))
                builder.setMessage(
                    resources.getString(R.string.results_format, score)
                )
                builder.setPositiveButton(R.string.reset_game,
                    DialogInterface.OnClickListener { _, _->newGame() }
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev =
                    activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.isCancelable = false
                gameResult.show(ft,"dialog")
            }
        )
    }

    fun newGame() {
        drawing = true
        score = 0
        run()
        if (gameover) {
            gameover = false
            thread = Thread(this)
            thread.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = Thread(this)
        thread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.join()
    }
}
