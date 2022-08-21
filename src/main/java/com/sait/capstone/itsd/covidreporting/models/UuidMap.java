/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.models;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alex Hill
 */
@Entity
@Table(name = "uuid_map", catalog = "capstonecovidreporting", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"UserID"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UuidMap.findAll", query = "SELECT u FROM UuidMap u"),
    @NamedQuery(name = "UuidMap.findById", query = "SELECT u FROM UuidMap u WHERE u.id = :id"),
    @NamedQuery(name = "UuidMap.findByIterations", query = "SELECT u FROM UuidMap u WHERE u.iterations = :iterations"),
    @NamedQuery(name = "UuidMap.findBySalt", query = "SELECT u FROM UuidMap u WHERE u.salt = :salt"),
    @NamedQuery(name = "UuidMap.findByUuid", query = "SELECT u FROM UuidMap u WHERE u.uuid = :uuid")})
public class UuidMap implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Iterations", nullable = false)
    private int iterations;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "Salt", nullable = false, length = 32)
    private String salt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "UUID", nullable = false, length = 256)
    private String uuid;
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private User userID;

    public UuidMap() {
    }

    public UuidMap(Integer id) {
        this.id = id;
    }

    public UuidMap(Integer id, int iterations, String salt, String uuid) {
        this.id = id;
        this.iterations = iterations;
        this.salt = salt;
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UuidMap)) {
            return false;
        }
        UuidMap other = (UuidMap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sait.capstone.itsd.covidreporting.models.UuidMap[ id=" + id + " ]";
    }
    
}
