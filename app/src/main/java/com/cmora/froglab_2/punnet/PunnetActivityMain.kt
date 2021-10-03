package com.cmora.froglab_2.punnet

import android.app.AlertDialog
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cmora.froglab_2.laboratory_use_case.Juego
import com.cmora.froglab_2.R
import com.cmora.froglab_2.genetics.GenomeInitializer
import com.cmora.froglab_2.genetics.GenomeModel
import com.cmora.froglab_2.tools.HeaderFragment
import com.cmora.froglab_2.tools.PointsFragment
import com.cmora.froglab_2.tools.TimerFragment
import com.cmora.froglab_2.tools.TimerListener
import kotlin.random.Random

class PunnetActivityMain : AppCompatActivity(), TimerListener, PunnetListener {

    var punnet_fragment: PunnetFragment? = PunnetFragment()
    var points_fragment: PointsFragment? = PointsFragment()
    var question_fragment: PunnetQuestionFragment? = PunnetQuestionFragment()
    var time_fragment: TimerFragment? = TimerFragment()
    var head_fragment: HeaderFragment? = null
    private val DEFAULT_GENOME = "genome1"
    private var db_genome_name: String? = DEFAULT_GENOME
    private var genome: GenomeModel? = null
    private var text_view_selected_sperm :TextView? = null
    private var text_view_selected_egg :TextView? = null
    private val all_problemtypes: MutableList<String> = mutableListOf(
        "punnet_question_type1",
        "punnet_question_type2",
        "punnet_question_type3",
        "punnet_question_type4",
        "punnet_question_type5"
    )

    //private val all_problemtypes : MutableList<String> = mutableListOf("bastard_question_type5")
    var soundPool: SoundPool? = null
    var problem_type: PunnetProblemType? = null
    var problem_instance: PunnetProblemInstance? = null
    var right_answer: Int = -1
    var wrong_answer: Int = -1
    val QUESTION_TIME = 30
    val WARNING_TIME = 10
    var handler: Handler = Handler()
    var run: Runnable? = null

    var time_set: Int = -1 //for timer soundpool
    var time_end: Int = -1
    var sound_change_tube = -1
    var sound_drop_gamete = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PUNNET_MAIN", "Start OnCreate")
        setContentView(R.layout.punnet_main_layout)
        Log.d("PUNNET_MAIN", "OnCreate 0")
        punnet_fragment =
            supportFragmentManager.findFragmentById(R.id.punnet_fragment_main) as PunnetFragment?
        Log.d("PUNNET_MAIN", "OnCreate 1")
        head_fragment =
            supportFragmentManager.findFragmentById(R.id.punnet_fragment_header_main) as HeaderFragment?
        Log.d("PUNNET_MAIN", "OnCreate 2")
        if (head_fragment == null) {
            Log.d("PUNNET_MAIN", "head fragment null ")
        }
        question_fragment =
            supportFragmentManager.findFragmentById(R.id.punnet_fragment_question) as PunnetQuestionFragment?
        Log.d("PUNNET_MAIN", "OnCreate 2")
        if (question_fragment == null) {
            Log.d("PUNNET_MAIN", "punnet_question_fragment fragment null ")
        }
        points_fragment =
            head_fragment?.childFragmentManager?.findFragmentById(R.id.points_fragment_header) as PointsFragment
        Log.d("PUNNET_MAIN", "OnCreate 3")
        time_fragment =
            head_fragment?.childFragmentManager?.findFragmentById(R.id.timer_fragment_header) as TimerFragment
        Log.d("PUNNET_MAIN", "OnCreate 4")
        time_fragment?.timerListener = this
        //supportFragmentManager.findFragmentById(R.id.points_fragment_header) as PointsFragment?

        text_view_selected_sperm = findViewById(R.id.male_selected)
        text_view_selected_egg = findViewById(R.id.female_selected)

        if (points_fragment == null) {
            Log.d("PUNNET_MAIN", "points fragment null ")
        }
        soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        right_answer = soundPool!!.load(this, R.raw.right_answer, 0)
        wrong_answer = soundPool!!.load(this, R.raw.wrong_answer, 0)
        sound_drop_gamete = soundPool!!.load(this, R.raw.drop_in_tube, 0)
        sound_change_tube = soundPool!!.load(this, R.raw.change_tube, 0)

        time_set = soundPool!!.load(this, R.raw.timer_start, 0)
        time_end = soundPool!!.load(this, R.raw.timer_final, 0)
        //mp = MediaPlayer.create(this, R.raw.timer_countdown)
        //mp?.prepare()
        Log.d("PUNNET_MAIN", "OnCreate 5")
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        db_genome_name = pref.getString("genome", DEFAULT_GENOME)
        if (db_genome_name == null) {
            db_genome_name = DEFAULT_GENOME
            Log.d("PUNNET_MAIN", "Genome is null, using default genome")
        }
        Log.d("PUNNET_MAIN", "Genome to load: $db_genome_name")
        val genome_resource2 =
            resources.getIdentifier(db_genome_name, "raw", packageName)
        val JSONFileInputStream2 =
            resources.openRawResource(genome_resource2) //R.raw.genome1

        Log.d("PUNNET_MAIN", "Loading genome $db_genome_name")
        //InputStream JSONFileInputStream2 = getResources().openRawResource(R.raw.genome1);
        //InputStream JSONFileInputStream2 = getResources().openRawResource(R.raw.genome1);
        this.genome =
            GenomeInitializer.buildGenomeFromJSON(Juego.readTextFile(JSONFileInputStream2))
        Log.d("PUNNET_MAIN", "Loaded genome $db_genome_name")

        //val fm: FragmentManager = supportFragmentManager
        //fm.beginTransaction().replace(R.id.bastard_fragment_question_main, question_fragment).commit()
        //fm.beginTransaction().replace(R.id.bastard_fragment_options_main, options_fragment).commit()

        makePunnetStartAlert()
        Log.d("PUNNET_MAIN", "End OnCreate")
    }

    override fun onStart() {
        super.onStart()
        startGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(run)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(run)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(run)
    }

    override fun onResume() {
        super.onResume()
        Log.d("PunnetActivityMain", "onResume 1")
        //punnet_fragment?.vista?.unpauseGametes()
        //Log.d("PunnetActivityMain", "onResume 2")
    }

    override fun onAnswer(right: Boolean) {
        var points:Int
        if(right){
            Log.d("PunnetActivityMain", "OnAnswer right")
            soundPool?.play(right_answer, 1f, 1f, 1, 0, 1f)
            points = problem_instance?.points_right!!
            points_fragment?.addPoints(points, PointsFragment.Constants.POINTS_PUNNET)
        }else{
            Log.d("PunnetActivityMain", "OnAnswer wrong")
            soundPool?.play(wrong_answer, 1f, 1f, 1, 0, 1f)
            points = problem_instance?.points_wrong!!
            points_fragment?.addPoints(points, PointsFragment.Constants.POINTS_PUNNET)
        }
        handler.removeCallbacks(run)
        startGame();
    }

    override fun onEvent(event: Int, info: String) {

        when(event){
            PunnetView.EVENT_SET_EGG -> {
                text_view_selected_egg?.setText( getString(R.string.string_selected_egg) + " " + info)
                text_view_selected_egg?.visibility = View.VISIBLE
                soundPool?.play(sound_drop_gamete, 1f, 1f, 1, 0, 1f)
            }
            PunnetView.EVENT_SET_SPERM ->{
                text_view_selected_sperm?.setText(getString(R.string.string_selected_sperm) + " " + info)
                text_view_selected_sperm?.visibility = View.VISIBLE
                soundPool?.play(sound_drop_gamete, 1f, 1f, 1, 0, 1f)
            }
            else ->{
                soundPool?.play(sound_drop_gamete, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    fun startGame(){
        Log.d("PUNNET_MAIN", "StartGame 1")
        var id = Random.nextInt(0, all_problemtypes.size)
        handler.removeCallbacks(run)
        text_view_selected_sperm?.visibility = View.INVISIBLE
        text_view_selected_egg?.visibility = View.INVISIBLE
        Log.d("PUNNET_MAIN", "Current Problem: " + Integer.toString(id))
        Toast.makeText(
            this,
            "Problem selected: " + all_problemtypes.get(id),
            Toast.LENGTH_LONG
        )
        id = resources.getIdentifier(all_problemtypes.get(id), "raw", packageName)
        val JSONFileInputStream = resources.openRawResource(id)
        problem_type = PunnetProblemInitializer.buildProblemTypeFromJSON(
            Juego.readTextFile(JSONFileInputStream), genome!!
        )
        problem_instance = problem_type?.getProblemInstance()
        Log.d("PUNNET_MAIN", "problem instance: $problem_instance")
        putQuestion()
        putOptions()
        setTimer()
        soundPool?.play(sound_change_tube, 3f, 3f, 1, 0, 1f)
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
    fun putQuestion(){
        question_fragment =
            supportFragmentManager.findFragmentById(R.id.punnet_fragment_question) as PunnetQuestionFragment?
        question_fragment!!.problem_instance = problem_instance!!
        question_fragment!!.setFrogs()
    }
    fun putOptions(){
        punnet_fragment?.vista?.stopGametes()
        punnet_fragment = supportFragmentManager.findFragmentById(R.id.punnet_fragment_main) as PunnetFragment?
        problem_instance?.let { punnet_fragment?.setProblemInstanceToView(it) }
        punnet_fragment?.vista?.setGametes()
        punnet_fragment?.setListener(this)
    }

    override fun onTimeEnds() {
        Log.d("PunnetMainActivity", "onTimeEnds")
        //mp?.pause()
        //mp?.seekTo(0)
        //soundPool?.play(time_end, 1f, 1f, 1, 0, 1f)
        handler.removeCallbacks(run)
        startGame()
    }

    override fun onTimeDecrease() {
    }

    override fun onTimeLag() {
    }
    fun makePunnetStartAlert() {
        val alertName = AlertDialog.Builder(this)
        //final EditText editTextName = new EditText(this);
        Log.d("ElementListAdapter", "makeSaveAlert 1")
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.punnet_prompt, null)
        val alertDialogBuilder =
            AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id -> dialog.dismiss() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        Log.d("ElementListAdapter", "makeSaveAlert 2")
    }

}