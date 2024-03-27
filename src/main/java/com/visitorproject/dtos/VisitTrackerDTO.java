package com.visitorproject.dtos;

import com.visitorproject.entity.VisitTracker;
import com.visitorproject.entity.VisitType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class VisitTrackerDTO {

    private Integer version;

    private String visitorUsername;

    private String fullName;

    private String ownerUsername;

    private Long ownerAddressId;

    private String AddressName;

    private LocalDateTime visitDateTime;

    private LocalDateTime exitDateTime;

    private LocalDateTime dateTimeOfVisitSchedule;

    private VisitType visitType;

    private LocalDateTime actualVisitDateTime;

    private LocalDateTime actualExitDateTime;

    private String AuthCode;

    private Integer NumberOfVisitors;

    private Boolean isVehiclePresent;

    private String visitorVehicleName;

    private Boolean ownerApproval;

    private LocalDateTime approvalOrRejectionTime;

    private String rejectionReason;

    private String extraManualComments;

    public static VisitTrackerDTO fromVisitorTrackerToVisitorTrackerDTO(VisitTracker visitTracker){

        VisitTrackerDTO resultDTO = VisitTrackerDTO.builder()
                .actualExitDateTime(visitTracker.getActualExitDateTime())
                .actualVisitDateTime(visitTracker.getActualVisitDateTime())
                .approvalOrRejectionTime(visitTracker.getApprovalOrRejectionTime())
                .AuthCode(visitTracker.getAuthCode())
                .ownerApproval(visitTracker.getOwnerApproval())
                .visitType(visitTracker.getVisitType())
                .extraManualComments(visitTracker.getExtraManualComments())
                .rejectionReason(visitTracker.getRejectionReason())
                .NumberOfVisitors(visitTracker.getNumberOfVisitors())
                .ownerUsername(visitTracker.getOwnerUsername())
//                .AddressName(addressName)
                .dateTimeOfVisitSchedule(visitTracker.getDateTimeOfVisitSchedule())
                .visitDateTime(visitTracker.getVisitDateTime())
                .exitDateTime(visitTracker.getExitDateTime())
                .version(visitTracker.getVersion())
                .isVehiclePresent(visitTracker.getIsVehiclePresent())
                .visitorVehicleName(visitTracker.getVisitorVehicleName())
                .visitorUsername(visitTracker.getVisitorUsername())
                .ownerAddressId(visitTracker.getOwnerAddressId()).build();
        return resultDTO;
    }

}
