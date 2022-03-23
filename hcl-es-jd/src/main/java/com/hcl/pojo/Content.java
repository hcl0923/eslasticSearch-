package com.hcl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Jsoup
 * @description:
 * @author: 作者
 * @create: 2021-04-30 18:22
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private String title;
    private String img;
    private String price;
    //可以自己添加属性
}
