package com.example.exoesqueletov1.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.exoesqueletov1.ConstantsDatabase
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.clases.Authentication
import com.example.exoesqueletov1.clases.Database
import com.example.exoesqueletov1.clases.Storge
import com.example.exoesqueletov1.databinding.FragmentProfileBinding
import com.example.exoesqueletov1.dialogs.DialogUpdateData
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class NewProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var textViewName: TextView
    private lateinit var textViewUser: TextView
    private lateinit var textViewDes: TextView
    private lateinit var textViewMail: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewCell: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewSchool: TextView
    private lateinit var circleImageViewProfile: CircleImageView

    private val PICK_IMAGE = 1
    private val CUT_PICTURE = 3535
    private val ASPECT_RATIO_X = 1
    private val ASPECT_RATIO_Y = 1

    private var id: String? = null
    private var database: Database? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonOk: Button
        val buttonChangeImage: CircleImageView
        binding.apply {
            textViewName = textProfileViewName
            textViewUser = textProfileViewUser
            textViewDes = textProfileViewDescription
            textViewMail = textProfileViewEmail
            textViewAddress = textProfileViewAddress
            textViewCell = textProfileViewCell
            textViewPhone = textProfileViewPhone
            textViewSchool = textProfileViewSchool
            circleImageViewProfile = imageViewProfileView
            buttonOk = buttonProfileViewSave
            buttonChangeImage = imageViewProfileView
        }
        id = Authentication().currentUser.email
        database = Database(fragmentManager, context)
        database!!.getProfile(
            id, textViewName, textViewUser, textViewDes, textViewMail,
            textViewAddress, textViewCell, textViewPhone, textViewSchool,
            circleImageViewProfile, binding.cardView3
        )
        textViewDes.setOnClickListener(this)
        textViewMail.setOnClickListener(this)
        textViewAddress.setOnClickListener(this)
        textViewCell.setOnClickListener(this)
        textViewPhone.setOnClickListener(this)
        textViewSchool.setOnClickListener(this)
        buttonOk.setOnClickListener(this)
        buttonChangeImage.setOnClickListener(this)
    }

    private fun okOnClick() {
        var user = "a"
        val data: MutableMap<String, Any> = HashMap()
        if (textViewUser.text == "Administrador") {
            user = "a"
        }
        if (textViewUser.text == "Fisioterapeuta") {
            user = "b"
        }
        if (textViewUser.text == "Paciente") {
            user = "c"
        }
        data[ConstantsDatabase.NAME] = textViewName.text.toString().trim { it <= ' ' }
        data[ConstantsDatabase.USER] = user
        data[ConstantsDatabase.DESCRIPTION] = textViewDes.text.toString().trim { it <= ' ' }
        data[ConstantsDatabase.EMAIL] = textViewMail.text.toString().trim { it <= ' ' }
        data[ConstantsDatabase.ADDRESS] = textViewAddress.text.toString().trim { it <= ' ' }
        data[ConstantsDatabase.CELL] = textViewCell.text.toString().trim { it <= ' ' }
        data[ConstantsDatabase.PHONE] = textViewPhone.text.toString().trim { it <= ' ' }
        data[ConstantsDatabase.SCHOOL] = textViewSchool.text.toString().trim { it <= ' ' }
        database!!.updateData(id, ConstantsDatabase.DOCUMENT_PROFILE, data)
    }

    private fun selectImage(context: Context?) {
        val options = arrayOf<CharSequence>(
            getString(R.string.chose_from_gallery),
            getString(R.string.cancelar)
        )
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.fto_de_perf_l))
        builder.setItems(options) { dialog: DialogInterface, item: Int ->
            if (options[item] == getString(R.string.chose_from_gallery)) {
                val gallery = Intent()
                gallery.type = "image/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(gallery, getString(R.string.fto_de_perf_l)),
                    PICK_IMAGE
                )
            }
            if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun cropCapturedImage(urlImagen: Uri?) {
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.setDataAndType(urlImagen, "image/*")
        cropIntent.putExtra("crop", "true")
        cropIntent.putExtra("aspectX", ASPECT_RATIO_X)
        cropIntent.putExtra("aspectY", ASPECT_RATIO_Y)
        cropIntent.putExtra("outputX", 400)
        cropIntent.putExtra("outputY", 250)
        cropIntent.putExtra("return-data", true)
        startActivityForResult(cropIntent, CUT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                PICK_IMAGE -> cropCapturedImage(data!!.data)
                CUT_PICTURE -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val resultUri = data.data
                    try {
                        val picturePath =
                            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, resultUri)
                        circleImageViewProfile!!.setImageBitmap(picturePath)
                        Storge(
                            fragmentManager, "pictureProfile",
                            Authentication().currentUser.email,
                            context
                        )
                            .setProfile(resultUri)
                    } catch (ignored: IOException) {
                    }
                }
            }
        }
    }

    private fun dialog(textView: TextView?, string: String, i: Int) {
        val updateData = DialogUpdateData(string, i, textView)
        updateData.show(parentFragmentManager, "algo")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.text_profile_view_description -> dialog(
                textViewDes,
                getString(R.string.description),
                0
            )
            R.id.text_profile_view_email -> dialog(
                textViewMail,
                getString(R.string.correo),
                R.drawable.ic_mail
            )
            R.id.text_profile_view_address -> dialog(
                textViewAddress,
                getString(R.string.direcci_n),
                R.drawable.ic_place
            )
            R.id.text_profile_view_cell -> dialog(
                textViewCell,
                getString(R.string.celular),
                R.drawable.ic_phone_android
            )
            R.id.text_profile_view_phone -> dialog(
                textViewPhone,
                getString(R.string.telefono),
                R.drawable.ic_phone
            )
            R.id.text_profile_view_school -> dialog(
                textViewSchool,
                getString(R.string.estudio),
                R.drawable.ic_school
            )
            R.id.button_profile_view_save -> okOnClick()
            R.id.image_view_profile_view -> selectImage(context)
        }
    }

}