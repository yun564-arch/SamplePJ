package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // タスク一覧表示
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ModelAndView tasks(
            @RequestParam(name = "page", defaultValue = "1") int page,
            Principal principal, ModelAndView mv) {
        String username = principal.getName();
        int size = 10;
        List<Task> taskList = taskService.findByPage(username, page, size);
        int totalCount = taskService.countByUsername(username);
        int totalPages = (int) Math.ceil((double) totalCount / size);
        mv.addObject("tasks", taskList);
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", totalPages);
        mv.setViewName("tasks/list");
        return mv;
    }

    @RequestMapping(value = "/tasks/new", method = RequestMethod.GET)
    public ModelAndView newTask(ModelAndView mv) {
        mv.setViewName("tasks/form-new");
        return mv;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.POST)
    public ModelAndView tasks(@ModelAttribute Task task, Principal principal, ModelAndView mv) {
        try {
            task.setUsername(principal.getName());
            taskService.save(task);
            mv.setViewName("redirect:/tasks");
        } catch (IllegalArgumentException error) {
            mv.addObject("error", error.getMessage());
            mv.addObject("task", task);
            mv.setViewName("tasks/form-new");
        }
        return mv;
    }

    @RequestMapping(value = "/tasks/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editTask(@PathVariable("id") Long id, Principal principal, ModelAndView mv) {
        Task task = taskService.findById(id, principal.getName());
        mv.addObject("task", task);
        mv.setViewName("tasks/form-edit");
        return mv;
    }

    @RequestMapping(value = "/tasks/update/{id}", method = RequestMethod.POST)
    public ModelAndView updateTask(@PathVariable("id") Long id, @ModelAttribute Task task,
            Principal principal, ModelAndView mv) {
        try {
            task.setId(id);
            task.setUsername(principal.getName());
            taskService.update(task);
            mv.setViewName("redirect:/tasks");
        } catch (IllegalArgumentException error) {
            mv.addObject("error", error.getMessage());
            mv.addObject("task", task);
            mv.setViewName("tasks/form-edit");
        }
        return mv;
    }

    @RequestMapping(value = "/tasks/delete/{id}", method = RequestMethod.POST)
    public String deleteTask(@PathVariable("id") Long id, Principal principal) {
        taskService.deleteById(id, principal.getName());
        return "redirect:/tasks";
    }
}