/*
 *
 *     Copyright (C) 2010  David Fernandes
 *
 *                         Rua da Quinta Amarela, 60
 *                         4475-663 MAIA
 *                         PORTUGAL
 *
 *                         <daugfernandes@aim.com>
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
import java.util.Date;

/*

    structure::CIssue.java

 */
public class CIssue extends CManage
{
    private CIssuer _issuer;
    private Date _dateIssued;
    private int _status;
    private CManager _manager;

    CIssue()
    {
        super();
        initProperties();
    }

    CIssue(int id, String name)
    {
        super(id,name);
        initProperties();
    }

    /**
     * Inicialize own properties
     */
    private void initProperties()
    {
        setStatus(CIssueStatus.Open());
        setDateIssued(new Date());
        setIssuer(null);
        setManager(null);
    }

    CIssue(int id, Connection conn) throws Exception
    {
        super(id,"");

        String simpleProc = "SELECT NAME, ID_ISSUER, ID_MANAGER FROM ISSUE WHERE ID=?";
        PreparedStatement ps = conn.prepareStatement(simpleProc);

        ps.setInt(1, id);
        ResultSet res=ps.executeQuery();

        if(res.next())
        {
            super.setName(res.getString(1));
            setIssuer(new CIssuer());
            setManager(new CManager());
        }

    }

    public Date getDateIssued()
    {
        return _dateIssued;
    }
    
    public void setDateIssued(Date newDate)
    {
        _dateIssued=newDate;
    }

    public int getStatus()
    {
        return _status;
    }

    public void setStatus(int newStatus)
    {
        _status=newStatus;
    }

    public CIssuer getIssuer()
    {
        return _issuer;
    }

    public void setIssuer(CIssuer newIssuer)
    {
        _issuer=newIssuer;
    }

    public CManager getManager()
    {
        return _manager;
    }

    public void setManager(CManager newManager)
    {
        _manager=newManager;
    }

    @Override public int Commit(Connection conn) throws Exception
    {
        // there is no point commiting a not changed object
        if(hasChanged())
        {
            String simpleProc;
            CallableStatement cs;

            if(isNew()) // insert
                simpleProc = "{ ? = call ISSUE_ADD(?,?) }";
            else // update
                simpleProc = "{ ? = call ISSUE_UPD(?,?) }";

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
    @Override public int Delete(Connection conn) throws Exception
    {
        String simpleProc = "{ ? = call ISSUE_DEL(?) }";
        CallableStatement cs = conn.prepareCall(simpleProc);
        cs.registerOutParameter(1, Types.INTEGER);
        cs.setInt(2, getId());
        cs.execute();

        super.Delete(conn);

        return(cs.getInt(1));
    }
}

