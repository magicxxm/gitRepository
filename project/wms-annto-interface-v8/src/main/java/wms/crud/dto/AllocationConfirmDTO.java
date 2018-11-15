package wms.crud.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 补货单确认
 *
 */
public class AllocationConfirmDTO {

    //"orderCode": "补货单号，string (50)，必填",
    @NotNull
    private String orderCode;

    //"replenishmentMode":"补货方式（闲时补货 free,紧急补货 emergency）,string(20),必填",
    @NotNull
    private String replenishmentMode;

    //"status": "状态(新建 new , 关闭 closed)，string (50)，必填",
    @NotNull
    private String status;

    //"remark": "备注，string (500)",
    private String remark;

    //"finishDate": "wanc时间, yyyy-MM-dd HH:mm:ss，string (20)，必填",
    private String finishDate;

    private List<OrderItemsDTO> orderItems = new ArrayList<>();
    
}
