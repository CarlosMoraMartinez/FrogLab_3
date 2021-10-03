package com.cmora.froglab_2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.Serializable

class GenomeList : Serializable {
    data class GenomeId(val name: String, val resource: String, val description: String): Serializable {
    }

    val genomes: MutableList<GenomeId> = mutableListOf()

    constructor(genomestr: String) {
        Log.d("GENOMELIST", "file read: $genomestr")
        val jsonobj = JSONObject(genomestr)
        val aux_json_array = jsonobj.getJSONArray("genome_list")
        var aux: JSONObject
        for (i in 0 until aux_json_array.length()) {
            aux = aux_json_array.getJSONObject(i);
            Log.d("GENOMELIST", "reading array: $i : $aux")
            genomes.add(
                GenomeId(
                    aux.getString("name"),
                    aux.getString("resource"),
                    aux.getString("description")
                )
            )
        }
        Log.d("GENOMELIST", "list:  $genomes")
    }

    fun length(): Int{
        return genomes.size
    }
    fun get(i: Int): GenomeId{
        return genomes.get(i)
    }
}