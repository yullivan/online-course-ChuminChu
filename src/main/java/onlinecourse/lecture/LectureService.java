package onlinecourse.lecture;

import onlinecourse.Category;
import onlinecourse.lecture.dto.*;
import onlinecourse.lecture.dto.StudentEnrollmentResponse;
import onlinecourse.teacher.Teacher;
import onlinecourse.teacher.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureQueryRepository lectureQueryRepository;
    private final TeacherRepository teacherRepository;

    public LectureService(LectureRepository lectureRepository, LectureQueryRepository lectureQueryRepository, TeacherRepository teacherRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureQueryRepository = lectureQueryRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<LectureListResponse> findAll(String title, String teacherName, Category category, Pageable pageable) {
        return lectureQueryRepository.findAll(title, teacherName,category,pageable)
                .stream()
                .map(lecture ->  new LectureListResponse(
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getTeacher().getName(),
                        lecture.getPrice(),
                        lecture.getCountStudent(),
                        lecture.getCategory(),
                        lecture.getCreateTime()))
                .toList();
    }

    public LectureDetailResponse findById(Long lectureId) {
        Lecture lecture = lectureRepository.findByIdAndDeletedFalse(lectureId)
                .orElseThrow(() -> new NoSuchElementException("강의가 존재하지 않습니다."));


        List<StudentEnrollmentResponse> studentResponses = lecture.getStudents()
                .stream()
                .filter(studentLecture -> !studentLecture.getStudent().isDeleted())
                .map(s -> new StudentEnrollmentResponse(
                        s.getStudent().getNickName(),
                        s.getEnrollmentTime()
                        ))
                .toList();


        return new LectureDetailResponse(
                lectureId,
                lecture.getTitle(),
                lecture.getIntroduce(),
                lecture.getPrice(),
                lecture.getCountStudent(),
                studentResponses,
                lecture.getCategory(),
                lecture.getCreateTime(),
                lecture.getUpdateTime());
    }

    public LectureResponse save(LectureCreateRequest lectureCreateRequest) {
        Teacher teacher = teacherRepository.findById(lectureCreateRequest.teacherId())
                .orElseThrow(() -> new NoSuchElementException("강사가 없습니다."));

      /*  Lecture lecture = lectureRepository.findById(lectureCreateRequest.lectureId);
        lecture.getTeacher().getId()*/

        Lecture lecture = lectureRepository.save(new Lecture(
                lectureCreateRequest.title(),
                lectureCreateRequest.price(),
                lectureCreateRequest.category(),
                lectureCreateRequest.introduce(),
                teacher,
                LocalDateTime.now()
        ));

        return new LectureResponse(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getIntroduce(),
                lecture.getPrice(),
                lecture.getCategory(),
                teacher.getName(),
                lecture.isPrivate(),
                lecture.getCreateTime());
    }

    @Transactional
    public LectureResponse update(Long lectureId, LectureUpdateRequest lectureUpdateRequest) {
        Lecture lecture = lectureRepository.findByIdAndDeletedFalse(lectureId)
                .orElseThrow(() -> new NoSuchElementException("찾으시는 강의가 없습니다."));

        lecture.update(
                lectureUpdateRequest.title(),
                lectureUpdateRequest.introduce(),
                lectureUpdateRequest.price(),
                LocalDateTime.now());

        return new LectureResponse(
                lectureId,
                lecture.getTitle(),
                lecture.getIntroduce(),
                lecture.getPrice(),
                lecture.getCategory(),
                lecture.getTeacher().getName(),
                lecture.isPrivate(),
                lecture.getUpdateTime()
        );


    }

    @Transactional
    public void delete(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementException("찾는 강의가 없습니다."));

        lecture.deleteBy();
    }

    @Transactional
    public void updatePrivate(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementException("찾는 강의가 없습니다."));

        lecture.setPublic();
    }
}
