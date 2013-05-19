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
@Table(name = "fbs_planname")
@NamedQueries({
    @NamedQuery(name = "FbsPlanname.findAll", query = "SELECT f FROM FbsPlanname f"),
    @NamedQuery(name = "FbsPlanname.findByPlanId", query = "SELECT f FROM FbsPlanname f WHERE f.planId = :planId"),
    @NamedQuery(name = "FbsPlanname.findByPlanName", query = "SELECT f FROM FbsPlanname f WHERE f.planName = :planName"),
    @NamedQuery(name = "FbsPlanname.findByDiscount", query = "SELECT f FROM FbsPlanname f WHERE f.discount = :discount"),
    @NamedQuery(name = "FbsPlanname.findByFullName", query = "SELECT f FROM FbsPlanname f WHERE f.fullName = :fullName"),
    @NamedQuery(name = "FbsPlanname.findByStatus", query = "SELECT f FROM FbsPlanname f WHERE f.status = :status")})
public class FbsPlanname implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PLAN_ID")
    private Integer planId;
    @Column(name = "PLAN_NAME")
    private String planName;
    @Column(name = "DISCOUNT")
    private Double discount;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "STATUS")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsPlanname")
    private Collection<FbsPayplan> fbsPayplanCollection;
    @OneToMany(mappedBy = "fbsPlanname")
    private Collection<FbsBooking> fbsBookingCollection;
    @JoinColumn(name = "FK_PROJ_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;

    public FbsPlanname() {
    }

    public FbsPlanname(Integer planId) {
        this.planId = planId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<FbsPayplan> getFbsPayplanCollection() {
        return fbsPayplanCollection;
    }

    public void setFbsPayplanCollection(Collection<FbsPayplan> fbsPayplanCollection) {
        this.fbsPayplanCollection = fbsPayplanCollection;
    }

    public Collection<FbsBooking> getFbsBookingCollection() {
        return fbsBookingCollection;
    }

    public void setFbsBookingCollection(Collection<FbsBooking> fbsBookingCollection) {
        this.fbsBookingCollection = fbsBookingCollection;
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
        hash += (planId != null ? planId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsPlanname)) {
            return false;
        }
        FbsPlanname other = (FbsPlanname) object;
        if ((this.planId == null && other.planId != null) || (this.planId != null && !this.planId.equals(other.planId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsPlanname[planId=" + planId + "]";
    }
}