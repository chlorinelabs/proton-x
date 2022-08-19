package org.chlorinelabs.protonx.db;

import java.util.Arrays;
import java.util.Vector;

public class DatabaseTemplate {

    public static class Column {
        public String identifier;
        public String label;
        public boolean isUnique;
        public int[] modifiers;

        @Override
        public String toString() {
            return "TemplateColumn ID:"+identifier+" Label:"+label+" isUnique:"+isUnique+" Modifiers:"+ Arrays.toString(modifiers);
        }
    }

    public Vector<Column> cols = new Vector<>(1,1);

    public void newColumnTemplate(String id, String label, boolean isUnique, int[] mods) {
        Column col = new Column();
        col.identifier = id;
        col.label = label;
        col.isUnique = isUnique;
        col.modifiers = mods;
        cols.addElement(col);
        System.out.println(col.toString());
    }

    public void newColumnTemplate(String id, String label, int[] mods) {
        newColumnTemplate(id, label, false, mods);
    }

    public void newColumnTemplate(String id, String label, boolean isUnique) {
        newColumnTemplate(id, label, isUnique, null);
    }

    public void newColumnTemplate(String id, String label) {
        newColumnTemplate(id, label, false, null);
    }

    public void newColumnTemplate(Column col) {
        cols.addElement(col);
    }

}
