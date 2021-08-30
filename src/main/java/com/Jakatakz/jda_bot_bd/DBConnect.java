package com.Jakatakz.jda_bot_bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class DBConnect
{
	private static Connection connection = null;
	
	public static Connection dbConnect()
	{
		try
		{
			
	
			return connection;
		}
		catch (SQLTimeoutException ex)
		{
			ex.printStackTrace();
			return null;	//must return type of connection, null is fine here for now.
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	
	}
}
