package com.metflix.controller;

import com.metflix.service.VideoStreamingService;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/videos/")
public class VideoController {

    final VideoStreamingService service;
    public static final String VideoUploadingDir = System.getProperty("user.dir") + "/src/main/resources/videos/";

    public VideoController(VideoStreamingService service) {
        this.service = service;
    }

    @GetMapping(value = "/{videoName}", produces = "application/octet-stream")
    public ResponseEntity<ResourceRegion> getVideo(@RequestHeader(value = "Range", required = false) String rangeHeader,
                                                   @PathVariable("videoName")String videoName)
            throws IOException {

        if (!new File(VideoUploadingDir).exists()) {
            new File(VideoUploadingDir).mkdirs();
        }
        return service.getVideoRegion(rangeHeader, VideoUploadingDir, videoName);

    }

}