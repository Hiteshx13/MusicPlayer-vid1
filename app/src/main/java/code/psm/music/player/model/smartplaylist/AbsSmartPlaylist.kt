package code.psm.music.player.model.smartplaylist

import androidx.annotation.DrawableRes
import code.psm.music.player.R
import code.psm.music.player.model.AbsCustomPlaylist

abstract class AbsSmartPlaylist(
    name: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_queue_music
) : AbsCustomPlaylist(
    id = PlaylistIdGenerator(name, iconRes),
    name = name
)