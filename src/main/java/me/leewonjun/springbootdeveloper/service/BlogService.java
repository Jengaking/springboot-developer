package me.leewonjun.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.leewonjun.springbootdeveloper.domain.Article;
import me.leewonjun.springbootdeveloper.dto.AddArticleRequest;
import me.leewonjun.springbootdeveloper.dto.UpdateArticleRequest;
import me.leewonjun.springbootdeveloper.repository.BlogRepository;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntiry(userName));
    }

    
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    // self constructing
    public void deleteById(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: "+id));

        authorizeAricleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    public void delete(long id) {
        Article article = blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found : " + id));


    }


    //게시글을 작성한 유저인지 확인
    private static void authorizeAricleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
