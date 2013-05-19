/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_parking")
@NamedQueries({

    @NamedQuery(name = "FbsParking.findAll", query = "SELECT f FROM FbsParking f"),
    @NamedQuery(name = "FbsParking.findByRegistrationNumber", query = "SELECT f FROM FbsParking f WHERE f.fbsBooking = :fbsBooking"),
    @NamedQuery(name = "FbsParking.findByParkingId", query = "SELECT f FROM FbsParking f WHERE f.parkingId = :parkingId"),
    @NamedQuery(name = "FbsParking.findByName", query = "SELECT f FROM FbsParking f WHERE f.name = :name"),
    @NamedQuery(name = "FbsParking.findByCharges", query = "SELECT f FROM FbsParking f WHERE f.charges = :charges"),
    @NamedQuery(name = "FbsParking.findByStatus", query = "SELECT f FROM FbsParking f WHERE f.status = :status"),
    @NamedQuery(name = "FbsParking.countAvailableParking", query = "SELECT f FROM FbsParking f WHERE f.fbsParkingType = :fbsParkingType AND f.fbsBooking IS  NULL")})
public class FbsParking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PARKING_ID")
    private Integer parkingId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CHARGES")
    private Double charges;
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "FK_PARKING_TYPE_ID", referencedColumnName = "PARKING_TYPE_ID")
    @ManyToOne(optional = false)
    private FbsParkingType fbsParkingType;
    @JoinColumn(name = "FK_REG_NUMBER", referencedColumnName = "REG_NUMBER")
    @ManyToOne
    private FbsBooking fbsBooking;

    public FbsParking() {
    }

    public FbsParking(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FbsParkingType getFbsParkingType() {
        return fbsParkingType;
    }

    public void setFbsParkingType(FbsParkingType fbsParkingType) {
        this.fbsParkingType = fbsParkingType;
    }

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parkingId != null ? parkingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsParking)) {
            return false;
        }
        FbsParking other = (FbsParking) object;
        if ((this.parkingId == null && other.parkingId != null) || (this.parkingId != null && !this.parkingId.equals(other.parkingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsParking[parkingId=" + parkingId + "]";
    }
}
