package code.psm.music.player.activities

import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContentProviderCompat.requireContext
import code.psm.music.appthemehelper.ThemeStore
import code.psm.music.appthemehelper.util.VersionUtils
import code.psm.music.player.R
import code.psm.music.player.activities.base.AbsBaseActivity
import code.psm.music.player.appshortcuts.DynamicShortcutManager

class SplashActivity : AbsBaseActivity() {

    private val SPLASH_TIME_OUT = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        ThemeStore.editTheme(this).accentColor(getColor(R.color.color_splash)).commit()
//        if (VersionUtils.hasNougatMR())
//            DynamicShortcutManager(this).updateDynamicShortcuts()

        Handler().postDelayed(Runnable {
            launchActivity(PlayerSelectActivity.getIntent(this))
            finish()
        }, SPLASH_TIME_OUT.toLong())

    }


}