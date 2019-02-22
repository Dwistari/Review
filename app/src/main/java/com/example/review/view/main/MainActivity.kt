package com.example.review.view.main

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.review.R
import com.example.review.data.model.Review
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity: AppCompatActivity(), MainView {
    private lateinit var adapter: MainAdapter
    private lateinit var presenter: MainPresenter

    private val GALLERY = 1
    private val CAMERA = 2
    var description: RequestBody? = null
    var rating: RequestBody? = null
    var image: MultipartBody.Part? = null
    private var imageUriFromCamera: Uri? = null

    private var permission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    override fun showLoading() {
        loadings.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loadings.visibility = View.GONE
    }


    override fun showErrorAlert(it: Throwable) {
        Log.e("Main", it.localizedMessage)
        Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun showData(review: MutableList<Review>)
    {
        adapter.setData(review)
//        swipe_refresh?.isRefreshing = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecylerView()
        initPresenter()
        presenter.getReview()


        upload!!.setOnClickListener {
            showPictureDialog()
            Toast.makeText(this@MainActivity, "UPLOAD ",Toast.LENGTH_SHORT)

        }


        btn_submit.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {

                if (input_text.equals("")) {
                    Toast.makeText(this@MainActivity, "Harap masukan feedback anda ", Toast.LENGTH_SHORT)
                } else {

                    description = RequestBody.create(MediaType.parse("text/plain"), input_text.text.toString())
                    presenter.createReview()

                    Toast.makeText(this@MainActivity, "Thanks for your feedback ", Toast.LENGTH_SHORT)

                }
            }
        })

    }

    private fun isHaveStorageAndCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val cameraPermission = checkSelfPermission(Manifest.permission.CAMERA)
            val writeExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return !(storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED || writeExternalPermission != PackageManager.PERMISSION_GRANTED)
        } else {
            return true
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        val bm = Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        ) as Bitmap
        return bm
    }

    private fun getRealPathFromURI(contentUri: Uri?): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor
            ?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)!!
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //ASK_PERMISSIONS_REQUEST_CODE
        }

        if (isHaveStorageAndCameraPermission()) {
            showPictureDialog()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, GALLERY)//ASK_PERMISSIONS_REQUEST_CODE
            }
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)

    }

    private fun takePhotoFromCamera() {
        val values = ContentValues()
        imageUriFromCamera = contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    display_img!!.setImageBitmap(bitmap)

                    val selectedImage = data.data
                    val projection = arrayOf(MediaStore.MediaColumns.DATA)
                    val cursor = managedQuery(selectedImage, projection, null, null, null)
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    cursor.moveToFirst()
                    val path = cursor.getString(columnIndex)
                    val file = File(path)

                    val type: String?
                    val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
                    type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                    val reqfile = RequestBody.create(MediaType.parse(type), file)
                    image = MultipartBody.Part.createFormData("image", file.name, reqfile)
                    Log.d("IMAGEPATH", file.path)


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            } else if (requestCode == CAMERA) {

                val thumbnail = MediaStore.Images.Media.getBitmap(contentResolver, imageUriFromCamera)
                val ei = ExifInterface(getRealPathFromURI(imageUriFromCamera))
                val orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                val rotatedBitmap: Bitmap?
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> {
                        rotatedBitmap = rotateImage(thumbnail, 90f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_180 -> {
                        rotatedBitmap = rotateImage(thumbnail, 180f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_270 -> {
                        rotatedBitmap = rotateImage(thumbnail, 270f)
                    }
                    else -> rotatedBitmap = thumbnail
                }

                val bytes = ByteArrayOutputStream()
                val compressedBitmap = Bitmap.createScaledBitmap(
                    rotatedBitmap,
                    rotatedBitmap?.width!! / 4,
                    rotatedBitmap.height / 4,
                    false
                )
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
//                display_img.setImageBitmap(compressedBitmap)
                val destination = File(
                    Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis().toString() + ".jpg"
                )
                val fo: FileOutputStream
                try {
                    destination.createNewFile()
                    fo = FileOutputStream(destination)
                    fo.write(bytes.toByteArray())
                    fo.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val type: String
                val extension = MimeTypeMap.getFileExtensionFromUrl(destination.getAbsolutePath())
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                val reqFile = RequestBody.create(MediaType.parse(type), destination)
                image = MultipartBody.Part.createFormData("image", destination.getName(), reqFile)
            }
        }
    }




    private fun initRecylerView() {
        card_recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter()
        card_recycler_view.adapter = adapter
    }
    private fun initPresenter() {
        presenter = MainPresenterImp()
        presenter.initView(this)
    }

}