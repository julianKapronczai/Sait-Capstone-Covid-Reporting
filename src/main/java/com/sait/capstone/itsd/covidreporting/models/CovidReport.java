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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alex Hill
 */
@Entity
@Table(name = "covid_reports", catalog = "capstonecovidreporting", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CovidReport.findAll", query = "SELECT c FROM CovidReport c"),
    @NamedQuery(name = "CovidReport.findByCovidReportID", query = "SELECT c FROM CovidReport c WHERE c.covidReportID = :covidReportID"),
    @NamedQuery(name = "CovidReport.findByReportDate", query = "SELECT c FROM CovidReport c WHERE c.reportDate = :reportDate"),
    @NamedQuery(name = "CovidReport.findByOneDayDifference", query = "SELECT c FROM CovidReport c WHERE c.oneDayDifference = :oneDayDifference"),
    @NamedQuery(name = "CovidReport.findBySevenDayDifference", query = "SELECT c FROM CovidReport c WHERE c.sevenDayDifference = :sevenDayDifference"),
    @NamedQuery(name = "CovidReport.findByFourteenDayDifference", query = "SELECT c FROM CovidReport c WHERE c.fourteenDayDifference = :fourteenDayDifference"),
    @NamedQuery(name = "CovidReport.findByTotalCases", query = "SELECT c FROM CovidReport c WHERE c.totalCases = :totalCases"),
    @NamedQuery(name = "CovidReport.findByTotalActiveCases", query = "SELECT c FROM CovidReport c WHERE c.totalActiveCases = :totalActiveCases"),
    @NamedQuery(name = "CovidReport.findByTotalRecovered", query = "SELECT c FROM CovidReport c WHERE c.totalRecovered = :totalRecovered"),
    @NamedQuery(name = "CovidReport.findByTotalDeaths", query = "SELECT c FROM CovidReport c WHERE c.totalDeaths = :totalDeaths"),
    @NamedQuery(name = "CovidReport.findByPositivityRate", query = "SELECT c FROM CovidReport c WHERE c.positivityRate = :positivityRate")})
public class CovidReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CovidReportID", nullable = false)
    private Integer covidReportID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ReportDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reportDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "oneDayDifference", nullable = false)
    private int oneDayDifference;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sevenDayDifference", nullable = false)
    private int sevenDayDifference;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fourteenDayDifference", nullable = false)
    private int fourteenDayDifference;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalCases", nullable = false)
    private int totalCases;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalActiveCases", nullable = false)
    private int totalActiveCases;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalRecovered", nullable = false)
    private int totalRecovered;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalDeaths", nullable = false)
    private int totalDeaths;
    @Basic(optional = false)
    @NotNull
    @Column(name = "positivityRate", nullable = false)
    private double positivityRate;
    @JoinTable(name = "covid_report_recipients", joinColumns = {
        @JoinColumn(name = "CovidReportID", referencedColumnName = "CovidReportID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "UserID", referencedColumnName = "UserID", nullable = false)})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> userList;
    @JoinTable(name = "covid_report_covid_test_results", joinColumns = {
        @JoinColumn(name = "CovidReportID", referencedColumnName = "CovidReportID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "CovidTestResultID", referencedColumnName = "CovidTestResultID", nullable = false)})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<CovidTestResult> covidTestResultList;

    public CovidReport() {
    }

    public CovidReport(Integer covidReportID) {
        this.covidReportID = covidReportID;
    }

    public CovidReport(Integer covidReportID, Date reportDate, int oneDayDifference, int sevenDayDifference, int fourteenDayDifference, int totalCases, int totalActiveCases, int totalRecovered, int totalDeaths, double positivityRate) {
        this.covidReportID = covidReportID;
        this.reportDate = reportDate;
        this.oneDayDifference = oneDayDifference;
        this.sevenDayDifference = sevenDayDifference;
        this.fourteenDayDifference = fourteenDayDifference;
        this.totalCases = totalCases;
        this.totalActiveCases = totalActiveCases;
        this.totalRecovered = totalRecovered;
        this.totalDeaths = totalDeaths;
        this.positivityRate = positivityRate;
    }

    public Integer getCovidReportID() {
        return covidReportID;
    }

    public void setCovidReportID(Integer covidReportID) {
        this.covidReportID = covidReportID;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public int getOneDayDifference() {
        return oneDayDifference;
    }

    public void setOneDayDifference(int oneDayDifference) {
        this.oneDayDifference = oneDayDifference;
    }

    public int getSevenDayDifference() {
        return sevenDayDifference;
    }

    public void setSevenDayDifference(int sevenDayDifference) {
        this.sevenDayDifference = sevenDayDifference;
    }

    public int getFourteenDayDifference() {
        return fourteenDayDifference;
    }

    public void setFourteenDayDifference(int fourteenDayDifference) {
        this.fourteenDayDifference = fourteenDayDifference;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public int getTotalActiveCases() {
        return totalActiveCases;
    }

    public void setTotalActiveCases(int totalActiveCases) {
        this.totalActiveCases = totalActiveCases;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public double getPositivityRate() {
        return positivityRate;
    }

    public void setPositivityRate(double positivityRate) {
        this.positivityRate = positivityRate;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @XmlTransient
    public List<CovidTestResult> getCovidTestResultList() {
        return covidTestResultList;
    }

    public void setCovidTestResultList(List<CovidTestResult> covidTestResultList) {
        this.covidTestResultList = covidTestResultList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (covidReportID != null ? covidReportID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CovidReport)) {
            return false;
        }
        CovidReport other = (CovidReport) object;
        if ((this.covidReportID == null && other.covidReportID != null) || (this.covidReportID != null && !this.covidReportID.equals(other.covidReportID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sait.capstone.itsd.covidreporting.models.CovidReport[ covidReportID=" + covidReportID + " ]";
    }
    
    public String[] toStringArray()
    {
        String concatonatedNotes = "";
        
        for (CovidTestResult testResult : this.getCovidTestResultList())
        {
            concatonatedNotes += testResult.getNotes() + " | ";
        }
        
        return new String[]{this.getReportDate().toString(), this.getSevenDayDifference()+"", this.getFourteenDayDifference()+"", this.getTotalCases()+"", this.getTotalActiveCases()+"",  this.getTotalRecovered()+"", this.getPositivityRate()+"", concatonatedNotes};
    }
    
}
