package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HttpSession session;

    // タスク一覧表示
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ModelAndView tasks(
            @RequestParam(name = "page", defaultValue = "1") int page,
            ModelAndView mv) {

        String username = (String) session.getAttribute("username");

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
    

    // 新規タスク登録フォーム表示
    @RequestMapping(value = "/tasks/new", method = RequestMethod.GET)
    public ModelAndView newTask(ModelAndView mv) {
        mv.setViewName("tasks/form-new");
        return mv;
    }

    // 新規タスク登録処理
    @RequestMapping(value = "/tasks", method = RequestMethod.POST)
    public ModelAndView tasks(@ModelAttribute Task task, ModelAndView mv) {
        try {
            String username = (String) session.getAttribute("username");
            task.setUsername(username);

            taskService.save(task);
            mv.setViewName("redirect:/tasks");
        } catch (IllegalArgumentException error) {
            mv.addObject("error", error.getMessage());
            mv.addObject("task", task);
            mv.setViewName("tasks/form-new");
        }
        return mv;
    }
    

    // タスク編集フォーム表示
    @RequestMapping(value = "/tasks/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editTask(@PathVariable("id") Long id, ModelAndView mv) {        Task task = taskService.findById(id);
        mv.addObject("task", task);
        mv.setViewName("tasks/form-edit");
        return mv;
    }

    // タスク更新処理
    @RequestMapping(value = "/tasks/update/{id}", method = RequestMethod.POST)
    public ModelAndView updateTask(@PathVariable("id") Long id, @ModelAttribute Task task, ModelAndView mv) {
    
        try {
            taskService.update(task);
            mv.setViewName("redirect:/tasks");
        } catch (IllegalArgumentException error) {
            mv.addObject("error", error.getMessage());
            mv.addObject("task", task);
            mv.setViewName("tasks/form-edit");
        }
        return mv;
    }

    // タスク削除処理
    @RequestMapping(value = "/tasks/delete/{id}", method = RequestMethod.POST)
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteById(id);
        return "redirect:/tasks";
    }
}