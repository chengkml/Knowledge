package com.ck.knowledge.controller;

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

    @GetMapping("knowledge")
    public String knowledge() {
        return "knowledge";
    }
}
