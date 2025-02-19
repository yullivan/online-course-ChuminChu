package onlinecourse.lecture;

import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.*;
import onlinecourse.Category;
import onlinecourse.student.Student;
import onlinecourse.student.StudentLecture;
import onlinecourse.teacher.Teacher;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int price;

    private Category category;

    private String introduce;

    private boolean deleted = false;

    @CreatedDate
    private LocalDateTime createTime = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updateTime = LocalDateTime.now();

    @ManyToOne
    private Teacher teacher;

    @OneToMany(mappedBy = "lecture")
    private List<StudentLecture> students;

    private int countStudent;


    public Lecture() {
    }

    public Lecture(String title, int price, Category category, String introduce, Teacher teacher, LocalDateTime createTime) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.introduce = introduce;
        this.teacher = teacher;
        this.createTime = createTime;

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public List<StudentLecture> getStudents() {
        return students;
    }

    public String getIntroduce() {
        return introduce;
    }

    public int getCountStudent() {
        return countStudent;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void countStudent() {
        this.countStudent++;
    }

    public void update(
            String title,
            String introduce,
            int price
    ){
        this.title = title;
        this.introduce = introduce;
        this.price = price;
    }

    public void deleteBy(){
        if(!this.getStudents().isEmpty()){
            throw new NoSuchElementException("수강을 신청한 학생이 있어 강의를 삭제할 수 없습니다.");
        }
        this.deleted=true;
    }

}
