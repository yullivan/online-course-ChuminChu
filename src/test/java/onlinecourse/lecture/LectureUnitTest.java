package onlinecourse.lecture;

import onlinecourse.Category;
import onlinecourse.teacher.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LectureUnitTest {

    @Autowired
    private LectureRepository lectureRepository;

    public Teacher teacher = new Teacher("추");
    public final Lecture LECTURE1 = new Lecture(
            "자바 배우기",
            5000,
            Category.Math,
            "자바, Spring을 통한 웹 개발 강의입니다.",
            new Teacher("추"),
            LocalDateTime.now());

    @BeforeEach
    void setUp() {
        lectureRepository.save(LECTURE1);
    }

}
