/*
 * This file is generated by jOOQ.
 */
package uk.ncl.rohingillgallon.jooq.model.tables.records;


import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

import uk.ncl.rohingillgallon.jooq.model.tables.Overallappresults;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OverallappresultsRecord extends UpdatableRecordImpl<OverallappresultsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.overallappresults.unique_entry_id</code>.
     */
    public void setUniqueEntryId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.overallappresults.unique_entry_id</code>.
     */
    public Integer getUniqueEntryId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.overallappresults.appname</code>.
     */
    public void setAppname(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.overallappresults.appname</code>.
     */
    public String getAppname() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.overallappresults.endtime</code>.
     */
    public void setEndtime(Double value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.overallappresults.endtime</code>.
     */
    public Double getEndtime() {
        return (Double) get(2);
    }

    /**
     * Setter for
     * <code>public.overallappresults.iotdevicebatteryconsumption</code>.
     */
    public void setIotdevicebatteryconsumption(Double value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>public.overallappresults.iotdevicebatteryconsumption</code>.
     */
    public Double getIotdevicebatteryconsumption() {
        return (Double) get(3);
    }

    /**
     * Setter for <code>public.overallappresults.iotdevicedrained</code>.
     */
    public void setIotdevicedrained(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.overallappresults.iotdevicedrained</code>.
     */
    public String getIotdevicedrained() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.overallappresults.simluationtime</code>.
     */
    public void setSimluationtime(Double value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.overallappresults.simluationtime</code>.
     */
    public Double getSimluationtime() {
        return (Double) get(5);
    }

    /**
     * Setter for <code>public.overallappresults.starttime</code>.
     */
    public void setStarttime(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.overallappresults.starttime</code>.
     */
    public Double getStarttime() {
        return (Double) get(6);
    }

    /**
     * Setter for <code>public.overallappresults.totalcloudletsizes</code>.
     */
    public void setTotalcloudletsizes(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.overallappresults.totalcloudletsizes</code>.
     */
    public Integer getTotalcloudletsizes() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>public.overallappresults.totaledgeletsizes</code>.
     */
    public void setTotaledgeletsizes(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.overallappresults.totaledgeletsizes</code>.
     */
    public Integer getTotaledgeletsizes() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>public.overallappresults.totaliotgenerateddata</code>.
     */
    public void setTotaliotgenerateddata(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.overallappresults.totaliotgenerateddata</code>.
     */
    public Integer getTotaliotgenerateddata() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>public.overallappresults.totalmelgenerateddata</code>.
     */
    public void setTotalmelgenerateddata(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.overallappresults.totalmelgenerateddata</code>.
     */
    public Integer getTotalmelgenerateddata() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>public.overallappresults.apptotalrunningtime</code>.
     */
    public void setApptotalrunningtime(Double value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.overallappresults.apptotalrunningtime</code>.
     */
    public Double getApptotalrunningtime() {
        return (Double) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OverallappresultsRecord
     */
    public OverallappresultsRecord() {
        super(Overallappresults.OVERALLAPPRESULTS);
    }

    /**
     * Create a detached, initialised OverallappresultsRecord
     */
    public OverallappresultsRecord(Integer uniqueEntryId, String appname, Double endtime, Double iotdevicebatteryconsumption, String iotdevicedrained, Double simluationtime, Double starttime, Integer totalcloudletsizes, Integer totaledgeletsizes, Integer totaliotgenerateddata, Integer totalmelgenerateddata, Double apptotalrunningtime) {
        super(Overallappresults.OVERALLAPPRESULTS);

        setUniqueEntryId(uniqueEntryId);
        setAppname(appname);
        setEndtime(endtime);
        setIotdevicebatteryconsumption(iotdevicebatteryconsumption);
        setIotdevicedrained(iotdevicedrained);
        setSimluationtime(simluationtime);
        setStarttime(starttime);
        setTotalcloudletsizes(totalcloudletsizes);
        setTotaledgeletsizes(totaledgeletsizes);
        setTotaliotgenerateddata(totaliotgenerateddata);
        setTotalmelgenerateddata(totalmelgenerateddata);
        setApptotalrunningtime(apptotalrunningtime);
        resetChangedOnNotNull();
    }
}
