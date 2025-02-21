package onlinecourse.lecture.dto;

import onlinecourse.Category;

import java.time.LocalDateTime;

public record LectureUpdateRequest(
        String title,
        String introduce,
        int price,
        LocalDateTime updateTime
) {
}
