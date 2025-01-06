package service;

import model.Video;
import repository.VideoRepository;

import java.util.List;

public abstract class VideoServiceImpl implements VideoService {
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
}