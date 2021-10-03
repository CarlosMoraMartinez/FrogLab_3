package com.cmora.froglab_2.bastard

import android.util.Log
import com.cmora.froglab_2.genetics.Family
import com.cmora.froglab_2.genetics.Individual
import com.cmora.froglab_2.genetics.Sex

class BastardProblemInstance (val individuals: MutableList<Individual>,
                              var answer: Int,
                              var all_correct:Int,
                              var none_correct:Int,
                              var no_info:Int,
                              val problem_gene: Int=-1,
                              val names: MutableList<String>, val points_right:Int = 0, val points_wrong:Int = 0) {

    val P0_father = 0
    val P0_mother = 1
    val F1_father = 2
    val F1_mother = 3
    val OPTION_0 = 4
    val OPTION_1 = 5
    val OPTION_3 = 6
    val OPTION_4 = 7
    var p0_father: Individual
    var p0_mother: Individual
    var f1_father: Individual
    var f1_mother: Individual
    var option0: Individual
    var option1: Individual
    var option2: Individual
    var option3: Individual
    init {
        Log.d("PROBLEM_INSTANCE", "start init")

        p0_father = individuals[P0_father];
        p0_mother = individuals[P0_mother];
        f1_father = individuals[F1_father];
        f1_mother = individuals[F1_mother];

        var optionorder : List<Int> = (4..7).toMutableList()
        Log.d("BastardProblemInstance", "optionorder: $optionorder")
        optionorder = optionorder.shuffled()
        Log.d("BastardProblemInstance", "optionorder shuffled: $optionorder")
        option0 = individuals[optionorder[0]]
        option1 = individuals[optionorder[1]]
        option2 = individuals[optionorder[2]]
        option3 = individuals[optionorder[3]]
        if(all_correct != -1){
            all_correct = optionorder.indexOf(all_correct + 4)
        }
        if(none_correct != -1){
            none_correct = optionorder.indexOf(none_correct + 4)
        }
        if(no_info != -1){
            no_info = optionorder.indexOf(no_info + 4)
        }

        Log.d("BastardProblemInstance", "answer before: $answer")
        answer = optionorder.indexOf(answer + 4)
        Log.d("BastardProblemInstance", "answer after: $answer")
        Log.d("BastardProblemInstance", "all_correct: $all_correct, none_correct: $none_correct, no_info: $no_info")

    }
    fun checkAnswer(ans: Int):Boolean{
        return ans == answer
    }

    fun checkGenotype(ind: Int, test_genotype:String, test_gene:Int = problem_gene):Boolean{
        if(problem_gene == -1)
            return false
        return this.individuals[ind].getGenotype(test_gene) == test_genotype
    }

    override fun toString(): String {
        return "BastardProblemInstance(problem_gene=$problem_gene, individuals=$individuals, )"
    }


}