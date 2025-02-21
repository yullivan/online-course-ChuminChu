package onlinecourse.lectureEnrollment.dto;

public record LectureEnrollmentRequest(
        Long lectureId,
        Long studentId
) {
}
