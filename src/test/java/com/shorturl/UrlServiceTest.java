package com.shorturl;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.shorturl.model.Url;
import com.shorturl.repository.UrlRepository;
import com.shorturl.service.UrlService;


@SpringBootTest
@ActiveProfiles("test")
public class UrlServiceTest {

    
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

    @Test
    public void testCreateShortUrl() {

        Url url = urlService.createShortUrl("https://youtube.com", null, null);

        assertThat(url.getShortCode()).isNotNull();
        assertThat(url.getOriginalUrl()).isEqualTo("https://youtube.com");
    }


}
