package onlinecourse.student.dto;

import java.time.LocalDateTime;

public record StudentLectureResponse(
        Long id,
        Long lectureId,
        Long studentId,
        LocalDateTime enrollmentDate
) {
}
