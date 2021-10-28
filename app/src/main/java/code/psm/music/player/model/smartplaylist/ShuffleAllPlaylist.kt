package code.psm.music.player.model.smartplaylist

import code.psm.music.player.App
import code.psm.music.player.R
import code.psm.music.player.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class ShuffleAllPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.action_shuffle_all),
    iconRes = R.drawable.ic_shuffle
) {
    override fun songs(): List<Song> {
        return songRepository.songs()
    }
}