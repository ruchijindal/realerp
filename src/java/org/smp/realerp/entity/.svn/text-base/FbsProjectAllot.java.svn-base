/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_project_allot")
@NamedQueries({
    @NamedQuery(name = "FbsProjectAllot.findAll", query = "SELECT f FROM FbsProjectAllot f"),
    @NamedQuery(name = "FbsProjectAllot.findByProjectAllotId", query = "SELECT f FROM FbsProjectAllot f WHERE f.projectAllotId = :projectAllotId")})
public class FbsProjectAllot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)   
    @Column(name = "PROJECT_ALLOT_ID")
    private Integer projectAllotId;
    @JoinColumn(name = "FK_LOGIN_ID", referencedColumnName = "LOGIN_ID")
    @ManyToOne(optional = false)
    private FbsLogin fbsLogin;
    @JoinColumn(name = "FK_PROJECT_ID", referencedColumnName = "PROJ_ID")
    @ManyToOne(optional = false)
    private FbsProject fbsProject;

    public FbsProjectAllot() {
    }

    public FbsProjectAllot(Integer projectAllotId) {
        this.projectAllotId = projectAllotId;
    }

    public FbsProjectAllot(Integer projectAllotId, FbsLogin fbsLogin, FbsProject fbsProject) {
        this.projectAllotId = projectAllotId;
        this.fbsLogin = fbsLogin;
        this.fbsProject = fbsProject;
    }

    public Integer getProjectAllotId() {
        return projectAllotId;
    }

    public void setProjectAllotId(Integer projectAllotId) {
        this.projectAllotId = projectAllotId;
    }

    public FbsLogin getFbsLogin() {
        return fbsLogin;
    }

    public void setFbsLogin(FbsLogin fbsLogin) {
        this.fbsLogin = fbsLogin;
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
        hash += (projectAllotId != null ? projectAllotId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsProjectAllot)) {
            return false;
        }
        FbsProjectAllot other = (FbsProjectAllot) object;
        if ((this.projectAllotId == null && other.projectAllotId != null) || (this.projectAllotId != null && !this.projectAllotId.equals(other.projectAllotId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsProjectAllot[ projectAllotId=" + projectAllotId + " ]";
    }
}
