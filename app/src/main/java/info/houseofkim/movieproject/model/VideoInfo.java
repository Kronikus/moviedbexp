package info.houseofkim.movieproject.model;

import android.support.v7.widget.RecyclerView;

public class VideoInfo  {
    private String videoid;
    private String videokey;
    private String videoname;
    private int videomovieid;
    public VideoInfo (String videoid,String videokey, String videoname){
        this.videoid = videoid;
        this.videokey = videokey;
        this.videoname = videoname;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getVideokey() {
        return videokey;
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public int getVideomovieid() {
        return videomovieid;
    }

    public void setVideomovieid(int videomovieid) {
        this.videomovieid = videomovieid;
    }
}
