package com.cmora.froglab_2.bastard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.cmora.froglab_2.R
import com.cmora.froglab_2.adapters.LayerDrawableBuilder
import com.cmora.froglab_2.genetics.Individual
import com.cmora.froglab_2.genetics.Sex


class OptionsFragment : Fragment(){
    val MALE_UNICODE = "\u2642"
    val FEMALE_UNICODE = "\u2640"
    val option_numbers = mutableListOf<String>("a)", "b)", "c)", "d)")
    lateinit var problem_instance: BastardProblemInstance
    lateinit var question_listener : QuestionListener
    lateinit var view_option_a: CardView
    lateinit var view_option_b: CardView
    lateinit var view_option_c: CardView
    lateinit var view_option_d: CardView
    var vista: View? = null
    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        vista = inflador.inflate(
            R.layout.bastard_fragment_options,
            contenedor, false)
        contenedor?.setClipChildren(false);
        contenedor?.setClipToPadding(false);
        view_option_a = vista!!.findViewById(R.id.option_a)
        view_option_b = vista!!.findViewById(R.id.option_b)
        view_option_c = vista!!.findViewById(R.id.option_c)
        view_option_d = vista!!.findViewById(R.id.option_d)
        Log.d("CARLOS", "OptionsFragment")
        return vista
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QuestionListener) {
            question_listener = context as QuestionListener
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement QuestionListener"
            )
        }
    }
    fun setQuestionListener(listener: QuestionListener){
        question_listener = listener
    }
    fun setOptions(){
        setOption(0, problem_instance.option0)
        setOption(1, problem_instance.option1)
        setOption(2, problem_instance.option2)
        setOption(3, problem_instance.option3)

    }

    fun inactivateCards(){
        view_option_a.isClickable = false
        view_option_b.isClickable = false
        view_option_c.isClickable = false
        view_option_d.isClickable = false

        view_option_a.isFocusable = false
        view_option_b.isFocusable = false
        view_option_c.isFocusable = false
        view_option_d.isFocusable = false

        view_option_a.isEnabled = false
        view_option_b.isEnabled = false
        view_option_c.isEnabled = false
        view_option_d.isEnabled = false
    }
    fun activateCards(){
        view_option_a.isFocusable = true
        view_option_b.isFocusable = true
        view_option_c.isFocusable = true
        view_option_d.isFocusable = true

        view_option_a.isClickable = true
        view_option_b.isClickable = true
        view_option_c.isClickable = true
        view_option_d.isClickable = true

        view_option_a.isEnabled = true
        view_option_b.isEnabled = true
        view_option_c.isEnabled = true
        view_option_d.isEnabled = true



    }
    fun setWordsOnlyOption(view:View?, words: Int){
        Log.d("OptionsFragment", "setWordsOnlyOption")
        var image : ImageView = view!!.findViewById(R.id.option_icono)
        image.visibility = View.INVISIBLE
        var option_name: TextView
        option_name = view!!.findViewById(R.id.option_name)
        var namestr = getString(words)
        option_name.setText(namestr) //Clean this at some point
    }
    fun setOption(option:Int, individual: Individual){
        var view: CardView? = null
        var option_num: TextView
        Log.d("OptionsFragment", "setOption 0")
        if (vista != null) {
            when (option) {
                0 -> view = view_option_a
                1 -> view = view_option_b
                2 -> view = view_option_c
                3 -> view = view_option_d
            }
            Log.d("OptionsFragment", "setOption 1")
            option_num = view!!.findViewById(R.id.option_number)
            option_num.setText(option_numbers[option])
            Log.d("OptionsFragment", "setOption 2")
            if(problem_instance?.all_correct == option){
                Log.d("OptionsFragment", "setOption 3")
                setWordsOnlyOption(view, R.string.bastard_all_correct)
            }else if(problem_instance?.none_correct == option){
                Log.d("OptionsFragment", "setOption 4")
                setWordsOnlyOption(view, R.string.bastard_none_correct)
            }else if(problem_instance?.no_info == option){
                Log.d("OptionsFragment", "setOption 5")
                setWordsOnlyOption(view, R.string.bastard_cant_know)
            }else{
                Log.d("OptionsFragment", "setOption 7")
                //Now set Frog Image
                var image : ImageView = view!!.findViewById(R.id.option_icono)
                image.visibility = View.VISIBLE
                image.setImageDrawable(LayerDrawableBuilder.getLayerDrawable(context, individual));
                Log.d("OptionsFragment", "setOption 8")
                var option_name: TextView
                option_name = view!!.findViewById(R.id.option_name)
                var namestr = problem_instance.names[option + 4] + ' ' + if(individual.sex == Sex.MALE) MALE_UNICODE else FEMALE_UNICODE
                option_name.setText(namestr) //Clean this at some point
                Log.d("OptionsFragment", "setOption 9")
            }
            Log.d("OptionsFragment", "setOption 10")
            view.setCardBackgroundColor(resources.getColor( R.color.colorIndividualCard))
            view.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    if (view != null) {
                        val txv = view.findViewById(com.cmora.froglab_2.R.id.option_number) as TextView
                        var s = txv.text
                        var option:Int = 0

                        when (s.first()){
                            'a' -> option = 0
                            'b' -> option = 1
                            'c' -> option = 2
                            'd' -> option = 3
                        }
                        Log.d("OptionsFragment", "onClickListener option: " + s.first().toString() + ", " + option.toString())
                        question_listener.onQuestionAnswered(option)
                    }
                }
            })
            Log.d("OptionsFragment", "setOption 11")

        }
    }
}