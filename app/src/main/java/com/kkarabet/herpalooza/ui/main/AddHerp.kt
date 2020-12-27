package com.kkarabet.herpalooza.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.kkarabet.herpalooza.R
import com.kkarabet.herpalooza.database.Herp
import kotlinx.android.synthetic.main.add.*
import java.io.File
import java.util.*
import com.squareup.picasso.Picasso

class AddHerp : DialogFragment(), AdapterView.OnItemSelectedListener{
    private var listener: AddHerpListener? = null
    private var newHerp: Herp = Herp()
    private val viewModel: MainViewModel by activityViewModels()

    interface AddHerpListener {
        fun onAddHerp(herp: Herp)
    }

    private lateinit var name:EditText
    private lateinit var species:EditText
    private lateinit var age:EditText
    private lateinit var location:EditText
    private lateinit var habitat:EditText
    private lateinit var notes:EditText
    private lateinit var title:TextView
    private var uuid = UUID.randomUUID()
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private val picasso = Picasso.get()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.editing.value!! == 1){
            val herp = viewModel.currentProduct.value!!
            uuid = UUID.fromString(herp.uuid)
        }
        photoFile = File(context?.applicationContext?.filesDir, "IMG_$uuid.jpg")
        photoUri = FileProvider.getUriForFile(
            requireActivity(),
            "com.kkarabet.herpalooza.fileprovider",
            photoFile
        )
        if(viewModel.editing.value!! == 1){
            if (photoFile.exists()) {
                picasso.load(photoUri)
                    .fit()
                    .centerCrop()
                    .into(imageView)
            }
        }

        name = view.findViewById(R.id.name)
        species = view.findViewById(R.id.species)
        age = view.findViewById(R.id.age)
        location = view.findViewById(R.id.location)
        habitat = view.findViewById(R.id.habitat)
        notes = view.findViewById(R.id.notes)
        title = view.findViewById(R.id.textView)
        typeSpinner.onItemSelectedListener = this
        sexSpinner.onItemSelectedListener = this


        if (viewModel.editing.value!! == 1){
            val herp = viewModel.currentProduct.value!!
            title.setText(R.string.edit)
            name.setText(herp.name)
            species.setText(herp.species)
            age.setText(herp.age)
            location.setText(herp.location)
            habitat.setText(herp.place)
            notes.setText(herp.note)
            typeSpinner.setSelection(herp.type)
            sexSpinner.setSelection(herp.sex)
        }else{
            title.setText(R.string.add)
        }

        imageButton2.setOnClickListener{
            if (viewModel.editing.value!! == 0){
                newHerp.name = name.text.toString()
                newHerp.age = age.text.toString()
                newHerp.species = species.text.toString()
                newHerp.location = location.text.toString()
                newHerp.place= habitat.text.toString()
                newHerp.note = notes.text.toString()
                newHerp.found = viewModel.found.value!!
                newHerp.uuid = uuid.toString()
                listener?.onAddHerp(newHerp)
                dismiss()
            }else{
                val herp = viewModel.currentProduct.value!!
                herp.name = name.text.toString()
                viewModel.upDateHerp(herp)
                herp.age = age.text.toString()
                viewModel.upDateHerp(herp)
                herp.species = species.text.toString()
                viewModel.upDateHerp(herp)
                herp.location = location.text.toString()
                viewModel.upDateHerp(herp)
                herp.place= habitat.text.toString()
                viewModel.upDateHerp(herp)
                herp.note = notes.text.toString()
                viewModel.upDateHerp(herp)
                herp.found = viewModel.found.value!!
                viewModel.upDateHerp(herp)
                herp.uuid= uuid.toString()
                viewModel.upDateHerp(herp)
                dismiss()
            }

        }

        imageButton.apply {
            val thingy = requireActivity().packageManager
            val capture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolve =
                thingy.resolveActivity(capture, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolve == null || !cameraPermission()) {
                isEnabled = false
            }
            setOnClickListener {
                capture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities =
                    thingy.queryIntentActivities(capture, PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                    )
                    startActivityForResult(capture, 0)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (viewModel.editing.value!! == 0){
            when (parent){
                typeSpinner -> newHerp.type = position
            }
            when (parent){
                sexSpinner -> newHerp.sex = position
            }
        }else{
            val herp:Herp = viewModel.currentProduct.value!!
            when (parent){
                typeSpinner -> herp.type = position
            }
            when (parent){
                sexSpinner -> herp.sex = position
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.add, container, false)
    }

    companion object {
        fun newInstance(listener: AddHerpListener) = AddHerp().apply {
            this.listener = listener
        }
    }

    private fun cameraPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.CAMERA)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == 0 -> {
                requireActivity().revokeUriPermission(
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                if (photoFile.exists()) {
                    picasso.load(photoUri)
                        .fit()
                        .centerCrop()
                        .into(imageView)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }


}