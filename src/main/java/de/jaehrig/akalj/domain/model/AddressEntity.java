package de.jaehrig.akalj.domain.model;

import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(indexes = {
        @Index(name = "street_number_ux", columnList = "street, number", unique = true)
})
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic
    private Integer id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private String number;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "address_fk")
    private Collection<CalendarEntryEntity> entries;

    public AddressEntity() {
        // empty
    }

    public AddressEntity(String street, String number) {
         this.street = street;
         this.number = number;
         entries = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Collection<CalendarEntryEntity> getEntries() {
        return entries;
    }

    public void setEntries(final Collection<CalendarEntryEntity> entries) {
        this.entries.clear();
        this.entries .addAll(entries);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
               "street = " + street + ", " +
               "number = " + number + ")";
    }
}
