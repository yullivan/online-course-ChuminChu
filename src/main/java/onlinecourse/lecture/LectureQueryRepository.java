package onlinecourse.lecture;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import onlinecourse.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QLecture lecture = QLecture.lecture;

    public LectureQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Lecture> findAll(String title, String teacherName, Category category){
        return jpaQueryFactory
                .selectFrom(lecture)
                .join(lecture.teacher).fetchJoin()
                .where(
                        lecture.deleted.isFalse(),
                        lecture.isPrivate.isFalse(),
                        containsTitle(title),
                        containsTeacherName(teacherName),
                        containsCategory(category))
                .orderBy(
                        lecture.createTime.desc(),
                        lecture.countStudent.desc())
                .fetch();
    }

    private BooleanExpression containsCategory(Category category) {
        return (category == null) ? null : lecture.category.eq(category);
    }

    public BooleanExpression containsTitle(String title){
        return (title == null || title.isEmpty()) ? null : lecture.title.containsIgnoreCase(title);
    }

    public BooleanExpression containsTeacherName(String teacherName){
        return (teacherName == null || teacherName.isEmpty()) ? null : lecture.teacher.name.containsIgnoreCase(teacherName);
    }




}
