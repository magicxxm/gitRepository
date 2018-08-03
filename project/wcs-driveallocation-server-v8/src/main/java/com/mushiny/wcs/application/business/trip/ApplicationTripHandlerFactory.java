package com.mushiny.wcs.application.business.trip;

import com.mushiny.wcs.common.context.ApplicationBeanContextAware;
import org.springframework.stereotype.Component;


/**
 * @author:
 * @Description: Created by wangjianwei on 2017/10/23.
 */
@Component
public class ApplicationTripHandlerFactory {
    private ApplicationTripHandlerFactory() {
    }

    private static class TripHandlerHolder {
        private static TripHandler tripHandler;

        static {
            PodRunTripHandler podRunTripHandler = ApplicationBeanContextAware.getBean(PodRunTripHandler.class);

            CommonTripHandler commonTripHandler = ApplicationBeanContextAware.getBean(CommonTripHandler.class);
            PodSanTripHandler podSanTripHandler = ApplicationBeanContextAware.getBean(PodSanTripHandler.class);
            ChargerDriveTripHandler chargerDriveTripHandler = ApplicationBeanContextAware.getBean(ChargerDriveTripHandler.class);
            chargerDriveTripHandler.setHandler(podSanTripHandler);
            commonTripHandler.setHandler(chargerDriveTripHandler);
            podRunTripHandler.setHandler(commonTripHandler);
            tripHandler = podRunTripHandler;


        }
    }

    public static TripHandler getInstance() {
        return TripHandlerHolder.tripHandler;
    }

}
