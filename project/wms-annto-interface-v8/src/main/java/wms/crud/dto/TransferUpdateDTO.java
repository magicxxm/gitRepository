package wms.crud.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 移库单同步
 *
 */
public class TransferUpdateDTO {

    //  "Code": "移库单号，string (50)，必填",
    @NotNull
    private String Code;

    //"status": "状态(新建 new , 关闭 closed)，string (50)，必填",
    @NotNull
    private String status;

    //"remark": "备注，string (500)",
    private String remark;

    //"orderDate": "创建时间, yyyy-MM-dd HH:mm:ss，string (20)，必填",
    private String orderDate;

    private List<OrderItemsDTO> orderItems = new ArrayList<>();
}
