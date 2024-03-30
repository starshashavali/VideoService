package com.tcs.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.service.StreamingService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

	@Autowired
	private StreamingService streamingService;

	@PostMapping("/upload")
	public Mono<ResponseEntity<Void>> saveVideo(@RequestParam("title") String title,
			@RequestParam("file") MultipartFile file) {
		return Mono.fromRunnable(() -> {
			try {
				byte[] data = file.getBytes();
				streamingService.saveVideo(title, data).subscribe(); // Subscribe to trigger the saving
			} catch (IOException e) {
				e.printStackTrace();
				// Handle exception
			}
		}).then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
	}


	 @GetMapping("/{id}")
	    public Mono<ResponseEntity<ByteArrayResource>> getVideo(@PathVariable Long id) {
	        return streamingService.getVideo(id)
	                .map(videoData -> {
	                    // Create ByteArrayResource from the video data
	                    ByteArrayResource resource = new ByteArrayResource(videoData);

	                    // Build HttpHeaders with appropriate content type and content disposition
	                    HttpHeaders headers = new HttpHeaders();
	                    headers.setContentType(MediaType.parseMediaType("video/mp4")); // Set appropriate content type for MP4
	                    headers.setContentDispositionFormData("filename", "video.mp4"); // Set filename for download

	                    // Return ResponseEntity with ByteArrayResource and HttpHeaders
	                    return ResponseEntity.ok().headers(headers).body(resource);
	                })
	                .defaultIfEmpty(ResponseEntity.notFound().build())
	                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
	    }
	
	}