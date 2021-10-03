package com.cmora.froglab_2.tools

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cmora.froglab_2.R

class PointsFragment : Fragment(){
    var points = 0
    private var mTextView: TextView? = null
    private var mImageView: ImageView? = null
    private var animation_text: Animation? = null
    private var pref: SharedPreferences? = null
    object Constants {
        const val POINTS_BASTARD = 0;
        const val POINTS_PUNNET = 1;
    }
    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vista = inflador.inflate(
            R.layout.points_fragment,
            contenedor, false)
        contenedor?.setClipChildren(false);
        contenedor?.setClipToPadding(false);
        (vista.findViewById(R.id.dnapoints_text) as TextView).setText(points.toString())
        mTextView = vista.findViewById(R.id.dnapoints_text) as TextView?
        mImageView = vista.findViewById(R.id.dnapoints_icon) as ImageView?
        animation_text = AnimationUtils.loadAnimation(context, R.anim.timer_decrease)

        pref = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("PointsFragment", "PointsFragment")
        return vista
    }

    fun addPoints(p:Int, tipo:Int=Constants.POINTS_BASTARD){
        points += p
        Log.d("PointsFragment", "PointsFragment: Adding points $p")
        var animacion: Animation
        if(p > 0){
            animacion = AnimationUtils.loadAnimation(context, R.anim.points_increase)
        }else{
            animacion = AnimationUtils.loadAnimation(context, R.anim.points_decrease)
        }
        mImageView?.startAnimation(animacion)
        mTextView?.startAnimation(animation_text)
        mTextView?.setText(points.toString())

        var totalp:Int = p
        when(tipo){
            Constants.POINTS_BASTARD -> {
                totalp += pref?.getInt("points_bastard", 0)!!
                pref?.edit()?.putInt("points_bastard", totalp)?.commit()
            }
            Constants.POINTS_PUNNET ->{
                totalp += pref?.getInt("points_punnet", 0)!!
                pref?.edit()?.putInt("points_punnet", totalp)?.commit()
            }
        }
    }

}