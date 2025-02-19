package onlinecourse.lecture;

import onlinecourse.lecture.dto.*;
import onlinecourse.student.dto.StudentResponse;
import onlinecourse.teacher.Teacher;
import onlinecourse.teacher.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<LectureListResponse> findAll() {
        return lectureQueryRepository.findAll()
                .stream()
                .map(lecture ->  new LectureListResponse(
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getTeacher().getName(),
                        lecture.getPrice(),
                        lecture.getCategory(),
                        lecture.getCreateTime()))
                .toList();
    }

    public LectureDetailResponse findById(Long lectureId) {
        Lecture lecture = lectureRepository.findByIdAndDeletedFalse(lectureId)
                .orElseThrow(() -> new NoSuchElementException("강의가 존재하지 않습니다."));

        List<StudentResponse> studentResponses = lecture.getStudents()
                .stream()
                .map(s -> new StudentResponse(
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
                lecture.getCreateTime());
    }

    @Transactional
    public LectureResponse update(Long lectureId, LectureUpdateRequest lectureUpdateRequest) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementException("찾으시는 강의가 없습니다."));

        lecture.update(
                lectureUpdateRequest.title(),
                lectureUpdateRequest.introduce(),
                lectureUpdateRequest.price());

        return new LectureResponse(
                lectureId,
                lecture.getTitle(),
                lecture.getIntroduce(),
                lecture.getPrice(),
                lecture.getCategory(),
                lecture.getTeacher().getName(),
                lecture.getUpdateTime()
        );


    }

    public void delete(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementException("찾는 강의가 없습니다."));

        lecture.deleteBy();
        lectureRepository.save(lecture);
    }
}
