/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_parking_type")
@NamedQueries({
    @NamedQuery(name = "FbsParkingType.findAll", query = "SELECT f FROM FbsParkingType f"),
    @NamedQuery(name = "FbsParkingType.findByParkingTypeId", query = "SELECT f FROM FbsParkingType f WHERE f.parkingTypeId = :parkingTypeId"),
    @NamedQuery(name = "FbsParkingType.findByParkingTypeName", query = "SELECT f FROM FbsParkingType f WHERE f.name = :name"),
    @NamedQuery(name = "FbsParkingType.findByNoOfParking", query = "SELECT f FROM FbsParkingType f WHERE f.noOfParking = :noOfParking"),
    @NamedQuery(name = "FbsParkingType.findByParkingAbvr", query = "SELECT f FROM FbsParkingType f WHERE f.parkingAbvr = :parkingAbvr"),
    @NamedQuery(name = "FbsParkingType.findByCharges", query = "SELECT f FROM FbsParkingType f WHERE f.charges = :charges")})
public class FbsParkingType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PARKING_TYPE_ID")
    private Integer parkingTypeId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "NO_OF_PARKING")
    private Integer noOfParking;
    @Column(name = "parking_abvr")
    private String parkingAbvr;
    @Column(name = "charges")
    private Double charges;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsParkingType")
    private Collection<FbsParking> fbsParkingCollection;
    @JoinColumn(name = "FK_PROJ_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;

    public FbsParkingType() {
    }

    public FbsParkingType(Integer parkingTypeId) {
        this.parkingTypeId = parkingTypeId;
    }

    public Integer getParkingTypeId() {
        return parkingTypeId;
    }

    public void setParkingTypeId(Integer parkingTypeId) {
        this.parkingTypeId = parkingTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

    public Integer getNoOfParking() {
        return noOfParking;
    }

    public void setNoOfParking(Integer noOfParking) {
        this.noOfParking = noOfParking;
    }

    public String getParkingAbvr() {
        return parkingAbvr;
    }

    public void setParkingAbvr(String parkingAbvr) {
        this.parkingAbvr = parkingAbvr;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Collection<FbsParking> getFbsParkingCollection() {
        return fbsParkingCollection;
    }

    public void setFbsParkingCollection(Collection<FbsParking> fbsParkingCollection) {
        this.fbsParkingCollection = fbsParkingCollection;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parkingTypeId != null ? parkingTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsParkingType)) {
            return false;
        }
        FbsParkingType other = (FbsParkingType) object;
        if ((this.parkingTypeId == null && other.parkingTypeId != null) || (this.parkingTypeId != null && !this.parkingTypeId.equals(other.parkingTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsParkingType[parkingTypeId=" + parkingTypeId + "]";
    }

}
