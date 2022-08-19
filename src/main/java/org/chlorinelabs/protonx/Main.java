package org.chlorinelabs.protonx;

import org.chlorinelabs.protonx.db.Database;
import org.chlorinelabs.protonx.db.DatabaseMetadata;
import org.chlorinelabs.protonx.db.DatabaseTemplate;
import org.chlorinelabs.protonx.schema.SchemaParser;
import org.chlorinelabs.protonx.schema.SchemaCompiler;

public class Main {

    public static void main(String[] args) {
        System.out.println("ProtonX version 1.0");
        System.out.println("Copyright(C) 2022, ChlorineLabs");
        SchemaParser schparser = new SchemaParser("books.schema");
        SchemaCompiler schcompiler = new SchemaCompiler(schparser.parse());
        DatabaseTemplate dtemp = schcompiler.compile();
        DatabaseMetadata dmeta = schcompiler.compileMetadata();
        Database db = new Database(dtemp, dmeta);
        db.initialize();
        db.display();
    }

}
