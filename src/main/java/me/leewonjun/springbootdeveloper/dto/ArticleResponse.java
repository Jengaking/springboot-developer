package me.leewonjun.springbootdeveloper.dto;

import lombok.Getter;
import me.leewonjun.springbootdeveloper.domain.Article;
import me.leewonjun.springbootdeveloper.service.BlogService;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
