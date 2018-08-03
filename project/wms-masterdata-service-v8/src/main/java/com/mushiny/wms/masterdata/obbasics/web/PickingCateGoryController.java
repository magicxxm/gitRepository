package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import com.mushiny.wms.masterdata.obbasics.crud.PickingCategoryCRUD;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PickingCateGoryController {

    private final Logger log = LoggerFactory.getLogger(PickingCateGoryController.class);

    private final PickingCategoryCRUD pickingCategoryCRUD;

    public PickingCateGoryController(PickingCategoryCRUD pickingCategoryCRUD) {
        this.pickingCategoryCRUD = pickingCategoryCRUD;
    }

    @PostMapping("/masterdata/outbound/picking-categories")
    public ResponseEntity<PickingCateGoryDTO> createPickingCategory(@RequestBody @Valid PickingCateGoryDTO pickingCateGoryDTO) throws URISyntaxException, FacadeException {
        PickingCateGoryDTO created = pickingCategoryCRUD.create(pickingCateGoryDTO);
        return ResponseEntity.created(new URI("/picking-categories/" + created.getId()))
                .body(created);
    }

    @PutMapping("/masterdata/outbound/picking-categories")
    public ResponseEntity<PickingCateGoryDTO> updatePickingCategory(@RequestBody @Valid PickingCateGoryDTO pickingCateGoryDTO) throws FacadeException {
        PickingCateGoryDTO updated = pickingCategoryCRUD.update(pickingCateGoryDTO);
        return ResponseEntity.ok()
                .body(updated);
    }

    @DeleteMapping("/masterdata/outbound/picking-categories/{id}")
    public ResponseEntity<Void> deletePickingCategory(@PathVariable String id) throws FacadeException {
        pickingCategoryCRUD.delete(id);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/masterdata/outbound/picking-categories/{id}")
    public ResponseEntity<PickingCateGoryDTO> getPickingCategory(@PathVariable String id) throws FacadeException {
        PickingCateGoryDTO pickingCateGoryDTO = pickingCategoryCRUD.get(id);
        return ResponseEntity.ok()
                .body(pickingCateGoryDTO);
    }

    @GetMapping("/masterdata/outbound/picking-categories")
    public ResponseEntity<List<PickingCateGoryDTO>> getPickingCategories(Sort sort) {
        List<PickingCateGoryDTO> pickingCateGoryDTOS = pickingCategoryCRUD.getList(sort);
        return ResponseEntity.ok()
                .body(pickingCateGoryDTOS);
    }

    @GetMapping(path = "/masterdata/outbound/picking-categories", params = {"page", "size"})
    public ResponseEntity<Page<PickingCateGoryDTO>> getPickingCategories(Pageable pageable) {
        Page<PickingCateGoryDTO> pickingCategoryDTOs = pickingCategoryCRUD.getList(pageable);
        return ResponseEntity.ok()
                .body(pickingCategoryDTOs);
    }

    @GetMapping(path = "/masterdata/outbound/picking-categories", params = {"search"})
    public ResponseEntity<List<PickingCateGoryDTO>> getPickingCategoriesBySearchTerm(@RequestParam(value = "search") String search, Sort sort) throws FacadeException {
        List<PickingCateGoryDTO> pickingCateGoryDTOS = pickingCategoryCRUD.getListBySearchTerm(search, sort);
        return ResponseEntity.ok()
                .body(pickingCateGoryDTOS);
    }

    @GetMapping(path = "/masterdata/outbound/picking-categories", params = {"search", "page", "size"})
    public ResponseEntity<Page<PickingCateGoryDTO>> getPickingCategoriesBySearchTerm(@RequestParam(value = "search") String search, Pageable pageable) throws FacadeException {
        Page<PickingCateGoryDTO> pickingCategoryDTOs = pickingCategoryCRUD.getListBySearchTerm(search, pageable);
        return ResponseEntity.ok()
                .body(pickingCategoryDTOs);
    }
}
