package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItemId(Long itemId);

    List<Comment> findByItemIdIn(List<Long> itemIds);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.item.id IN :itemIds")
    void deleteByItemIdIn(@Param("itemIds") List<Long> itemIds);
}