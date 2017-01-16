package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Calendar.
 */
@Entity
@Table(name = "calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "calendar_name")
    private String calendarName;

    @OneToMany(mappedBy = "calendar")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> calendarEvents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public Calendar calendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Set<Event> getCalendarEvents() {
        return calendarEvents;
    }

    public Calendar calendarEvents(Set<Event> events) {
        this.calendarEvents = events;
        return this;
    }

    public Calendar addCalendarEvent(Event event) {
        calendarEvents.add(event);
        event.setCalendar(this);
        return this;
    }

    public Calendar removeCalendarEvent(Event event) {
        calendarEvents.remove(event);
        event.setCalendar(null);
        return this;
    }

    public void setCalendarEvents(Set<Event> events) {
        this.calendarEvents = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Calendar calendar = (Calendar) o;
        if (calendar.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, calendar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Calendar{" +
            "id=" + id +
            ", calendarName='" + calendarName + "'" +
            '}';
    }
}
