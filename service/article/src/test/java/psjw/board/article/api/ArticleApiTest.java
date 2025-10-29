package psjw.board.article.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import psjw.board.article.entity.Article;
import psjw.board.article.service.response.ArticlePageResponse;
import psjw.board.article.service.response.ArticleResponse;

import java.util.List;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest(){
        ArticleResponse response = create(new ArticleCreateRequest("hi", "my content", 1L, 1L));
        System.out.println("response = " + response);
        
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }


    @Test
    void readTest(){
        ArticleResponse response = read(241866106913931264L);
        System.out.println("response = " + response);
    }

    @Test
    void updateTest(){
        update(241866106913931264L);
        ArticleResponse response = read(241866106913931264L);
        System.out.println("response = " + response);
    }



    void update(Long articleId){
        restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest("hi 2", "my content 22"))
                .retrieve()
                .body(ArticleResponse.class);
    }


    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void deleteTest(){
        restClient.delete()
                .uri("/v1/articles/{articleId}", 241866106913931264L)
                .retrieve();
    }

    @Test
    void readAllTest(){
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&pageSize=30&page=50000")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response.getArticleCount() = " + response.getArticleCount());
        for(ArticleResponse article : response.getArticles()) {
            System.out.println("articleId() = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest(){
        List<ArticleResponse> articles1 = restClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {

                });

        System.out.println("firstPage");
        for(ArticleResponse article : articles1) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }
        Long lastArticleId = articles1.getLast().getArticleId();
        List<ArticleResponse> articles2 = restClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=%s".formatted(lastArticleId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {

                });
        System.out.println("secondPage");
        for(ArticleResponse article : articles2) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }
    }


    @Getter
    @AllArgsConstructor
    public class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;

    }

    @Getter
    @AllArgsConstructor
    public class ArticleUpdateRequest {
        private String title;
        private String content;

    }
}
