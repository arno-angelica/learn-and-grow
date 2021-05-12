package com.arno.mybatis.mapper;

import com.arno.mybatis.entry.Person;

import java.util.List;

/**
 * @author Arno
 * @since
 */
public interface PersonMapper {
    List<Person> selectPerson();
}
