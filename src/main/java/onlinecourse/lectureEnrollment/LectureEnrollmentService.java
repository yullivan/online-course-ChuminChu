package onlinecourse.lectureEnrollment;

import onlinecourse.lecture.Lecture;
import onlinecourse.lecture.LectureRepository;
import onlinecourse.lectureEnrollment.dto.LectureEnrollmentRequest;
import onlinecourse.lectureEnrollment.dto.LectureEnrollmentResponse;
import onlinecourse.student.Student;
import onlinecourse.student.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class LectureEnrollmentService {

    private final LectureEnrollmentRepository lectureEnrollmentRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;


    public LectureEnrollmentService(LectureEnrollmentRepository lectureEnrollmentRepository, LectureRepository lectureRepository, StudentRepository studentRepository) {
        this.lectureEnrollmentRepository = lectureEnrollmentRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public LectureEnrollmentResponse select(LectureEnrollmentRequest request) {
        Lecture lecture = lectureRepository.findByIdAndDeletedFalse(request.lectureId())
                .orElseThrow(() -> new NoSuchElementException("찾는 강의가 없습니다."));

        Student student = studentRepository.findByIdAndDeletedFalse(request.studentId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        LectureEnrollment lectureEnrollment = new LectureEnrollment();
        lectureEnrollment.setLecture(lecture);
        lectureEnrollment.setStudent(student);

        lecture.countStudent();
        LectureEnrollment savedStudentLecture = lectureEnrollmentRepository.save(lectureEnrollment);

        return new LectureEnrollmentResponse(
                savedStudentLecture.getId(),
                savedStudentLecture.getLecture().getId(),
                savedStudentLecture.getStudent().getId(),
                LocalDateTime.now());
    }
}
