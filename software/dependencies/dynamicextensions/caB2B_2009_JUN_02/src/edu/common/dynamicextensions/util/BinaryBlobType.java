/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.*;
import oracle.sql.BLOB;

import org.jboss.resource.adapter.jdbc.WrappedPreparedStatement;
import org.postgresql.PGStatement;

/**
 * @author rahul_ner
 *
 */
public class BinaryBlobType implements UserType
{

	/**
	 * @see org.hibernate.UserType#sqlTypes()
	 */
	public int[] sqlTypes()
	{
		return new int[]{Types.BLOB};
	}

	/**
	 * @see org.hibernate.UserType#returnedClass()
	 */
	public Class returnedClass()
	{
		return byte[].class;
	}

	/**
	 * @see org.hibernate.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(Object x, Object y)
	{
		return (x == y)
				|| (x != null && y != null && java.util.Arrays.equals((byte[]) x, (byte[]) y));
	}

	/**
	 * @see org.hibernate.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException
	{
		Statement st = rs.getStatement();
	
		if (st instanceof WrappedPreparedStatement) {
			WrappedPreparedStatement wrappedPreparedStatement = (WrappedPreparedStatement) st;
			st = wrappedPreparedStatement.getUnderlyingStatement();
		}
		
		//This is for PostgreSQL
		if (st instanceof PGStatement)
		{
			return rs.getBytes(names[0]);
		}

		//Other databases
		Blob blob = rs.getBlob(names[0]);
		if (blob != null)
		{
			return blob.getBytes(1, (int) blob.length());
		}
		else
		{
			return null;
		}
	}

	/**
	 * @see org.hibernate.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException
	{
		
		if (value == null)
		{
			st.setNull(index, Types.BINARY);
			return;
		}
		
		Statement statement = null;

		if (st instanceof WrappedPreparedStatement) {
			WrappedPreparedStatement wrappedPreparedStatement = (WrappedPreparedStatement) st;
			statement = wrappedPreparedStatement.getUnderlyingStatement();
		} else {
			statement = st;
		}

		// Oracle gives problems - so check for it
		if (statement instanceof oracle.jdbc.OraclePreparedStatement)
		{

			BLOB blob = BLOB.createTemporary(((oracle.jdbc.OraclePreparedStatement) statement)
					.getConnection(), false, BLOB.DURATION_SESSION);

			blob.open(BLOB.MODE_READWRITE);

			OutputStream out = blob.getBinaryOutputStream();

			try
			{
				out.write((byte[]) value);
				out.flush();
				out.close();
			}
			catch (IOException e)
			{
				throw new SQLException("failed write to blob" + e.getMessage());
			}

			blob.close();

			((oracle.jdbc.OraclePreparedStatement) statement).setBLOB(index, blob);
		}
		else if (statement instanceof PGStatement)
		{
			//for PostgreSQL database
			st.setBytes(index, (byte[]) value);
		}
		else
		{
			st.setBlob(index, Hibernate.createBlob((byte[]) value));
		}
	}

	/**
	 * @see org.hibernate.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy(Object value)
	{
		if (value == null)
			return null;

		byte[] bytes = (byte[]) value;
		byte[] result = new byte[bytes.length];
		System.arraycopy(bytes, 0, result, 0, bytes.length);

		return result;
	}

	/**
	 * @see org.hibernate.UserType#isMutable()
	 */
	public boolean isMutable()
	{
		return true;
	}

	/**
	 * @param x
	 * @return
	 * @throws HibernateException
	 */
	public int hashCode(Object x) throws HibernateException
	{
		return x.hashCode();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws HibernateException
	 */
	public Serializable disassemble(Object arg0) throws HibernateException
	{
		return (String) arg0;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws HibernateException
	 */
	public Object assemble(Serializable arg0, Object arg1) throws HibernateException
	{
		return this.deepCopy(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws HibernateException
	 */
	public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException
	{
		return this.deepCopy(arg0);
	}
}