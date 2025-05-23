package org.example.authtesterapi.Controller;


import org.example.authtesterapi.Model.Link;
import org.example.authtesterapi.Model.LinkRequestDTO;
import org.example.authtesterapi.Model.LinkResponseDTO;
import org.example.authtesterapi.Service.LinkGenerateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class LinkController {

    private final LinkGenerateService linkGenerateService;

    public LinkController(LinkGenerateService linkGenerateService) {
        this.linkGenerateService = linkGenerateService;
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponseDTO> generateLink(@RequestBody LinkRequestDTO linkRequestDTO) {
        LinkResponseDTO link = linkGenerateService.saveLink(linkRequestDTO);
        URI generatedNewURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(link.getName() + link.getTargetURL()).toUri();
        return ResponseEntity.created(generatedNewURI).body(link);
    }

    @GetMapping("/red/{id}")
    public ResponseEntity<LinkResponseDTO> getLink(@PathVariable String id) {
        return linkGenerateService.increaseVisits(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
