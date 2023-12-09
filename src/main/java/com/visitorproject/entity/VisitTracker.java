package com.visitorproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
public class VisitTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String visitorUsername;

    @Column(nullable = false)
    private String ownerUsername;

    @Column(nullable = false)
    private Long ownerAddressId;

    @Column
    private LocalDateTime visitDateTime;

    private LocalDateTime exitDateTime;

    private Boolean isShortVisit;

    private LocalDateTime actualVisitDateTime;

    private LocalDateTime actualExitDateTime;

    @Column(nullable = false)
    private Integer NumberOfVisitors;

    private Boolean isVehiclePresent;

    private Long visitorVehicleId;

    private Boolean ownerApproval;

    private String rejectionReason;

    private String extraManualComments;

}
