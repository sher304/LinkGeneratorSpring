package org.example.authtesterapi.Controller;


import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.example.authtesterapi.Model.Link;
import org.example.authtesterapi.Model.LinkRequestDTO;
import org.example.authtesterapi.Model.LinkResponseDTO;
import org.example.authtesterapi.Service.LinkGenerateService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@RestController
public class LinkController {

    private final LinkGenerateService linkGenerateService;
    private final ObjectMapper objectMapper;

    public LinkController(LinkGenerateService linkGenerateService,  ObjectMapper objectMapper) {
        this.linkGenerateService = linkGenerateService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/api/v1/links")
    public ResponseEntity<LinkResponseDTO> generateLink(@RequestBody LinkRequestDTO linkRequestDTO) {
        LinkResponseDTO link = null;
        try {
            link = linkGenerateService.saveLink(linkRequestDTO);
            URI generatedNewURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(link.getName() + link.getTargetURL()).toUri();
            return ResponseEntity.created(generatedNewURI).body(link);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("error", e.getMessage()).body(link);
        }
    }

    @GetMapping("/api/v1/links/{id}")
    public ResponseEntity<LinkResponseDTO> getLink(@PathVariable String id) {
        return linkGenerateService.getLink(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/red/{id}")
    public ResponseEntity<Object>  redirect(@PathVariable String id) {
        Optional<LinkResponseDTO> linkResponseDTO = linkGenerateService.increaseVisits(id);
        if (linkResponseDTO.isPresent()) {
            return ResponseEntity
                    .status(302)
                    .header("Found", linkResponseDTO.get().getTargetURL())
                    .build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/api/v1/links/{id}")
    public ResponseEntity<?> updateLink(@PathVariable String id, @RequestBody JsonMergePatch patch) {
        try {
            LinkResponseDTO linkResponseDTO = linkGenerateService.getLink(id).orElseThrow();
            LinkRequestDTO linkRequestDTO = applyPatch(linkResponseDTO, patch);
            if (linkResponseDTO.getPassword().isEmpty() || !linkRequestDTO.getPassword().equals(linkResponseDTO.getPassword())) return ResponseEntity.status(403).header("reason", "wrong password").build();
            linkGenerateService.updateLink(id, linkRequestDTO).orElseThrow();
        } catch (JsonPatchException | JsonProcessingException | EmptyResultDataAccessException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("api/v1/links/{id}")
    public ResponseEntity<?> deleteLink(@PathVariable String id, @RequestBody LinkRequestDTO linkRequestDTO) {
        try {
            LinkResponseDTO linkResponseDTO = linkGenerateService.getLink(id).orElseThrow();
            if (linkRequestDTO.getPassword().isEmpty() || !linkResponseDTO.getPassword().equals(linkRequestDTO.getPassword())) {
                return ResponseEntity.status(403).header("reason", "wrong password").build();
            }
            linkGenerateService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public LinkRequestDTO applyPatch(LinkResponseDTO responseDTO, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode linkNode = objectMapper.valueToTree(responseDTO);
        JsonNode patchNode = patch.apply(linkNode);
        return  objectMapper.convertValue(patchNode, LinkRequestDTO.class);
    }
}
