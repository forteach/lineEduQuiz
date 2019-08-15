package com.project.quiz.questionlibrary.repository;

import com.project.quiz.questionlibrary.domain.BigQuestion;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-15 15:06
 * @version: 1.0
 * @description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    public void findAllByChapterIdAndCourseId() {
        Query query = Query.query(Criteria.where("chapterId").is("132dewefece")
                .and("courseId").is("12123"));
        Sort sort = new Sort(Sort.Direction.DESC, "uDate");
        Pageable pageable = PageRequest.of(0, 2, sort);
        query.with(sort);
        query.with(pageable);
        Mono<List<BigQuestion>> list = reactiveMongoTemplate.find(query, BigQuestion.class).collectList();
        Mono<Long> count = reactiveMongoTemplate.count(query, BigQuestion.class);
        list.zipWith(count).map(t -> new PageImpl(t.getT1(), pageable, t.getT2()))
                .log("==========>>>>>> ")
                .doOnNext(System.out::println)
                .subscribe();

    }
}