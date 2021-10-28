package code.psm.music.player.interfaces

import android.view.View
import code.psm.music.player.model.Genre

interface IGenreClickListener {
    fun onClickGenre(genre: Genre, view: View)
}