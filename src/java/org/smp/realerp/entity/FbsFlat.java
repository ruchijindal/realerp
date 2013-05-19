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
@Table(name = "fbs_flat")
@NamedQueries({
    @NamedQuery(name = "FbsFlat.findByFloor&Locked&Unbooked", query = "SELECT f FROM FbsFlat f where f.fbsFloor = :fbsFloor and f.lockStatus = :lockStatus and f.status = :status"),
    @NamedQuery(name = "FbsFlat.findAll", query = "SELECT f FROM FbsFlat f"),
    @NamedQuery(name = "FbsFlat.findByUnitCode", query = "SELECT f FROM FbsFlat f WHERE f.unitCode = :unitCode"),
    @NamedQuery(name = "FbsFlat.findByFlatNo", query = "SELECT f FROM FbsFlat f WHERE f.flatNo = :flatNo"),
    @NamedQuery(name = "FbsFlat.findByStatus", query = "SELECT f FROM FbsFlat f WHERE f.status = :status"),
    @NamedQuery(name = "FbsFlat.findByLockStatus", query = "SELECT f FROM FbsFlat f WHERE f.lockStatus = :lockStatus"),
    @NamedQuery(name = "FbsFloor.findMaxFlatNo", query = "SELECT MAX(f.flatNo) FROM FbsFlat f where f.fbsFloor = :fbsFloor")})
public class FbsFlat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UNIT_CODE")
    private Integer unitCode;
    @Column(name = "FLAT_NO")
    private String flatNo;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "LOCK_STATUS")
    private String lockStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsFlat")
    private Collection<FbsDocs> fbsDocsCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsFlat")
    private Collection<FbsLoan> fbsLoanCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsFlat")
    private Collection<FbsPlcAllot> fbsPlcAllotCollection;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsFlat")
//    private Collection<FbsBrPayment> fbsBrPaymentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsFlat")
    private Collection<FbsBooking> fbsBookingCollection;
    @JoinColumn(name = "FK_FLAT_TYPE_ID", referencedColumnName = "FLAT_TYPE_ID")
    @ManyToOne
    private FbsFlatType fbsFlatType;
    @JoinColumn(name = "FK_FLOOR_ID", referencedColumnName = "FLOOR_ID")
    @ManyToOne(optional = false)
    private FbsFloor fbsFloor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsFlat")
    private Collection<FbsApplicant> fbsApplicantCollection;

    public FbsFlat() {
    }

    public FbsFlat(Integer unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Integer unitCode) {
        this.unitCode = unitCode;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Collection<FbsDocs> getFbsDocsCollection() {
        return fbsDocsCollection;
    }

    public void setFbsDocsCollection(Collection<FbsDocs> fbsDocsCollection) {
        this.fbsDocsCollection = fbsDocsCollection;
    }

 
    public Collection<FbsLoan> getFbsLoanCollection() {
        return fbsLoanCollection;
    }

    public void setFbsLoanCollection(Collection<FbsLoan> fbsLoanCollection) {
        this.fbsLoanCollection = fbsLoanCollection;
    }

    public Collection<FbsPlcAllot> getFbsPlcAllotCollection() {
        return fbsPlcAllotCollection;
    }

    public void setFbsPlcAllotCollection(Collection<FbsPlcAllot> fbsPlcAllotCollection) {
        this.fbsPlcAllotCollection = fbsPlcAllotCollection;
    }

//    public Collection<FbsBrPayment> getFbsBrPaymentCollection() {
//        return fbsBrPaymentCollection;
//    }
//
//    public void setFbsBrPaymentCollection(Collection<FbsBrPayment> fbsBrPaymentCollection) {
//        this.fbsBrPaymentCollection = fbsBrPaymentCollection;
//    }

    public Collection<FbsBooking> getFbsBookingCollection() {
        return fbsBookingCollection;
    }

    public void setFbsBookingCollection(Collection<FbsBooking> fbsBookingCollection) {
        this.fbsBookingCollection = fbsBookingCollection;
    }

    public FbsFlatType getFbsFlatType() {
        return fbsFlatType;
    }

    public void setFbsFlatType(FbsFlatType fbsFlatType) {
        this.fbsFlatType = fbsFlatType;
    }

    public FbsFloor getFbsFloor() {
        return fbsFloor;
    }

    public void setFbsFloor(FbsFloor fbsFloor) {
        this.fbsFloor = fbsFloor;
    }

    public Collection<FbsApplicant> getFbsApplicantCollection() {
        return fbsApplicantCollection;
    }

    public void setFbsApplicantCollection(Collection<FbsApplicant> fbsApplicantCollection) {
        this.fbsApplicantCollection = fbsApplicantCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitCode != null ? unitCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsFlat)) {
            return false;
        }
        FbsFlat other = (FbsFlat) object;
        if ((this.unitCode == null && other.unitCode != null) || (this.unitCode != null && !this.unitCode.equals(other.unitCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsFlat[unitCode=" + unitCode + "]";
    }
}
