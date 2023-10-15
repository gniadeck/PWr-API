package dev.wms.pwrapi.utils.csv.appendable;

/*
    When implemented, an entity has its CSV representation which then may be used
    in compliance with CSVBuilder instance in order to construct CSV String.
 */
public interface CSVAppendable {
    String toCSV();
}
