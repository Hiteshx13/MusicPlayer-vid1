package code.psm.music.player.activities

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.psm.music.player.R
import code.psm.music.player.adapter.VideoListAdapter
import code.psm.music.player.interfaces.IFolderClickListener
import code.psm.music.player.model.VideoModel
import code.psm.music.player.videoplayer.player.PlayerActivity
import java.util.*

class VideoListActivity : AppCompatActivity() {
    var al_images = ArrayList<VideoModel>()
    var videolist: RecyclerView? = null
    var count = 0
    private var mAdapter: VideoListAdapter? = null
    private val videocursor: Cursor? = null
    private val video_column_index = 0
    var listBitmap= ArrayList<Bitmap?>();
    var context: Context?=null

    /**
     * Called when the activity is first created.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context=this
        setContentView(R.layout.activity_video_list)
        //        ArrayList<VideoModel> list = getVideoFolders();
        val model = intent.getSerializableExtra("VideoData") as VideoModel?
//        getThumb(model!!.videoPath)
         mAdapter = VideoListAdapter(this,model!!.videoPath , listBitmap,object : IFolderClickListener {
            override fun onItemClick(position: Int) {
                var intent= Intent(context,PlayerActivity::class.java)
                intent.putExtra("video_path", model!!.videoPath[position])
                startActivity(intent)
            }
        })
        videolist = findViewById<View>(R.id.simpleGridView) as RecyclerView
        videolist!!.layoutManager = GridLayoutManager(this,2)
        videolist!!.adapter = mAdapter
        //        videolist.setOnItemClickListener(videogridlistener);
        //init_phone_video_grid();
    } //    public ArrayList<VideoModel> getVideoFolders() {


    fun getThumb(videoPath:ArrayList<String>) {

        videoPath?.forEach {
            val thumb = ThumbnailUtils.createVideoThumbnail(
                it,
                MediaStore.Images.Thumbnails.MINI_KIND
            )
            listBitmap.add(thumb)
        }

    }
    //        boolean boolean_folder = false;
    //        al_images.clear();
    //
    //        int int_position = 0;
    //        Uri uri;
    //        Cursor cursor;
    //        int column_index_data, column_index_folder_name;
    //
    //        String absolutePathOfImage = null;
    //        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    //
    //        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
    //
    //        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
    //        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
    //
    //        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    //        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
    //        while (cursor.moveToNext()) {
    //            absolutePathOfImage = cursor.getString(column_index_data);
    //            Log.e("Column", absolutePathOfImage);
    //            Log.e("Folder", cursor.getString(column_index_folder_name));
    //
    //            for (int i = 0; i < al_images.size(); i++) {
    //                if (al_images.get(i).getFolderName().equals(cursor.getString(column_index_folder_name))) {
    //                    boolean_folder = true;
    //                    int_position = i;
    //                    break;
    //                } else {
    //                    boolean_folder = false;
    //                }
    //            }
    //
    //
    //            if (boolean_folder) {
    //
    //                ArrayList<String> al_path = new ArrayList<>();
    //                al_path.addAll(al_images.get(int_position).getVideoPath());
    //                al_path.add(absolutePathOfImage);
    //                al_images.get(int_position).setVideoPath(al_path);
    //
    //            } else {
    //                ArrayList<String> al_path = new ArrayList<>();
    //                al_path.add(absolutePathOfImage);
    //                VideoModel obj_model = new VideoModel();
    //                obj_model.setFolderName(cursor.getString(column_index_folder_name));
    //                obj_model.setVideoPath(al_path);
    //
    //                al_images.add(obj_model);
    //
    //
    //            }
    //
    //
    //        }
    //
    //
    //        for (int i = 0; i < al_images.size(); i++) {
    //            Log.e("##FOLDER", al_images.get(i).getFolderName());
    //            for (int j = 0; j < al_images.get(i).getVideoPath().size(); j++) {
    //                Log.e("FILE", al_images.get(i).getVideoPath().get(j));
    //            }
    //        }
    //
    //        return al_images;
    //    }
}