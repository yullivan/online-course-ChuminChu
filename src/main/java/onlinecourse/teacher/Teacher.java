package onlinecourse.teacher;

import jakarta.persistence.*;
import onlinecourse.lecture.Lecture;

import java.util.List;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "teacher")
    private List<Lecture> lectures;

    public Teacher() {
    }

    public Teacher(String name, List<Lecture> lecture) {
        this.name = name;
        this.lectures = lecture;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }
}
