package com.cmora.froglab_2.punnet

import android.util.Log
import com.cmora.froglab_2.genetics.GenomeModel
import com.cmora.froglab_2.genetics.Haplotype
import com.cmora.froglab_2.genetics.Sex
import org.json.JSONArray
import org.json.JSONObject

class PunnetProblemInitializer {
    companion object {
        fun buildProblemTypeFromJSON(problemtypestr: String, genome: GenomeModel): PunnetProblemType {
            Log.d("PunnetProblemInit", "1: Building bastard problem type from .json")
            Log.d("PunnetProblemInit", problemtypestr)

            val jsonobj = JSONObject(problemtypestr)
            var name : String = jsonobj.getString("name")
            var description : String = jsonobj.getString("description")
            var hap1: Haplotype = mutableListOf()
            var hap2: Haplotype = mutableListOf()
            var genelist: MutableList<Int> = mutableListOf()
            var f1_sex: Sex
            var right_possibilities: MutableList<MutableList<String>> = mutableListOf()
            var wrong_sperm: MutableList<String> = mutableListOf()
            var wrong_eggs: MutableList<String> = mutableListOf()
            var individual_names: MutableList<String> = mutableListOf()
            var haplotype_list: MutableList<Haplotype> = mutableListOf()
            var points_right:Int
            var points_wrong:Int
            var numSperms: Int
            var numEggs: Int

            Log.d("PunnetProblemInit", "2: Variables declared")
            var aux_array = jsonobj.getJSONArray("base_haplotype1")
            Log.d("PunnetProblemInit", "3: got haplotype1")
            for(i in 0 until aux_array.length()) {
                hap1.add(genome.getAlleleIdByName(i, aux_array.getString(i)))
            }
            Log.d("PunnetProblemInit", "4: converted haplotype 1")
            aux_array = jsonobj.getJSONArray("base_haplotype2")
            for(i in 0 until aux_array.length()) {
                hap2.add(genome.getAlleleIdByName(i, aux_array.getString(i)))
            }
            Log.d("PunnetProblemInit", "5")
            aux_array = jsonobj.getJSONArray("problem_genes")
            for(i in 0 until aux_array.length()) {
                genelist.add(genome.getGeneIdByName(aux_array.getString(i)))
            }
            Log.d("PunnetProblemInit", "6")
            aux_array = jsonobj.getJSONArray("haplotype_names")
            var aux_array2: JSONArray
            for(i in 0 until aux_array.length()) {
                Log.d("PunnetProblemInit", "7, getting haplotype $i ")
                aux_array2 = jsonobj.getJSONArray(aux_array.getString(i))
                Log.d("PunnetProblemInit", "8, got haplotype $i: $aux_array2")
                haplotype_list.add(i, mutableListOf<Int>())
                Log.d("PunnetProblemInit", "9, haplotype_list $haplotype_list")
                for(j in 0 until aux_array2.length())
                    haplotype_list.get(i).add(genome.getAlleleIdByName(genelist[j], aux_array2.getString(j)))
            }

            Log.d("PunnetProblemInit", "10")


            numSperms = jsonobj.getInt("num_sperms")
            numEggs = jsonobj.getInt("num_eggs")

            points_right = jsonobj.getInt("points_right")
            points_wrong = jsonobj.getInt("points_wrong")

            aux_array = jsonobj.getJSONArray("names")
            for(i in 0 until aux_array.length()) {
                individual_names.add(aux_array.getString(i))
            }
            f1_sex = Sex.valueOf(jsonobj.getString("f1_sex"))

            aux_array =jsonobj.getJSONArray("right_possibilities") // [["a","a"]],
            for(i in 0 until aux_array.length()) {
                Log.d("PunnetProblemInit", "11 getting right_possibilities $i from $aux_array")
                aux_array2 = aux_array.getJSONArray(i)
                Log.d("PunnetProblemInit", "12, got right_possibilities $i: $aux_array2")
                right_possibilities.add(i, mutableListOf<String>())
                Log.d("PunnetProblemInit", "13, right_possibilities list $right_possibilities")
                for (j in 0 until aux_array2.length())
                    right_possibilities.get(i)
                        .add(aux_array2.getString(j)) //genome.getAlleleIdByName(genelist[j], aux_array2.getString(j)
            }

            aux_array =jsonobj.getJSONArray("wrong_possibilities_male")
            for(i in 0 until aux_array.length()) {
                Log.d("PunnetProblemInit", "14 getting wrong sperm $i ")
                wrong_sperm.add(i, aux_array.getString(i))
            }
            aux_array =jsonobj.getJSONArray("wrong_possibilities_female")
            for(i in 0 until aux_array.length()) {
                Log.d("PunnetProblemInit", "15 getting wrong egg $i ")
                wrong_eggs.add(i, aux_array.getString(i))
            }

            var gene_groups: MutableList<MutableList<Int>>? = null
            if(jsonobj.has("gene_groups")){
                Log.d("PunnetProblemInit", "16: has gene_groups")
                aux_array = jsonobj.getJSONArray("gene_groups")
                Log.d("PunnetProblemInit", "17: aux_array_len: ${aux_array.length()}")
                gene_groups = mutableListOf()
                var group: MutableList<Int>
                var aux_array2 : JSONArray
                for(i in 0 until aux_array.length()){
                    aux_array2 = aux_array.getJSONArray(i)
                    group = mutableListOf()
                    for(j in 0 until aux_array2.length())
                        group.add(genome.getGeneIdByName(aux_array2.getString(j)))
                    Log.d("PunnetProblemInit", "18: group: $group")
                    gene_groups.add(group.toMutableList())
                    Log.d("PunnetProblemInit", "19: gene_groups: $gene_groups")
                }
            }
            Log.d("PunnetProblemTypeInit", "20: gene_groups:${gene_groups}")
            var problem_type = PunnetProblemType(
                name, description, genome,
                hap1, hap2, genelist,
                haplotype_list,
                f1_sex,
                right_possibilities,
                wrong_sperm, wrong_eggs,
                numSperms, numEggs,
                individual_names,
                points_right,
                points_wrong,
                gene_groups
            )

            Log.d("PunnetProblemInit", "21: problem_type initialized. Returning")

            return problem_type
        }
    }
}