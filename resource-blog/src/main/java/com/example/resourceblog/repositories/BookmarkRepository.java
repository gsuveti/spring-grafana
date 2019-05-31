package com.example.resourceblog.repositories;

import com.example.resourceblog.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "bookmarks", path = "bookmarks")
public interface BookmarkRepository extends JpaRepository<Bookmark, String> {
}
