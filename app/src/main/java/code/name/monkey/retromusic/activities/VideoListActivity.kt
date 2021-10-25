package code.name.monkey.retromusic.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.adapter.VideoFolderListAdapter;
import code.name.monkey.retromusic.adapter.VideoListAdapter;
import code.name.monkey.retromusic.interfaces.IFolderClickListener;
import code.name.monkey.retromusic.model.VideoModel;

public class VideoListActivity extends AppCompatActivity {
    public ArrayList<VideoModel> al_images = new ArrayList<>();
    RecyclerView videolist;
    int count;
    private VideoListAdapter mAdapter;
    private Cursor videocursor;
    private int video_column_index;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
//        ArrayList<VideoModel> list = getVideoFolders();
        VideoModel model=(VideoModel)getIntent().getSerializableExtra("VideoData");
        mAdapter = new VideoListAdapter(this, model.getVideoPath(), new IFolderClickListener() {
            @Override
            public void onItemClick(int position) {
//                VideoModel model=list.get(position);
            }
        });
        videolist = (RecyclerView) findViewById(R.id.simpleGridView);
        videolist.setLayoutManager(new LinearLayoutManager(this));
        videolist.setAdapter(mAdapter);
//        videolist.setOnItemClickListener(videogridlistener);
        //init_phone_video_grid();
    }

//    public ArrayList<VideoModel> getVideoFolders() {
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