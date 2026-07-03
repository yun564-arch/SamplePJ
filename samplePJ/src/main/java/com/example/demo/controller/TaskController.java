package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.List;

@Controller
public class TaskController {

    private static final int PAGE_SIZE = 10;
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ModelAndView tasks(
            @RequestParam(name = "page", defaultValue = "1") int page,
            Principal principal, ModelAndView mv) {
        String username = principal.getName();
        List<Task> taskList = taskService.findByPage(username, page, PAGE_SIZE);
        int totalCount = taskService.countByUsername(username);
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        mv.addObject("tasks", taskList);
        mv.addObject("currentPage", page);
        mv.addObject("totalPages", totalPages);
        mv.setViewName("tasks/list");
        return mv;
    }

    // 新規タスク登録フォーム表示
    @RequestMapping(value = "/tasks/new", method = RequestMethod.GET)
    public ModelAndView newTask(ModelAndView mv) {
        mv.addObject("task", new Task());
        mv.setViewName("tasks/form-new");
        return mv;
    }

    // 新規タスク登録処理
    @RequestMapping(value = "/tasks", method = RequestMethod.POST)
    public ModelAndView tasks(@Valid @ModelAttribute("task") Task task,
            BindingResult bindingResult, Principal principal, ModelAndView mv) {

        // 日付相関チェック(Bean Validationの後に行う)
        validateDateRange(task, bindingResult);

        if (bindingResult.hasErrors()) {
            mv.setViewName("tasks/form-new");
            return mv;
        }
        task.setUsername(principal.getName());
        taskService.save(task);
        mv.setViewName("redirect:/tasks");
        return mv;
    }

    @RequestMapping(value = "/tasks/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editTask(@PathVariable("id") Long id, Principal principal, ModelAndView mv) {
        Task task = taskService.findById(id, principal.getName());
        mv.addObject("task", task);
        mv.setViewName("tasks/form-edit");
        return mv;
    }

    // タスク更新処理
    @RequestMapping(value = "/tasks/update/{id}", method = RequestMethod.POST)
    public ModelAndView updateTask(@PathVariable("id") Long id,
            @Valid @ModelAttribute("task") Task task, BindingResult bindingResult,
            Principal principal, ModelAndView mv) {
        task.setId(id);

        // 日付相関チェック(Bean Validationの後に行う)
        validateDateRange(task, bindingResult);

        if (bindingResult.hasErrors()) {
            mv.setViewName("tasks/form-edit");
            return mv;
        }
        task.setUsername(principal.getName());
        taskService.update(task);
        mv.setViewName("redirect:/tasks");
        return mv;
    }

    @RequestMapping(value = "/tasks/delete/{id}", method = RequestMethod.POST)
    public String deleteTask(@PathVariable("id") Long id, Principal principal) {
        taskService.deleteById(id, principal.getName());
        return "redirect:/tasks";
    }

    // 日付相関チェック(共通メソッド)
    private void validateDateRange(Task task, BindingResult bindingResult) {
        if (task.getStartDate() != null && task.getEndDate() != null) {
            if (task.getStartDate().isAfter(task.getEndDate())) {
                bindingResult.rejectValue(
                    "endDate",
                    "task.endDate.invalid",
                    "終了日は開始日以降にしてください"
                );
            }
        }
    }
}