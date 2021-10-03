package com.cmora.froglab_2

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.cmora.froglab_2.bastard.FindTheBastardActivity
import com.cmora.froglab_2.database.DatabaseActivity
import com.cmora.froglab_2.laboratory_use_case.Juego
import com.cmora.froglab_2.punnet.PunnetActivityMain
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    var mp : MediaPlayer = MediaPlayer();
    val REQ_CODE_DB = 1
    val SOLICITUD_PERMISO_EXTERNAL_STORAGE = 1
    /*val SOLICITUD_PERMISO_BLUETOOTH = 2
    val SOLICITUD_PERMISO_BLUETOOTH_ADMIN = 3
    val SOLICITUD_PERMISO_FINE_LOCATION = 4
    val GENOME_PREF_NAME = "genome"
    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_SMS,
        Manifest.permission.CAMERA
    )*/
    lateinit var genome_list: GenomeList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        button01.setOnClickListener {
            lanzarJuego()
        }
        button02.setOnClickListener {
            lanzarPunnetSquare()
        }
        button03.setOnClickListener {
            lanzarFindTheBastard()
        }
        button04.setOnClickListener {
            lanzarDatabase()
        }
        //val animacion = AnimationUtils.loadAnimation(this, R.anim.animation3)
        val animacion5 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion6 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion7 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion8 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion2 = AnimationUtils.loadAnimation(this, R.anim.animation4)
        //animacion.setRepeatCount(Animation.INFINITE);
        //textView3.startAnimation(animacion);
        //textView4.startAnimation(animacion2);
        button01.startAnimation(animacion5);
        button02.startAnimation(animacion5);
        button03.startAnimation(animacion5);
        button04.startAnimation(animacion5);

        mp = MediaPlayer.create(this, R.raw.audio);
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("musica", true)) {
            mp.start();
        }
        if ( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                resources.getString(R.string.permission_justify_writemem),
                SOLICITUD_PERMISO_EXTERNAL_STORAGE, this)
        }
        /*if ( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso(Manifest.permission.BLUETOOTH,
                resources.getString(R.string.permission_justify_bluetooth),
                SOLICITUD_PERMISO_BLUETOOTH, this)
        }
        if ( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso(Manifest.permission.BLUETOOTH_ADMIN,
                resources.getString(R.string.permission_justify_bluetoothadmin),
                SOLICITUD_PERMISO_BLUETOOTH_ADMIN, this)
        }
        if ( ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                resources.getString(R.string.permission_justify_finelocation),
                SOLICITUD_PERMISO_FINE_LOCATION, this)
        }*/
        /*if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }*/
        Log.d("MAIN", "Calling GENOMELIST")
        val JSONFileInputStream: InputStream = resources.openRawResource(R.raw.genome_list)
        val genomestr = Juego.readTextFile(JSONFileInputStream)
        genome_list = GenomeList(genomestr)

        Log.d("MAIN", "Called GENOMELIST")

        /*
        var gnames: HashSet<String> = HashSet()
        val editor = pref.edit()
        for(g: GenomeList.GenomeId in genome_list.genomes){
            gnames.add( g.name)
            editor.putString("genome", g.name)
        }
        Log.d("MAIN", "gnames: " + gnames.toString())

        //editor.putStringSet("genome", gnames)
        editor.commit()*/
    }
    /*fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.acercade -> {
                lanzarAcercaDe()
                true
            }
            R.id.action_settings -> {
                lanzarPreferencias()
                true
            }
            R.id.how_to_play -> {
                lanzarAyuda()
                true
            }
            R.id.totalpoints ->{
                lanzarPuntos()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun lanzarAcercaDe(view: View? = null) {
        val i = Intent(this, AboutActivity::class.java)
        startActivity(i)
    }
    fun lanzarPuntos(){
        val i = Intent(this, PointsActivity::class.java)
        startActivity(i)
    }

    fun lanzarJuego(view: View? = null) {
        val i = Intent(this, Juego::class.java)
        i.putExtra("FROM_DATABASE", false);
        //i.putExtra("genome_name", data?.extras?.getString("genome_name"));//Inside Juego, get from prefs
        Log.d("MAIN", "lanzarJuego 1")
        startActivity(i)
        Log.d("MAIN", "lanzarJuego 2")
    }
    fun lanzarAyuda(view: View? = null){
        val i = Intent(this, HelpActivity::class.java)
        Log.d("MAIN", "lanzarAyuda 1")
        startActivity(i)
        Log.d("MAIN", "lanzarAyuda 2")
    }
    fun lanzarDatabase(view: View? = null){
        Log.d("MAIN", "lanzarDatabaseActivity 1")
        val i = Intent(this, DatabaseActivity::class.java)
        startActivityForResult(i, REQ_CODE_DB);
    }
    fun lanzarFindTheBastard(){
            val i = Intent(this, FindTheBastardActivity::class.java)
            Log.d("MAIN", "lanzarFindTheBastard 1")
            startActivity(i)
            Log.d("MAIN", "lanzarFindTheBastard 2")
    }
    fun lanzarPunnetSquare(){
        val i = Intent(this, PunnetActivityMain::class.java)
        Log.d("MAIN", "lanzarPunnetSquare 1")
        startActivity(i)
        Log.d("MAIN", "lanzarPunnetSquare 2")
    }
    fun lanzarPreferencias(view: View? = null) {
        val i = Intent(this, PreferencesActivity::class.java)
        i.putExtra("genome_list", genome_list);
        startActivity(i)
        Log.d("MAIN", "lanzarPreferencias 2")
    }
    override fun onPause() {
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("musica", true)) {
            mp.pause();
        }
        button01.animate().cancel()
        button02.animate().cancel()
        button03.animate().cancel()
        button04.animate().cancel()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean("musica", true)) {
            mp.start();
        }
        //val animacion = AnimationUtils.loadAnimation(this, R.anim.animation3)
        val animacion5 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion6 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion7 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion8 = AnimationUtils.loadAnimation(this, R.anim.points_activity_icon)
        //val animacion2 = AnimationUtils.loadAnimation(this, R.anim.animation4)
        //animacion.setRepeatCount(Animation.INFINITE);
        //textView3.startAnimation(animacion);
        //textView4.startAnimation(animacion2);
        button01.startAnimation(animacion5);
        button02.startAnimation(animacion5);
        button03.startAnimation(animacion5);
        button04.startAnimation(animacion5);
    }

    // override fun onDestroy() {
    //mp.stop();
    //super.onDestroy()
    //}
    override fun onSaveInstanceState(estadoGuardado: Bundle) {
        super.onSaveInstanceState(estadoGuardado)
        if (mp != null) {
            val pos = mp.getCurrentPosition()
            estadoGuardado.putInt("posicion", pos)
        }
    }
    override fun onRestoreInstanceState(estadoGuardado: Bundle?) {
        super.onRestoreInstanceState(estadoGuardado)
        if (estadoGuardado != null && mp != null) {
            val pos = estadoGuardado.getInt("posicion")
            mp.seekTo(pos)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == REQ_CODE_DB){
            Log.d("MAIN OnActivityResult", "lanzarJuego DB 1")
            val i = Intent(this, Juego::class.java)
            i.putExtra("FROM_DATABASE", true);
            i.putExtra("female_hap1", data?.extras?.getString("female_hap1"));
            i.putExtra("female_hap2", data?.extras?.getString("female_hap2"));
            i.putExtra("male_hap1", data?.extras?.getString("male_hap1"));
            i.putExtra("male_hap2", data?.extras?.getString("male_hap2"));
            i.putExtra("genome_name", data?.extras?.getString("genome_name"));
            Log.d("MAIN", "Genome from database: " + data?.extras?.getString("genome_name"));
            Log.d("MAIN OnActivityResult", "lanzarJuego DB 3")
            startActivity(i)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("CARLOS", "onRequestPermissionsResult 1")
        if (requestCode == SOLICITUD_PERMISO_EXTERNAL_STORAGE) {
            Log.d("CARLOS", "onRequestPermissionsResult 2")
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("CARLOS", "onRequestPermissionsResult 3")
                Toast.makeText(
                    this,
                    getResources().getString(R.string.permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Log.d("CARLOS", "onRequestPermissionsResult 4")
            Toast.makeText(
                this,
                getResources().getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d("CARLOS", "onRequestPermissionsResult 5")
    }
    fun solicitarPermiso(permiso: String, justificacion: String?, requestCode: Int, actividad: Activity?) {
        Log.d("CARLOS", "Sol. permiso 1")
        val permissions = arrayOf(permiso)
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                actividad!!, permiso)) {
            Log.d("CARLOS", "Sol. permiso 2")
            AlertDialog.Builder(actividad)
                .setTitle(getResources().getString(R.string.permission_required))
                .setMessage(justificacion)
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        ActivityCompat.requestPermissions(
                            actividad,
                            permissions, requestCode
                        )
                    }).show()
            Log.d("CARLOS", "Sol. permiso 3")
        } else {
            Log.d("CARLOS", "Sol. permiso 4")
            ActivityCompat.requestPermissions(
                actividad,
                permissions, requestCode
            )
            Log.d("CARLOS", "Sol. permiso 5")
        }
        Log.d("CARLOS", "Sol. permiso 6")
    }


}