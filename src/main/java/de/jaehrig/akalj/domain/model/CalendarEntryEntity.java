package de.jaehrig.akalj.domain.model;

import de.jaehrig.akalj.domain.GarbageType;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CalendarEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private GarbageType type;

    @Column(nullable = false)
    private LocalDate date;

    public CalendarEntryEntity(GarbageType type, LocalDate date) {
        this.type = type;
        this.date = date;
    }

    public CalendarEntryEntity() {
        // empty
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public GarbageType getType() {
        return type;
    }

    public void setType(final GarbageType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }
}
