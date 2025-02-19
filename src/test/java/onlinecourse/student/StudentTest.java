package onlinecourse.student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import onlinecourse.Category;
import onlinecourse.DatabaseCleanup;
import onlinecourse.lecture.dto.LectureCreateRequest;
import onlinecourse.lecture.dto.LectureResponse;
import onlinecourse.student.dto.SignUpRequest;
import onlinecourse.student.dto.SignUpResponse;
import onlinecourse.student.dto.StudentLectureResponse;
import onlinecourse.teacher.Teacher;
import onlinecourse.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    TeacherRepository teacherRepository;

    Teacher teacher;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        teacher = new Teacher("추민영");
        teacherRepository.save(teacher);
    }

    @Test
    void 회원가입() {
        SignUpResponse student = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(
                        "chu@gmail.com",
                        "chuchu"
                ))
                .when()
                .post("/members/signup")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(SignUpResponse.class);

        assertThat(student.id()).isEqualTo(1);

    }

    @Test
    void 회원탈퇴() {
        SignUpResponse student = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(
                        "chu@gmail.com",
                        "chuchu"
                ))
                .when()
                .post("/members/signup")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(SignUpResponse.class);

        RestAssured
                .given().log().all()
                .pathParam("memberId",student.id())
                .when()
                .delete("/members/{memberId}")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 수강신청() {
        SignUpResponse student = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(
                        "chu@gmail.com",
                        "chuchu"
                ))
                .when()
                .post("/members/signup")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(SignUpResponse.class);

        LectureResponse lecture = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LectureCreateRequest(
                        "자바 배우기",
                        "자바, Spring을 통한 웹 개발 강의입니다.",
                        50000,
                        Category.Math,
                        teacher.getId(),
                        LocalDateTime.now()
                ))
                .when()
                .post("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureResponse.class);

        StudentLectureResponse 수강신청 = RestAssured
                .given().log().all()
                .pathParam("lectureId", lecture.id())
                .queryParam("studentId", student.id())
                .when()
                .post("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(StudentLectureResponse.class);

        assertThat(수강신청.lectureId()).isEqualTo(1);
        assertThat(수강신청.studentId()).isEqualTo(1);



    }
}
