package onlinecourse.lecture;

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

    public List<Lecture> findAll(){
        return jpaQueryFactory
                .selectFrom(lecture)
                .join(lecture.teacher).fetchJoin()
                .where(
                        lecture.deleted.isFalse(),
                        lecture.isPrivate.isFalse())
                .orderBy(lecture.createTime.desc())
                .fetch();
    }




}
