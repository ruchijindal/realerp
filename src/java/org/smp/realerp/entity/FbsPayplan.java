/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_payplan")
@NamedQueries({
    @NamedQuery(name = "FbsPayplan.findAll", query = "SELECT f FROM FbsPayplan f"),
    @NamedQuery(name = "FbsPayplan.findByPayplanId", query = "SELECT f FROM FbsPayplan f WHERE f.payplanId = :payplanId"),
    @NamedQuery(name = "FbsPayplan.findByPercentage", query = "SELECT f FROM FbsPayplan f WHERE f.percentage = :percentage"),
    @NamedQuery(name = "FbsPayplan.findByPlanDesc", query = "SELECT f FROM FbsPayplan f WHERE f.planDesc = :planDesc"),
    @NamedQuery(name = "FbsPayplan.findByFbsPlanName", query = "SELECT f FROM FbsPayplan f WHERE f.fbsPlanname = :fbsPlanname ORDER BY f.serialNo "),
    @NamedQuery(name = "FbsPayplan.findBySerialNo", query = "SELECT f FROM FbsPayplan f WHERE f.serialNo = :serialNo"),
    @NamedQuery(name = "FbsPayplan.findByDueDate", query = "SELECT f FROM FbsPayplan f WHERE f.dueDate = :dueDate")})
public class FbsPayplan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PAYPLAN_ID")
    private Integer payplanId;
    @Column(name = "PERCENTAGE")
    private Double percentage;
    @Column(name = "PLAN_DESC")
    private String planDesc;
    @Column(name = "SERIAL_NO")
    private Integer serialNo;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @JoinColumn(name = "FK_PLANNAME_ID", referencedColumnName = "PLAN_ID")
    @ManyToOne(optional = false)
    private FbsPlanname fbsPlanname;

    public FbsPayplan() {
    }

    public FbsPayplan(Integer payplanId) {
        this.payplanId = payplanId;
    }

    public Integer getPayplanId() {
        return payplanId;
    }

    public void setPayplanId(Integer payplanId) {
        this.payplanId = payplanId;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getPlanDesc() {
        return planDesc;
    }

    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public FbsPlanname getFbsPlanname() {
        return fbsPlanname;
    }

    public void setFbsPlanname(FbsPlanname fbsPlanname) {
        this.fbsPlanname = fbsPlanname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (payplanId != null ? payplanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsPayplan)) {
            return false;
        }
        FbsPayplan other = (FbsPayplan) object;
        if ((this.payplanId == null && other.payplanId != null) || (this.payplanId != null && !this.payplanId.equals(other.payplanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsPayplan[payplanId=" + payplanId + "]";
    }

}
