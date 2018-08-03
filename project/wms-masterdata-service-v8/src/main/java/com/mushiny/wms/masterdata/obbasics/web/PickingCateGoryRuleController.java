package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import com.mushiny.wms.masterdata.obbasics.crud.PickingCategoryRuleCRUD;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryRuleDTO;
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
public class PickingCateGoryRuleController {

    private final Logger log = LoggerFactory.getLogger(PickingCateGoryRuleController.class);

    private final PickingCategoryRuleCRUD categoryRuleCRUD;

    public PickingCateGoryRuleController(PickingCategoryRuleCRUD categoryRuleCRUD) {
        this.categoryRuleCRUD = categoryRuleCRUD;
    }

    @PostMapping("/masterdata/outbound/picking-category-rules")
    public ResponseEntity<PickingCateGoryRuleDTO> createPickingCategoryRule(@RequestBody @Valid PickingCateGoryRuleDTO pickingCategoryDTO) throws URISyntaxException, FacadeException {
        PickingCateGoryRuleDTO created = categoryRuleCRUD.create(pickingCategoryDTO);
        return ResponseEntity.created(new URI("/picking-category-rules/" + created.getId()))
                .body(created);
    }

    @PutMapping("/masterdata/outbound/picking-category-rules")
    public ResponseEntity<PickingCateGoryRuleDTO> updatePickingCategoryRule(@RequestBody @Valid PickingCateGoryRuleDTO pickingCategoryDTO) throws FacadeException {
        PickingCateGoryRuleDTO updated = categoryRuleCRUD.update(pickingCategoryDTO);
        return ResponseEntity.ok()
                .body(updated);
    }

    @DeleteMapping("/masterdata/outbound/picking-category-rules/{id}")
    public ResponseEntity<Void> deletePickingCategoryRule(@PathVariable String id) throws FacadeException {
        categoryRuleCRUD.delete(id);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/masterdata/outbound/picking-category-rules/{id}")
    public ResponseEntity<PickingCateGoryRuleDTO> getPickingCategoryRule(@PathVariable String id) throws FacadeException {
        PickingCateGoryRuleDTO pickingCategoryDTO = categoryRuleCRUD.get(id);
        return ResponseEntity.ok()
                .body(pickingCategoryDTO);
    }

    @GetMapping("/masterdata/outbound/picking-category-rules")
    public ResponseEntity<List<PickingCateGoryRuleDTO>> getPickingCategories(Sort sort) {
        List<PickingCateGoryRuleDTO> pickingCategoryDTOs = categoryRuleCRUD.getList(sort);
        return ResponseEntity.ok()
                .body(pickingCategoryDTOs);
    }

    @GetMapping(path = "/masterdata/outbound/picking-category-rules", params = {"page", "size"})
    public ResponseEntity<Page<PickingCateGoryRuleDTO>> getPickingCategories(Pageable pageable) {
        Page<PickingCateGoryRuleDTO> pickingCategoryDTOs = categoryRuleCRUD.getList(pageable);
        return ResponseEntity.ok()
                .body(pickingCategoryDTOs);
    }

    @GetMapping(path = "/masterdata/outbound/picking-category-rules", params = {"search"})
    public ResponseEntity<List<PickingCateGoryRuleDTO>> getPickingCategoriesBySearchTerm(@RequestParam(value = "search") String search, Sort sort) throws FacadeException {
        List<PickingCateGoryRuleDTO> pickingCategoryDTOs = categoryRuleCRUD.getListBySearchTerm(search, sort);
        return ResponseEntity.ok()
                .body(pickingCategoryDTOs);
    }

    @GetMapping(path = "/masterdata/outbound/picking-category-rules", params = {"search", "page", "size"})
    public ResponseEntity<Page<PickingCateGoryRuleDTO>> getPickingCategoriesBySearchTerm(@RequestParam(value = "search") String search, Pageable pageable) throws FacadeException {
        Page<PickingCateGoryRuleDTO> pickingCategoryDTOs = categoryRuleCRUD.getListBySearchTerm(search, pageable);
        return ResponseEntity.ok()
                .body(pickingCategoryDTOs);
    }
}
