package com.belleange.mall.repository;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CertificationNumberRepository {

    private final Map<String, CertificationEntry> store = new ConcurrentHashMap<>();

    private static class CertificationEntry {
        String certificationNumber;
        Instant expirationTime;

        CertificationEntry(String certificationNumber, Instant expirationTime) {
            this.certificationNumber = certificationNumber;
            this.expirationTime = expirationTime;
        }
    }

    public void saveCertificationNumber(String email, String certificationNumber, long expirationInSeconds) {
        Instant expirationTime = Instant.now().plusSeconds(expirationInSeconds);
        store.put(email, new CertificationEntry(certificationNumber, expirationTime));
    }

    public String getCertificationNumber(String email) {
        CertificationEntry entry = store.get(email);
        if (entry == null || Instant.now().isAfter(entry.expirationTime)) {
            return null;
        }
        return entry.certificationNumber;
    }

    public void removeCertificationNumber(String email) {
        store.remove(email);
    }

    public boolean hasKey(String email) {
        CertificationEntry entry = store.get(email);
        return entry != null && Instant.now().isBefore(entry.expirationTime);
    }
}
