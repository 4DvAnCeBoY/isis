package org.nakedobjects.object.reflect.internal;

import org.nakedobjects.object.InternalCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.Persistable;
import org.nakedobjects.object.defaults.AbstractNakedReference;
import org.nakedobjects.utility.ToString;

import java.util.Enumeration;


public class ArrayAdapter extends AbstractNakedReference implements InternalCollection {
    private Object[] array;
    private NakedObjectSpecification elementSpecification;

    public ArrayAdapter(Object[] array, NakedObjectSpecification spec) {
        this.array = array;
        elementSpecification = spec;
    }

    private static NakedObject adapter(Object element) {
        return NakedObjects.getObjectLoader().getAdapterForElseCreateAdapterForTransient(element);
    }

    public boolean contains(NakedObject object) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == object) {
                return true;
            }
        }
        return false;
    }

    public void destroyed() {}

    public NakedObject elementAt(int index) {
        Object element = array[index];
        return (NakedObject) adapter(element);
    }

    public Enumeration elements() {
        return new Enumeration() {
            int count = 0;

            public boolean hasMoreElements() {
                return count < array.length;
            }

            public Object nextElement() {
                return adapter(array[count++]);
            }
        };
    }

    public NakedObjectSpecification getElementSpecification() {
        return elementSpecification;
    }
 
    public Object getObject() {
        return array;
    }

    public boolean isAggregated() {
        return false;
    }

    public void init(Object[] elements) {
        int size = elements.length;
        array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = elements[i];
        }
    }
    
    public NakedObject parent() {
        return null;
    }

    public Persistable persistable() {
        return Persistable.TRANSIENT;
    }
    
    public int size() {
        return array.length;
    }

    public String titleString() {
        return "Vector";
    }

    public String toString() {
        ToString s = new ToString(this);
        toString(s);

        // title
        String title;
        try {
            title = "'" + this.titleString() + "'";
        } catch (NullPointerException e) {
            title = "none";
        }
        s.append("title", title);

        s.append("array", array);

        return s.toString();
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business objects directly to the user.
 * Copyright (C) 2000 - 2005 Naked Objects Group Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address of Naked Objects Group is
 * Kingsway House, 123 Goldworth Road, Woking GU21 1NR, UK).
 */