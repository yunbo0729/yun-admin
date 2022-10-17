package com.yun.admin.service.impl;

import com.yun.admin.annotation.DataSource;
import com.yun.admin.enumn.DataSourceType;
import com.yun.admin.mapper.PersonMapper;
import com.yun.admin.model.pojo.Person;
import com.yun.admin.model.pojo.SubSign;
import com.yun.admin.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements IPersonService {
    @Autowired
    private PersonMapper personMapper;

    @Override
    public List<Person> selectList(Person person) {
        return personMapper.selectList(person);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<SubSign> selectSubSignList(SubSign subSign) {
        return personMapper.selectSubSignList(subSign);
    }
}
