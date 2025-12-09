package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.owner.id = :userId")
    List<Item> findByUserId(@Param("userId") long userId);

    @Modifying
    @Query("DELETE FROM Item i WHERE i.owner.id = :ownerId")
    void deleteAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.available = true " +
            "AND (LOWER(i.name) LIKE %:text% " +
            "OR LOWER(i.description) LIKE %:text%)")
    List<Item> findItemByParametrs(@Param("text") String text);

    @Query("SELECT i.id FROM Item i WHERE i.owner.id = :userId")
    List<Long> findItemIdsByUserId(@Param("userId") long userId);

    @Modifying
    @Query("DELETE FROM Item i WHERE i.owner.id = :userId")
    void deleteByOwnerId(@Param("userId") long userId);
}