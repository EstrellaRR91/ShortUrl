package com.shorturl.service;

import com.shorturl.model.Url;
import com.shorturl.repository.UrlRepository;
import org.springframework.data.redis.core.RedisTemplate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class UrlServiceTest {

    @MockBean
    private RedisTemplate<String, String> RedisTemplate;
    
    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @AfterEach
    void cleanUp(){
        urlRepository.deleteAll();
    }

    @Test
    public void testCreateAndFindUrl() {
        Url url = new Url();
        url.setOriginalUrl("https://youtube.com");
        url.setShortCode("abc123");

        urlRepository.save(url);

        Optional<Url> found = urlService.findByShortCode("abc123");

        assertThat(found).isPresent();
        assertThat(found.get().getOriginalUrl()).isEqualTo("https://youtube.com");

    }

}
