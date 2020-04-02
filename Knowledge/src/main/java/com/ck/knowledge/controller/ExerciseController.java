package com.ck.knowledge.controller;

import com.ck.knowledge.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Title: ExerciseController
 * @Author: Chengkai
 * @Date: 2019/9/15 20:19
 * @Version: 1.0
 */
@RestController
@RequestMapping("/exercise/")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("generateExercise")
    public void generateExercise(@RequestParam("size") int size) throws IOException {
        exerciseService.generateExercise(size);
    }

    @GetMapping("generateExerciseByCategory")
    public void generateExerciseByCategory(@RequestParam("categorys") List<Long> categorys,
                                           @RequestParam("size") int size){
        exerciseService.generateExerciseByCategory(categorys,size);
    }
}
