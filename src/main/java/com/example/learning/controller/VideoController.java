package com.example.learning.controller;

import com.example.learning.dto.VideoDTO;
import com.example.learning.entity.Role;
import com.example.learning.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class VideoController {

    @Autowired private VideoService videoService;

    @GetMapping("/courses/{courseId}/videos")
    public List<VideoDTO> list(@PathVariable Long courseId){
        return videoService.listByCourse(courseId).stream().map(v -> {
            VideoDTO d = new VideoDTO();
            d.id = v.getId();
            d.title = v.getTitle();
            d.description = v.getDescription();
            d.url = v.getUrl();
            d.orderIndex = v.getOrderIndex();
            return d;
        }).collect(Collectors.toList());
    }

    @PostMapping("/courses/{courseId}/videos")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public VideoDTO create(@PathVariable Long courseId, @RequestBody VideoDTO req, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        var v = videoService.addVideo(courseId, req.title, req.description, req.url, req.orderIndex, auth.getName(), isAdmin);
        VideoDTO d = new VideoDTO();
        d.id = v.getId(); d.title = v.getTitle(); d.description = v.getDescription(); d.url = v.getUrl(); d.orderIndex = v.getOrderIndex();
        return d;
    }

    @PutMapping("/videos/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public VideoDTO update(@PathVariable Long id, @RequestBody VideoDTO req, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        var v = videoService.updateVideo(id, req.title, req.description, req.url, req.orderIndex, auth.getName(), isAdmin);
        VideoDTO d = new VideoDTO();
        d.id = v.getId(); d.title = v.getTitle(); d.description = v.getDescription(); d.url = v.getUrl(); d.orderIndex = v.getOrderIndex();
        return d;
    }

    @DeleteMapping("/videos/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        videoService.deleteVideo(id, auth.getName(), isAdmin);
        return ResponseEntity.ok().build();
    }
}
