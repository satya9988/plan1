package com.example.learning.repository;

import com.example.learning.entity.Video;
import com.example.learning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByCourseOrderByOrderIndexAsc(Course course);
}
