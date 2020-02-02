package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.data.TagsRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.EventDetails;
import org.launchcode.codingevents.models.Tags;
import org.launchcode.codingevents.models.dto.EventTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Chris Bay
 */
@Controller
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @GetMapping
    public String displayEvents(@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Integer tagId, Model model) {
        if (categoryId == null && tagId == null) {
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventRepository.findAll());
        } else {
            if (categoryId != null) {
                if (tagId == null) {
                    Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
                    if (result.isEmpty()) {
                        model.addAttribute("title", "Invalid Category ID: " + categoryId);
                    } else {
                        EventCategory category = result.get();
                        model.addAttribute("title", "Events in category: " + category.getName());
                        model.addAttribute("events", category.getEvents());
                    }
                } else {
                    Optional<EventCategory> result1 = eventCategoryRepository.findById(categoryId);
                    Optional<Tags> result2 = tagsRepository.findById(tagId);
                    if (result1.isEmpty() || result2.isEmpty()) {
                        model.addAttribute("title", "Either an invalid Category ID or Tag ID");
                    } else {
                        EventCategory category = result1.get();
                        Tags tag = result2.get();
                        List<Event> eventResults = new ArrayList<>();
                        for (Event event : category.getEvents()) {
                            if (tag.getEvents().contains(event)) {
                                eventResults.add(event);
                            }
                        }
                        model.addAttribute("title", "Events with category: " + category.getName() + " and Tag: " + tag.getName());
                        model.addAttribute("events", eventResults);
                    }
                }
            } else {
                Optional<Tags> result = tagsRepository.findById(tagId);
                if (result.isEmpty()) {
                    model.addAttribute("title", "Invalid Tag ID: " + tagId);
                } else {
                    Tags tag = result.get();
                    model.addAttribute("title", "Events with tag: " + tag.getName());
                    model.addAttribute("events", tag.getEvents());
                }
            }
        }
        return "events/index";
    }

    @GetMapping("create")
    public String displayCreateEventForm(Model model) {
        model.addAttribute("title", "Create Event");
        model.addAttribute(new Event());
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "events/create";
    }

    @PostMapping("create")
    public String processCreateEventForm(@ModelAttribute @Valid Event newEvent,
                                         Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("title", "Create Event");
            model.addAttribute("categories", eventCategoryRepository.findAll());
            return "events/create";
        }

        eventRepository.save(newEvent);
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteEventForm(Model model) {
        model.addAttribute("title", "Delete Events");
        model.addAttribute("events", eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] eventIds) {

        if (eventIds != null) {
            for (int id : eventIds) {
                eventRepository.deleteById(id);
            }
        }

        return "redirect:";
    }
    @GetMapping("edit/{eventId}")
    public String displayEditForm(Model model, @PathVariable int eventId) {
        Optional<Event> result = eventRepository.findById(eventId);
        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid event ID");
        } else {
            Event editEvent = result.get();
            model.addAttribute("title", "Edit Event " + editEvent.getName() + "(id=" + editEvent.getId() + ")");
            model.addAttribute("event", editEvent);
            model.addAttribute("categories", eventCategoryRepository.findAll());
        }
        return "events/edit";
    }
    @PostMapping("edit")
    public String processEditForm(@ModelAttribute @Valid Event editEvent, Errors errors, Model model) {
        Optional<Event> result = eventRepository.findById(editEvent.getId());
        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid event ID");
        } else if(errors.hasErrors()) {
            model.addAttribute("title", "Edit Event " + editEvent.getName() + "(id=" + editEvent.getId() + ")");
            model.addAttribute("event", editEvent);
            model.addAttribute("categories", eventCategoryRepository.findAll());
            return "events/edit";
        } else {
            eventRepository.save(editEvent);
        }
        return "redirect:";
    }
    @GetMapping("detail")
    public String displayEvent(@RequestParam Integer eventId, Model model) {
        Optional<Event> result = eventRepository.findById(eventId);
        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid event ID: " + eventId);
        } else {
            Event event = result.get();
            model.addAttribute("title", "Details for event: " + event.getName());
            model.addAttribute("event", event);
        }
        return "events/detail";
    }
    @GetMapping("add-tag")
    public String displayAddTagForm(@RequestParam Integer eventId, Model model){
        Optional<Event> result = eventRepository.findById(eventId);
        Event event = result.get();
        model.addAttribute("title", "Add Tag to: " + event.getName());
        model.addAttribute("tags", tagsRepository.findAll());
        EventTagDTO eventTag = new EventTagDTO();
        eventTag.setEvent(event);
        model.addAttribute("eventTag", eventTag);
        return "events/add-tag.html";
    }

    @PostMapping("add-tag")
    public String processAddTagForm(@ModelAttribute @Valid EventTagDTO eventTag,
                                    Errors errors,
                                    Model model){

        if (!errors.hasErrors()) {
            Event event = eventTag.getEvent();
            Tags tag = eventTag.getTag();
            if (!event.getTags().contains(tag)){
                event.addTag(tag);
                eventRepository.save(event);
            }
            return "redirect:detail?eventId=" + event.getId();
        }

        return "redirect:add-tag";
    }
}
