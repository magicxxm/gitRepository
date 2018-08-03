package com.mushiny.wms.system.web;

import com.mushiny.wms.system.crud.dto.UserGroupDTO;
import com.mushiny.wms.system.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/usergroups")
public class UserGroupController {

    private final UserGroupService userGroupService;

    @Autowired
    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserGroupDTO> create(@RequestBody UserGroupDTO dto) {
        UserGroupDTO userGroupDTO = userGroupService.create(dto);
        return ResponseEntity.ok(userGroupDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userGroupService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserGroupDTO> update(@RequestBody UserGroupDTO dto) {
        UserGroupDTO userGroupDTO = userGroupService.update(dto);
        return ResponseEntity.ok(userGroupDTO);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserGroupDTO> get(@PathVariable String id) {
        UserGroupDTO userGroupDTO = userGroupService.retrieve(id);
        return ResponseEntity.ok(userGroupDTO);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserGroupDTO>> getAll() {
        List<UserGroupDTO> userGroupDTOs = userGroupService.getAll();
        return ResponseEntity.ok(userGroupDTOs);
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserGroupDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(userGroupService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserGroupDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(userGroupService.getBySearchTerm(search, pageable));
    }
}
