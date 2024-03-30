package com.tcs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.domain.Video;
import com.tcs.exception.VideoNotFoundException;
import com.tcs.repository.VideoRepository;

import reactor.core.publisher.Mono;

@Service
public class StreamingService {

    @Autowired
    private VideoRepository videoRepository;


    public Mono<Void> saveVideo(String title, byte[] data) {
        return Mono.fromRunnable(() -> {
            Video video = new Video();
            video.setTitle(title);
            video.setVideoData(data);
            videoRepository.save(video);
        }).then();
    }
    public Mono<byte[]> getVideo(Long id) {
        return Mono.fromCallable(() -> {
            Video video = videoRepository.findById(id)
                    .orElseThrow(() -> new VideoNotFoundException("Video not found with ID: " + id));
            return video.getVideoData();
        });
    }
}