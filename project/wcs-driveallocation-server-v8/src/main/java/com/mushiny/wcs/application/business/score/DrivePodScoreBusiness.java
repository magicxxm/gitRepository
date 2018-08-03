package com.mushiny.wcs.application.business.score;

import com.mushiny.wcs.application.business.dto.DrivePodScore;
import com.mushiny.wcs.application.business.dto.MixTrip;
import com.mushiny.wcs.application.domain.Pod;
import com.mushiny.wcs.application.domain.Trip;
import com.mushiny.wcs.application.domain.WCSRobot;
import com.mushiny.wcs.application.domain.enums.TripType;
import com.mushiny.wcs.application.repository.PodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DrivePodScoreBusiness {

    private final PodRepository podRepository;
    private final EnRouteWorkBusiness enRouteWorkBusiness;

    @Autowired
    public DrivePodScoreBusiness(PodRepository podRepository,
                                 EnRouteWorkBusiness enRouteWorkBusiness) {
        this.podRepository = podRepository;
        this.enRouteWorkBusiness = enRouteWorkBusiness;
    }

    public List<DrivePodScore> getDrivePodScores(List<Trip> trips,
                                                 List<WCSRobot> robots) {
        List<DrivePodScore> drivePodScores = new ArrayList<>();
        for (Trip trip : trips) {
            Pod pod = podRepository.findOne(trip.getPodId());
            Duration duration = Duration.between(LocalDateTime.now(), trip.getCreatedDate());
            BigDecimal podWaitTime = BigDecimal.valueOf(Math.abs(duration.getSeconds()));
            BigDecimal enRouteWork;
            if (trip.getTripType().equalsIgnoreCase(TripType.PICK_POD.getName())) {
                enRouteWork = enRouteWorkBusiness.getPickTripEnRouteWork(trip.getWorkStationId());
            } else if (trip.getTripType().equalsIgnoreCase(TripType.STOW_POD.getName())) {
                enRouteWork = enRouteWorkBusiness.getStowTripEnRouteWork(trip.getWarehouseId());
            } else if (trip.getTripType().equalsIgnoreCase(TripType.IBP_POD.getName())
                    || trip.getTripType().equalsIgnoreCase(TripType.OBP_POD.getName())
                    || trip.getTripType().equalsIgnoreCase(TripType.ICQA_POD.getName())) {
                enRouteWork = enRouteWorkBusiness.getPQATripEnRouteWork(trip.getPodId());
            } else {
                continue;
            }
            for (WCSRobot robot : robots) {
                BigDecimal fetch = BigDecimal.valueOf(
                        Math.abs(robot.getxPosition() - pod.getxPos())
                                + Math.abs(robot.getyPosition() - pod.getyPos()));
                BigDecimal score = fetch.add(enRouteWork).subtract(podWaitTime);
                DrivePodScore drivePodScore = new DrivePodScore();
                drivePodScore.setDriveId(robot.getRobotId());
                drivePodScore.setPodId(trip.getPodId());
                drivePodScore.setScore(score);
                drivePodScore.setTripId(trip.getId());
                drivePodScores.add(drivePodScore);
            }
            drivePodScores.sort(Comparator.comparing(DrivePodScore::getScore));
        }
        return drivePodScores;
    }

    public List<DrivePodScore> getDrivePodScores(Trip trip,
                                                 List<WCSRobot> robots) {
        List<DrivePodScore> drivePodScores = new ArrayList<>();

        Pod pod = podRepository.findOne(trip.getPodId());
        Duration duration = Duration.between(LocalDateTime.now(), trip.getCreatedDate());
        BigDecimal podWaitTime = BigDecimal.valueOf(Math.abs(duration.getSeconds()));
        BigDecimal enRouteWork = new BigDecimal(0);
        if (trip.getTripType().equalsIgnoreCase(TripType.PICK_POD.getName())) {
            enRouteWork = enRouteWorkBusiness.getPickTripEnRouteWork(trip.getWorkStationId());
        } else if (trip.getTripType().equalsIgnoreCase(TripType.STOW_POD.getName())) {
            enRouteWork = enRouteWorkBusiness.getStowTripEnRouteWork(trip.getWarehouseId());
        } else if (trip.getTripType().equalsIgnoreCase(TripType.IBP_POD.getName())
                || trip.getTripType().equalsIgnoreCase(TripType.OBP_POD.getName())
                || trip.getTripType().equalsIgnoreCase(TripType.ICQA_POD.getName())) {
            enRouteWork = enRouteWorkBusiness.getPQATripEnRouteWork(trip.getPodId());
        }
        for (WCSRobot robot : robots) {
            BigDecimal fetch = BigDecimal.valueOf(
                    Math.abs(robot.getxPosition() - pod.getxPos())
                            + Math.abs(robot.getyPosition() - pod.getyPos()));
            BigDecimal score = fetch.add(enRouteWork).subtract(podWaitTime);
            DrivePodScore drivePodScore = new DrivePodScore();
            drivePodScore.setDriveId(robot.getRobotId());
            drivePodScore.setPodId(trip.getPodId());
            drivePodScore.setScore(score);
            drivePodScore.setTripId(trip.getId());
            drivePodScores.add(drivePodScore);
        }
        drivePodScores.sort(Comparator.comparing(DrivePodScore::getScore));

        return drivePodScores;
    }

    public Map<MixTrip, List<DrivePodScore>> getDrivePodScores2(List<MixTrip> mixTrips,
                                                                List<WCSRobot> robots) {


        Map<MixTrip, List<DrivePodScore>> result = new HashMap<>();

        for (MixTrip mixTrip : mixTrips) {
            List<DrivePodScore> drivePodScores = new ArrayList<>();
            Trip trip = mixTrip.getCurrentTrip();
            Pod pod = podRepository.findOne(trip.getPodId());
            Duration duration = Duration.between(LocalDateTime.now(), trip.getCreatedDate());
            BigDecimal podWaitTime = BigDecimal.valueOf(Math.abs(duration.getSeconds()));
            BigDecimal enRouteWork;
            if (trip.getTripType().equalsIgnoreCase(TripType.PICK_POD.getName())) {
                enRouteWork = enRouteWorkBusiness.getPickTripEnRouteWork(trip.getWorkStationId());
            } else if (trip.getTripType().equalsIgnoreCase(TripType.STOW_POD.getName())) {
                enRouteWork = enRouteWorkBusiness.getStowTripEnRouteWork(trip.getWarehouseId());
            } else if (trip.getTripType().equalsIgnoreCase(TripType.IBP_POD.getName())
                    || trip.getTripType().equalsIgnoreCase(TripType.OBP_POD.getName())
                    || trip.getTripType().equalsIgnoreCase(TripType.ICQA_POD.getName())) {
                enRouteWork = enRouteWorkBusiness.getPQATripEnRouteWork(trip.getPodId());
            } else {
                continue;
            }
            for (WCSRobot robot : robots) {
                BigDecimal fetch = BigDecimal.valueOf(
                        Math.abs(robot.getxPosition() - pod.getxPos())
                                + Math.abs(robot.getyPosition() - pod.getyPos()));
                BigDecimal score = fetch.add(enRouteWork).subtract(podWaitTime);
                DrivePodScore drivePodScore = new DrivePodScore();
                drivePodScore.setDriveId(robot.getRobotId());
                drivePodScore.setPodId(trip.getPodId());
                drivePodScore.setScore(score);
                drivePodScore.setTripId(trip.getId());
                drivePodScores.add(drivePodScore);
            }

            drivePodScores.sort(Comparator.comparing(DrivePodScore::getScore));
            result.put(mixTrip, drivePodScores);

        }
        return result;
    }
}
