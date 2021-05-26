package com.arno.mybatis.boot.start.auto.mapper;


import com.arno.mybatis.boot.start.auto.entry.Person;

import java.util.List;

/**
 * @author Arno
 * @since
 */
public interface PersonMapper {
    List<Person> selectPerson();
}
