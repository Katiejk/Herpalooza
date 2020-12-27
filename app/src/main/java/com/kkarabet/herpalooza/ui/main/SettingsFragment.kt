package com.kkarabet.herpalooza.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.kkarabet.herpalooza.MainActivity.Companion.SHOW_FOUND
import com.kkarabet.herpalooza.MainActivity.Companion.SHOW_TYPE
import com.kkarabet.herpalooza.MainActivity.Companion.SORT_BY
import com.kkarabet.herpalooza.R
import com.kkarabet.herpalooza.database.Herp
import kotlinx.android.synthetic.main.add.*
import kotlinx.android.synthetic.main.settings_fragment.*
import java.lang.NullPointerException

class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var foundSpinner:Spinner
    private lateinit var showSpinner:Spinner
    private lateinit var sortSpinner:Spinner


    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.setHasOptionsMenu(true)
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foundSpinner = view.findViewById(R.id.foundSpinner)
        showSpinner = view.findViewById(R.id.showSpinner)
        sortSpinner = view.findViewById(R.id.sortSpinner)
        foundSpinner.setSelection(settings.getInt(SHOW_FOUND,0))
        showSpinner.setSelection(settings.getInt(SHOW_TYPE,0))
        sortSpinner.setSelection(settings.getInt(SORT_BY,0))
        foundSpinner.onItemSelectedListener = this
        showSpinner.onItemSelectedListener = this
        sortSpinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent){
            foundSpinner -> with(settings.edit()) {
                putInt(SHOW_FOUND,position)
                apply()}

                showSpinner -> with(settings.edit()) {
                    putInt(SHOW_TYPE,position)
                    apply()}
                sortSpinner ->
                    with(settings.edit()) {
                        putInt(SORT_BY,position)
                        apply()}
        }
    }

}