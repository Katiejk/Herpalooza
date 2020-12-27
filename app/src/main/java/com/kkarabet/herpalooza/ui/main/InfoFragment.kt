package com.kkarabet.herpalooza.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.kkarabet.herpalooza.R

class InfoFragment  : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.setHasOptionsMenu(true)
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}