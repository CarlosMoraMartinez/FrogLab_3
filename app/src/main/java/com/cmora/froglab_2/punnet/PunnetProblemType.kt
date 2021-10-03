package com.cmora.froglab_2.punnet

import android.util.Log
import com.cmora.froglab_2.genetics.*
import kotlin.random.Random

class PunnetProblemType(val name:String, val description:String,
                        val genome: GenomeModel,
                        base_haplotype1 : Haplotype,
                        base_haplotype2 : Haplotype,
                        problem_genes:MutableList<Int>,
                        var haplotypes:MutableList<Haplotype>,
                        var f1_sex: Sex,
                        var right_possibilities: MutableList<MutableList<String>>,
                        var wrong_sperm: MutableList<String>,
                        var wrong_eggs: MutableList<String>,
                        val numSperms: Int,
                        val numEggs: Int,
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
    fun getProblemInstance(): PunnetProblemInstance {
        Log.d("PROBLEM_TYPE", "Getting problem instance")
        if(this.gene_groups != null) {
            Log.d("PROBLEM_TYPE", "Gene Groups Not Null: ${this.gene_groups}")
            return this.getProblemInstance_groups(this.gene_groups!!)
        }
        Log.d("PROBLEM_TYPE", "Gene Groups Null")
        return this.getProblemInstance_single()
    }
    fun getProblemInstance_single(): PunnetProblemInstance {
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

        val f1 = Individual(0,
            -1,
            newBaseGenotype,
            MutableList(1) { haplotypes[4][problem_gene_ind] },
            MutableList(1) { haplotypes[5][problem_gene_ind] },
            f1_sex
        )

        val individuals : MutableList<Individual> = mutableListOf(p0_male, p0_female, f1)

        var sperm_genotypes: MutableList<String> = mutableListOf()
        var egg_genotypes: MutableList<String> = mutableListOf()
        var right_answer = right_possibilities.random()
        //Add right gametes to the lists
        sperm_genotypes.add(right_answer.get(0))
        egg_genotypes.add(right_answer.get(1))
        //Add wrong gamentes. Note that the final list will be 1 element longer
        for(i in 1..numSperms) {
            Log.d("PunnetProblemType", "Adding sperm genotype: $i")
            sperm_genotypes.add(wrong_sperm.random())
        }
        for(i in 1..numEggs) {
            Log.d("PunnetProblemType", "Adding egg genotype: $i")
            egg_genotypes.add(wrong_eggs.random())
        }

        val problem = PunnetProblemInstance(individuals, right_answer,
            sperm_genotypes, egg_genotypes,
            problem_gene, names, points_right, points_wrong)
        Log.d("PROBLEM_TYPE", "returning problem: $problem")
        return problem
    }
    //Not implemented
    fun getProblemInstance_groups(groups: MutableList<MutableList<Int>>): PunnetProblemInstance {
        Log.d("PROBLEM_TYPE", "groups start")
        val problem = PunnetProblemInstance(mutableListOf<Individual>(), mutableListOf(),
            mutableListOf(), mutableListOf(),
            -1, mutableListOf<String>(), 0, 0)
        return problem
    }
    override fun toString(): String {
        return "ProblemType(name='$name', description='$description', " +
                "genome=${genome.name}, \nhaplotype_list=$haplotypes, \nbase_genotype=$base_genotype, " +
                "gene_in_problem=$gene_in_problem, sex_of_options:\ngroups=$gene_groups)"
    }


}