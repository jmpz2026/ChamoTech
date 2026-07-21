package com.chamo.chamotech.repository;

import com.chamo.chamotech.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    boolean existsByName(String name);
}
