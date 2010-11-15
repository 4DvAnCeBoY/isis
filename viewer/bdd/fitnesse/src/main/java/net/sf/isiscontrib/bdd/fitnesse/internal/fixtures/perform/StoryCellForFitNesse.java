package net.sf.isiscontrib.bdd.fitnesse.internal.fixtures.perform;

import org.apache.isis.viewer.bdd.common.StoryCell;

import fit.Parse;

public class StoryCellForFitNesse implements StoryCell {

    private final Parse source;

    public StoryCellForFitNesse(Parse source) {
        this.source = source;
    }

    @Override
    public String getText() {
        return source.text();
    }

    /**
     * The implementation-specific representation of this text.
     * 
     * <p>
     * Holds a Fit {@link Parse} object.
     */
    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public void setText(String str) {
        source.body = str;
    }

}