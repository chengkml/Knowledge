package com.ck.journal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: RouterController
 * @Author: Chengkai
 * @Date: 2019/8/20 11:48
 * @Version: 1.0
 */
@Controller
@RequestMapping
public class RouterController {

    @GetMapping("journal")
    public String journal() {
        return "journal";
    }
}
