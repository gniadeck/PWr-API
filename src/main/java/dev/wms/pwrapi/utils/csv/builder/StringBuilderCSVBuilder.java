package dev.wms.pwrapi.utils.csv.builder;

import dev.wms.pwrapi.utils.csv.appendable.CSVAppendable;

import java.util.List;

/*
    A StringBuilder implementation of CSVBuilder which stores CSV String in memory, as a class variable.
 */
public class StringBuilderCSVBuilder implements CSVBuilder {

    private String headers;
    private StringBuilder builder;

    public StringBuilderCSVBuilder(String headers) {
        this.headers = headers;
    }

    public String buildCSV(List<? extends CSVAppendable> CSVAppendables) {
        initializeBuilderWithHeaders();
        for(CSVAppendable appendable: CSVAppendables) {
            builder.append(appendable.toCSV()).append("\n");
        }
        return builder.toString();
    }

    private void initializeBuilderWithHeaders() {
        builder = new StringBuilder(headers + "\n");
    }
}
