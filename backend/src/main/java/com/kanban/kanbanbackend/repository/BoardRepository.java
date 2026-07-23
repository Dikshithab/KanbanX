package com.kanban.kanbanbackend.repository;

import com.kanban.kanbanbackend.entity.Board;
import com.kanban.kanbanbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByUser(User user);

}