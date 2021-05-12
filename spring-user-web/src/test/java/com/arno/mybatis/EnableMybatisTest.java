package com.arno.mybatis;

import com.arno.mybatis.annotation.EnableMybatis;
import com.arno.mybatis.mapper.PersonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


/**
 * @author Arno
 * @since
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EnableMybatisTest.class, DatabaseConfig.class})
@EnableMybatis(dataSource = "demoDataSource",
        mapperLocations="classpath*:/mapper/*Mapper.xml",
        typeAliasesPackage="com.arno.mybatis.entry.*",
        basePackages = "com.arno.mybatis.mapper")
public class EnableMybatisTest {

    @Resource
    private PersonMapper personMapper;

    @Test
    public void test() {
        System.out.println(personMapper.selectPerson());
    }
}
