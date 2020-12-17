package com.ck.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: RouterController
 * @Author: Chengkai
 * @Date: 2019/6/19 19:46
 * @Version: 1.0
 */
@Controller
@RequestMapping
public class RouterController {

    @GetMapping("frame")
    public String frame() {
        return "frame/frame";
    }

    @GetMapping("knowledge")
    public String knowledge() {
        return "knowledge/knowledge";
    }

    @GetMapping("api/list")
    public String apiList() {
        return "api/api_list";
    }

    @GetMapping("menu")
    public String menuTree() {
        return "menu/menu_tree";
    }

    @GetMapping("todo")
    public String todo() {
        return "todo/todo_list";
    }

    @GetMapping("bat")
    public String bat() {
        return "bat/bat_list";
    }
}
