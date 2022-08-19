package org.chlorinelabs.protonx;

import org.chlorinelabs.protonx.db.DatabaseTemplate;
import org.chlorinelabs.protonx.schema.Schema;
import org.chlorinelabs.protonx.schema.SchemaCompiler;

public class Main {

    public static void main(String[] args) {
        System.out.println("ProtonX version 1.0");
        System.out.println("Copyright(C) 2022, ChlorinePentoxide");
        Schema sch = new Schema("books.schema");
        SchemaCompiler schcompiler = new SchemaCompiler(sch.parse());
        DatabaseTemplate dtemp = schcompiler.compile();
    }

}
