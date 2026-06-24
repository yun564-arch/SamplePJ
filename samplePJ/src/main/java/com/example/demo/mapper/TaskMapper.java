package com.example.demo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.demo.entity.Task;

@Mapper
public interface TaskMapper {

    // タスク一覧を取得するメソッド
    List<Task> findByAll(String username);

    // タスクを1件取得するメソッド(所有者チェック付き)
    Task findById(@Param("id") Long id, @Param("username") String username);

    // タスクを登録するメソッド
    void save(Task task);

    // タスクを更新するメソッド(所有者チェック付き)
    int update(Task task);

    // タスクを削除するメソッド(所有者チェック付き)
    int deleteById(@Param("id") Long id, @Param("username") String username);

    //ページング機能
    List<Task> findByPage(@Param("username") String username,
            @Param("limit") int limit,
            @Param("offset") int offset);

    int countByUsername(@Param("username") String username);
}