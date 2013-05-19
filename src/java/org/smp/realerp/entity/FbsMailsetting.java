/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_mailsetting")
@NamedQueries({
    @NamedQuery(name = "FbsMailsetting.findAll", query = "SELECT f FROM FbsMailsetting f"),
    @NamedQuery(name = "FbsMailsetting.findByMailsettingId", query = "SELECT f FROM FbsMailsetting f WHERE f.mailsettingId = :mailsettingId"),
    @NamedQuery(name = "FbsMailsetting.findByReplyId", query = "SELECT f FROM FbsMailsetting f WHERE f.replyId = :replyId"),
    @NamedQuery(name = "FbsMailsetting.findByPort", query = "SELECT f FROM FbsMailsetting f WHERE f.port = :port"),
    @NamedQuery(name = "FbsMailsetting.findByUser", query = "SELECT f FROM FbsMailsetting f WHERE f.user = :user"),
    @NamedQuery(name = "FbsMailsetting.findByPassword", query = "SELECT f FROM FbsMailsetting f WHERE f.password = :password"),
    @NamedQuery(name = "FbsMailsetting.findByHost", query = "SELECT f FROM FbsMailsetting f WHERE f.host = :host")})
public class FbsMailsetting implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MAILSETTING_ID")
    private Integer mailsettingId;
    @Column(name = "REPLY_Id")
    private String replyId;
    @Column(name = "PORT")
    private Integer port;
    @Column(name = "USER")
    private String user;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "HOST")
    private String host;
    @JoinColumn(name = "FK_COMPANY_ID", referencedColumnName = "COMPANY_ID")
    @ManyToOne(optional = false)
    private FbsCompany fbsCompany;

    public FbsMailsetting() {
    }

    public FbsMailsetting(Integer mailsettingId) {
        this.mailsettingId = mailsettingId;
    }

    public Integer getMailsettingId() {
        return mailsettingId;
    }

    public void setMailsettingId(Integer mailsettingId) {
        this.mailsettingId = mailsettingId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

  

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public FbsCompany getFbsCompany() {
        return fbsCompany;
    }

    public void setFbsCompany(FbsCompany fbsCompany) {
        this.fbsCompany = fbsCompany;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mailsettingId != null ? mailsettingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsMailsetting)) {
            return false;
        }
        FbsMailsetting other = (FbsMailsetting) object;
        if ((this.mailsettingId == null && other.mailsettingId != null) || (this.mailsettingId != null && !this.mailsettingId.equals(other.mailsettingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsMailsetting[mailsettingId=" + mailsettingId + "]";
    }

}
