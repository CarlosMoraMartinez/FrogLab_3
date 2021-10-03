package com.cmora.froglab_2.bastard

import android.util.Log
import com.cmora.froglab_2.genetics.GenomeModel
import com.cmora.froglab_2.genetics.Haplotype
import com.cmora.froglab_2.genetics.Sex
import com.cmora.froglab_2.laboratory_use_case.ProblemType
import org.json.JSONArray
import org.json.JSONObject

class BastardProblemInitializer {
    companion object {
        fun buildProblemTypeFromJSON(problemtypestr: String, genome: GenomeModel): BastardProblemType {
            Log.d("BastardProblemTypeInit", "1: Building bastard problem type from .json")
            Log.d("BastardProblemTypeInit", problemtypestr)

            val jsonobj = JSONObject(problemtypestr)
            var name : String = jsonobj.getString("name")
            var description : String = jsonobj.getString("description")
            var hap1: Haplotype = mutableListOf()
            var hap2: Haplotype = mutableListOf()
            var genelist: MutableList<Int> = mutableListOf()
            var options_sex = mutableListOf<Sex>()
            var individual_names: MutableList<String> = mutableListOf()
            var haplotype_list: MutableList<Haplotype> = mutableListOf()

            Log.d("BastardProblemTypeInit", "2: Variables declared")
            var aux_array = jsonobj.getJSONArray("base_haplotype1")
            Log.d("BastardProblemTypeInit", "3: got haplotype1")
            for(i in 0 until aux_array.length()) {
                hap1.add(genome.getAlleleIdByName(i, aux_array.getString(i)))
            }
            Log.d("BastardProblemTypeInit", "4: converted haplotype 1")
            aux_array = jsonobj.getJSONArray("base_haplotype2")
            for(i in 0 until aux_array.length()) {
                hap2.add(genome.getAlleleIdByName(i, aux_array.getString(i)))
            }
            Log.d("BastardProblemTypeInit", "5")
            aux_array = jsonobj.getJSONArray("problem_genes")
            for(i in 0 until aux_array.length()) {
                genelist.add(genome.getGeneIdByName(aux_array.getString(i)))
            }
            Log.d("BastardProblemTypeInit", "6")
            aux_array = jsonobj.getJSONArray("haplotype_names")
            var aux_array2: JSONArray
            for(i in 0 until aux_array.length()) {
                Log.d("BastardProblemTypeInit", "7, getting haplotype $i ")
                aux_array2 = jsonobj.getJSONArray(aux_array.getString(i))
                Log.d("BastardProblemTypeInit", "8, got haplotype $i: $aux_array2")
                haplotype_list.add(i, mutableListOf<Int>())
                Log.d("BastardProblemTypeInit", "9, haplotype_list $haplotype_list")
                for(j in 0 until aux_array2.length())
                    haplotype_list.get(i).add(genome.getAlleleIdByName(genelist[j], aux_array2.getString(j)))
            }

            Log.d("BastardProblemTypeInit", "10")

            var answer = jsonobj.getInt("answer")
            var all_correct = jsonobj.getInt("all_correct")
            var none_correct = jsonobj.getInt("none_correct")
            var no_info = jsonobj.getInt("no_info")

            var points_right = jsonobj.getInt("points_right")
            var points_wrong = jsonobj.getInt("points_wrong")
            aux_array = jsonobj.getJSONArray("names")
            for(i in 0 until aux_array.length()) {
                individual_names.add(aux_array.getString(i))
            }
            aux_array = jsonobj.getJSONArray("problem_f2_sex")
            for(i in 0 until aux_array.length()) {
                options_sex.add(Sex.valueOf(aux_array.getString(i)))
            }

            var gene_groups: MutableList<MutableList<Int>>? = null
            if(jsonobj.has("gene_groups")){
                Log.d("BastardProblemTypeInit", "10b: has gene_groups")
                aux_array = jsonobj.getJSONArray("gene_groups")
                Log.d("BastardProblemTypeInit", "10c: aux_array_len: ${aux_array.length()}")
                gene_groups = mutableListOf()
                var group: MutableList<Int>
                var aux_array2 : JSONArray
                for(i in 0 until aux_array.length()){
                    aux_array2 = aux_array.getJSONArray(i)
                    group = mutableListOf()
                    for(j in 0 until aux_array2.length())
                        group.add(genome.getGeneIdByName(aux_array2.getString(j)))
                    Log.d("BastardProblemTypeInit", "10d: group: $group")
                    gene_groups.add(group.toMutableList())
                    Log.d("BastardProblemTypeInit", "10e: gene_groups: $gene_groups")
                }
            }
            Log.d("BastardProblemTypeInit", "11: gene_groups:${gene_groups}")
            var problem_type = BastardProblemType(
                name, description, genome,
                hap1, hap2, genelist,
                haplotype_list,
                options_sex,
                answer,
                all_correct, none_correct, no_info,
                individual_names,
                points_right,
                points_wrong,
                gene_groups
            )

            Log.d("ProblemTypeInitializer", "12: problem_type initialized. Returning")

            return problem_type
        }
    }
}