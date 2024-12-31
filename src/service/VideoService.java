package service;

import model.Video;

import java.util.List;

public interface VideoService {
    void addVideo(Video video);

    List<Video> listVideos(String query);

    Video findVideoByTitle(String titulo);

    boolean removeVideoList(Video video);

    List<Video> filterByCategory(String categoria);

    List<Video> sortByDate();

    String getTotalVideos();

    String getTotalDuration();

    String getVideosByCategory();
}