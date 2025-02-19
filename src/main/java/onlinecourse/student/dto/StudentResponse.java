package onlinecourse.student.dto;

import java.time.LocalDateTime;

public record StudentResponse(
        String nickName,
        LocalDateTime enrollmentTime
) {
}
