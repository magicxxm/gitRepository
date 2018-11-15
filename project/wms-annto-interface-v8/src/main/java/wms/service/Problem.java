package wms.service;

import wms.business.dto.ProblemDTO;
import wms.common.crud.AccessDTO;

/**
 * Created by Laptop-9 on 2018/3/15.
 */
public interface Problem {

    AccessDTO createProblem(ProblemDTO problemDTO);
}
