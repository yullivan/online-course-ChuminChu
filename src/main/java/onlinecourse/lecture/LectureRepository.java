package onlinecourse.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
        Optional<Lecture> findByIdAndDeletedFalse(Long id);
}
