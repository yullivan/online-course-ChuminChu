package onlinecourse.lectureEnrollment.dto;

import onlinecourse.lectureEnrollment.LectureEnrollment;

import java.time.LocalDateTime;

public record LectureEnrollmentResponse(
        Long Id,
        Long lectureId,
        Long studentId,
        LocalDateTime enrollmentTime
) {
}
