package com.codebelief.app.DatabaseConnection;

import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 
 * @ClassName: MySQLDatabaseConnection
 * @Description: Connecting with Database
 * @author 何涛
 * @date 2017年10月13日
 *
 */
public class MySQLDatabaseConnection implements DatabaseConnection{
	
	private static String host = null;
	private static String port = null;
	private static String database = null;
	private static String userName = null;
	private static String password = null;
	private Connection conn = null;
	private String url = null;
	
	public MySQLDatabaseConnection() throws IOException {
		Properties prop = new Properties();
		FileInputStream file = null;
		try {
			//String path = Path.Combine();
			file = new FileInputStream("C://Users/何涛/Desktop/作业/软件工程/项目/webpage-update-subscribe/module-dao/src/main/resources/db.properties");
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		prop.load(file);
		
		host = prop.getProperty("host");
		port = prop.getProperty("port");
		database = prop.getProperty("database");
		userName = prop.getProperty("username");
		password = prop.getProperty("password");
		url="jdbc:mysql://"+host+":"+port+"/"+database+"?characterEncoding=utf-8";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection(url,userName,password);
		} catch (SQLException e) {
			System.out.print("Connecting Action failed!");
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * @Title: getConnection
	 * @Description: return the Connection to the database
	 * @return Connection
	 */
	public Connection getConnection() {
		return conn;
	}

	@Override
	/**
	 * @Title: free
	 * @Description: Close the connection to the database
	 */
	public void free() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.print("Close failed!");
			e.printStackTrace();
		}
	}

	/**
	 * @Title: initialDatabaseDeploy
	 * @Description: Initiate the database configuration.
	 * 				 If the needed configuration file, 
	 * 				 db.properties, is not exist under the path of ${user.home}/.wus/db.properties,
	 * 				 then build it under this path.
	 * 				 If the contents contained in needed configuration file are not complete,
	 * 				 then add the default settings into the configuration file.
	 * 				 Else use the file that developers has builded directly.
	 */
	public static void initialDatabaseDeploy() {
		String home = System.getProperty("user.home");
		String fileSeparator = System.getProperty("file.separator");
		String path = home + fileSeparator+".wus"+fileSeparator+"db.properties";
		File file = new File(path);
		if(!file.exists()){
			try {
				createDefaultDbProperties(path,"root","musql","localhost","3306","musql");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				readDbProperties(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @Title: createDefaultDbProperties
	 * @Description: Create the configuration file if this file is not exist
	 * 				 using the given arguments.
	 * @param path
	 * @param username
	 * @param password
	 * @param host
	 * @param port
	 * @param database
	 * @throws IOException
	 */
	private static void createDefaultDbProperties(String path, 
			String username, String password, String host, String port, String database) throws IOException{
		File file = new File(path);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		
		FileWriter fw = new FileWriter(file);
		BufferedWriter write = new BufferedWriter(fw);
		write.write("username="+username);
		write.newLine();
		write.write("password="+password);
		write.newLine();
		write.write("host="+host);
		write.newLine();
		write.write("database="+database);
		write.newLine();
		write.write("port="+port);
		write.flush();
		
		write.close();
		fw.close();
	}
	
	/**
	 * 
	 * @Title: readDbProperties
	 * @Description: Supplement the missing information in the configuration file.
	 * @param path
	 * @throws IOException
	 */
	private static void readDbProperties(String path) throws IOException{
		Properties prop = new Properties();
		FileInputStream file = null;
		try {
			file = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		try {
			prop.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String newUserName = prop.getProperty("username");//"root";
		String newPassword = prop.getProperty("password");//"mysql";
		String newHost = prop.getProperty("host");//"localhost";
		String newPort = prop.getProperty("port");//"3306";
		String newDatabase = prop.getProperty("database");//"mysql";
		if(isValid(newUserName)
				&& isValid(newPassword) 
				&& isValid(newHost) 
				&& isValid(newPort)
				&& isValid(newDatabase))
			return;
		
		if(! isValid(newUserName))
			newUserName = "root";
		if(! isValid(newPassword))
			newPassword = "mysql";
		if(! isValid(newHost))
			newHost = "localhost";
		if(! isValid(newPort))
			newPort = "3306";
		if(! isValid(newDatabase))
			newDatabase = "mysql";
		createDefaultDbProperties(path, newUserName, newPassword, newHost, newPort, newDatabase);
	}
	
	private static boolean isValid(String object){
		return object != null && !"".equals(object);
	}
}
