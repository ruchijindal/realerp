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
 * @author SMP Technologies
 */
@Entity
@Table(name = "fbs_complaint_reply")
@NamedQueries({
    @NamedQuery(name = "FbsComplaintReply.findAll", query = "SELECT f FROM FbsComplaintReply f"),
    @NamedQuery(name = "FbsComplaintReply.findByComplaintReplyId", query = "SELECT f FROM FbsComplaintReply f WHERE f.complaintReplyId = :complaintReplyId"),
    @NamedQuery(name = "FbsComplaintReply.findByMessage", query = "SELECT f FROM FbsComplaintReply f WHERE f.message = :message"),
    @NamedQuery(name = "FbsComplaintReply.findByMessageDate", query = "SELECT f FROM FbsComplaintReply f WHERE f.messageDate = :messageDate"),
    @NamedQuery(name = "FbsComplaintReply.findByFbsComplaint", query = "SELECT f FROM FbsComplaintReply f WHERE f.fbsComplaint = :fbsComplaint"),
    @NamedQuery(name = "FbsComplaintReply.findByStatus", query = "SELECT f FROM FbsComplaintReply f WHERE f.status = :status")})
public class FbsComplaintReply implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COMPLAINT_REPLY_ID")
    private Integer complaintReplyId;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "MESSAGE_DATE")
    @Temporal(TemporalType.DATE)
    private Date messageDate;
    
    @Column(name = "STATUS")
    private String status;
     @Column(name = "TYPE")
    private String type;
    
    @JoinColumn( name = "FK_COMPLAINT_ID",referencedColumnName = "COMPLAINT_ID")
    @ManyToOne(optional = false)
    private FbsComplaint fbsComplaint;

    public FbsComplaintReply() {
    }

    public FbsComplaintReply(Integer complaintReplyId) {
        this.complaintReplyId = complaintReplyId;
    }

    public Integer getComplaintReplyId() {
        return complaintReplyId;
    }

    public void setComplaintReplyId(Integer complaintReplyId) {
        this.complaintReplyId = complaintReplyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public FbsComplaint getFbsComplaint() {
        return fbsComplaint;
    }

    public void setFbsComplaint(FbsComplaint fbsComplaint) {
        this.fbsComplaint = fbsComplaint;
    }

   

     

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (complaintReplyId != null ? complaintReplyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsComplaintReply)) {
            return false;
        }
        FbsComplaintReply other = (FbsComplaintReply) object;
        if ((this.complaintReplyId == null && other.complaintReplyId != null) || (this.complaintReplyId != null && !this.complaintReplyId.equals(other.complaintReplyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsComplaintReply[complaintReplyId=" + complaintReplyId + "]";
    }

}
