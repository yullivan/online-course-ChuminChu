package onlinecourse.lecture;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QLecture lecture = QLecture.lecture;

    public LectureQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Lecture> findAll(String title, String teacherName){
        return jpaQueryFactory
                .selectFrom(lecture)
                .join(lecture.teacher).fetchJoin()
                .where(
                        lecture.deleted.isFalse(),
                        lecture.isPrivate.isFalse(),
                        containsTitle(title),
                        containsTeacherName(teacherName))
                .orderBy(
                        lecture.createTime.desc(),
                        lecture.countStudent.desc())
                .fetch();
    }

    public BooleanExpression containsTitle(String title){
        return (title == null || title.isEmpty()) ? null : lecture.title.containsIgnoreCase(title);
    }

    public BooleanExpression containsTeacherName(String teacherName){
        return (teacherName == null || teacherName.isEmpty()) ? null : lecture.teacher.name.containsIgnoreCase(teacherName);
    }




}
