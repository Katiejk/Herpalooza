package com.kkarabet.herpalooza.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kkarabet.herpalooza.R
import com.kkarabet.herpalooza.database.Herp
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.add.*
import kotlinx.android.synthetic.main.recycler_item.*
import kotlinx.android.synthetic.main.recycler_item.view.*
import java.io.File
import com.kkarabet.herpalooza.databinding.RecyclerItemBinding
import java.lang.IllegalArgumentException
import java.util.*

class MainFragment : Fragment(), AddHerp.AddHerpListener {


    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:HerpAdapter
    private val picasso = Picasso.get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        this.setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAddHerp(herp: Herp) {
        viewModel.insert(herp)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.refreshMenu).setVisible(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HerpAdapter()
        recycler = view.findViewById(R.id.RecyclerView1)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        viewModel.getList().observe(viewLifecycleOwner, Observer{
              adapter.updateHerps(it)
        })

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val herp = adapter.getHerpAtPosition(viewHolder.adapterPosition)
                    viewModel.delete(herp)
                    deletedToast(herp)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.AddItem -> {
                viewModel.setFound(0)
                viewModel.setEditing(0)
                showDialog()
                true
            }R.id.deleteMenu ->{
                deleteAllAlert()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDialog(){
        val dialog = AddHerp.newInstance(this)
        dialog.show(childFragmentManager, "dialog")
    }


    private inner class HerpViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root),  View.OnClickListener {
        private lateinit var herp: Herp
        private val NameTextView: TextView = itemView.textView3
        private val typeTextView: TextView = itemView.textView5
        private val checkBox:CheckBox = itemView.checkBox
        private val imageView:ImageView = itemView.imageView4
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            viewModel.setCurrent(herp)
            v!!.findNavController().navigate(R.id.mainToD)

        }


        @SuppressLint("SetTextI18n") //Laaaaazy
        fun bind(herp: Herp) {
            this.herp = herp
            NameTextView.text = herp.name
            when (herp.type){
                0 ->
                    typeTextView.setText("Type: Lizard")
                1 ->
                    typeTextView.setText("Type: Snake")
                2 ->
                    typeTextView.setText("Type: Turtle")
                3 ->
                    typeTextView.setText("Type: Tortoise")
                4 ->
                    typeTextView.setText("Type: Crocodilian")
                5 ->
                    typeTextView.setText("Type: Salamander")
                6 ->
                    typeTextView.setText("Type: Frog")
                7 ->
                    typeTextView.setText("Type: Toad")
                8 ->
                    typeTextView.setText("Type: Other")
            }

            try {//I don't remember why this was needed but it was. It might not still be as I figured out more about binding. But can't hurt?
                binding.apply {
                    binding.herp = herp
                    val uuid = UUID.fromString(herp.uuid)
                    picasso.load(File(context?.filesDir, "IMG_$uuid.jpg"))
                        .placeholder(R.drawable.gecko)
                        .fit()
                        .centerCrop()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(imageView)

                    executePendingBindings()
                }
            }catch (e:IllegalArgumentException){

            }

            when (herp.found){
                0 -> checkBox.isChecked=false
                1-> checkBox.isChecked=true
            }
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (herp.found == 0){
                    herp.found = 1
                    viewModel.upDateHerp(herp)
                }else{
                    herp.found = 0
                    viewModel.upDateHerp(herp)
                }
            }


        }
    }

    private inner class HerpAdapter : RecyclerView.Adapter<HerpViewHolder>() {
        var herps: List<Herp> = emptyList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HerpViewHolder =
            HerpViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.recycler_item, parent, false
                )
            )

        override fun getItemCount() = herps.size

        override fun onBindViewHolder(holder: HerpViewHolder, position: Int) {
            holder.bind(herps[position])
        }

        fun updateHerps(newHerps:List<Herp>){
            this.herps = newHerps
            notifyDataSetChanged()
        }

        fun getHerpAtPosition(position: Int): Herp{
            return herps[position]
        }
    }

    fun deleteAllAlert() {
        val msg = resources.getString(R.string.deleteAllAlert)
        val builder = AlertDialog.Builder(context)
        with(builder) {
            setTitle(R.string.alert)
            setMessage(msg)
            setIcon(R.drawable.ic_feedback)
            setNegativeButton(R.string.neg, null)
            setPositiveButton(R.string.pos, DialogInterface.OnClickListener{dialog,id-> viewModel.deleteAll()})
            show()
        }
    }

    fun deletedToast(herp:Herp){
        Toast.makeText(activity, "Deleted: " + herp.name, Toast.LENGTH_SHORT).show()
    }
}
