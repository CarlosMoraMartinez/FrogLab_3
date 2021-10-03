package com.cmora.froglab_2.tools

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.cmora.froglab_2.R


class HeaderFragment: Fragment() {
    override fun onCreateView(inflador: LayoutInflater, contenedor: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vista = inflador.inflate(
            R.layout.bastard_header_fragment,
            contenedor, false)
        contenedor?.setClipChildren(false);
        contenedor?.setClipToPadding(false);
        //This can be done in XML
        //This can be done in XML
        Log.d("CARLOS", "BastardHeaderFragment")
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var vg: ViewGroup? = view as ViewGroup
        while (vg != null) {
            vg.clipChildren = false
            vg.clipToPadding = false
            vg = if (vg.parent is ViewGroup) vg.parent as ViewGroup else null
        }
    }
}