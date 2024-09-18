package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public List<PostDTO> getPosts() {
        var posts = postRepository.findAll();
        return posts.stream().map(this::toPostDto).toList();
    }

    @GetMapping(path = "/{id}")
    public PostDTO getPosts(@PathVariable long id) {
        var post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post with id " + id + " not found")
        );
        return toPostDto(post);
    }

    private PostDTO toPostDto(Post post) {
        var comments = commentRepository.findByPostId(post.getId());
        var commentsList = comments.stream().map(this::toCommentDto).toList();
        var dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setComments(commentsList);
        return dto;
    }

    private CommentDTO toCommentDto(Comment comment) {
        var dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());
        return dto;
    }
}
// END
