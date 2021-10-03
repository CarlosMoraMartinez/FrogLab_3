package com.cmora.froglab_2.punnet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.cmora.froglab_2.R

class PunnetFragment : Fragment() {


    //var punnet_listener: PunnetListener? = null
    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QuestionListener) {
            punnet_listener = context as PunnetListener
        } else {
            throw RuntimeException(
                "$context must implement QuestionListener"
            )
        }
    }*/
    //var onTouchListener : PunnetOnTouchListener? = null
    lateinit var context_this: Context
    var vista: PunnetView? = null
    var vista2: ImageView? = null
    var vista_tubo: ImageView? = null
    var v:View? = null

    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflador.inflate(
            R.layout.punnet_fragment_layout,
            contenedor, false)
        //vista?.setOnTouchListener(PunnetOnTouchListener())
        vista = v?.findViewById(R.id.punnet_vista_juego)
        vista2 = v?.findViewById(R.id.hidden_image)
        vista_tubo = v?.findViewById(R.id.in_vitro)

        //vista_tubo?.setOnDragListener(vista?.dragListen)
        vista?.setIg(vista2)
        vista?.setVistaTubo(vista_tubo)
        return v
    }

    fun setProblemInstanceToView(instance:PunnetProblemInstance){
        vista?.setProblemInstance(instance)
    }
    override fun onAttach(context: Context) {
        Log.d("PunnetFragment", "onAttach 1")
        super.onAttach(context)
        context_this = context

    }

    fun setListener(listener: PunnetListener){
        Log.d("PunnetFragment", "setListener 1")
        vista?.setPunnetListener(listener)
        Log.d("PunnetFragment", "setListener 2")
    }

    override fun onStart() {
        super.onStart()
        //vista?.thread?.reanudar()
    }

    override fun onPause() {
        super.onPause()
        vista?.stopGametes()
        //vista?.thread?.pausar()
    }

    override fun onStop() {
        super.onStop()
        vista?.stopGametes()
        //vista?.thread?.detener()
    }

    override fun onDestroyView() {
        vista?.stopGametes()
        super.onDestroyView()
    }

    override fun onDestroy() {
        vista?.stopGametes()
        super.onDestroy()
    }


}