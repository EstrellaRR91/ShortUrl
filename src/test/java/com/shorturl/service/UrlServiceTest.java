package com.shorturl.service;

import com.shorturl.model.Url;
import com.shorturl.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UrlServiceTest {
    
    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @Test
    public void testCreateAndFindUrl() {
        // Crear una URL nueva
        Url url = new Url();
        url.setOriginalUrl("https://openai.com");
        url.setShortUrl("abc123");

        // Guardar en base de datos (H2 en memoria)
        urlRepository.save(url);

        // Buscar por shortUrl usando el servicio
        Optional<Url> found = urlService.findByShortUrl("abc123");

        assertThat(found).isPresent();
        assertThat(found.get().getOriginalUrl()).isEqualTo("https://openai.com");

    }
}
