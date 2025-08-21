# Learning Platform Backend

Spring Boot (Java 17) backend implementing:
- Role-based access (ADMIN, TEACHER, STUDENT) with JWT
- Course CRUD (Teacher/Admin), student enroll
- Video CRUD per course (Teacher/Admin), students can view
- Quiz per course (Teacher/Admin create/update), students submit; score auto-saved
- Performance endpoint (combines completion + quiz results)
- Student dashboard endpoint

## Quick start

```bash
mvn spring-boot:run
```

Default users (inserted on first run):
- admin / admin123 (ADMIN)
- teacher / teacher123 (TEACHER)
- student / student123 (STUDENT)

### Auth
- `POST /api/auth/login` -> `{ "username":"student", "password":"student123" }` returns `{ token }`
- Add header: `Authorization: Bearer <token>` for protected endpoints

### Key endpoints

- **Courses**
  - `GET /api/courses`
  - `POST /api/courses` (TEACHER/ADMIN) body: `{"title":"T","description":"D"}`
  - `PUT /api/courses/{id}` (owner TEACHER/ADMIN)
  - `DELETE /api/courses/{id}` (owner TEACHER/ADMIN)
  - `POST /api/courses/{id}/enroll` (STUDENT)

- **Videos**
  - `GET /api/courses/{courseId}/videos`
  - `POST /api/courses/{courseId}/videos` (TEACHER/ADMIN)
  - `PUT /api/videos/{id}` (TEACHER/ADMIN)
  - `DELETE /api/videos/{id}` (TEACHER/ADMIN)

- **Quizzes**
  - `GET /api/courses/{courseId}/quizzes`
  - `POST /api/courses/{courseId}/quizzes` (TEACHER/ADMIN) – body:
    ```json
    {
      "title": "Final Quiz",
      "questions": [
        {
          "text":"2+2=?",
          "options":["3","4","5","6"],
          "correctOptionIndex":1,
          "points":50
        }
      ]
    }
    ```
  - `PUT /api/quizzes/{quizId}` (TEACHER/ADMIN) – same body as create
  - `POST /api/quizzes/{quizId}/submit` (STUDENT) – body:
    ```json
    {
      "answers": [{"questionId":1,"selectedOptionIndex":1}]
    }
    ```

- **Performance**
  - `GET /api/performance/me` (STUDENT) – returns blended percentage + feedback

- **Dashboard**
  - `GET /api/dashboard/me` (STUDENT)

H2 console at `/h2-console` (JDBC URL: `jdbc:h2:mem:learn`).

> **Note**: For simplicity, student video access is open via GET. In a real app you might restrict viewing to enrolled students only.
