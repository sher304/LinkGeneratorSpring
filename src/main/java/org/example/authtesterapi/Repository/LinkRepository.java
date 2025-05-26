package org.example.authtesterapi.Repository;

import org.example.authtesterapi.Model.Link;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends CrudRepository<Link, String> {
    boolean existsByTargetURL(String targetURL);
}
