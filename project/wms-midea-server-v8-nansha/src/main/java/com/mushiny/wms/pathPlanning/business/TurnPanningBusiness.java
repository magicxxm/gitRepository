package com.mushiny.wms.pathPlanning.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import org.springframework.stereotype.Component;

@Component
public class TurnPanningBusiness {

    /**
     * 计算面到目的地需要旋转多少度
     *
     * @param face         需要POD的面
     * @param sourceToward POD的零面
     * @param targetToward 目的地的朝向
     * @return POD零面转向度数
     */
    public int getPodTurning(String face, int sourceToward, int targetToward) {
        // 获取目的地需要朝向是多少度
        switch (targetToward) {
            case 0:
                targetToward = 180;
                break;
            case 90:
                targetToward = 270;
                break;
            case 180:
                targetToward = 0;
                break;
            case 270:
                targetToward = 90;
                break;
            default:
                throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        // 基于零面是A面计算,最大体积面当前是多少度（POD中的当前朝向是指零面的当前朝向）
        int faceToward;
        switch (face) {
            case "A":
                faceToward = sourceToward;
                break;
            case "B":
                faceToward = sourceToward + 90;
                if (faceToward >= 360) {
                    faceToward = 0;
                }
                break;
            case "C":
                faceToward = sourceToward + 180;
                if (faceToward >= 360) {
                    faceToward = faceToward - 360;
                }
                break;
            case "D":
                faceToward = sourceToward + 270;
                if (faceToward >= 360) {
                    faceToward = faceToward - 360;
                }
                break;
            default:
                throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        // 计算需要旋转多少度
        int rotating = 0;
        boolean rotatingFlag = true;
        for (int i = 0; i < 4; i++) {
            if (faceToward == targetToward) {
                rotatingFlag = false;
                break;
            } else {
                faceToward = faceToward + 90;
                rotating = rotating + 90;
                if (faceToward >= 360) {
                    faceToward = 0;
                }
            }
        }
        if (rotatingFlag) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        // 计算零面转向多少度
        sourceToward = sourceToward + rotating;
        if (sourceToward >= 360) {
            sourceToward = sourceToward - 360;
        }
        return sourceToward;
    }
}
