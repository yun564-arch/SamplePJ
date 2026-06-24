package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final HttpSession session;

    public TaskController(TaskService taskService, HttpSession session) {
        this.taskService = taskService;
        this.session = session;
    }

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

    // タスク編集フォーム表示(所有者チェック付き)
    @RequestMapping(value = "/tasks/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editTask(@PathVariable("id") Long id, ModelAndView mv) {
        String username = (String) session.getAttribute("username");
        Task task = taskService.findById(id, username); // 見つからなければ例外がスローされる
        mv.addObject("task", task);
        mv.setViewName("tasks/form-edit");
        return mv;
    }

    // タスク更新処理(所有者チェック付き)
    @RequestMapping(value = "/tasks/update/{id}", method = RequestMethod.POST)
    public ModelAndView updateTask(@PathVariable("id") Long id, @ModelAttribute Task task, ModelAndView mv) {
        try {
            String username = (String) session.getAttribute("username");
            task.setId(id);
            task.setUsername(username); // 所有者チェックのためusernameをセット
            taskService.update(task);
            mv.setViewName("redirect:/tasks");
        } catch (IllegalArgumentException error) {
            mv.addObject("error", error.getMessage());
            mv.addObject("task", task);
            mv.setViewName("tasks/form-edit");
        }
        return mv;
    }

    // タスク削除処理(所有者チェック付き)
    @RequestMapping(value = "/tasks/delete/{id}", method = RequestMethod.POST)
    public String deleteTask(@PathVariable("id") Long id) {
        String username = (String) session.getAttribute("username");
        taskService.deleteById(id, username); // 見つからなければ例外がスローされる
        return "redirect:/tasks";
    }
}