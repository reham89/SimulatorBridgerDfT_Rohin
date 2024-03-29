/*
 * This file is generated by jOOQ.
 */
package uk.ncl.rohingillgallon.jooq.model.tables.records;


import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

import uk.ncl.rohingillgallon.jooq.model.tables.Historyentry;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class HistoryentryRecord extends UpdatableRecordImpl<HistoryentryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.historyentry.unique_entry_id</code>.
     */
    public void setUniqueEntryId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.historyentry.unique_entry_id</code>.
     */
    public Integer getUniqueEntryId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.historyentry.numactiveports</code>.
     */
    public void setNumactiveports(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.historyentry.numactiveports</code>.
     */
    public Integer getNumactiveports() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.historyentry.starttime</code>.
     */
    public void setStarttime(Double value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.historyentry.starttime</code>.
     */
    public Double getStarttime() {
        return (Double) get(2);
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
     * Create a detached HistoryentryRecord
     */
    public HistoryentryRecord() {
        super(Historyentry.HISTORYENTRY);
    }

    /**
     * Create a detached, initialised HistoryentryRecord
     */
    public HistoryentryRecord(Integer uniqueEntryId, Integer numactiveports, Double starttime) {
        super(Historyentry.HISTORYENTRY);

        setUniqueEntryId(uniqueEntryId);
        setNumactiveports(numactiveports);
        setStarttime(starttime);
        resetChangedOnNotNull();
    }
}
