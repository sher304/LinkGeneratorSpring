package org.example.authtesterapi.Service;

import org.example.authtesterapi.Model.Link;
import org.example.authtesterapi.Model.LinkRequestDTO;
import org.example.authtesterapi.Model.LinkResponseDTO;
import org.example.authtesterapi.Repository.LinkRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LinkGenerateService {

    private LinkRepository linkRepository;
    private LinkDTOMapper linkDTOMapper;
    private String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private SecureRandom RANDOM = new SecureRandom();

    public LinkGenerateService(LinkRepository linkRepository, LinkDTOMapper linkDTOMapper) {
        this.linkRepository = linkRepository;
        this.linkDTOMapper = linkDTOMapper;
    }

    public LinkResponseDTO saveLink(LinkRequestDTO linkRequestDTO) {
        Link link = linkRepository.save(linkDTOMapper.map(linkRequestDTO, newLinkGenerator()));
        return linkDTOMapper.map(link);
    }

    public Optional<LinkResponseDTO> increaseVisits(String id) {
        Optional<Link> optionalLink = linkRepository.findById(id);
        if (optionalLink.isEmpty()) {
            return Optional.empty();
        }

        Link link = optionalLink.get();
        link.setVisits(link.getVisits() + 1);
        linkRepository.save(link);
        return Optional.of(linkDTOMapper.map(link));
    }

    public Optional<LinkResponseDTO> getLink(String id) {
        return linkRepository.findById(id).map(linkDTOMapper::map);
    }

    private String newLinkGenerator() {
        return RANDOM.ints(10, 0, letters.length())
                .mapToObj(letters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
