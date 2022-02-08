package io.legacyfighter.cabs.entity;

import io.legacyfighter.cabs.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class AwardedMiles extends BaseEntity {
    // Aggregate
    // 1. mile celowo są osobno, aby się mogło rozjechać na ich wydawaniu -> docelowo: kolekcja VOs w agregacie

    // VO
    // 1. miles + expirationDate -> VO przykrywające logikę walidacji, czy nie przekroczono daty ważności punktów
    // 2. wydzielenie interfejsu Miles -> różne VO z różną logiką, np. ExpirableMiles, NonExpirableMiles, LinearExpirableMiles

    @ManyToOne
    private Client client;

    @Column(nullable = false)
    private Integer miles;

    @Column(nullable = false)
    private Instant date = Instant.now();

    private Instant expirationDate;

    private Boolean isSpecial;

    @ManyToOne
    private Transit transit;

    public AwardedMiles() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getMiles() {
        return miles;
    }

    public void setMiles(Integer miles) {
        this.miles = miles;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(Boolean special) {
        isSpecial = special;
    }

    public void setTransit(Transit transit) {
        this.transit = transit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AwardedMiles))
            return false;

        AwardedMiles other = (AwardedMiles) o;

        return this.getId() != null &&
                this.getId().equals(other.getId());
    }
}