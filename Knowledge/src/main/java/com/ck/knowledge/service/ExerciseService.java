package com.ck.knowledge.service;

import com.ck.knowledge.dao.ExerciseQuestionMapRepository;
import com.ck.knowledge.dao.ExerciseRepository;
import com.ck.knowledge.dao.QuestionRepository;
import com.ck.knowledge.po.ExercisePo;
import com.ck.knowledge.po.ExerciseQuestionMapPo;
import com.ck.knowledge.po.QuestionPo;
import com.ck.knowledge.properties.NamingProperties;
import com.ck.knowledge.helper.TemplateHelper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * @Title: ExerciseService
 * @Author: Chengkai
 * @Date: 2019/9/15 19:54
 * @Version: 1.0
 */
@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private ExerciseQuestionMapRepository mapRepo;

    @Autowired
    private MailService mailService;

    @Autowired
    private NamingProperties namingProperties;

    @Transactional
    public void generateExercise(int size) throws IOException, TemplateException {
        ExercisePo po = insertExercise(questionRepo.randQuestion(size), null);
        Map<String, Object> dataMap = new HashMap<>();
        List<QuestionPo> questions = po.getQuestions();
        List<QuestionPo> selectQuestions = new ArrayList<>();
        List<QuestionPo> judgeQuestions = new ArrayList<>();
        List<QuestionPo> multiSelectQuestions = new ArrayList<>();
        for (QuestionPo qu : questions) {
            if ("select".equals(qu.getType())) {
                selectQuestions.add(qu);
                qu.setOptionItems(Arrays.asList(qu.getOptions().split(",")));
            } else if ("judge".equals(qu.getType())) {
                judgeQuestions.add(qu);
            } else if ("multiSelect".equals(qu.getType())) {
                multiSelectQuestions.add(qu);
                qu.setOptionItems(Arrays.asList(qu.getOptions().split(",")));
            }
        }
        dataMap.put("selectQuestions", selectQuestions);
        dataMap.put("judgeQuestions", judgeQuestions);
        dataMap.put("multiSelectQuestions", multiSelectQuestions);
        StringWriter sw = new StringWriter();
        Template template = TemplateHelper.getTemplate(TemplateHelper.EXERCISE_TPL);
        template.process(dataMap, sw);
        System.out.println(sw);
        mailService.sendHTMLMail(po.getCode(), sw.toString());
    }

    private ExercisePo insertExercise(List<QuestionPo> questions, String categorys) {
        ExercisePo exercise = new ExercisePo();
        if (StringUtils.isNotBlank(categorys)) {
            exercise.setCategorys(categorys);
        }
        exercise.setCode(namingProperties.getExercisePrefix() + DateFormatUtils.format(exercise.getCreateDate(), "yyyyMMddHHmmss"));
        exercise = exerciseRepo.save(exercise);
        List<ExerciseQuestionMapPo> mapPos = new ArrayList<>();
        for (QuestionPo question : questions) {
            ExerciseQuestionMapPo mapPo = new ExerciseQuestionMapPo();
            mapPo.setExerciseId(exercise.getId());
            mapPo.setQuestionId(question.getId());
            mapPos.add(mapPo);
        }
        mapRepo.saveAll(mapPos);
        exercise.setQuestions(questions);
        return exercise;
    }

    @Transactional
    public void generateExerciseByCategory(List<Long> categorys, int size) {
        insertExercise(questionRepo.randQuestionByCategory(categorys, size), StringUtils.join(categorys.toArray(), ","));
    }

}
