package org.chlorinelabs.protonx.schema;

import org.chlorinelabs.protonx.db.DatabaseMetadata;
import org.chlorinelabs.protonx.db.DatabaseTemplate;

import java.util.Vector;

public class SchemaCompiler {

    private final LinkedBlock root;
    private LinkedBlock metadataBlock;

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
            } else if(tlb.content.equals("define database")) {
                metadataBlock = tlb;
            }
        }
        return dt;
    }

    public DatabaseMetadata compileMetadata() {
        return MetadataCompiler.compile(metadataBlock);
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

    private static class MetadataCompiler {
        public static DatabaseMetadata compile(LinkedBlock block) {
            DatabaseMetadata meta = new DatabaseMetadata();
            for(LinkedBlock b: block.children) {
                if(b.content.startsWith("name:")) {
                    meta.name = b.content.replace("name:", "").trim();
                } else if(b.content.startsWith("filename:")) {
                    meta.file = b.content.replace("filename:", "").trim();
                } else if(b.content.startsWith("import_as:")) {
                    meta.type = b.content.replace("import_as:", "").trim();
                } else if(b.content.startsWith("ignore_header:")) {
                    meta.ignoreHeader = Boolean.parseBoolean(b.content.replace("ignore_header:", "").trim());
                }
            }
            return meta;
        }
    }

}
