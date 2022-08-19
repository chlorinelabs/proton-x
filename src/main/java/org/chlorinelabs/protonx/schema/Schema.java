package org.chlorinelabs.protonx.schema;

import org.chlorinelabs.protonx.util.DirectStreamReader;

import java.util.Vector;

public class Schema {

    private final Vector<String> inputSchema;
    private final Vector<Integer> signalSchema;
    private final Vector<LinkedBlock> blocks;

    public Schema(String file) {
        inputSchema = DirectStreamReader.read(file);
        signalSchema = new Vector<>(1,1);
        blocks = new Vector<>(1,1);
    }

    public LinkedBlock parse() {
        int cid = 0;

        LinkedBlock root = new LinkedBlock();
        root.blockID = cid++;
        root.content = null;

        for(String in:inputSchema) {
            if(in.equals("")) {
                signalSchema.addElement(0);
                blocks.addElement(null);
            } else {
                int blockLevel = ((in.length() - in.trim().length())/4)+1;
                signalSchema.addElement(blockLevel);
                LinkedBlock lb = new LinkedBlock();
                lb.blockID = cid++;
                lb.content = in.trim();
                blocks.addElement(lb);
            }
        }

        for(int i=0;i<signalSchema.size();i++) {
            int signal = signalSchema.elementAt(i);
            if(signal == 0) continue;
            LinkedBlock block = blocks.elementAt(i);
            if(signal == 1) {
                root.children.addElement(block);
            } else {
                for(int j=i;j>=0;j--) {
                    int topSignal = signalSchema.elementAt(j);
                    if(topSignal == (signal - 1)) {
                        LinkedBlock topBlock = blocks.elementAt(j);
                        topBlock.children.addElement(block);
                        break;
                    }
                }
            }
        }

        return root;
    }

}
