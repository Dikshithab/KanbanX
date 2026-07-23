package com.kanban.kanbanbackend.controller;

import com.kanban.kanbanbackend.entity.Board;
import com.kanban.kanbanbackend.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kanban.kanbanbackend.dto.BoardRequest;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping
    public Board createBoard(@RequestBody BoardRequest request) {

        System.out.println(request);

        return boardService.createBoard(request);
    }

    @GetMapping
    public List<Board> getMyBoards() {
        return boardService.getMyBoards();
    }

    @GetMapping("/{id}")
    public Board getBoardById(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    @PutMapping("/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board board) {
        return boardService.updateBoard(id, board);
    }

    /*@DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return "Board deleted successfully";
    }
}
*/
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable Long id) {

        System.out.println("DELETE BOARD ID = " + id);

        boardService.deleteBoard(id);

        return "Board deleted successfully";
    }
}
