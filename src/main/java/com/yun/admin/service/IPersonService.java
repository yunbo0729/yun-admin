package com.yun.admin.service;


import com.yun.admin.model.pojo.Person;
import com.yun.admin.model.pojo.SubSign;

import java.util.List;

public interface IPersonService {
    List<Person> selectList(Person person);

    List<SubSign> selectSubSignList(SubSign subSign);
}
