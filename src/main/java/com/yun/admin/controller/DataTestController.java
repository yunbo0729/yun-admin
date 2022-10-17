package com.yun.admin.controller;

import com.yun.admin.model.pojo.Person;
import com.yun.admin.model.pojo.SubSign;
import com.yun.admin.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/person")
public class DataTestController {
    @Autowired
    private IPersonService personService;

    @GetMapping("/a01")
    public Object getPersonMsg(Person person){
        return personService.selectList(person);
    }

    @GetMapping("/a02")
    public Object getPersonMsg(SubSign subSign){
        return personService.selectSubSignList(subSign);
    }
}
