package com.cmora.froglab_2.bastard

import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.cmora.froglab_2.laboratory_use_case.Juego
import com.cmora.froglab_2.R
import com.cmora.froglab_2.genetics.GenomeInitializer.Companion.buildGenomeFromJSON
import com.cmora.froglab_2.genetics.GenomeModel
import com.cmora.froglab_2.tools.*
import kotlin.random.Random.Default.nextInt

class FindTheBastardActivity : AppCompatActivity(), QuestionListener, TimerListener {

    var options_fragment: OptionsFragment? = OptionsFragment()
    var question_fragment: QuestionFragment? = QuestionFragment()
    var points_fragment: PointsFragment? = PointsFragment()
    var time_fragment: TimerFragment? = TimerFragment()
    var head_fragment : HeaderFragment? = null
    private val DEFAULT_GENOME = "genome1"
    private var db_genome_name: String? = DEFAULT_GENOME
    private var genome: GenomeModel? = null
    private val all_problemtypes : MutableList<String> = mutableListOf(
        "bastard_question_type1",
        "bastard_question_type2",
        "bastard_question_type3",
        "bastard_question_type4",
        "bastard_question_type5",
        "bastard_question_type6",
        "bastard_question_type7"
    )
    //private val all_problemtypes : MutableList<String> = mutableListOf("bastard_question_type5")
    var soundPool: SoundPool? = null
    var problem_type:BastardProblemType? = null
    var problem_instance:BastardProblemInstance? = null
    var right_answer: Int = -1
    var wrong_answer: Int = -1
    val QUESTION_TIME = 20
    val WARNING_TIME = 10
    var handler: Handler = Handler()
    var run: Runnable? = null

    //var time_set: Int = -1 //for timer soundpool
    //var time_end: Int = -1
    //var mp: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BASTARD_MAIN", "Start OnCreate")
        setContentView(R.layout.bastard_main_layout)
        options_fragment =
            supportFragmentManager.findFragmentById(R.id.bastard_fragment_options_main) as OptionsFragment?

        head_fragment = supportFragmentManager.findFragmentById(R.id.bastard_fragment_header_main) as HeaderFragment?
        if(head_fragment == null) {
            Log.d("BASTARD_MAIN", "head fragment null ")
        }
        points_fragment = head_fragment?.childFragmentManager?.findFragmentById(R.id.points_fragment_header) as PointsFragment
        time_fragment = head_fragment?.childFragmentManager?.findFragmentById(R.id.timer_fragment_header) as TimerFragment
        time_fragment?.timerListener = this
        //supportFragmentManager.findFragmentById(R.id.points_fragment_header) as PointsFragment?

        if(points_fragment == null) {
            Log.d("BASTARD_MAIN", "points fragment null ")
        }
        soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        right_answer = soundPool!!.load(this, R.raw.right_answer, 0)
        wrong_answer = soundPool!!.load(this, R.raw.wrong_answer, 0)

        //time_set = soundPool!!.load(this, R.raw.timer_start, 0)
        //time_end = soundPool!!.load(this, R.raw.timer_final, 0)
        //mp = MediaPlayer.create(this, R.raw.timer_countdown)
        //mp?.prepare()

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        db_genome_name = pref.getString("genome", DEFAULT_GENOME)
        if (db_genome_name == null) {
            db_genome_name = DEFAULT_GENOME
            Log.d("BASTARD_MAIN", "Genome is null, using default genome")
        }
        Log.d("BASTARD_MAIN", "Genome to load: $db_genome_name")
        val genome_resource2 =
            resources.getIdentifier(db_genome_name, "raw", packageName)
        val JSONFileInputStream2 =
            resources.openRawResource(genome_resource2) //R.raw.genome1

        Log.d("BASTARD_MAIN", "Loading genome $db_genome_name")
        //InputStream JSONFileInputStream2 = getResources().openRawResource(R.raw.genome1);
        //InputStream JSONFileInputStream2 = getResources().openRawResource(R.raw.genome1);
        this.genome =
            buildGenomeFromJSON(Juego.readTextFile(JSONFileInputStream2))
        Log.d("BASTARD_MAIN", "Loaded genome $db_genome_name")

        //val fm: FragmentManager = supportFragmentManager
        //fm.beginTransaction().replace(R.id.bastard_fragment_question_main, question_fragment).commit()
        //fm.beginTransaction().replace(R.id.bastard_fragment_options_main, options_fragment).commit()

        Log.d("BASTARD_MAIN", "End OnCreate")
    }

    override fun onStart() {
        super.onStart()
        startGame()
    }

    fun startGame(){
            Log.d("BASTARD_MAIN", "StartGame 1")
            var id = nextInt(0, all_problemtypes.size)
            handler.removeCallbacks(run)
            Log.d("BASTARD_MAIN", "Current Problem: " + Integer.toString(id))
            Toast.makeText(
                this,
                "Problem selected: " + all_problemtypes.get(id),
                Toast.LENGTH_LONG
            )
            id = resources.getIdentifier(all_problemtypes.get(id), "raw", packageName)
            val JSONFileInputStream = resources.openRawResource(id)
            problem_type = BastardProblemInitializer.buildProblemTypeFromJSON(
                Juego.readTextFile(JSONFileInputStream), genome!!
            )
            problem_instance = problem_type?.getProblemInstance()
            Log.d("BASTARD_MAIN", "problem instance: $problem_instance")
        putQuestion()
        putOptions()
        options_fragment?.activateCards()
        setTimer()
    }
    fun putQuestion(){
        question_fragment =
            supportFragmentManager.findFragmentById(R.id.bastard_fragment_question_main) as QuestionFragment?
            question_fragment!!.problem_instance = problem_instance!!
            question_fragment!!.setFrogs()
    }
    fun putOptions(){
        options_fragment =
            supportFragmentManager.findFragmentById(R.id.bastard_fragment_options_main) as OptionsFragment?
            options_fragment!!.problem_instance = problem_instance!!
            options_fragment!!.setOptions()
    }
    fun setTimer(){
        //soundPool?.play(time_set, 1f, 1f, 1, 0, 1f)
        time_fragment?.setTimer(QUESTION_TIME, WARNING_TIME)
        handler = Handler()
        run = object : Runnable {
            var running = true
            override fun run() {
                if(running) {
                    time_fragment?.decreaseTimer()
                    if(time_fragment?.getTimerState() == time_fragment?.FINAL_TIME)
                        running = false
                    handler.postDelayed(this, 1000)// 1 second
                }
            }
        }
        handler.post(run)
    }

    override fun onQuestionAnswered(answer: Int) {
        Log.d("onQuestionAnswered", "Question answered")
        time_fragment?.stopTimer() //stops sound
        handler.removeCallbacks(run)
        options_fragment?.inactivateCards()
        /*if(mp?.isPlaying!!) {
            mp?.pause()
            mp?.seekTo(0)
        }*/
            val right = problem_instance?.checkAnswer(answer)
            var points = 0
            var card_resource:CardView = options_fragment?.view_option_a!!//options_fragment?.getView()?.findViewById(R.id.option_a) as CardView //default
            when (answer) {
                1 -> card_resource = options_fragment?.view_option_b!!
                2 -> card_resource = options_fragment?.view_option_c!!
                3 -> card_resource = options_fragment?.view_option_d!!
            }
            Log.d("onQuestionAnswered", "answer: $answer, right: $right, theory: ${problem_instance?.answer}")
            if(right!!){
                //Points only count if time > 0
                if(time_fragment?.getTimerState()!! > 0) {
                    points = problem_instance?.points_right!!
                    points_fragment?.addPoints(points, PointsFragment.Constants.POINTS_BASTARD)
                }
                card_resource.setCardBackgroundColor(resources.getColor( R.color.colorRight))
                val animacion = AnimationUtils.loadAnimation(baseContext, R.anim.right_answer_anim)
                animacion.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation:Animation) {}
                    override fun onAnimationRepeat(animation:Animation) {}
                    override fun onAnimationEnd(animation:Animation) {
                        startGame()
                    }
                })
                card_resource.startAnimation(animacion)
                soundPool!!.play(right_answer, 1f, 1f, 1, 0, 1f)
            }else{
                card_resource.setCardBackgroundColor(resources.getColor( R.color.colorWrong))
                points = problem_instance?.points_wrong!!
                points_fragment?.addPoints(points, PointsFragment.Constants.POINTS_BASTARD)
                var card_resource2: CardView? = null
                when (problem_instance?.answer) {
                    0 -> card_resource2 = options_fragment?.view_option_a!!
                    1 -> card_resource2 = options_fragment?.view_option_b!!
                    2 -> card_resource2 = options_fragment?.view_option_c!!
                    3 -> card_resource2 = options_fragment?.view_option_d!!
                }

                card_resource2?.setCardBackgroundColor(resources.getColor( R.color.colorRight))
                val animacion = AnimationUtils.loadAnimation(baseContext, R.anim.wrong_answer_anim)
                val animacion2 = AnimationUtils.loadAnimation(baseContext, R.anim.right_answer_anim)
                animacion2.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation:Animation) {}
                    override fun onAnimationRepeat(animation:Animation) {}
                    override fun onAnimationEnd(animation:Animation) {
                        startGame()
                    }
                })
                card_resource.startAnimation(animacion)
                card_resource2?.startAnimation(animacion2)
                soundPool!!.play(wrong_answer, 1f, 1f, 1, 0, 1f)

            }
        //startGame()
    }

    override fun onPause() {
        super.onPause()
        //mp?.pause()
        //mp?.seekTo(0)
        handler.removeCallbacks(run)
    }

    override fun onStop() {
        super.onStop()
        //mp?.pause()
        //mp?.seekTo(0)
        handler.removeCallbacks(run)
    }

    override fun onDestroy() {
        super.onDestroy()
        //mp?.pause()
        //mp?.seekTo(0)
        handler.removeCallbacks(run)
    }

    override fun onTimeEnds() {
        Log.d("FindTheBastardActivity", "onTimeEnds")
        //mp?.pause()
        //mp?.seekTo(0)
        //soundPool?.play(time_end, 1f, 1f, 1, 0, 1f)
        startGame()
    }

    override fun onTimeDecrease() {
        /*if (time_fragment?.getTimerState() == time_fragment?.WARNING_SECONDS) {
            mp?.start()
        }*/
    }

    override fun onTimeLag() {
        options_fragment?.inactivateCards()
    }
}