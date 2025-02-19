package onlinecourse.lecture.dto;

import onlinecourse.Category;
import onlinecourse.teacher.Teacher;

import java.time.LocalDateTime;

public record LectureResponse(
        Long id,
        String title,
        String introduce,
        int price,
        Category category,
        String teacherName,
        LocalDateTime createTime
) {
}
