package onlinecourse.student;

import jakarta.persistence.*;
import onlinecourse.lecture.Lecture;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class StudentLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Lecture lecture;

    @CreatedDate
    private LocalDateTime enrollmentTime = LocalDateTime.now();

    public StudentLecture() {
    }

    public StudentLecture(Student student, Lecture lecture) {
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
