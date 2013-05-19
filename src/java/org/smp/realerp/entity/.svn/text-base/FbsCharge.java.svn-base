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
@Table(name = "fbs_charge")
@NamedQueries({
    @NamedQuery(name = "FbsCharge.findAll", query = "SELECT f FROM FbsCharge f"),
    @NamedQuery(name = "FbsCharge.findByChargeId", query = "SELECT f FROM FbsCharge f WHERE f.chargeId = :chargeId"),
    @NamedQuery(name = "FbsCharge.findByChargeName", query = "SELECT f FROM FbsCharge f WHERE f.chargeName = :chargeName"),
    @NamedQuery(name = "FbsCharge.findByProject", query = "SELECT f FROM FbsCharge f WHERE f.fbsProject = :fbsProject"),
    @NamedQuery(name = "FbsCharge.findByAmount", query = "SELECT f FROM FbsCharge f WHERE f.amount = :amount")})
public class FbsCharge implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CHARGE_ID")
    private Integer chargeId;
    @Column(name = "CHARGE_NAME")
    private String chargeName;
    @Column(name = "AMOUNT")
    private Double amount;
    @JoinColumn(name = "FK_PROJ_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;

    public FbsCharge() {
    }

    public FbsCharge(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
        hash += (chargeId != null ? chargeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsCharge)) {
            return false;
        }
        FbsCharge other = (FbsCharge) object;
        if ((this.chargeId == null && other.chargeId != null) || (this.chargeId != null && !this.chargeId.equals(other.chargeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsCharge[chargeId=" + chargeId + "]";
    }

}
