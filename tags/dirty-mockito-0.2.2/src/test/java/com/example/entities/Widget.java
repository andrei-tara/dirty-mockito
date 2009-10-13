/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Oct 12, 2009
 */
package com.example.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Alistair A. Israel
 */
@Entity
@NamedQueries({ @NamedQuery(name = Widget.FIND_BY_ID, query = "SELECT w FROM Widget w WHERE w.id = :id") })
public class Widget implements Serializable {

    private static final long serialVersionUID = 4404034370024717523L;

    /**
     * {@value Widget#FIND_BY_ID}
     */
    public static final String FIND_BY_ID = "Widget.findById";

    private Long id;

    private String name;

    /**
     * @return the id
     */
    public final Long getId() {
        return id;
    }

    /**
     * @param id
     *        the id to set
     */
    public final void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name
     *        the name to set
     */
    public final void setName(final String name) {
        this.name = name;
    }

}
