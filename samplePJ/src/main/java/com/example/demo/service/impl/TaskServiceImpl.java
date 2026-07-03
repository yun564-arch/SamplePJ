package com.example.demo.service.impl;

import com.example.demo.entity.Task;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.service.TaskService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }


    // タスクを1件取得するメソッド(所有者チェック付き、見つからなければ例外)
    @Transactional(readOnly = true)
    public Task findById(Long id, String username) {
        Task task = taskMapper.findById(id, username);
        if (task == null) {
            throw new TaskNotFoundException("タスクが見つかりません");
        }
        return task;
    }

 // 入力チェック用の共通メソッド(日付相関チェックはControllerのBindingResultに移動)
    private void validate(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new IllegalArgumentException("タイトルを入力してください");
        }
        // 日付相関チェックを削除(ControllerのvalidateDateRange()で対応)
    }
        

    // タスクを登録するメソッド
    public void save(Task task) {
        validate(task);
        taskMapper.save(task);
    }

    // タスクを更新するメソッド(所有者チェック付き)
    public void update(Task task) {
        validate(task);
        int updatedCount = taskMapper.update(task);
        if (updatedCount == 0) {
            throw new TaskNotFoundException("タスクが見つかりません");
        }
    }

    // タスクを削除するメソッド(所有者チェック付き)
    public void deleteById(Long id, String username) {
        int deletedCount = taskMapper.deleteById(id, username);
        if (deletedCount == 0) {
            throw new TaskNotFoundException("タスクが見つかりません");
        }
    }

    //ページング機能
    @Override
    @Transactional(readOnly = true)
    public List<Task> findByPage(String username, int page, int size) {
        int offset = (page - 1) * size;
        return taskMapper.findByPage(username, size, offset);
    }

    @Override
    @Transactional(readOnly = true)
    public int countByUsername(String username) {
        return taskMapper.countByUsername(username);
    }
}