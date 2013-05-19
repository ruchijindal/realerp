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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author smp
 */
@Entity
@Table(name = "fbs_docs")
@NamedQueries({
    @NamedQuery(name = "FbsDocs.findAll", query = "SELECT f FROM FbsDocs f"),
    @NamedQuery(name = "FbsDocs.findByDocId", query = "SELECT f FROM FbsDocs f WHERE f.docId = :docId"),
    @NamedQuery(name = "FbsDocs.findByDocName", query = "SELECT f FROM FbsDocs f WHERE f.docName = :docName"),
    @NamedQuery(name = "FbsDocs.findByDescription", query = "SELECT f FROM FbsDocs f WHERE f.description = :description")})
public class FbsDocs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DOC_ID")
    private Integer docId;
    @Column(name = "DOC_NAME")
    private String docName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Lob
    @Column(name = "FILE")
    private byte[] file;
    @JoinColumn(name = "FK_UNIT_CODE", referencedColumnName = "UNIT_CODE")
    @ManyToOne(optional = false)
    private FbsFlat fbsFlat;

    public FbsDocs() {
    }

    public FbsDocs(Integer docId) {
        this.docId = docId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docId != null ? docId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FbsDocs)) {
            return false;
        }
        FbsDocs other = (FbsDocs) object;
        if ((this.docId == null && other.docId != null) || (this.docId != null && !this.docId.equals(other.docId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.smp.realerp.entity.FbsDocs[docId=" + docId + "]";
    }

}
