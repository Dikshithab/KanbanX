package com.kanban.kanbanbackend.repository;

import com.kanban.kanbanbackend.entity.Board;
import com.kanban.kanbanbackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByBoard(Board board);

}