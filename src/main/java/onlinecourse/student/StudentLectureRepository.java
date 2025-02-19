package onlinecourse.student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentLectureRepository extends JpaRepository<StudentLecture, Long> {
}
