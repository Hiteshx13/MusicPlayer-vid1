package code.name.monkey.retromusic.model;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoModel implements Serializable {
    String FolderName;
    ArrayList<String> VideoPath;

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public ArrayList<String> getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(ArrayList<String> videoPath) {
        VideoPath = videoPath;
    }
}