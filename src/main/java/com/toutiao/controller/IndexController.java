package com.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by snowWave.
 */

@Controller
public class IndexController {

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(){
        return "头条";
    }





}
