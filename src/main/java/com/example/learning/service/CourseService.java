package com.example.learning.service;

import com.example.learning.entity.*;
import com.example.learning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired private CourseRepository courseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    public List<Course> listAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> get(Long id) { return courseRepository.findById(id); }

    @Transactional
    public Course createCourse(String title, String description, String teacherUsername) {
        User teacher = userRepository.findByUsername(teacherUsername).orElseThrow();
        Course c = new Course();
        c.setTitle(title);
        c.setDescription(description);
        c.setTeacher(teacher);
        return courseRepository.save(c);
    }

    @Transactional
    public Course updateCourse(Long id, String title, String description, String username, boolean isAdmin) {
        Course c = courseRepository.findById(id).orElseThrow();
        if (!isAdmin && !c.getTeacher().getUsername().equals(username)) {
            throw new RuntimeException("Only the course owner or admin can update the course");
        }
        c.setTitle(title);
        c.setDescription(description);
        return courseRepository.save(c);
    }

    @Transactional
    public void deleteCourse(Long id, String username, boolean isAdmin) {
        Course c = courseRepository.findById(id).orElseThrow();
        if (!isAdmin && !c.getTeacher().getUsername().equals(username)) {
            throw new RuntimeException("Only the course owner or admin can delete the course");
        }
        courseRepository.delete(c);
    }

    @Transactional
    public Enrollment enroll(String username, Long courseId) {
        User student = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        return enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseGet(() -> {
                    Enrollment e = new Enrollment();
                    e.setStudent(student);
                    e.setCourse(course);
                    e.setEnrolledAt(new java.util.Date());
                    e.setProgressPct(0);
                    e.setCompleted(false);
                    return enrollmentRepository.save(e);
                });
    }
}
