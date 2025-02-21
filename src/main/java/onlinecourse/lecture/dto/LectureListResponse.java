package onlinecourse.lecture.dto;

import onlinecourse.Category;

import java.time.LocalDateTime;

public record LectureListResponse(
        Long id,
        String title,
        String teacherName,
        int price,
        int countStudent,
        Category category,
        LocalDateTime createTime
) {
}
