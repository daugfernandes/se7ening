/*
 *
 *     Copyright (C) 2010  David Fernandes
 *
 *                         Rua da Quinta Amarela, 60
 *                         4475-663 MAIA
 *                         PORTUGAL
 *
 *                         <azserrata@gmail.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package se7ening.structure;

import java.sql.*;

/**
 *
 * Main class of the structure. All classes that represent
 * objects store in database inherit from this one.
 *
 */
public class CManage
{
    private int _id;
    private String _name;
    /**
     *  chan
     */
    protected boolean _changed;
    protected boolean _new;
    protected boolean _deleted;

    /**
     * Constructor
     */
    public CManage()
    {
        _id=0;
        _name="";
        _changed=true;
        _new=true;
        _deleted=false;
    }
    /**
     * Constructor with parameters
     */

    public CManage(int id, String name)
    {
        _id=id;
        _name=name;
        _changed=true;
        _new=true;
        _deleted=false;
    }

    /**
     * Getter for name.
     */
    public String getName() { return(_name); }
    /**
     * Setter for name.
     */
    public void setName(String newName)
    {
        if(!_name.equals(newName))
            _changed=true;

        this._name=newName;
    }

    public int getId() { return(_id); }

    public boolean hasChanged() { return(_changed); }
    public boolean isNew() { return(_new); }
    public boolean isDeleted() {  return(_deleted); }

    /**
     * Inserts or Updates 'this' record.
     *    on INSERT returns new ID (autonumber);
     *    on Update returns number os rows affected (see MySQL function for more info)
     */
    public int Commit(Connection conn) throws Exception
    {
        _new=false;
        _changed=false;
        return(0);
    }

    /**
     * Deletes 'this' record.
     *      Can be "recovered" by simply using Commit function; you should expect a new ID however.
     *      For that matter test for hasChanghed AND isDeleted.
     */
    public int Delete(Connection conn) throws Exception
    {
        _deleted=true;
        _changed=true;
        _new=true;
        return(0);
    }

    @Override
    protected void finalize() throws Throwable
    {
        
    }
}

