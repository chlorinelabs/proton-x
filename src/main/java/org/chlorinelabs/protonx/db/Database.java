package org.chlorinelabs.protonx.db;

import org.chlorinelabs.protonx.util.DirectStreamReader;

import java.util.Arrays;
import java.util.Vector;

public class Database {

    private DatabaseTemplate.Column[] header;
    private final Vector<String[]> data = new Vector<>(1,1);
    private final DatabaseTemplate template;
    private final DatabaseMetadata metadata;

    public Database(DatabaseTemplate template, DatabaseMetadata metadata) {
        this.template = template;
        this.metadata = metadata;
    }

    public boolean initialize() {
        // Load headers
        int nCols = template.cols.size();
        header = new DatabaseTemplate.Column[nCols];
        for(int i=0;i<nCols;i++) header[i] = template.cols.elementAt(i);

        // Load data
        String datafile = metadata.file;
        if(metadata.type.equals("csv")) {
            if(loadCSVData(datafile) != 0) return false;
        }
        return true;
    }

    public int addRecord(String[] record) {
        if(record.length != header.length) {
            // Hey, Wrong Lengths!
            System.out.println("Invalid Record Length!");
            return 1;
        }
        for(int i=0;i<header.length;i++) {
            DatabaseTemplate.Column col = header[i];
            if(col.modifiers != null) if(col.modifiers.length == 0) continue;
            else {
                for(int modcode:col.modifiers) {
                    if(modcode == 11) {
                        if(!isNumeric(record[i])) {
                            // Invalid!
                            System.out.println("Current modifiers (Modifier 11) do not allow this addition of record.");
                            return 2;
                        }
                    }
                }
            }
            if(col.isUnique) {
                if(!checkUniqueness(record[i], i))  {
                    System.out.println("Current modifiers (isUniqueness check) do not allow this addition of record.");
                    return 3;
                }
            }
        }
        data.addElement(record);
        return 0;
    }

    public void display(String[] record, int[] normNum) {
        for(int i=0;i<record.length;i++) {
            String data = record[i];
            String suffix = "";
            int size = normNum[i] - data.length();
            for(int j=1;j<=size;j++) suffix += " ";
            System.out.print(data+suffix+" ");
        }
        System.out.println();
    }

    public void display() {
        int[] normNum = new int[header.length];
        for(String[] record:data) {
            for(int i=0;i<normNum.length;i++)
                normNum[i] = Math.max(normNum[i], record[i].length());
        }
        String[] headerTitles = new String[header.length];
        for(int i=0;i<headerTitles.length;i++) headerTitles[i] = header[i].label;
        for(int i=0;i<normNum.length;i++)
            normNum[i] = Math.max(normNum[i], headerTitles[i].length());
        for(int i=0;i<normNum.length;i++) normNum[i] += 3;
        display(headerTitles, normNum);
        for(String[] record:data) display(record, normNum);
    }

    private boolean checkUniqueness(String s, int i) {
        for(String[] record:data) {
            if(record[i].equals(s)) return false;
        }
        return true;
    }

    private int loadCSVData(String file) {
        Vector<String> csvdata = DirectStreamReader.read(file);
        for(int i=0;i<csvdata.size();i++) {
            if(i==0 && metadata.ignoreHeader) continue;
            String csvrecord = csvdata.elementAt(i);
            String[] rec = csvrecord.split(",");
            for(int j=0;j<rec.length;j++) rec[j] = rec[j].trim();
            int retcode = addRecord(rec);
            if(retcode != 0) {
                System.out.println("This record could not be added: "+Arrays.toString(rec) +" at line "+i+" in "+file);
            }
        }
        return 0;
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

}
