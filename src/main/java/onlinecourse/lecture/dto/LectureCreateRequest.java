package onlinecourse.lecture.dto;

import onlinecourse.Category;
import onlinecourse.teacher.Teacher;

import java.time.LocalDateTime;

public record LectureCreateRequest(
        String title,
        String introduce,
        int price,
        Category category,
        Long teacherId,
        LocalDateTime createTime
) {
}
