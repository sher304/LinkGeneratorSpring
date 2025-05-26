package org.example.authtesterapi.Repository;

import org.example.authtesterapi.Model.Link;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LinkRepositoryDelegate {
    private Set<Link> links;

    public LinkRepositoryDelegate() {
        links = new HashSet<>();
    }


    public void addLink(Link link) {
        links.add(link);
    }

    public Set<Link> getLinks() {
        return links;
    }
}
