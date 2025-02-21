package onlinecourse.lectureEnrollment;

import onlinecourse.lectureEnrollment.dto.LectureEnrollmentRequest;
import onlinecourse.lectureEnrollment.dto.LectureEnrollmentResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class LectureEnrollmentController {

    private final LectureEnrollmentService lectureEnrollmentService;

    public LectureEnrollmentController(LectureEnrollmentService lectureEnrollmentService) {
        this.lectureEnrollmentService = lectureEnrollmentService;
    }

    //수강신청
    @PostMapping("/lectureEnrollment")
    public LectureEnrollmentResponse select(@RequestBody LectureEnrollmentRequest request){
        return lectureEnrollmentService.select(request);
    }
}
