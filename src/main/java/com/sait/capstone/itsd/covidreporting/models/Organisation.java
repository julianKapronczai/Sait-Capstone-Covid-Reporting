/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.models;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alex Hill
 */
@Entity
@Table(name = "organisations", catalog = "capstonecovidreporting", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organisation.findAll", query = "SELECT o FROM Organisation o"),
    @NamedQuery(name = "Organisation.findByOrganisationId", query = "SELECT o FROM Organisation o WHERE o.organisationId = :organisationId"),
    @NamedQuery(name = "Organisation.findByOrganisationName", query = "SELECT o FROM Organisation o WHERE o.organisationName = :organisationName")})
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "organisation_id", nullable = false)
    private Integer organisationId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "organisationName", nullable = false, length = 256)
    private String organisationName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation", fetch = FetchType.EAGER)
    private List<User> userList;

    public Organisation() {
    }

    public Organisation(Integer organisationId) {
        this.organisationId = organisationId;
    }

    public Organisation(Integer organisationId, String organisationName) {
        this.organisationId = organisationId;
        this.organisationName = organisationName;
    }

    public Integer getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (organisationId != null ? organisationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organisation)) {
            return false;
        }
        Organisation other = (Organisation) object;
        if ((this.organisationId == null && other.organisationId != null) || (this.organisationId != null && !this.organisationId.equals(other.organisationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sait.capstone.itsd.covidreporting.models.Organisation[ organisationId=" + organisationId + " ]";
    }
    
}
