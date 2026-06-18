package com.example.demo.service;

import com.example.demo.entity.Task;
import java.util.List;


public interface TaskService {
	

    // タスク一覧を取得するメソッド
    public List<Task> findAll(String username);
    // タスクを1件取得するメソッド
    Task findById(Long id);
    // タスクを登録するメソッド
    void save(Task task);
    // タスクを更新するメソッド
    void update(Task task); 
    // タスクを削除するメソッド
    void deleteById(Long id);
    //ページング機能
    List<Task> findByPage(String username, int page, int size);
    int countByUsername(String username);
}
