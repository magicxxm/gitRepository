package wms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.business.dto.ProblemDTO;
import wms.common.crud.AccessDTO;
import wms.service.Problem;

@RestController
public class ProblemController {

    @Autowired
    private Problem obProblem;

    @RequestMapping(value="/wms/robot/problem/handle",
            method= RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> createProblem(@RequestBody ProblemDTO problemDTO){

        return ResponseEntity.ok(obProblem.createProblem(problemDTO));
    }
}
