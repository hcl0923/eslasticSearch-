package com.hcl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: hcl-es-api
 * @description:
 * @author: 作者
 * @create: 2022-02-07 11:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private int age;
}
