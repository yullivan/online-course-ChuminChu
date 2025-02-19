package onlinecourse.student;

import onlinecourse.student.dto.SignUpRequest;
import onlinecourse.student.dto.SignUpResponse;
import onlinecourse.student.dto.StudentLectureResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentRestController {

    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    //회원가입
    @PostMapping("/members/signup")
    public SignUpResponse create(@RequestBody SignUpRequest sign){
        return studentService.save(sign);
    }

    @DeleteMapping("/members/{memberId}")
    public void delete(@PathVariable Long memberId){
        studentService.delete(memberId);
    }

    //수강신청
    @PostMapping("/lectures/{lectureId}")
    public StudentLectureResponse select(@PathVariable Long lectureId, @RequestParam Long studentId){
        return studentService.select(lectureId,studentId);
    }
}
