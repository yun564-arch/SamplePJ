package com.example.demo.service;

import com.example.demo.entity.Task;
import java.util.List;

public interface TaskService {

   

    // タスクを1件取得するメソッド(所有者チェック付き)
    Task findById(Long id, String username);

    // タスクを登録するメソッド
    void save(Task task);

    // タスクを更新するメソッド
    void update(Task task);

    // タスクを削除するメソッド(所有者チェック付き)
    void deleteById(Long id, String username);

    //ページング機能
    List<Task> findByPage(String username, int page, int size);
    int countByUsername(String username);
}