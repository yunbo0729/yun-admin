package com.yun.admin.mapper;



import com.yun.admin.model.pojo.Person;
import com.yun.admin.model.pojo.SubSign;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {
    List<Person> selectList(Person person);

    List<SubSign> selectSubSignList(SubSign subSign);
}
