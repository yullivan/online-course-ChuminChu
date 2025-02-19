package onlinecourse.student;

import jakarta.persistence.*;
import onlinecourse.lecture.Lecture;

import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    private String Email;

    @OneToMany(mappedBy = "student")
    private List<StudentLecture> studentLecture;

    public Student() {
    }

    public Student(String nickName, String email) {
        this.nickName = nickName;
        Email = email;

    }

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public List<StudentLecture> getStudentLecture() {
        return studentLecture;
    }

    public String getEmail() {
        return Email;
    }
}
