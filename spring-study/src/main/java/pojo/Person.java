package pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @date 2024/4/18 22:16
 */

@Data
public class Person implements Serializable {

    private String name;
    private Integer age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
