
package com.shorturl.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shorturl.model.Url;
import com.shorturl.repository.UrlRepository;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate; 
    private static final String ALPHANUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int SHORT_CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();
    private static final int MAX_REQUESTS_PER_MINUTE = 5; 

    public UrlService(UrlRepository urlRepository, RedisTemplate<String, String> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;  
    }
    public void save (Url url){
        urlRepository.save(url);
    }

    public Url createShortUrl(String originalUrl, Integer maxUses, Integer expireMinutes) {
        String shortCode = generateUniqueShortCode();
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        url.setMaxUses(maxUses);
        
        if (expireMinutes != null && expireMinutes > 0){
            url.setExpiresAt(LocalDateTime.now().plusMinutes(expireMinutes
            ));
            redisTemplate.opsForValue().set(shortCode, originalUrl, Duration.ofMinutes(expireMinutes));

        }else{
            redisTemplate.opsForValue().set(shortCode, originalUrl);
            url.setExpiresAt(null);
        }
        return urlRepository.save(url);
    }

    public Optional<Url> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    private String generateUniqueShortCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (urlRepository.findByShortCode(code).isPresent());
        return code;
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
    private static final List<String> BLACKLISTED_DOMAINS = List.of(
    "malware.com", "phishing.net", "evil.org");

    @SuppressWarnings("unused")
    private boolean isBlacklisted(String url) {
    try {
        URI uri = new URI(url);
        String host = uri.getHost();
        return BLACKLISTED_DOMAINS.stream().anyMatch(host::contains);
    } catch (URISyntaxException e) {
        return true; 
    }
}
    public Optional<Url> findByShortCode(String shortCode) {
        boolean isActive = redisTemplate.hasKey(shortCode);
        if (!isActive){
            return Optional.empty();
        }
        String cachedoriginalUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedoriginalUrl != null){
            Url url= new Url();
            url.setOriginalUrl(cachedoriginalUrl);
            url.setShortCode(shortCode);
            return Optional.of(url);
        }
        Optional<Url> urlOpt = urlRepository.findByShortCode(shortCode);
        urlOpt.ifPresent( url ->
            redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl()));
            return urlOpt;
        
    }
    public Url registerVisit(Url url, String ip, String userAgent ){

        url.setVisitCount(url.getVisitCount()+1);
        url.setLastAccessIp(ip);
        url.setLastAccessUserAgent(userAgent);
        url.setLastAccess(LocalDateTime.now());
        return urlRepository.save(url);
    }
    
    public boolean isBlackListed (String url ){
        try{
            URI uri = new URI ( url );
            String host =uri.getHost();
            List<String> blacklist=List.of("malware.com","phishing.net","evil.org");
            return blacklist.stream().anyMatch(host::contains);
        } catch(Exception e) {
            return true; 
        }
    }

    public boolean isRateLimited (String ip) {
        String key = "rate_limit:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count ==1){
        redisTemplate.expire(key, Duration.ofMinutes(1));
        }
        
        return count != null && count > MAX_REQUESTS_PER_MINUTE;
    }

    public Optional<Map<String, Object>> getUrlStatistics(String shortCode){
        Optional<Url> urlOpt =urlRepository.findByShortCode(shortCode);
        if (urlOpt.isEmpty()){
            return Optional.empty();
        }
        Url url = urlOpt.get();
        Map<String, Object> stats= new HashMap<>();
        stats.put("shortCode", url.getShortCode());
        stats.put("originalUrl",url.getOriginalUrl());
        stats.put("createdAd", url.getCreatedAt());
        stats.put("visitCount", url.getVisitCount());
        stats.put("lastAccess", url.getLastAccess());
        stats.put("maxUses", url.getMaxUses());
        stats.put("expiresAt", url.getExpiresAt());

        return Optional.of(stats);
    }

}
