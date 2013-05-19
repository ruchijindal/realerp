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
@Table(name = "fbs_plc_allot")
@NamedQueries({
    @NamedQuery(name = "FbsPlcAllot.findAll", query = "SELECT f FROM FbsPlcAllot f"),
    @NamedQuery(name= "FbsPlcAllot.findByFbsFlat", query="SELECT f FROM FbsPlcAllot f WHERE f.fbsFlat = :fbsFlat" ),//added new Named Query
    @NamedQuery(name = "FbsPlcAllot.findByPlcAllotId", query = "SELECT f FROM FbsPlcAllot f WHERE f.plcAllotId = :plcAllotId")})
public class FbsPlcAllot implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PLC_ALLOT_ID")
    private Integer plcAllotId;
    @JoinColumn(name = "FK_UNIT_CODE", referencedColumnName = "UNIT_CODE")
    @ManyToOne(optional = false)
    private FbsFlat fbsFlat;
    @JoinColumn(name = "FK_PLC_ID", referencedColumnName = "PLC_ID")
    @ManyToOne(optional = false)
    private FbsPlc fbsPlc;

    public FbsPlcAllot() {
    }

    public FbsPlcAllot(Integer plcAllotId) {
        this.plcAllotId = plcAllotId;
    }

    public Integer getPlcAllotId() {
        return plcAllotId;
    }

    public void setPlcAllotId(Integer plcAllotId) {
        this.plcAllotId = plcAllotId;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    public FbsPlc getFbsPlc() {
        return fbsPlc;
    }

    public void setFbsPlc(FbsPlc fbsPlc) {
        this.fbsPlc = fbsPlc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plcAllotId != null ? plcAllotId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsPlcAllot)) {
            return false;
        }
        FbsPlcAllot other = (FbsPlcAllot) object;
        if ((this.plcAllotId == null && other.plcAllotId != null) || (this.plcAllotId != null && !this.plcAllotId.equals(other.plcAllotId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsPlcAllot[plcAllotId=" + plcAllotId + "]";
    }

}
