package com.shorturl.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse {
    private String shortCode;
    private String originalUrl;
    private int visitCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccess;
    private String lastAccessIp;
    private String lastAccessUserAgent;
    private Integer maxUses;

    // Getters y setters
    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getLastAccessIp() {
        return lastAccessIp;
    }

    public void setLastAccessIp(String lastAccessIp) {
        this.lastAccessIp = lastAccessIp;
    }

    public String getLastAccessUserAgent() {
        return lastAccessUserAgent;
    }

    public void setLastAccessUserAgent(String lastAccessUserAgent) {
        this.lastAccessUserAgent = lastAccessUserAgent;
    }

    public Integer getMaxUses(){
        return maxUses;
    }

    public void setMaxUses (Integer maxUses){
        this.maxUses = maxUses;
    }
}
