package onlinecourse.lectureEnrollment;

import jakarta.persistence.*;
import onlinecourse.lecture.Lecture;
import onlinecourse.student.Student;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class LectureEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Lecture lecture;

    @CreatedDate
    private LocalDateTime enrollmentTime = LocalDateTime.now();

    public LectureEnrollment() {
    }

    public LectureEnrollment(Student student, Lecture lecture) {
        this.student = student;
        this.lecture = lecture;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public LocalDateTime getEnrollmentTime() {
        return enrollmentTime;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
