package org.example.authtesterapi.Service;

import org.example.authtesterapi.Model.Link;
import org.example.authtesterapi.Model.LinkRequestDTO;
import org.example.authtesterapi.Model.LinkResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class LinkDTOMapper {

    public Link map(LinkRequestDTO linkRequestDTO, String generatedID) {
        Link link = new Link();
        link.setId(generatedID);
        link.setName(linkRequestDTO.getName());
        link.setTargetURL(linkRequestDTO.getTargetURL());
        link.setPassword(linkRequestDTO.getPassword());
        return link;
    }

    public Link map(LinkRequestDTO linkRequestDTO) {
        Link link = new Link();
        link.setName(linkRequestDTO.getName());
        link.setTargetURL(linkRequestDTO.getTargetURL());
        link.setPassword(linkRequestDTO.getPassword());
        return link;
    }

    public LinkResponseDTO map(Link link) {
        LinkResponseDTO linkResponseDTO = new LinkResponseDTO();
        linkResponseDTO.setId(link.getId());
        linkResponseDTO.setName(link.getName());
        linkResponseDTO.setTargetURL(link.getTargetURL());
        linkResponseDTO.setPassword(link.getPassword());
        linkResponseDTO.setRedirectURL("http://localhost:8080/red/" + link.getId());
        linkResponseDTO.setVisits(link.getVisits());
        return linkResponseDTO;
    }
}
