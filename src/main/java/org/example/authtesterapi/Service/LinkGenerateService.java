package org.example.authtesterapi.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.authtesterapi.Model.Link;
import org.example.authtesterapi.Model.LinkRequestDTO;
import org.example.authtesterapi.Model.LinkResponseDTO;
import org.example.authtesterapi.Repository.LinkRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class LinkGenerateService {

    private LinkRepository linkRepository;
    private LinkDTOMapper linkDTOMapper;
    private final Validator validator;

    private String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private SecureRandom RANDOM = new SecureRandom();


    public LinkGenerateService(LinkRepository linkRepository,
                               LinkDTOMapper linkDTOMapper,
                               Validator validator) {
        this.linkRepository = linkRepository;
        this.linkDTOMapper = linkDTOMapper;
        this.validator = validator;
    }

    public LinkResponseDTO saveLink(LinkRequestDTO linkRequestDTO) throws Exception {
        Link link = linkDTOMapper.map(linkRequestDTO);
        Set<ConstraintViolation<Link>> errors = validator.validate(link);
        if (linkRepository.existsByTargetURL(linkRequestDTO.getTargetURL())) {
            throw new Exception("Link already exists");
        }
        if (errors.isEmpty()) {
            link = linkRepository.save(linkDTOMapper.map(linkRequestDTO, newLinkGenerator()));
            return linkDTOMapper.map(link);
        } else {
            System.out.println("Cannot add Link object! Errors:");
            String message = errors.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
            throw new Exception("Validation failed: " + message);
        }
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

    public Optional<LinkResponseDTO> updateLink(String id, LinkRequestDTO linkRequestDTO) {
        Optional<Link> link = linkRepository.findById(id);
        if (!link.isPresent()) return Optional.empty();

        if (linkRequestDTO.getName() != null) link.get().setName(linkRequestDTO.getName());
        if (linkRequestDTO.getPassword() != null) link.get().setPassword(linkRequestDTO.getPassword());
        if (linkRequestDTO.getTargetURL() != null) link.get().setTargetURL(linkRequestDTO.getTargetURL());

        linkRepository.save(link.get());
        return Optional.of(linkDTOMapper.map(link.get()));
    }

    public void delete(String id) {
        linkRepository.deleteById(id);
    }

    private String newLinkGenerator() {
        return RANDOM.ints(10, 0, letters.length())
                .mapToObj(letters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
