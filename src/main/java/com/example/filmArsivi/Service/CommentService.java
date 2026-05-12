package com.example.filmArsivi.Service;

import com.example.filmArsivi.Entity.Comment;
import com.example.filmArsivi.Repository.CommentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    // CONTROLLER'IN BULAMADIĞI METOT 1:
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    // CONTROLLER'IN BULAMADIĞI METOT 2:
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}