package com.shorturl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shorturl.model.Url;


public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);

}
