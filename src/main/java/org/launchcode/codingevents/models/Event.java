package org.launchcode.codingevents.models;

import org.thymeleaf.util.ArrayUtils;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chris Bay
 */
@Entity
public class Event extends AbstractEntity {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @ManyToOne
    @NotNull(message = "Event Category required")
    private EventCategory eventCategory;

    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    private EventDetails eventDetails;

    @ManyToMany
    private final List<Tags> tags = new ArrayList<>();

    public Event(String name, EventCategory eventCategory, EventDetails eventDetails) {
        this.name = name;
        this.eventDetails = eventDetails;
        this.eventCategory = eventCategory;
    }

    public Event() { }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

    public List<Tags> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTag(Tags tag) {
        this.tags.add(tag);
    }
}
