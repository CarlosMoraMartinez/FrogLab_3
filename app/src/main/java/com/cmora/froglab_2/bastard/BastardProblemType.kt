package com.cmora.froglab_2.bastard

import com.cmora.froglab_2.laboratory_use_case.ProblemInstance

import android.util.Log
import com.cmora.froglab_2.genetics.*
import kotlin.random.Random

class BastardProblemType(val name:String, val description:String,
                         val genome: GenomeModel,
                         base_haplotype1 : Haplotype,
                         base_haplotype2 : Haplotype,
                         problem_genes:MutableList<Int>,
                         var haplotypes:MutableList<Haplotype>,
                         var options_sex_list:MutableList<Sex>,
                         val answer:Int,
                         var all_correct:Int,
                         var none_correct:Int,
                         var no_info:Int,
                         val names: MutableList<String>,
                         val points_right:Int,
                         val points_wrong:Int,
                         gene_groups: MutableList<MutableList<Int>>?=null){
    val base_genotype = BaseGenotype(
        base_haplotype1,
        base_haplotype2,
        problem_genes,
        genome
    )
    var gene_in_problem = MutableList<Boolean>(genome.num_genes){false}
    var num_groups = 1
    var gene_groups: MutableList<MutableList<Int>>? = null//(1){problem_genes}

    init{
        for(g in problem_genes)
            gene_in_problem[g] = true
        this.gene_groups = gene_groups
        if (gene_groups != null) {
            Log.d("PROBLEM_TYPE", "init: gene groups==null")
            num_groups = gene_groups.size
        }
        Log.d("PROBLEM_TYPE", "init: end")
    }
    fun getProblemInstance(): BastardProblemInstance {
        Log.d("PROBLEM_TYPE", "Getting problem instance")
        if(this.gene_groups != null) {
            Log.d("PROBLEM_TYPE", "Gene Groups Not Null: ${this.gene_groups}")
            return this.getProblemInstance_groups(this.gene_groups!!)
        }
        Log.d("PROBLEM_TYPE", "Gene Groups Null")
        return this.getProblemInstance_single()
    }
    fun getProblemInstance_single(): BastardProblemInstance {
        Log.d("PROBLEM_TYPE", "single 1")
        val problem_gene_ind = Random.nextInt(0, this.base_genotype.editable_genes.size)
        val problem_gene = this.base_genotype.editable_genes[problem_gene_ind]
        Log.d("PROBLEM_TYPE", "gene:$problem_gene, gene_ind:$problem_gene_ind, ed_genes_${this.base_genotype.editable_genes}")
        val newBaseGenotype= BaseGenotype(
            this.base_genotype,
            problem_gene
        )
        Log.d("PROBLEM_TYPE", "newBaseGenotype: $newBaseGenotype")
        val p0_male = Individual(0,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[0][problem_gene_ind] },
            MutableList(1) { haplotypes[1][problem_gene_ind] },
            Sex.MALE
        )
        Log.d("PROBLEM_TYPE", "female: $p0_male")
        val p0_female = Individual(1,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[2][problem_gene_ind] },
            MutableList(1) { haplotypes[3][problem_gene_ind] },
            Sex.FEMALE
        )
        Log.d("PROBLEM_TYPE", "female: $p0_female")

        val f1_male = Individual(0,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[4][problem_gene_ind] },
            MutableList(1) { haplotypes[5][problem_gene_ind] },
            Sex.MALE
        )
        Log.d("PROBLEM_TYPE", "female: $p0_male")
        val f1_female = Individual(1,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[6][problem_gene_ind] },
            MutableList(1) { haplotypes[7][problem_gene_ind] },
            Sex.FEMALE
        )
        Log.d("PROBLEM_TYPE", "female: $p0_female")

        val option0 = Individual(0,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[8][problem_gene_ind] },
            MutableList(1) { haplotypes[9][problem_gene_ind] },
            options_sex_list[0]
        )
        val option1 = Individual(1,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[10][problem_gene_ind] },
            MutableList(1) { haplotypes[11][problem_gene_ind] },
            options_sex_list[1]
        )
        val option2 = Individual(2,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[12][problem_gene_ind] },
            MutableList(1) { haplotypes[13][problem_gene_ind] },
            options_sex_list[2]
        )
        val option3 = Individual(3,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[14][problem_gene_ind] },
            MutableList(1) { haplotypes[15][problem_gene_ind] },
            options_sex_list[3]
        )
        val individuals : MutableList<Individual> = mutableListOf(p0_male, p0_female, f1_male, f1_female, option0, option1, option2, option3)
        val problem = BastardProblemInstance(individuals, answer,
            all_correct, none_correct, no_info,
            problem_gene, names, points_right, points_wrong)
        Log.d("PROBLEM_TYPE", "returning problem: $problem")
        return problem
    }
    //Not implemented
    fun getProblemInstance_groups(groups: MutableList<MutableList<Int>>): BastardProblemInstance {
        Log.d("PROBLEM_TYPE", "groups start")
        val problem = BastardProblemInstance(mutableListOf<Individual>(), -1,-1, -1, -1, -1, mutableListOf<String>(), 0, 0)
        return problem
    }
    override fun toString(): String {
        return "ProblemType(name='$name', description='$description', " +
                "genome=${genome.name}, \nhaplotype_list=$haplotypes, \nbase_genotype=$base_genotype, " +
                "gene_in_problem=$gene_in_problem, sex_of_options:\ngroups=$gene_groups)"
    }


}