package onlinecourse.lecture.dto;

import java.time.LocalDateTime;

public record StudentEnrollmentResponse(
        String nickName,
        LocalDateTime enrollmentTime
) {
}
