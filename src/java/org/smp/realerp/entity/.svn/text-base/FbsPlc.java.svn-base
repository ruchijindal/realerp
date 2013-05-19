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
@Table(name = "fbs_plc")
@NamedQueries({
    @NamedQuery(name = "FbsPlc.findAll", query = "SELECT f FROM FbsPlc f"),
    @NamedQuery(name = "FbsPlc.findByPlcId", query = "SELECT f FROM FbsPlc f WHERE f.plcId = :plcId"),
    @NamedQuery(name = "FbsPlc.findByPlcName", query = "SELECT f FROM FbsPlc f WHERE f.plcName = :plcName"),
    @NamedQuery(name = "FbsPlc.findByPlcCharge", query = "SELECT f FROM FbsPlc f WHERE f.plcCharge = :plcCharge")})
public class FbsPlc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PLC_ID")
    private Integer plcId;
    @Basic(optional = false)
    @Column(name = "PLC_NAME")
    private String plcName;
    @Column(name = "PLC_CHARGE")
    private Double plcCharge;
    @JoinColumn(name = "FK_PROJ_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsPlc")
    private Collection<FbsPlcAllot> fbsPlcAllotCollection;

    public FbsPlc() {
    }

    public FbsPlc(Integer plcId) {
        this.plcId = plcId;
    }

    public FbsPlc(Integer plcId, String plcName) {
        this.plcId = plcId;
        this.plcName = plcName;
    }

    public Integer getPlcId() {
        return plcId;
    }

    public void setPlcId(Integer plcId) {
        this.plcId = plcId;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public Double getPlcCharge() {
        return plcCharge;
    }

    public void setPlcCharge(Double plcCharge) {
        this.plcCharge = plcCharge;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public Collection<FbsPlcAllot> getFbsPlcAllotCollection() {
        return fbsPlcAllotCollection;
    }

    public void setFbsPlcAllotCollection(Collection<FbsPlcAllot> fbsPlcAllotCollection) {
        this.fbsPlcAllotCollection = fbsPlcAllotCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plcId != null ? plcId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsPlc)) {
            return false;
        }
        FbsPlc other = (FbsPlc) object;
        if ((this.plcId == null && other.plcId != null) || (this.plcId != null && !this.plcId.equals(other.plcId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsPlc[plcId=" + plcId + "]";
    }

}
