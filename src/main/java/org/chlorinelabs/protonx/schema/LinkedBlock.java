package org.chlorinelabs.protonx.schema;

import java.util.Vector;

public class LinkedBlock {
    public int blockID;
    public final Vector<LinkedBlock> children = new Vector<>(1,1);
    public String content;
}
