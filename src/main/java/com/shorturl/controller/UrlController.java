package com.shorturl.controller;

import java.net.URI;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shorturl.dto.UrlRequest;
import com.shorturl.dto.UrlStatsResponse;
import com.shorturl.model.Url;
import com.shorturl.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService urlService;


    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody UrlRequest request, HttpServletRequest httpRequest) {
        String originalUrl = request.getOriginalUrl();
        String clientIp = httpRequest.getRemoteAddr(); 

        if(urlService.isRateLimited(clientIp)){
            return ResponseEntity.status(HttpStatus. TOO_MANY_REQUESTS)
            .body("Demasiadas solicitudes desde esta IP. Intenta más tarde.");
        }

        if (originalUrl == null || originalUrl.isBlank()) {
            return ResponseEntity.badRequest().body("La URL original no puede estar vacía");
        }

        if (!isValidUrl(originalUrl)) {
            return ResponseEntity.badRequest().body("La URL proporcionada no es válida");
        }

        if (urlService.isBlackListed(originalUrl)){
            return ResponseEntity.badRequest().body("La URL está en una lista negra y no puede ser acortada");
        }
        

        Url url = urlService.createShortUrl(originalUrl, request.getMaxUses(), request.getExpireMinutes());
        

        return ResponseEntity.ok(url);
    }

    private boolean isValidUrl(String url) {
        try {
            new java.net.URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode, HttpServletRequest request) {
        Url url = urlService.findByShortCode(shortCode);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        // máximo de usos 
        if (url.getMaxUses() != null && url.getVisitCount() >= url.getMaxUses()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Registrar visita
        urlService.registerVisit(url, request.getRemoteAddr(), request.getHeader("User-Agent"));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.getOriginalUrl()));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<?> getUrlStats(@PathVariable String shortCode, HttpServletRequest request) {
        String ip= request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        Url url = urlService.findByShortCode(shortCode);
        urlService.registerVisit( url, ip, userAgent);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        UrlStatsResponse stats = new UrlStatsResponse();
        stats.setOriginalUrl(url.getOriginalUrl());
        stats.setShortCode(url.getShortCode());
        stats.setVisitCount(url.getVisitCount());
        stats.setLastAccess(url.getLastAccess());
        stats.setLastAccessIp(url.getLastAccessIp());
        stats.setLastAccessUserAgent(url.getLastAccessUserAgent());
        stats.setMaxUses(url.getMaxUses());

        return ResponseEntity.ok(stats);
    }
}
