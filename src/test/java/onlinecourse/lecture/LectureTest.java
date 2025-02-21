package onlinecourse.lecture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import onlinecourse.Category;
import onlinecourse.DatabaseCleanup;
import onlinecourse.lecture.dto.*;
import onlinecourse.lectureEnrollment.dto.LectureEnrollmentRequest;
import onlinecourse.lectureEnrollment.dto.LectureEnrollmentResponse;
import onlinecourse.student.dto.SignUpRequest;
import onlinecourse.student.dto.SignUpResponse;
import onlinecourse.teacher.Teacher;
import onlinecourse.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

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

    //강의 만들기
    private LectureResponse createLecture(String title, String introduce, int price, Category category, Long teacherId) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LectureCreateRequest(title,introduce,price,category,teacherId,LocalDateTime.now()))
                .when()
                .post("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureResponse.class);
    }

    //회원가입
    private SignUpResponse signUpStudent(String email, String password) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignUpRequest(email, password))
                .when()
                .post("/members/signup")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(SignUpResponse.class);
    }

    //수강신청
    private LectureEnrollmentResponse enrollStudentInLecture(Long lectureId, Long studentId) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LectureEnrollmentRequest(lectureId, studentId))
                .when()
                .post("/lectureEnrollment")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureEnrollmentResponse.class);
    }

    //학생 탈퇴
    private void deleteStudent(Long studentId) {
        RestAssured
                .given().log().all()
                .pathParam("memberId", studentId)
                .when()
                .delete("/members/{memberId}")
                .then().log().all()
                .statusCode(200);
    }

    //강의 상세 조회
    private LectureDetailResponse getLectureDetails(Long lectureId) {
        return RestAssured
                .given().log().all()
                .pathParam("lectureId", lectureId)
                .when()
                .get("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureDetailResponse.class);
    }

    //공개로 바꾸기
    private void updateLectureToPublic(Long lectureId) {
        RestAssured
                .given().log().all()
                .pathParam("lectureId", lectureId)
                .when()
                .patch("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200);
    }

    //강의 수정
    private LectureResponse updateLecture(Long lectureId, String title, String introduce, int price) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("lectureId", lectureId)
                .body(new LectureUpdateRequest(
                        title,
                        introduce,
                        price,
                        LocalDateTime.now()
                ))
                .when()
                .put("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(LectureResponse.class);
    }


    //강의 삭제
    private void deleteLecture(Long lectureId) {
        RestAssured
                .given().log().all()
                .when()
                .pathParam("lectureId", lectureId)
                .delete("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(200);
    }



    @Test
    void 강의등록() {
        LectureResponse lecture = createLecture(
                "자바 배우기",
                "자바, Spring을 통한 웹 개발 강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );

        assertThat(lecture.id()).isEqualTo(1);
        assertThat(lecture.category()).isEqualTo(Category.Math);
        assertThat(lecture.isPrivate()).isTrue();
    }

    @Test
    void 비공개_강의목록조회() throws InterruptedException {
        createLecture(
                "자바 배우기",
                "자바, Spring을 통한 웹 개발 강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );
        Thread.sleep(1000); // Wait to simulate timing between lectures

        createLecture(
                "자바 응용하기",
                "자바, Spring을 통한 웹 개발 실습강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );
        Thread.sleep(1000);

        createLecture(
                "과학 배우기",
                "과학 강의입니다.",
                50000,
                Category.Science,
                teacher.getId()
        );

        List<LectureListResponse> list = RestAssured
                .given().log().all()
                .when()
                .get("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", LectureListResponse.class);

        assertThat(list.size()).isEqualTo(0);
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void 공개_강의목록조회() throws InterruptedException {
        LectureResponse lecture1 = createLecture(
                "자바 배우기",
                "자바, Spring을 통한 웹 개발 강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );
        Thread.sleep(1000);

        LectureResponse lecture2 = createLecture(
                "자바 응용하기",
                "자바, Spring을 통한 웹 개발 실습강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );
        Thread.sleep(1000);

        LectureResponse lecture3 = createLecture(
                "과학 배우기",
                "과학 강의입니다.",
                50000,
                Category.Science,
                teacher.getId()
        );

        updateLectureToPublic(lecture1.id());
        updateLectureToPublic(lecture2.id());

        List<LectureListResponse> list = RestAssured
                .given().log().all()
                .when()
                .get("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", LectureListResponse.class);

        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    void 비공개_강의상세조회() {
        LectureResponse lecture1 = createLecture(
                "자바 배우기",
                "자바, Spring을 통한 웹 개발 강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );

        SignUpResponse student = signUpStudent("chu@gmail.com", "chuchu");

        LectureEnrollmentResponse 수강신청 = enrollStudentInLecture(lecture1.id(), student.id());

        LectureDetailResponse lectureDetails = getLectureDetails(lecture1.id());

        assertThat(lectureDetails.title()).isEqualTo("자바 배우기");
        assertThat(lectureDetails.introduce()).isEqualTo("자바, Spring을 통한 웹 개발 강의입니다.");
        assertThat(lectureDetails.price()).isEqualTo(50000);
        assertThat(lectureDetails.studentCount()).isEqualTo(1);
        assertThat(lectureDetails.students().get(0).nickName()).isEqualTo("chuchu");
    }


    @Test
    void 강의수정() {
        LectureResponse lecture1 = createLecture(
                "자바 배우기",
                "자바, Spring을 통한 웹 개발 강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );

        LectureResponse 수정된강의 = updateLecture(
                lecture1.id(),
                "수정된 이름",
                "수정된 소개",
                1000
        );

        LectureDetailResponse lectureId = getLectureDetails(수정된강의.id());

        assertThat(수정된강의.introduce()).isEqualTo("수정된 소개");
        assertThat(lectureId.updateTime()).isEqualTo(수정된강의.createTime());
    }


    @Test
    void 강의삭제() {
        LectureResponse lecture1 = createLecture(
                "자바 배우기",
                "자바, Spring을 통한 웹 개발 강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );
        deleteLecture(lecture1.id());
    }


    @Test
    void 공개된_삭제된_강의목록_빼고_조회() {
        LectureResponse lecture1 = createLecture(
                "자바 배우기",
                "자바, Spring을 통한 웹 개발 강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );

        updateLectureToPublic(lecture1.id());

        LectureResponse lecture2 = createLecture(
                "자바 응용하기",
                "자바, Spring을 통한 웹 개발 실습강의입니다.",
                50000,
                Category.Math,
                teacher.getId()
        );

        updateLectureToPublic(lecture2.id());

        LectureResponse lecture3 = createLecture(
                "과학 배우기",
                "과학 강의입니다.",
                50000,
                Category.Science,
                teacher.getId()
        );

        updateLectureToPublic(lecture3.id());

        deleteLecture(lecture1.id());

        List<LectureListResponse> list = RestAssured
                .given().log().all()
                .when()
                .get("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", LectureListResponse.class);

        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    void 삭제된_강의상세조회x() {
        LectureResponse lecture = createLecture(
                "과학 배우기",
                "과학 강의입니다.",
                50000,
                Category.Science,
                teacher.getId()
        );

        deleteLecture(lecture.id());

        RestAssured
                .given().log().all()
                .pathParam("lectureId", lecture.id())
                .when()
                .get("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(500);
    }

    @Test
    void 삭제된_강의목록_수정x() {
        LectureResponse lecture = createLecture(
                "과학 배우기",
                "과학 강의입니다.",
                50000,
                Category.Science,
                teacher.getId()
        );

        deleteLecture(lecture.id());

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("lectureId", lecture.id())
                .body(new LectureUpdateRequest(
                        "수정된 이름",
                        "수정된 소개",
                        1000,
                        LocalDateTime.now()
                ))
                .when()
                .put("/lectures/{lectureId}")
                .then().log().all()
                .statusCode(500);
    }

    @Test
    void 공개된_강의_제목_검색() {
        LectureResponse lecture1 = createLecture(
                "자바 배우기", "자바, Spring을 통한 웹 개발 강의입니다.",
                50000, Category.Math, teacher.getId()
        );

        LectureResponse lecture2 = createLecture(
                "자바 응용하기", "자바, Spring을 통한 웹 개발 실습강의입니다.",
                50000, Category.Math, teacher.getId()
        );

        LectureResponse lecture3 = createLecture(
                "과학 배우기", "과학 강의입니다.",
                50000, Category.Science, teacher.getId()
        );

        updateLectureToPublic(lecture1.id());
        updateLectureToPublic(lecture2.id());
        updateLectureToPublic(lecture3.id());

        List<LectureListResponse> list = RestAssured
                .given().log().all()
                .param("title", "자바")
                .param("teacherName", teacher.getName())
                .when()
                .get("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", LectureListResponse.class);

        List<LectureListResponse> list1 = RestAssured
                .given().log().all()
                .when()
                .get("/lectures")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", LectureListResponse.class);


        assertThat(list.size()).isEqualTo(2);
        assertThat(list1.size()).isEqualTo(3);
        assertThat(list.stream().allMatch(l -> l.title().contains("자바"))).isTrue();
        assertThat(list.stream().allMatch(l -> l.teacherName().contains(teacher.getName()))).isTrue();
    }
}
