package com.ck.exercise.controller;

import com.ck.common.helper.TemplateHelper;
import com.ck.exercise.service.ExerciseService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

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

    @GetMapping("generateEmailExercise")
    public void generateEmailExercise(@RequestParam("size") int size) throws IOException, TemplateException, MessagingException {
        exerciseService.generateExercise(size, TemplateHelper.EXERCISE_EMAIL_TPL);
    }

    @GetMapping("generatePcExercise")
    public void generatePcExercise(@RequestParam("size") int size) throws IOException, TemplateException, MessagingException {
        exerciseService.generateExercise(size, TemplateHelper.EXERCISE_PC_TPL);
    }

}
