package org.launchcode.codingevents.models.dto;

import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.Tags;

import javax.validation.constraints.NotNull;

public class EventTagDTO {

    @NotNull
    private Event event;

    @NotNull
    private Tags tag;

    public EventTagDTO() {}

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }
}
