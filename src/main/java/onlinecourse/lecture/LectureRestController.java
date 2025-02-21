package onlinecourse.lecture;

import onlinecourse.Category;
import onlinecourse.lecture.dto.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                                             @RequestParam(required = false) String teacherName,
                                             @RequestParam(required = false) Category category,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return lectureService.findAll(title, teacherName,category, pageable);
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