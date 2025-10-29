package psjw.board.article.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psjw.board.article.entity.Article;
import psjw.board.article.repository.ArticleRepository;
import psjw.board.article.service.request.ArticleCreateRequest;
import psjw.board.article.service.request.ArticleUpdateRequest;
import psjw.board.article.service.response.ArticleResponse;
import psjw.board.common.snowflake.Snowflake;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(Article.create(snowflake.nextId(), request.getTitle(),
                request.getContent(), request.getBoardId(), request.getWriterId()));

        return ArticleResponse.from(article);

    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
    }

    @Transactional
    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
