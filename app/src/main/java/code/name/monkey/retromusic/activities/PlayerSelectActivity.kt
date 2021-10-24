package code.name.monkey.retromusic.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.databinding.ActivityPlayerselectBinding

class PlayerSelectActivity : AbsBaseActivity() {
var binding: ActivityPlayerselectBinding?=null

    companion object{
        fun getIntent(context: Context): Intent {
            var intent = Intent(context, PlayerSelectActivity::class.java)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_playerselect)
//
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playerselect)
//
        binding?.ivMusic?.setOnClickListener { launchActivity(MainActivity.getIntent(this)) }
        binding?.ivVideo?.setOnClickListener {
            var intent = Intent(this, VideoFolderListActivity::class.java)
            launchActivity(intent)
//        }
//
//        Handler().postDelayed(Runnable {
//            launchActivity(MainActivity.getIntent(this))
//
//        }, SPLASH_TIME_OUT.toLong())

        }
    }
}

