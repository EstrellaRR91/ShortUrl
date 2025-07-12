

package com.shorturl.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, length = 100, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private int visitCount =0; 

    private LocalDateTime lastAccess;

    private String lastAccessIp; 

    private String lastAccessUserAgent; 

    private Integer maxUses; 

    @JsonIgnore
    private LocalDateTime expiresAt; 

    @GetMapping

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getVisitCount()  {
        return visitCount;
    }

    public void setVisitCount (int visitCount){
        this.visitCount = visitCount;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }
    public void setLastAccess (LocalDateTime lastAccess){
        this.lastAccess = lastAccess; 
    }

    public String getLastAccessIp() {
        return lastAccessIp;
    }

    public void setLastAccessIp (String lastAccessIp) {
        this.lastAccessIp = lastAccessIp;
    }

    public String getLastAccessUserAgent () {
        return lastAccessUserAgent;
    }
    
    public void setLastAccessUserAgent (String lastAccessUserAgent) {
        this.lastAccessUserAgent = lastAccessUserAgent;
    }

    public Integer getMaxUses(){
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }
}
