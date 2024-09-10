package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @PostMapping("/posts")
    public Post create(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @GetMapping("/posts")
    public List<Post> showPosts(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        int fromIndex = page * limit;
        int toIndex = Math.min(posts.size(), fromIndex + limit);

        return posts.subList(fromIndex, toIndex);
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> showPost(@PathVariable String id) {
        return posts.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable String id, @RequestBody Post data) {
        var maybePost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (maybePost.isPresent()) {
            var post = maybePost.get();
            post.setId(data.getId());
            post.setBody(data.getBody());
            post.setTitle(data.getTitle());
        }
        return data;
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
    // END
}
