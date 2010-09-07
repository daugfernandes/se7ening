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

package structure;

import java.sql.*;

/**
 *
 * @author david
 */
public class CSolution extends CManage
{
    CSolution()
    {
        super();
    }

    CSolution(int id, String name)
    {
        super(id,name);
    }

    CSolution(int id, Connection conn) throws Exception
    {
        super(id,"");

        String simpleProc = "SELECT NAME FROM SOLUTION WHERE ID=?";
        PreparedStatement ps = conn.prepareStatement(simpleProc);

        ps.setInt(1, id);
        ResultSet res=ps.executeQuery( );

        if(res.next())
            super.setName(res.getString(1));

    }

    @Override protected int Commit(Connection conn) throws Exception
    {
        // there is no point commiting a not changed object
        if(hasChanged())
        {
            String simpleProc;
            CallableStatement cs;

            if(isNew()) // insert
                simpleProc = "{ ? = call SOLUTION_ADD(?,?) }";
            else // update
                simpleProc = "{ ? = call SOLUTION_UPD(?,?) }";

            cs = conn.prepareCall(simpleProc);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, getId());
            cs.setString(3, getName());
            cs.execute();

            super.Commit(conn);

            return(cs.getInt(1));
        }

        return(0);

    }

    /* Deletes 'this' record.
     *      Can be "recovered" by simply using Commit function; you should expect a new ID however.
     *      For that matter test for hasChanghed AND isDeleted.
     */
    @Override int Delete(Connection conn) throws Exception
    {
        String simpleProc = "{ ? = call SOLUTION_DEL(?) }";
        CallableStatement cs = conn.prepareCall(simpleProc);
        cs.registerOutParameter(1, Types.INTEGER);
        cs.setInt(2, getId());
        cs.execute();

        super.Delete(conn);

        return(cs.getInt(1));
    }
}

