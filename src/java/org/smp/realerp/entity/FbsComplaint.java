/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_complaint")
@NamedQueries({
    @NamedQuery(name = "FbsComplaint.findByCurrentDate", query = "SELECT f FROM FbsComplaint f  WHERE  f.complaintDt >= :beforeDate"),
    @NamedQuery(name = "FbsComplaint.findByDate", query = "SELECT f FROM FbsComplaint f  WHERE f.complaintDt >= :fromDate AND f.complaintDt <= :toDate"),
    @NamedQuery(name = "FbsComplaint.findAll", query = "SELECT f FROM FbsComplaint f"),
    @NamedQuery(name = "FbsComplaint.findByComplaintId", query = "SELECT f FROM FbsComplaint f WHERE f.complaintId = :complaintId"),
    @NamedQuery(name = "FbsComplaint.findByName", query = "SELECT f FROM FbsComplaint f WHERE f.name = :name"),
    @NamedQuery(name = "FbsComplaint.findByAddress", query = "SELECT f FROM FbsComplaint f WHERE f.address = :address"),
    @NamedQuery(name = "FbsComplaint.findByEmail", query = "SELECT f FROM FbsComplaint f WHERE f.email = :email"),
    @NamedQuery(name = "FbsComplaint.findBySubject", query = "SELECT f FROM FbsComplaint f WHERE f.subject = :subject"),
    @NamedQuery(name = "FbsComplaint.findByComplaint", query = "SELECT f FROM FbsComplaint f WHERE f.complaint = :complaint"),
    @NamedQuery(name = "FbsComplaint.findByComplaintDt", query = "SELECT f FROM FbsComplaint f WHERE f.complaintDt = :complaintDt"),
    @NamedQuery(name = "FbsComplaint.findByStatus", query = "SELECT f FROM FbsComplaint f WHERE f.status = :status"),
    @NamedQuery(name = "FbsComplaint.findByRegisteredBy", query = "SELECT f FROM FbsComplaint f WHERE f.registeredBy = :registeredBy"),
    @NamedQuery(name = "FbsComplaint.findByPhone", query = "SELECT f FROM FbsComplaint f WHERE f.phone = :phone")})
public class FbsComplaint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COMPLAINT_ID")
    private Integer complaintId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SUBJECT")
    private String subject;
    @Column(name = "COMPLAINT")
    private String complaint;
    @Column(name = "COMPLAINT_DT")
    @Temporal(TemporalType.DATE)
    private Date complaintDt;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "REGISTERED_BY")
    private String registeredBy;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "COMPLAINT_CATEGORY")
    private String complaintCategory;
    @Column(name = "PRIORITY")
    private String priority;
    @JoinColumn(name = "FK_PROJ_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fbsComplaint")
    private Collection<FbsComplaintReply> fbsComplaintReplyCollection;

    public FbsComplaint() {
    }

    public Collection<FbsComplaintReply> getFbsComplaintReplyCollection() {
        return fbsComplaintReplyCollection;
    }

    public void setFbsComplaintReplyCollection(Collection<FbsComplaintReply> fbsComplaintReplyCollection) {
        this.fbsComplaintReplyCollection = fbsComplaintReplyCollection;
    }

    public FbsComplaint(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public Integer getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public Date getComplaintDt() {
        return complaintDt;
    }

    public void setComplaintDt(Date complaintDt) {
        this.complaintDt = complaintDt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComplaintCategory() {
        return complaintCategory;
    }

    public void setComplaintCategory(String complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
        hash += (complaintId != null ? complaintId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsComplaint)) {
            return false;
        }
        FbsComplaint other = (FbsComplaint) object;
        if ((this.complaintId == null && other.complaintId != null) || (this.complaintId != null && !this.complaintId.equals(other.complaintId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsComplaint[complaintId=" + complaintId + "]";
    }
}
