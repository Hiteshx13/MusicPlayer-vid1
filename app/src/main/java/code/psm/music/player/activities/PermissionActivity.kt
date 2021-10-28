/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.psm.music.player.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import code.psm.music.appthemehelper.ThemeStore
import code.psm.music.appthemehelper.util.VersionUtils
import code.psm.music.player.activities.base.AbsMusicServiceActivity
import code.psm.music.player.databinding.ActivityPermissionBinding
import code.psm.music.player.extensions.show
import code.psm.music.player.util.RingtoneManager

class PermissionActivity : AbsMusicServiceActivity() {
    private lateinit var binding: ActivityPermissionBinding

    companion object{
        fun getIntent(context: Context): Intent {
            var intent = Intent(context, PermissionActivity::class.java)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setStatusbarColorAuto()
//        setNavigationbarColorAuto()
        setLightNavigationBar(true)
//        setTaskDescriptionColorAuto()
        setupTitle()

        binding.storagePermission.setButtonClick {
            requestPermissions()
        }
        if (VersionUtils.hasMarshmallow()) binding.audioPermission.show()
        binding.audioPermission.setButtonClick {
            if (RingtoneManager.requiresDialog(this@PermissionActivity)) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + applicationContext.packageName)
                startActivity(intent)
            }
        }
//        binding.finish.accentBackgroundColor()
        binding.finish.setOnClickListener {
            if (hasPermissions()) {
                startActivity(
                    Intent(this, PlayerSelectActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                )
                finish()
            }
        }
    }

    private fun setupTitle() {
        val color = ThemeStore.accentColor(this)
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        val appName = HtmlCompat.fromHtml(
            "<b>PSM Media Player</span></b>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        binding.appNameText.text = appName
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        if (hasStoragePermission()) {
            binding.storagePermission.checkImage.visibility = View.VISIBLE
//            binding.storagePermission.checkImage.imageTintList =
//                ColorStateList.valueOf(ThemeStore.accentColor(this))
        }
        if (hasAudioPermission()) {
            binding.audioPermission.checkImage.visibility = View.VISIBLE
//            binding.audioPermission.checkImage.imageTintList =
//                ColorStateList.valueOf(ThemeStore.accentColor(this))
        }

        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasStoragePermission(): Boolean {
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasAudioPermission(): Boolean {
        return Settings.System.canWrite(this)
    }
}
