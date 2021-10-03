package com.cmora.froglab_2.tools

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
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
import com.cmora.froglab_2.bastard.QuestionListener
import java.nio.file.WatchEvent
import kotlin.concurrent.timer


class TimerFragment : Fragment(){
    private var mTextView: TextView? = null
    private var mImageView: ImageView? = null
    var DEFAULT_SECONDS = 11
    var WARNING_SECONDS = 5
    val FINAL_TIME = -1
    private var seconds:Int = DEFAULT_SECONDS
    private var red_seconds:Int = WARNING_SECONDS
    var animation: Animation? = null
    var animation2: Animation? = null
    var animation3: Animation? = null
    var timerListener: TimerListener? = null
    var mp:MediaPlayer? = null
    var time_set: Int = -1 //for timer soundpool
    var time_end: Int = -1
    var soundPool: SoundPool? = null

    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vista = inflador.inflate(
            R.layout.timer_fragment,
            contenedor, false)
        mTextView = vista.findViewById(R.id.timer_text) as TextView?
        mTextView?.setText(seconds.toString())
        mImageView = vista.findViewById(R.id.ic_timer) as ImageView?
        contenedor?.setClipChildren(false);
        contenedor?.setClipToPadding(false);
        animation = AnimationUtils.loadAnimation(context, R.anim.timer_warning)
        animation2 = AnimationUtils.loadAnimation(context, R.anim.timer_decrease)
        animation3 = AnimationUtils.loadAnimation(context, R.anim.timer_end)
        mp = MediaPlayer.create(context, R.raw.timer_countdown)
        soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        time_set = soundPool!!.load(context, R.raw.timer_start, 0)
        time_end = soundPool!!.load(context, R.raw.timer_final, 0)
        Log.d("CARLOS", "TimerFragment")

        return vista
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var vg: ViewGroup? = view as ViewGroup
        while (vg != null) {
            vg.clipChildren = false
            vg.clipToPadding = false
            vg = if (vg.parent is ViewGroup) vg.parent as ViewGroup else null
        }
    }
    fun decreaseTimer(){
        Log.d("TimerFragment", "decreaseTimer, time=$seconds")
        seconds -=1
        if(context == null){
            Log.d("TimerFragment", "CONTEXT IS NULL AT decreaseTimer, time=$seconds")
            return
        }
        if(seconds > FINAL_TIME) {
            mTextView?.setText(seconds.toString())
            if(seconds == red_seconds){
                mp?.start()
            }
            if (seconds <= red_seconds) {
                mTextView?.setTextColor(resources.getColor(R.color.colorWrong))
                mTextView?.bringToFront()
                mTextView?.startAnimation(animation)
            } else {
                mTextView?.startAnimation(animation2)
            }
            timerListener?.onTimeDecrease()
        }else{
            Log.d("TimerFragment", " seconds is $FINAL_TIME")
            animation3?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation:Animation) {
                    //mTextView?.setText(seconds.toString())
                    mp?.pause()
                    mp?.seekTo(0)
                    timerListener?.onTimeLag()
                    soundPool!!.play(time_end, 1f, 1f, 1, 0, 1f)
                }
                override fun onAnimationRepeat(animation:Animation) {}
                override fun onAnimationEnd(animation:Animation) {
                    Log.d("TimerFragment", " Final animation Ends")
                    timerListener?.onTimeEnds()
                }
            })
            mImageView?.startAnimation(animation3)
        }
    }
    fun stopTimer(){
        if(mp?.isPlaying!!) {
            mp?.pause()
            mp?.seekTo(0)
        }
    }
    fun setTimer(time : Int = DEFAULT_SECONDS, warningtime : Int = WARNING_SECONDS){
        seconds = time + 1
        red_seconds = warningtime
        if(mp?.isPlaying!!) {
            mp?.pause()
            mp?.seekTo(0)
        }
        soundPool!!.play(time_set, 1f, 1f, 1, 0, 1f)
        mTextView?.setText(seconds.toString())
        mTextView?.setTextColor(resources.getColor( R.color.colorPoints))
    }
    fun getTimerState():Int{
        return seconds
    }

    override fun onPause() {
        super.onPause()
        mp?.pause()
        mp?.seekTo(0)
    }
    override fun onDetach() {
        super.onDetach()
        mp?.pause()
        mp?.seekTo(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.pause()
        mp?.seekTo(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mp?.pause()
        mp?.seekTo(0)
    }
}