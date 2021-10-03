package com.cmora.froglab_2.punnet

import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
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

class PunnetQuestionFragment: Fragment() {
    val MALE_UNICODE = "\u2642"
    val FEMALE_UNICODE = "\u2640"
    lateinit var problem_instance: PunnetProblemInstance
    var vista: View? = null
    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        vista = inflador.inflate(
            R.layout.punnet_question_fragment_layout,
            contenedor, false)
        contenedor?.setClipChildren(false);
        contenedor?.setClipToPadding(false);
        //This can be done in XML
        //This can be done in XML
        Log.d("CARLOS", "BastardHeaderFragment")
        return vista
    }
    fun setFrogs(){
        setFrog(0, problem_instance.p0_father)
        setFrog(1, problem_instance.p0_mother)
        setFrog(2, problem_instance.f1_ind)
    }
    fun setFrog(option:Int, individual: Individual){
        var view: CardView? = null
        var option_num: TextView
        var option_name: TextView
        if (vista != null) {
            when (option) {
                0 -> view = vista!!.findViewById(R.id.option_a)
                1 -> view = vista!!.findViewById(R.id.option_b)
                2 -> view = vista!!.findViewById(R.id.option_c)
            }
            option_name = view!!.findViewById(R.id.titulo)
            var namestr = problem_instance.names[option] + ' ' + if(individual.sex == Sex.MALE) MALE_UNICODE else FEMALE_UNICODE
            option_name.setText(namestr) //Clean this at some point

            option_name = view!!.findViewById(R.id.subtitulo)
            /*option_name.setText( Html.fromHtml("<b>" + context!!.getString(R.string.phenotype) + "</b> " +
                    individual.getAllPhenotypes() + "<br><b>" + context!!.getString(R.string.genotype) + "</b> " +
                    individual.getAllGenotypes()))*/
            option_name.setText( Html.fromHtml("<b>" +  individual.getAllGenotypes().removeSuffix(".") + "</b> "))

            var image : ImageView = view!!.findViewById(R.id.icono)
            image.setImageDrawable(LayerDrawableBuilder.getLayerDrawable(context, individual));

        }
    }
}