package code.name.monkey.retromusic.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.databinding.ActivityPlayerselectBinding

class PlayerSelectActivity : AbsBaseActivity() {
    var binding: ActivityPlayerselectBinding? = null
    var launchCode = 1

    companion object {
        fun getIntent(context: Context): Intent {
            var intent = Intent(context, PlayerSelectActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playerselect)
        binding?.ivMusic?.setOnClickListener {
            launchCode = 1
            if (isStoragePermissionGranted()) {
                openAudioVideo(launchCode)
            }else{
                launchActivity(PermissionActivity.getIntent(this))
            }
        }
        binding?.ivVideo?.setOnClickListener {
            launchCode = 2
            if (isStoragePermissionGranted()) {
                openAudioVideo(launchCode)
            }else{
                launchActivity(PermissionActivity.getIntent(this))
            }

        }
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

       return if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            === PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                1
//            )
            false

        }
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
        }
    }
}

