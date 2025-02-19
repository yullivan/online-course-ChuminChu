package onlinecourse.lecture.dto;

import onlinecourse.Category;
import onlinecourse.student.dto.StudentResponse;

import java.time.LocalDateTime;
import java.util.List;

public record LectureDetailResponse(
        Long id,
        String title,
        String introduce,
        int price,
        int studentCount,
        List<StudentResponse> students,
        Category category,
        LocalDateTime createTime,
        LocalDateTime updateTime

) {
}
