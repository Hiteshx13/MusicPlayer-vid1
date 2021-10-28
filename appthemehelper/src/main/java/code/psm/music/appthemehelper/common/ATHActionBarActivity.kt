package code.psm.music.appthemehelper.common

import androidx.appcompat.widget.Toolbar

import code.psm.music.appthemehelper.util.ToolbarContentTintHelper

class ATHActionBarActivity : ATHToolbarActivity() {

    override fun getATHToolbar(): Toolbar? {
        return ToolbarContentTintHelper.getSupportActionBarView(supportActionBar)
    }
}
