package org.launchcode.codingevents.models;

import javax.persistence.Entity;

@Entity
public class EventCategory extends AbstractEntity {
    public EventCategory(String name) {
        super(name);
    }
    public EventCategory() {}
}
