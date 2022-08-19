package org.chlorinelabs.protonx.schema;

import org.chlorinelabs.protonx.db.DatabaseTemplate;

import java.util.Vector;

public class SchemaCompiler {

    private final LinkedBlock root;

    public SchemaCompiler(LinkedBlock root) {
        this.root = root;
    }

    public DatabaseTemplate compile() {
        DatabaseTemplate dt = new DatabaseTemplate();
        for(LinkedBlock tlb:root.children) {
            if(tlb.content.equals("define cols")) {
                ColumnsCompiler.ref = dt;
                for(LinkedBlock coldef:tlb.children)
                    ColumnsCompiler.compile(coldef);
                ColumnsCompiler.ref = null;
            }
        }
        return dt;
    }

    private static class ColumnsCompiler {

        private static DatabaseTemplate ref;

        public static void compile(LinkedBlock coldef) {
            String def = coldef.content;
            String id = null;
            String label = null;
            boolean isUnique = false;
            Vector<Integer> mods = new Vector<>(1,1);
            if(def.startsWith("new_col")) {
                def = def.replace("new_col", "").trim();
                id = def.substring(0, def.indexOf(' '));
                def = def.replace(id, "").trim();
                if(def.startsWith("label")) {
                    def = def.replace("label", "").trim();
                    label = def;
                }
            }
            if(coldef.children.size() != 0) {
                for(LinkedBlock modb: coldef.children) {
                    String mod = modb.content;
                    if(mod.startsWith("isUnique:")) {
                        mod = mod.replace("isUnique:", "").trim();
                        isUnique = Boolean.parseBoolean(mod);
                    } else if(mod.startsWith("isNumber:")) {
                        mod = mod.replace("isNumber:", "").trim();
                        if(Boolean.parseBoolean(mod)) mods.addElement(11);
                    }
                }
            }
            int[] modsArray = null;
            if(mods.size() != 0) {
                modsArray = new int[mods.size()];
                for (int i = 0; i < modsArray.length; i++) modsArray[i] = mods.elementAt(i);
            }
            ref.newColumnTemplate(id, label, isUnique, modsArray);
        }

    }

}
