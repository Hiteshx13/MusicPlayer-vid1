package code.psm.music.player.activities;

import android.content.Context;
import android.content.Intent;
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

import code.psm.music.player.R;
import code.psm.music.player.adapter.VideoFolderListAdapter;
import code.psm.music.player.interfaces.IFolderClickListener;
import code.psm.music.player.model.VideoModel;

public class VideoFolderListActivity extends AppCompatActivity {
    RecyclerView videolist;
    int count;
    private VideoFolderListAdapter mAdapter;
    private Cursor videocursor;
    private int video_column_index;
    private Context context;
    private final OnItemClickListener videogridlistener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position,
                                long id) {
            System.gc();
            video_column_index = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            videocursor.moveToPosition(position);
            String filename = videocursor.getString(video_column_index);
            /*   Intent intent = new Intent(MainActivity.this, ViewVideo.class);
                  intent.putExtra("videofilename", filename);
                  startActivity(intent);*/
            Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder_list);
        ArrayList<VideoModel> list = getVideoFolders();
        context=VideoFolderListActivity.this;
        mAdapter = new VideoFolderListAdapter(this, list, new IFolderClickListener() {
            @Override
            public void onItemClick(int position) {
                VideoModel model=list.get(position);
                Intent intent = new Intent(context,VideoListActivity.class);
                intent.putExtra("VideoData",  model);
                startActivity(intent);
            }
        });
        videolist = (RecyclerView) findViewById(R.id.simpleGridView);
        videolist.setLayoutManager(new LinearLayoutManager(this));
        videolist.setAdapter(mAdapter);
//        videolist.setOnItemClickListener(videogridlistener);
        //init_phone_video_grid();
    }
    public ArrayList<VideoModel> al_images = new ArrayList<>();
    public ArrayList<VideoModel> getVideoFolders() {
        boolean boolean_folder = false;
        al_images.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getFolderName().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getVideoPath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setVideoPath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                VideoModel obj_model = new VideoModel();
                obj_model.setFolderName(cursor.getString(column_index_folder_name));
                obj_model.setVideoPath(al_path);

                al_images.add(obj_model);


            }


        }


        for (int i = 0; i < al_images.size(); i++) {
            Log.e("##FOLDER", al_images.get(i).getFolderName());
            for (int j = 0; j < al_images.get(i).getVideoPath().size(); j++) {
                Log.e("FILE", al_images.get(i).getVideoPath().get(j));
            }
        }

        return al_images;
    }


}