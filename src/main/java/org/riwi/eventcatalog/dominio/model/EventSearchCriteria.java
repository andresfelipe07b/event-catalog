package org.riwi.eventcatalog.dominio.model;

import java.time.LocalDate;

public class EventSearchCriteria {
    private String city;
    private String category;
    private LocalDate date;

    public EventSearchCriteria(String city, String category, LocalDate date) {
        this.city = city;
        this.category = category;
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
