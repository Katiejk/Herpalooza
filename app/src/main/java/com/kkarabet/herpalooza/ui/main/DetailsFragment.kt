package com.kkarabet.herpalooza.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkarabet.herpalooza.R
import com.kkarabet.herpalooza.database.Herp
import com.kkarabet.herpalooza.databinding.DetailFragmentBinding
import com.kkarabet.herpalooza.databinding.RecyclerItemBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_fragment.*
import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

class DetailsFragment :Fragment(), AddHerp.AddHerpListener {

    private lateinit var imageView:ImageView
    private lateinit var name:TextView
    private lateinit var type:TextView
    private lateinit var species:TextView
    private lateinit var sex:TextView
    private lateinit var age:TextView
    private lateinit var loc:TextView
    private lateinit var hab:TextView
    private lateinit var notes:TextView
    private lateinit var found:TextView
    private lateinit var edit:ImageButton
    private val picasso = Picasso.get()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.setHasOptionsMenu(true)
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView2)
        name = view.findViewById(R.id.NameD)
        type = view.findViewById(R.id.typeD)
        species = view.findViewById(R.id.speciesD)
        sex = view.findViewById(R.id.sexD)
        age = view.findViewById(R.id.ageD)
        loc = view.findViewById(R.id.LocD)
        hab = view.findViewById(R.id.HabD)
        notes = view.findViewById(R.id.notesD)
        found = view.findViewById(R.id.foundD)
        edit = view.findViewById(R.id.edit)

        updateInfo(imageView,name,type,species,sex,age,loc,hab,notes,found)

        edit.setOnClickListener{
            val herp: Herp = viewModel.currentProduct.value!!
            viewModel.setFound(herp.found.toInt())
            viewModel.setFound(herp.found.toInt())
            viewModel.setEditing(1)
            showDialog()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.deleteMenu).setVisible(false)
        menu.findItem(R.id.AddItem).setVisible(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshMenu -> {
                updateInfo(imageView,name,type,species,sex,age,loc,hab,notes,found)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAddHerp(herp: Herp) {
        viewModel.upDateHerp(herp)
        updateInfo(imageView,name,type,species,sex,age,loc,hab,notes,found)

    }

    fun showDialog(){
        val dialog = AddHerp.newInstance(this)
        dialog.show(childFragmentManager, "dialog")
    }

    @SuppressLint("SetTextI18n")//Lazy
    fun updateInfo(imageView:ImageView, name:TextView, type:TextView, species:TextView, sex:TextView, age:TextView, loc:TextView, hab:TextView, notes:TextView, found:TextView){
        val herp: Herp = viewModel.currentProduct.value!!

        val uuid = UUID.fromString(herp.uuid)
        picasso.load(File(context?.filesDir, "IMG_$uuid.jpg"))
            .placeholder(R.drawable.gecko)
            .fit()
            .centerCrop()
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(imageView)
        name.setText(herp.name)
        species.setText("Species: " + herp.species)
        when (herp.sex){
            0 -> sex.setText("Sex: Male")
            1-> sex.setText("Sex: Female")
            2 -> sex.setText("Sex: Unknown")
        }

        when (herp.type){
            0 ->
                type.setText("Lizard")
            1 ->
                type.setText("Snake")
            2 ->
                type.setText("Turtle")
            3 ->
                type.setText("Tortoise")
            4 ->
                type.setText("Crocodilian")
            5 ->
                type.setText("Salamander")
            6 ->
                type.setText("Frog")
            7 ->
                type.setText("Toad")
            8 ->
                type.setText("Other")
        }
        age.setText("Age: " + herp.age)
        loc.setText("Location found: " + herp.location)
        hab.setText("Found: " + herp.place)
        notes.setText("Notes: " + herp.note)
        when (herp.found){
            0-> found.setText("Not yet found.")
            1-> found.setText("Found")
        }
    }
}