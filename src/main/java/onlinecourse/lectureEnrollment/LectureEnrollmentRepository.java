package onlinecourse.lectureEnrollment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureEnrollmentRepository extends JpaRepository<LectureEnrollment, Long> {
}
