package onlinecourse.lecture.dto;

import onlinecourse.Category;

import java.time.LocalDateTime;
import java.util.List;

public record LectureDetailResponse(
        Long id,
        String title,
        String introduce,
        int price,
        int studentCount,
        List<StudentEnrollmentResponse> students,
        Category category,
        LocalDateTime createTime,
        LocalDateTime updateTime

) {
}
