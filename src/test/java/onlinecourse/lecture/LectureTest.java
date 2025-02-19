package onlinecourse.lecture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import onlinecourse.Category;
import onlinecourse.DatabaseCleanup;
import onlinecourse.lecture.dto.*;
import onlinecourse.student.dto.SignUpRequest;
import onlinecourse.student.dto.SignUpResponse;
import onlinecourse.student.dto.StudentLectureResponse;
import onlinecourse.teacher.Teacher;
import onlinecourse.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LectureTest {

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
        //save
        teacher = new Teacher("추");
        teacherRepository.save(teacher);
    }

    @Test
    void 강의등록() {
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

        assertThat(lecture.id()).isEqualTo(1);
        assertThat(lecture.category()).isEqualTo(Category.Math);
    }

    @Test
    void 강의목록조회() throws InterruptedException {
        LectureResponse lecture1 = RestAssured
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
        Thread.sleep(1000);

        LectureResponse lecture2 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LectureCreateRequest(
                        "자바 응용하기",
                        "자바, Spring을 통한 웹 개발 실습강의입니다.",
                        50000,
                        Category.Math,
                        teacher.getId(),
                        LocalDateTime.now().minusDays(2)
                ))
                .when()
                .post("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureResponse.class);
        Thread.sleep(1000);

        LectureResponse lecture3 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LectureCreateRequest(
                        "과학 배우기",
                        "과학 강의입니다.",
                        50000,
                        Category.Science,
                        teacher.getId(),
                        LocalDateTime.now()
                ))
                .when()
                .post("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureResponse.class);


        List<LectureListResponse> list = RestAssured
                .given().log().all()
                .when()
                .get("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", LectureListResponse.class);

        assertThat(list.size()).isEqualTo(3);
        assertThat(list.get(0).title()).isEqualTo(lecture3.title());
        assertThat(list.get(1).title()).isEqualTo(lecture2.title());
        assertThat(list.get(2).title()).isEqualTo(lecture1.title());

    }

    @Test
    void 강의상세조회() {
        LectureResponse lecture1 = RestAssured
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

        StudentLectureResponse 수강신청 = RestAssured
                .given().log().all()
                .pathParam("lectureId", lecture1.id())
                .queryParam("studentId", student.id())
                .when()
                .post("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(StudentLectureResponse.class);

        LectureDetailResponse lectureId = RestAssured
                .given().log().all()
                .pathParam("lectureId", lecture1.id())
                .when()
                .get("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureDetailResponse.class);

        assertThat(lectureId.title()).isEqualTo("자바 배우기");
        assertThat(lectureId.introduce()).isEqualTo("자바, Spring을 통한 웹 개발 강의입니다.");
        assertThat(lectureId.price()).isEqualTo(50000);
        assertThat(lectureId.studentCount()).isEqualTo(1);
        assertThat(lectureId.students().get(0).nickName()).isEqualTo("chuchu");

    }

    @Test
    void 강의수정() {
        LectureResponse lecture1 = RestAssured
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

        LectureResponse 수정된강의 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("lectureId", lecture1.id())
                .body(new LectureUpdateRequest(
                        "수정된 이름",
                        "수정된 소개",
                        1000
                ))
                .when()
                .put("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureResponse.class);

        assertThat(수정된강의.introduce()).isEqualTo("수정된 소개");
    }

    @Test
    void 강의삭제() {
        LectureResponse lecture1 = RestAssured
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

        RestAssured
                .given().log().all()
                .when()
                .pathParam("lectureId", lecture1.id())
                .delete("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200);

    }
}
