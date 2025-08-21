package com.example.learning.controller;

import com.example.learning.dto.CourseDTO;
import com.example.learning.entity.Course;
import com.example.learning.entity.Enrollment;
import com.example.learning.entity.Role;
import com.example.learning.entity.User;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired private CourseService courseService;
    @Autowired private UserRepository userRepository;

    @GetMapping
    public List<CourseDTO> all(){
        return courseService.listAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CourseDTO byId(@PathVariable Long id){
        return courseService.get(id).map(this::toDto).orElseThrow();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public CourseDTO create(@RequestBody CourseDTO req, Authentication auth){
        Course c = courseService.createCourse(req.title, req.description, auth.getName());
        return toDto(c);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public CourseDTO update(@PathVariable Long id, @RequestBody CourseDTO req, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        Course c = courseService.updateCourse(id, req.title, req.description, auth.getName(), isAdmin);
        return toDto(c);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth){
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+Role.ADMIN.name()));
        courseService.deleteCourse(id, auth.getName(), isAdmin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> enroll(@PathVariable Long id, Authentication auth){
        Enrollment e = courseService.enroll(auth.getName(), id);
        return ResponseEntity.ok("Enrolled with id " + e.getId());
    }

    private CourseDTO toDto(Course c){
        CourseDTO d = new CourseDTO();
        d.id = c.getId();
        d.title = c.getTitle();
        d.description = c.getDescription();
        d.teacherName = c.getTeacher() != null ? c.getTeacher().getFullName() : null;
        return d;
    }
}
