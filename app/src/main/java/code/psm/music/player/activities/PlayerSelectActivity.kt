package code.psm.music.player.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import code.psm.music.appthemehelper.ThemeStore
import code.psm.music.appthemehelper.util.VersionUtils
import code.psm.music.player.R
import code.psm.music.player.activities.base.AbsBaseActivity
import code.psm.music.player.databinding.ActivityPlayerselectBinding
import com.google.android.material.snackbar.Snackbar

class PlayerSelectActivity : AbsBaseActivity() {
    var binding: ActivityPlayerselectBinding? = null
    var launchCode = 1
    private val snackBarContainer: View
        get() = window.decorView
    companion object {
        fun getIntent(context: Context): Intent {
            var intent = Intent(context, PlayerSelectActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playerselect)

        if (hasAudioPermission()) {

        }
//        if (VersionUtils.hasMarshmallow()) binding.audioPermission.show()
        binding?.ivMusic?.setOnClickListener {
            launchCode = 1
            if (isStoragePermissionGranted()) {
                openAudioVideo(launchCode)
            }else{
//                Toast.makeText(this,"Please grant required permissions from application settings",Toast.LENGTH_LONG).show()

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    125
                )
//    launchActivity(PermissionActivity.getIntent(this))
            }
        }
        binding?.ivVideo?.setOnClickListener {
            launchCode = 2
            if (isStoragePermissionGranted()) {
                openAudioVideo(launchCode)
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    125
                )
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasAudioPermission(): Boolean {
        return Settings.System.canWrite(this)
    }
    fun openAudioVideo(code: Int) {

        if (code == 1) {
            launchActivity(MainActivity.getIntent(this))
        } else {
            var intent = Intent(this, VideoFolderListActivity::class.java)
            launchActivity(intent)
        }
    }

    fun isStoragePermissionGranted(): Boolean {

       return (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               === PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0])
            openAudioVideo(launchCode)
        }else{
            Snackbar.make(
                snackBarContainer,
                R.string.permission_external_storage_denied,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(code.psm.music.player.R.string.action_settings) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(
                    "package",
                    this.packageName,
                    null
                )
                intent.data = uri
                startActivity(intent)
            }.setActionTextColor(ThemeStore.accentColor(this)).show()
        }
    }

}

