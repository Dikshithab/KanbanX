package com.kanban.kanbanbackend.service;

import com.kanban.kanbanbackend.dto.BoardRequest;
import com.kanban.kanbanbackend.entity.Board;
import com.kanban.kanbanbackend.entity.User;
import com.kanban.kanbanbackend.repository.BoardRepository;
import com.kanban.kanbanbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Board
    public Board createBoard(BoardRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return null;
        }

        Board board = new Board();
        board.setName(request.getName());
        board.setUser(user);

        return boardRepository.save(board);
    }
    // Get All Boards
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // Get Board By Id
    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    // Update Board
    public Board updateBoard(Long id, Board updatedBoard) {

        Board existingBoard = boardRepository.findById(id).orElse(null);

        if (existingBoard != null) {
            existingBoard.setName(updatedBoard.getName());
            return boardRepository.save(existingBoard);
        }

        return null;
    }
    public List<Board> getMyBoards() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return List.of();
        }

        return boardRepository.findByUser(user);
    }

    // Delete Board
    public void deleteBoard(Long id) {
        System.out.println("Deleting board from service: " + id);


        boardRepository.deleteById(id);
    }
}