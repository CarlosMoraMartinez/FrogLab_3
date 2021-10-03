package com.cmora.froglab_2.bastard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.cmora.froglab_2.R
import com.cmora.froglab_2.adapters.LayerDrawableBuilder
import com.cmora.froglab_2.genetics.Individual
import com.cmora.froglab_2.genetics.Sex

class QuestionFragment : Fragment(){
    val MALE_UNICODE = "\u2642"
    val FEMALE_UNICODE = "\u2640"
    lateinit var problem_instance: BastardProblemInstance
    var vista: View? = null
    lateinit var actividad: Activity


    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        vista = inflador.inflate(
            R.layout.bastard_fragment_question,
            contenedor, false)
        contenedor?.setClipChildren(false);
        contenedor?.setClipToPadding(false);
        Log.d("CARLOS", "QuestionFragment")
        return vista
    }
    fun setFrogs(){
        setFrog(0, problem_instance.p0_father)
        setFrog(1, problem_instance.p0_mother)
        setFrog(2, problem_instance.f1_father)
        setFrog(3, problem_instance.f1_mother)
    }
    fun setFrog(option:Int, individual: Individual){
        var view: CardView? = null
        var option_num: TextView
        var option_name: TextView
        if (vista != null) {
            when (option) {
                0 -> view = vista!!.findViewById(R.id.option_a)
                1 -> view = vista!!.findViewById(R.id.option_c)
                2 -> view = vista!!.findViewById(R.id.option_b)
                3 -> view = vista!!.findViewById(R.id.option_d)
            }
            option_name = view!!.findViewById(R.id.frog_name)
            var namestr = problem_instance.names[option] + ' ' + if(individual.sex == Sex.MALE) MALE_UNICODE else FEMALE_UNICODE
            option_name.setText(namestr) //Clean this at some point

            var image : ImageView = view!!.findViewById(R.id.frog_icono)
            image.setImageDrawable(LayerDrawableBuilder.getLayerDrawable(context, individual));
            //Now set Frog Image

        }
    }
}