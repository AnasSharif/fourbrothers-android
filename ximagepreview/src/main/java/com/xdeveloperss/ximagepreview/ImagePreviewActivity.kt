package com.xdeveloperss.ximagepreview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.transition.Transition
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import java.io.File
import kotlin.math.abs

class ImagePreviewActivity : AppCompatActivity(), OnItemClickListener<PreviewFile> {
    private lateinit var toolbar: Toolbar
    private lateinit var vPager: ViewPager
    private var mUriList: List<PreviewFile> = listOf()
    private var mBitmap: Bitmap? = null
    private var view: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        toolbar = findViewById(R.id.toolbar)
        vPager = findViewById(R.id.vPager)
        setSupportActionBar(toolbar)
        toolbar.title = ""
        toolbar.subtitle = ""
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setUpViews()
    }

    private fun setUpViews() {
        mUriList = intent.getSerializableExtra(IMAGE_LIST) as List<PreviewFile>
        val slideAdapter = SlideAdapter(this@ImagePreviewActivity, mUriList, this)
        vPager.adapter = slideAdapter
        vPager.currentItem = intent.getIntExtra(CURRENT_ITEM, 0)
        vPager.setPageTransformer(false
        ) { page, position ->
            val normalizedPosition = abs(abs(position) - 1)
            page.scaleX = normalizedPosition / 2 + 0.5f
            page.scaleY = normalizedPosition / 2 + 0.5f
        }
    }

    override fun onItemClick(item: PreviewFile) {
        toolbar.visibility = if (toolbar.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save_share, menu)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            menu?.findItem(R.id.menu_save)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_save) {
            view = "Save"
            Glide.with(this@ImagePreviewActivity)
                .asBitmap()
                .load(mUriList[vPager.currentItem].imageURL)
                .into(object : SimpleTarget<Bitmap?>() {
                   override fun onResourceReady(resource: Bitmap, transition:  com.bumptech.glide.request.transition.Transition<in Bitmap?>?) {
                        handleSavePermission(resource)
                    }
                })
        } else if (item.itemId == R.id.menu_share) {
            view = "Share"
            Glide.with(this@ImagePreviewActivity)
                .asBitmap()
                .load(mUriList[vPager.currentItem].imageURL)
                .into(object : SimpleTarget<Bitmap?>() {
                   override fun onResourceReady(
                       resource: Bitmap,
                       transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                   ) {
                        handleSharePermission(mUriList[vPager.currentItem].imageDescription)
                    }
                })
        }
        return true
    }

    private fun handleSavePermission(resource: Bitmap) {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (CheckPermission.hasPermission(permission, this@ImagePreviewActivity)) {
            Util.saveImageToGallery(this@ImagePreviewActivity, resource)
        } else {
            mBitmap = resource
            CheckPermission.requestPerm(
                arrayOf(permission),
                CheckPermission.PERMISSION_STORAGE, this@ImagePreviewActivity
            )
        }
    }

    private fun handleSharePermission(resource: String) {
        socialShare(resource)
    }

    private fun socialShare(fileName: String, packageName:String? = null){
        val file = File(Util.getCreationFolder(this) +fileName)
        val uri = FileProvider.getUriForFile(
            this,
            this.packageName + ".provider",
            file
        )
        val share = Intent(Intent.ACTION_SEND)
        packageName?.let {
            share.setPackage(packageName)
        }
        share.putExtra(Intent.EXTRA_STREAM, uri)
        share.type = "image/jpeg"
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(share, getString(R.string.share_pirsma_photo)))
    }

   override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CheckPermission.PERMISSION_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (view == "Save") {
                    handleSavePermission(mBitmap!!)
                }
            } else {
                var i = 0
                val len = permissions.size
                while (i < len) {
                    val permission = permissions[i]
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // user rejected the permission
                        val showRationale: Boolean =
                            shouldShowRequestPermissionRationale(permission)
                        if (Manifest.permission.WRITE_EXTERNAL_STORAGE == permission || Manifest.permission.READ_EXTERNAL_STORAGE == permission) {
                            if (!showRationale) {
                                showAlertDialog()
                            } else shouldShowRequestPermissionRationale(permission)
                        }
                    }
                    i++
                }
            }
        }
    }

    private fun showAlertDialog() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this@ImagePreviewActivity).setPositiveButton("GO TO SETTING"
            ) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
                .setNegativeButton("DENY"
                ) { dialog, which -> dialog.dismiss() }
                .setTitle("Permission denied")
                .setMessage(
                    "Without storage permission the app" +
                            " is unable to open gallery or to save photos." +
                            " Are you sure want to deny this permission?"
                )
        builder.create().show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        var IMAGE_LIST = "intent_image_item"
        var CURRENT_ITEM = "current_item"
    }
}