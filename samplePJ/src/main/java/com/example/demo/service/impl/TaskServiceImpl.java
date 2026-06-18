package com.example.demo.service.impl;

import com.example.demo.entity.Task;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    // タスク一覧を取得するメソッド
    public List<Task> findAll(String username) {
        return taskMapper.findByAll(username);
    }

    // タスクを1件取得するメソッド
    public Task findById(Long id) {
        return taskMapper.findById(id);
    }

    // 入力チェック用の共通メソッド
    private void validate(Task task) {

        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new IllegalArgumentException("タイトルを入力してください");
        }

        if (task.getStartDate() != null && task.getEndDate() != null) {
            if (task.getStartDate().isAfter(task.getEndDate())) {
                throw new IllegalArgumentException("開始日は終了日より前にしてください");
            }
        }
    }

    // タスクを登録するメソッド
    public void save(Task task) {
        validate(task);
        taskMapper.save(task);
    }

    // タスクを更新するメソッド
    public void update(Task task) {
        validate(task);
        taskMapper.update(task);
    }

    // タスクを削除するメソッド
    public void deleteById(Long id) {
        taskMapper.deleteById(id);
    }
    //ページング機能
    @Override
    public List<Task> findByPage(String username, int page, int size) {
        int offset = (page - 1) * size;
        return taskMapper.findByPage(username, size, offset);
    }

    @Override
    public int countByUsername(String username) {
        return taskMapper.countByUsername(username);
    }
}
    	
    	