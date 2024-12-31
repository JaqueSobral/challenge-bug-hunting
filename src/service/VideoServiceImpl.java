package service;

import model.Video;
import repository.VideoRepository;

import java.util.List;

public class VideoServiceImpl implements VideoService {
    private final VideoRepository repository;

    public VideoServiceImpl(VideoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addVideo(Video video) {
        repository.save(video);
    }

    @Override
    public List<Video> listVideos(String query) {
        return repository.findAll();
    }

    @Override
    public Video findVideoByTitle(String titulo) {
        return null;
    }

    @Override
    public boolean removeVideoList(Video video) {
        return true;
    }

    @Override
    public List<Video> filterByCategory(String categoria) {
        return List.of();
    }

    @Override
    public List<Video> sortByDate() {
        return List.of();
    }

    @Override
    public String getTotalVideos() {
        return "";
    }

    @Override
    public String getTotalDuration() {
        return "";
    }

    @Override
    public String getVideosByCategory() {
        return "";
    }
}