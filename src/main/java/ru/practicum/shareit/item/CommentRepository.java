package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItemId(Long itemId);

    List<Comment> findByItemIdIn(List<Long> itemIds);

    @Query("DELETE FROM Comment c WHERE c.item.id IN :itemIds")
    void deleteAllByItemId(@Param("itemIds") List<Long> itemIds);

    void deleteAllByAuthorId(Long authorId);
}