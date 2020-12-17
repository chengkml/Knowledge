package com.ck.common.helper;

import com.ck.api.service.ApiService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TemplateHelper {

    private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

    private static StringTemplateLoader loader = new StringTemplateLoader();

    public static final String WORK_TPL = "workTpl";

    public static final String MY_TPL = "myTpl";

    public static final String EXERCISE_TPL = "exerciseTpl";

    public static final String TODO_TPL = "todoTpl";

    static {

        loadResourceAsTpl(EXERCISE_TPL, "/static/exercise/exercise.ftl");
        loadResourceAsTpl(MY_TPL, "/static/api/api_template.ftl");
        loadResourceAsTpl(WORK_TPL, "/static/api/api_template2.ftl");
        loadResourceAsTpl(TODO_TPL, "/static/todo/todo.ftl");
        configuration.setTemplateLoader(loader);
    }

    private static void loadResourceAsTpl(String name, String resource) {
        InputStream tplInput = ApiService.class.getResourceAsStream(resource);
        try (BufferedReader tplReader = new BufferedReader(new InputStreamReader(tplInput, "utf-8"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = tplReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            loader.putTemplate(name, sb.toString());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private TemplateHelper() {
    }

    public static Template getTemplate(String name) throws IOException {
        return configuration.getTemplate(name, "utf-8");
    }
}
