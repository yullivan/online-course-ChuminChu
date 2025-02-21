package onlinecourse.student;

import onlinecourse.lecture.LectureRepository;
import onlinecourse.lectureEnrollment.LectureEnrollmentRepository;
import onlinecourse.student.dto.SignUpRequest;
import onlinecourse.student.dto.SignUpResponse;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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

        student.deleteBy();
        studentRepository.save(student);
    }
}
