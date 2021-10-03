package com.cmora.froglab_2.punnet

import android.util.Log
import com.cmora.froglab_2.genetics.Family
import com.cmora.froglab_2.genetics.Individual
import com.cmora.froglab_2.genetics.Sex

class PunnetProblemInstance (val individuals: MutableList<Individual>,
                             val answer: MutableList<String>,
                             val sperm_genotypes: MutableList<String>,
                             val egg_genotypes: MutableList<String>,
                             val problem_gene: Int=-1,
                             val names: MutableList<String>,
                             val points_right:Int = 0,
                             val points_wrong:Int = 0) {

    val P0_father = 0
    val P0_mother = 1
    val F1 = 2
    val NULL_ALLELE="_"

    var p0_father: Individual
    var p0_mother: Individual
    var f1_ind: Individual
    var right_sperm: String
    var right_egg: String
    var numSperms: Int
    var numEggs: Int
    init {
        Log.d("PROBLEM_INSTANCE", "start init")
        p0_father = individuals[P0_father];
        p0_mother = individuals[P0_mother];
        f1_ind = individuals[F1];
        right_sperm = answer[0]
        right_egg = answer[1]
        numEggs = egg_genotypes.size
        numSperms = sperm_genotypes.size
       Log.d("PunnetProblemInstance", "answer: $answer")
    }
    fun checkAnswer(sperm:String, egg:String): Boolean{
        Log.d("PunnetProblemInstance", "CheckAnswer: p0Mother  "+ p0_mother.getGenotype(problem_gene) + "p0_father: " + p0_father.getGenotype(problem_gene) + "right sperm : $right_sperm, right_egg: $right_egg" )
        return (right_sperm == sperm && right_egg == egg) ||
                (right_sperm == egg && right_egg == sperm && !answer.contains(NULL_ALLELE) &&
                        p0_mother.getGenotype(problem_gene).contains(right_sperm) &&
                        p0_father.getGenotype(problem_gene).contains(right_egg))
    }

    fun checkGenotype(ind: Int, test_genotype:String, test_gene:Int = problem_gene):Boolean{
        if(problem_gene == -1)
            return false
        return this.individuals[ind].getGenotype(test_gene) == test_genotype
    }

    override fun toString(): String {
        return "PunnetProblemInstance(problem_gene=$problem_gene, individuals=$individuals, )"
    }


}