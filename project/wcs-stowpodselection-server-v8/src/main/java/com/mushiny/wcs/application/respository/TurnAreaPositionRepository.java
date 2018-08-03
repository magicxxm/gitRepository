package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.TurnAreaPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TurnAreaPositionRepository extends JpaRepository<TurnAreaPosition,String> {
}
