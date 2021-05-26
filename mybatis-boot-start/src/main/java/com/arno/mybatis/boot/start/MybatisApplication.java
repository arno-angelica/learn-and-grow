package com.arno.mybatis.boot.start;

import com.arno.mybatis.boot.start.auto.entry.Person;
import com.arno.mybatis.boot.start.auto.mapper.PersonMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Arno
 * @since
 */
@SpringBootApplication(scanBasePackages = {"com.arno.mybatis.boot.start.auto"})
@EnableAutoConfiguration
public class MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }

    @RestController
    @RequestMapping("test")
    public class TestController {

        @Resource
        private PersonMapper personMapper;

        @GetMapping("get")
        public String get() {
            List<Person> personList = personMapper.selectPerson();
            if (personList != null) {
                return personList.toString();
            }
            return null;
        }
    }
}
