/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alex Hill
 */
@Entity
@Table(name = "covid_test_results", catalog = "capstonecovidreporting", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CovidTestResult.findAll", query = "SELECT c FROM CovidTestResult c"),
    @NamedQuery(name = "CovidTestResult.findByCovidTestResultID", query = "SELECT c FROM CovidTestResult c WHERE c.covidTestResultID = :covidTestResultID"),
    @NamedQuery(name = "CovidTestResult.findByResultDate", query = "SELECT c FROM CovidTestResult c WHERE c.resultDate = :resultDate"),
    @NamedQuery(name = "CovidTestResult.findBySubmissionDate", query = "SELECT c FROM CovidTestResult c WHERE c.submissionDate = :submissionDate"),
    @NamedQuery(name = "CovidTestResult.findByNumberTestsTaken", query = "SELECT c FROM CovidTestResult c WHERE c.numberTestsTaken = :numberTestsTaken"),
    @NamedQuery(name = "CovidTestResult.findByNumberTestsPositive", query = "SELECT c FROM CovidTestResult c WHERE c.numberTestsPositive = :numberTestsPositive"),
    @NamedQuery(name = "CovidTestResult.findByNumberTestsInconclusive", query = "SELECT c FROM CovidTestResult c WHERE c.numberTestsInconclusive = :numberTestsInconclusive"),
    @NamedQuery(name = "CovidTestResult.findByPositivityRate", query = "SELECT c FROM CovidTestResult c WHERE c.positivityRate = :positivityRate"),
    @NamedQuery(name = "CovidTestResult.findByNotes", query = "SELECT c FROM CovidTestResult c WHERE c.notes = :notes")})
public class CovidTestResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CovidTestResultID", nullable = false)
    private Integer covidTestResultID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ResultDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date resultDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SubmissionDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date submissionDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NumberTestsTaken", nullable = false)
    private int numberTestsTaken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NumberTestsPositive", nullable = false)
    private int numberTestsPositive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NumberTestsInconclusive", nullable = false)
    private int numberTestsInconclusive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PositivityRate", nullable = false)
    private double positivityRate;
    @Size(max = 256)
    @Column(name = "Notes", length = 256)
    private String notes;
    @ManyToMany(mappedBy = "covidTestResultList", fetch = FetchType.EAGER)
    private List<CovidReport> covidReportList;
    @JoinColumn(name = "ReporterID", referencedColumnName = "UserID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User reporterID;

    public CovidTestResult() {
    }

    public CovidTestResult(Integer covidTestResultID) {
        this.covidTestResultID = covidTestResultID;
    }

    public CovidTestResult(Integer covidTestResultID, Date resultDate, Date submissionDate, int numberTestsTaken, int numberTestsPositive, int numberTestsInconclusive, double positivityRate) {
        this.covidTestResultID = covidTestResultID;
        this.resultDate = resultDate;
        this.submissionDate = submissionDate;
        this.numberTestsTaken = numberTestsTaken;
        this.numberTestsPositive = numberTestsPositive;
        this.numberTestsInconclusive = numberTestsInconclusive;
        this.positivityRate = positivityRate;
    }

    public Integer getCovidTestResultID() {
        return covidTestResultID;
    }

    public void setCovidTestResultID(Integer covidTestResultID) {
        this.covidTestResultID = covidTestResultID;
    }

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getNumberTestsTaken() {
        return numberTestsTaken;
    }

    public void setNumberTestsTaken(int numberTestsTaken) {
        this.numberTestsTaken = numberTestsTaken;
    }

    public int getNumberTestsPositive() {
        return numberTestsPositive;
    }

    public void setNumberTestsPositive(int numberTestsPositive) {
        this.numberTestsPositive = numberTestsPositive;
    }

    public int getNumberTestsInconclusive() {
        return numberTestsInconclusive;
    }

    public void setNumberTestsInconclusive(int numberTestsInconclusive) {
        this.numberTestsInconclusive = numberTestsInconclusive;
    }

    public double getPositivityRate() {
        return positivityRate;
    }

    public void setPositivityRate(double positivityRate) {
        this.positivityRate = positivityRate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @XmlTransient
    public List<CovidReport> getCovidReportList() {
        return covidReportList;
    }

    public void setCovidReportList(List<CovidReport> covidReportList) {
        this.covidReportList = covidReportList;
    }

    public User getReporterID() {
        return reporterID;
    }

    public void setReporterID(User reporterID) {
        this.reporterID = reporterID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (covidTestResultID != null ? covidTestResultID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CovidTestResult)) {
            return false;
        }
        CovidTestResult other = (CovidTestResult) object;
        if ((this.covidTestResultID == null && other.covidTestResultID != null) || (this.covidTestResultID != null && !this.covidTestResultID.equals(other.covidTestResultID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sait.capstone.itsd.covidreporting.models.CovidTestResult[ covidTestResultID=" + covidTestResultID + " ]";
    }
    
}
