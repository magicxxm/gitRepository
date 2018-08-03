package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.ItemData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDataRepository extends JpaRepository<ItemData, String> {

}
