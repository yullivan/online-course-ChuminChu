package onlinecourse.student;

import onlinecourse.lecture.Lecture;
import onlinecourse.lecture.LectureRepository;
import onlinecourse.student.dto.SignUpRequest;
import onlinecourse.student.dto.SignUpResponse;
import onlinecourse.student.dto.StudentLectureResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;
    private final StudentLectureRepository studentLectureRepository;

    public StudentService(StudentRepository studentRepository, LectureRepository lectureRepository, StudentLectureRepository studentLectureRepository) {
        this.studentRepository = studentRepository;
        this.lectureRepository = lectureRepository;
        this.studentLectureRepository = studentLectureRepository;
    }

    public SignUpResponse save(SignUpRequest sign) {
        Student student = studentRepository.save(new Student(
                sign.nickName(),
                sign.email()
        ));
        return new SignUpResponse(
                student.getId(),
                student.getNickName(),
                student.getEmail());
    }

    public void delete(Long memberId) {
        Student student = studentRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("등록된 Id가 없습니다."));

        studentRepository.delete(student);
    }

    @Transactional
    public StudentLectureResponse select(Long lectureId, Long studentId) {
        Lecture lecture = lectureRepository.findByIdAndDeletedFalse(lectureId)
                .orElseThrow(() -> new NoSuchElementException("찾는 강의가 없습니다."));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        StudentLecture studentLecture = new StudentLecture();
        studentLecture.setLecture(lecture);
        studentLecture.setStudent(student);

        lecture.countStudent();
        StudentLecture savedStudentLecture = studentLectureRepository.save(studentLecture);



        return new StudentLectureResponse(
                savedStudentLecture.getId(),
                lectureId,
                studentId,
                savedStudentLecture.getEnrollmentTime());


    }
}
