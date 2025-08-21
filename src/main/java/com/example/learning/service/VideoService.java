package com.example.learning.service;

import com.example.learning.entity.*;
import com.example.learning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoService {

    @Autowired private VideoRepository videoRepository;
    @Autowired private CourseRepository courseRepository;

    public List<Video> listByCourse(Long courseId){
        Course c = courseRepository.findById(courseId).orElseThrow();
        return videoRepository.findByCourseOrderByOrderIndexAsc(c);
    }

    @Transactional
    public Video addVideo(Long courseId, String title, String description, String url, int orderIndex, String username, boolean isAdmin){
        Course c = courseRepository.findById(courseId).orElseThrow();
        if (!isAdmin && !c.getTeacher().getUsername().equals(username)) throw new RuntimeException("Not allowed");
        Video v = new Video();
        v.setCourse(c);
        v.setTitle(title);
        v.setDescription(description);
        v.setUrl(url);
        v.setOrderIndex(orderIndex);
        return videoRepository.save(v);
    }

    @Transactional
    public Video updateVideo(Long videoId, String title, String description, String url, int orderIndex, String username, boolean isAdmin){
        Video v = videoRepository.findById(videoId).orElseThrow();
        Course c = v.getCourse();
        if (!isAdmin && !c.getTeacher().getUsername().equals(username)) throw new RuntimeException("Not allowed");
        v.setTitle(title);
        v.setDescription(description);
        v.setUrl(url);
        v.setOrderIndex(orderIndex);
        return videoRepository.save(v);
    }

    @Transactional
    public void deleteVideo(Long videoId, String username, boolean isAdmin){
        Video v = videoRepository.findById(videoId).orElseThrow();
        Course c = v.getCourse();
        if (!isAdmin && !c.getTeacher().getUsername().equals(username)) throw new RuntimeException("Not allowed");
        videoRepository.delete(v);
    }
}
