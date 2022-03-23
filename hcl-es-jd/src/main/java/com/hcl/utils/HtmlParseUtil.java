package com.hcl.utils;

import com.hcl.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: hcl-es-api
 * @description:
 * @author: 作者
 * @create: 2022-02-07 15:41
 */
@Component
public class HtmlParseUtil {
    public static void main(String[] args) throws IOException {
//        //获取请求https://search.jd.com/Search?keyword=java
//        // 前提,需要联网!
//        String url = "https://search.jd.com/Search?keyword=java";
//        //解析网页。(Jsoup返回Document就是浏览器Document对象)
//        Document document = Jsoup.parse(new URL(url), 30000);
//        //所有你在js中可以使用的方法，这里都能用!
//        Element element=document.getElementById("J_goodsList");
//        //获取所有的li元素
//        Elements elements=element.getElementsByTag("li");
//        //获取元素的内容，这里el就是每一个li标签
//        for (Element el : elements) {
//            //关于这种图片特别多的网站，所有的图片都是延迟加载的!
//            String img=el.getElementsByTag("img").eq(0).attr("data-lazy-img");
//            String price=el.getElementsByClass("p-price").eq(0).text();
//            String title=el.getElementsByClass("p-name").eq(0).text();
//        }
        new HtmlParseUtil().parseJD("vue").forEach(System.out::println);
    }
    public List<Content> parseJD(String keywords)throws IOException {
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element=document.getElementById("J_goodsList");
        Elements elements=element.getElementsByTag("li");
        List<Content> goodsList = new ArrayList<>();
        for (Element el : elements) {
            String img=el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price=el.getElementsByClass("p-price").eq(0).text();
            String title=el.getElementsByClass("p-name").eq(0).text();
            Content content = new Content();
            content.setTitle(title);
            content.setPrice(price);
            content.setImg(img);
            goodsList.add(content);
        }
        return goodsList;
    }
}
