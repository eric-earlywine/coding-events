package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.TagsRepository;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("tags")
public class TagsController {
    @Autowired
    private TagsRepository tagsRepository;

    @GetMapping
    public String displayAllTags(Model model) {
        model.addAttribute("title", "All Tags");
        model.addAttribute("tags", tagsRepository.findAll());
        return "tags/index";
    }

    @GetMapping("create")
    public String renderCreateTagForm(Model model) {
        model.addAttribute("title", "Create Tag");
        model.addAttribute(new Tags());
        return "tags/create";
    }
    @PostMapping("create")
    public String processCreateTagForm(@ModelAttribute @Valid Tags tag, Errors errors, Model model) {
        model.addAttribute("title", "Create Tag");
        if(errors.hasErrors()) {
            return "tags/create";
        }
        tagsRepository.save(tag);
        return "redirect:";
    }
}
