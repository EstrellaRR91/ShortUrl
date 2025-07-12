package com.shorturl.dto;

public class UrlRequest {
    private String originalUrl;
    private Integer maxUses;
    private Integer expireMinutes;   

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getExpireMinutes(){
        return expireMinutes;
    }

    public void setExpireMinutes (Integer expireMinutes){
        this.expireMinutes = expireMinutes;
    }
}