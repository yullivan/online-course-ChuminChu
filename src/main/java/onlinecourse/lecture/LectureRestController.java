package onlinecourse.lecture;

import onlinecourse.lecture.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LectureRestController {

    private final LectureService lectureService;

    public LectureRestController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    //강의 목록 조회
    @GetMapping("/lectures")
    public List<LectureListResponse> readAll(@RequestParam(required = false) String title,
                                             @RequestParam(required = false) String teacherName){
        return lectureService.findAll(title, teacherName);
    }

    //강의 상세 조회
    @GetMapping("/lectures/{lectureId}")
    public LectureDetailResponse readById(@PathVariable Long lectureId){
        return lectureService.findById(lectureId);
    }

    @PostMapping("/lectures")
    public LectureResponse create(@RequestBody LectureCreateRequest lectureCreateRequest){
        return lectureService.save(lectureCreateRequest);
    }

    @PutMapping("/lectures/{lectureId}")
    public LectureResponse update(@PathVariable Long lectureId,
                                  @RequestBody LectureUpdateRequest lectureUpdateRequest){
        return lectureService.update(lectureId, lectureUpdateRequest);
    }

    @DeleteMapping("/lectures/{lectureId}")
    public void delete(@PathVariable Long lectureId){
        lectureService.delete(lectureId);
    }

    @PatchMapping("/lectures/{lectureId}")
    public void updatePrivate(@PathVariable Long lectureId){
        lectureService.updatePrivate(lectureId);
    }
}