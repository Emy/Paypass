package me.Miny.Paypassage.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.Sign.InvalidSignLocationException;
import me.Miny.Paypassage.Sign.PPSign;
import me.Miny.Paypassage.Sign.PPSignStats;
import me.Miny.Paypassage.Sign.PPTeleport;
import me.Miny.Paypassage.logger.LoggerUtility;

import org.bukkit.Location;

/**
 * 
 * @author ibhh
 */
public class DatabaseUtility {

	private Paypassage plugin;
	private DatabaseConnector connector;

	public DatabaseUtility(Paypassage plugin) {
		this.plugin = plugin;
		connector = new DatabaseConnector(plugin, plugin.getConfig().getBoolean("use_MySQL"));
		connector.createConnection();
	}

	public void closeConnection() {
		connector.CloseCon();
	}
	
	public void deleteSign(final Location loc) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				long time = 0;
				plugin.getLoggerUtility().log("Insert sign into table!", LoggerUtility.Level.DEBUG);
				time = System.nanoTime();
				Statement st = null;
				try {
					st = connector.getConnection().createStatement();
				} catch (SQLException e) {
					DatabaseTools.SQLErrorHandler(plugin, e);
					return;
				}
				try {
					String sql = "DELETE from PaypassageSigns WHERE " + "Sign_X=" + loc.getX() + " AND Sign_Y=" + loc.getY() + " AND Sign_Z=" + loc.getZ();
					try {
						st.executeUpdate(sql);
					} catch (SQLException e1) {
						DatabaseTools.SQLErrorHandler(plugin, e1);
						return;
					}
					connector.getConnection().commit();
					st.close();
				} catch (SQLException e) {
					plugin.getLoggerUtility().log("Error while deleting Sign from DB! - " + e.getMessage(), LoggerUtility.Level.DEBUG);
				}
				time = (System.nanoTime() - time) / 1000000;
				plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
			}
		});
	}

	public void insertNewSign(final PPSign sign) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				long time = 0;
				plugin.getLoggerUtility().log("Insert sign into table!", LoggerUtility.Level.DEBUG);
				time = System.nanoTime();
				try {
					PreparedStatement ps = connector.getConnection().prepareStatement("INSERT INTO PaypassageSigns (" + "Name," + "World," + "Owner," + "Price," + "Sign_X," + "Sign_Y," + "Sign_Z," + "Destination_X," + "Destination_Y," + "Destination_Z) VALUES (?,?,?,?,?,?,?,?,?,?)");
					ps.setString(1, sign.getName());
					ps.setString(2, sign.getDestination().getWorld().getName());
					ps.setString(3, sign.getOwner());
					ps.setDouble(4, sign.getPrice());
					ps.setInt(5, sign.getSignLocation().getBlockX());
					ps.setInt(6, sign.getSignLocation().getBlockY());
					ps.setInt(7, sign.getSignLocation().getBlockZ());
					ps.setInt(8, sign.getDestination().getBlockX());
					ps.setInt(9, sign.getDestination().getBlockY());
					ps.setInt(10, sign.getDestination().getBlockZ());
					ps.execute();
					connector.getConnection().commit();
					ps.close();
				} catch (SQLException e) {
					plugin.getLoggerUtility().log("Error while inserting Sign into DB! - " + e.getMessage(), LoggerUtility.Level.DEBUG);
					plugin.getLoggerUtility().log(plugin.getServer().getPlayer(sign.getOwner()), e.getMessage(), LoggerUtility.Level.ERROR);
				}
				time = (System.nanoTime() - time) / 1000000;
				plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
			}
		});
	}

	public void PrepareDB() throws SQLException {
		Statement st = null;
		long time = 0;
		plugin.getLoggerUtility().log("Creaeting table!", LoggerUtility.Level.DEBUG);
		time = System.nanoTime();
		st = connector.getConnection().createStatement();
		if (plugin.getConfig().getBoolean("use_MySQL")) {
			st.executeUpdate("CREATE TABLE IF NOT EXISTS PaypassageSigns (ID int AUTO_INCREMENT, PRIMARY KEY(ID)," + "Name VARCHAR(100) NOT NULL, World VARCHAR(100) NOT NULL, " + "Owner VARCHAR(30), " + "Price DOUBLE, " + "Sign_X int, " + "Sign_Y int, " + "Sign_Z int, " + "Destination_X int, " + "Destination_Y int, " + "Destination_Z int);");
			plugin.getLoggerUtility().log("Table created!", LoggerUtility.Level.DEBUG);
		} else {
			st.executeUpdate("CREATE TABLE IF NOT EXISTS PaypassageSigns (ID INTEGER PRIMARY KEY AUTOINCREMENT," + "Name VARCHAR NOT NULL, " + "World VARCHAR NOT NULL, " + "Owner VARCHAR, " + "Price DOUBLE, " + "Sign_X INTEGER, " + "Sign_Y INTEGER, " + "Sign_Z INTEGER, " + "Destination_X INTEGER, " + "Destination_Y INTEGER, " + "Destination_Z INTEGER);");
			plugin.getLoggerUtility().log("Table created!", LoggerUtility.Level.DEBUG);
		}
		connector.getConnection().commit();
		st.close();
		time = (System.nanoTime() - time) / 1000000;
		plugin.getLoggerUtility().log("Created in " + time + " ms!", LoggerUtility.Level.DEBUG);
		// UpdateDB();
	}
	
	public void PrepareStatsDB() throws SQLException {
		Statement st = null;
		long time = 0;
		plugin.getLoggerUtility().log("Creaeting table!", LoggerUtility.Level.DEBUG);
		time = System.nanoTime();
		st = connector.getConnection().createStatement();
		if (plugin.getConfig().getBoolean("use_MySQL")) {
			st.executeUpdate("CREATE TABLE IF NOT EXISTS PaypassageStats (ID int AUTO_INCREMENT, PRIMARY KEY(ID), SignID int, Time LONG, Player VARCHAR(30), Owner VARCHAR(30), Price DOUBLE);");
			plugin.getLoggerUtility().log("Table created!", LoggerUtility.Level.DEBUG);
		} else {
			st.executeUpdate("CREATE TABLE IF NOT EXISTS PaypassageStats (ID INTEGER PRIMARY KEY AUTOINCREMENT, SignID INTEGER, Time LONG, Player VARCHAR, Owner VARCHAR, Price DOUBLE);");
			plugin.getLoggerUtility().log("Table created!", LoggerUtility.Level.DEBUG);
		}
		connector.getConnection().commit();
		st.close();
		time = (System.nanoTime() - time) / 1000000;
		plugin.getLoggerUtility().log("Created in " + time + " ms!", LoggerUtility.Level.DEBUG);
		// UpdateDB();
	}
	
	public void insertNewTeleport(final String player, final PPSign sign) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				long time = 0;
				plugin.getLoggerUtility().log("Insert teleport into table!", LoggerUtility.Level.DEBUG);
				time = System.nanoTime();
				try {
					PreparedStatement ps = connector.getConnection().prepareStatement("INSERT INTO PaypassageStats (SignID, Time, Player, Owner, Price) VALUES (?,?,?,?,?)");
					ps.setInt(1, getSignID(sign.getSignLocation()));
					ps.setLong(2, new java.util.Date().getTime());
					ps.setString(3, player);
					ps.setString(4, sign.getOwner());
					ps.setDouble(5, sign.getPrice());
					ps.execute();
					connector.getConnection().commit();
					ps.close();
				} catch (SQLException e) {
					plugin.getLoggerUtility().log("Error while inserting Sign into DB! - " + e.getMessage(), LoggerUtility.Level.DEBUG);
					plugin.getLoggerUtility().log(plugin.getServer().getPlayer(sign.getOwner()), e.getMessage(), LoggerUtility.Level.ERROR);
				} catch (InvalidSignLocationException e) {
					e.printStackTrace();
				}
				time = (System.nanoTime() - time) / 1000000;
				plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
			}
		});
	}

	public boolean isindb(final Location sign) throws SQLException {
		boolean a = false;
		long time = 0;
		plugin.getLoggerUtility().log("Checking if in table!", LoggerUtility.Level.DEBUG);
		time = System.nanoTime();
		Statement st = null;
		String sql;
		ResultSet result;
		try {
			st = connector.getConnection().createStatement();
		} catch (SQLException e) {
			DatabaseTools.SQLErrorHandler(plugin, e);
		}
		sql = "SELECT COUNT(Name) from PaypassageSigns WHERE " + "Sign_X=" + sign.getBlockX() + " AND Sign_Y=" + sign.getBlockY() + " AND Sign_Z=" + sign.getBlockZ();
		try {
			result = st.executeQuery(sql);
		} catch (SQLException e1) {
			DatabaseTools.SQLErrorHandler(plugin, e1);
			return false;
		}
		try {
			result.next();
			int b = result.getInt(1);
			plugin.getLoggerUtility().log("Lines: " + b, LoggerUtility.Level.DEBUG);
			if (b > 0) {
				a = true;
			}

			connector.getConnection().commit();
			result.close();
			st.close();
		} catch (SQLException e2) {
			DatabaseTools.SQLErrorHandler(plugin, e2);
		}
		time = (System.nanoTime() - time) / 1000000;
		plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
		return a;
	}

	public int getSignID(final Location sign) throws SQLException, InvalidSignLocationException{
		long time = 0;
		plugin.getLoggerUtility().log("getting Sign!", LoggerUtility.Level.DEBUG);
		time = System.nanoTime();
		Statement st = null;
		String sql;
		ResultSet result;
		try {
			st = connector.getConnection().createStatement();
		} catch (SQLException e) {
			DatabaseTools.SQLErrorHandler(plugin, e);
		}
		sql = "SELECT ID from PaypassageSigns WHERE " + "Sign_X=" + sign.getBlockX() + " AND Sign_Y=" + sign.getBlockY() + " AND Sign_Z=" + sign.getBlockZ();
		result = st.executeQuery(sql);
		int id = -1;
		try {
			while (result.next() == true) {
				id = result.getInt("ID");
			}
			connector.getConnection().commit();
			st.close();
			result.close();
		} catch (SQLException e2) {
			DatabaseTools.SQLErrorHandler(plugin, e2);
		}
		if(id == -1) {
			throw new InvalidSignLocationException("No sign registered at " + sign.getBlockX() + " " + sign.getBlockY() + " " + sign.getBlockZ());
		}
		time = (System.nanoTime() - time) / 1000000;
		plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
		return id;
	}

	/**
	 * Returns a PPSign from DB
	 * @param sign_loc
	 * @return PPSign
	 * @throws SQLException
	 */
	public PPSign getSign(final Location sign_loc) throws SQLException {
		long time = 0;
		plugin.getLoggerUtility().log("getting Sign!", LoggerUtility.Level.DEBUG);
		time = System.nanoTime();
		Statement st = null;
		String sql;
		ResultSet result;
		try {
			st = connector.getConnection().createStatement();
		} catch (SQLException e) {
			DatabaseTools.SQLErrorHandler(plugin, e);
		}
		sql = "SELECT * from PaypassageSigns WHERE " + "Sign_X=" + sign_loc.getBlockX() + " AND Sign_Y=" + sign_loc.getBlockY() + " AND Sign_Z=" + sign_loc.getBlockZ() + ";";
		result = st.executeQuery(sql);
		PPSign sign = null;
		try {
			while (result.next() == true) {
				sign = new PPSign(result.getString("Owner"));
				sign.setName(result.getString("Name"));
				sign.setDestination(new Location(plugin.getServer().getWorld(result.getString("World")), result.getInt("Destination_X"), result.getInt("Destination_Y"), result.getInt("Destination_Z")));
				sign.setPrice(result.getDouble("Price"));
				sign.setSignLocation(new Location(plugin.getServer().getWorld(result.getString("World")), result.getInt("Sign_X"), result.getInt("Sign_Y"), result.getInt("Sign_Z")));
			}
			connector.getConnection().commit();
			st.close();
			result.close();
		} catch (SQLException e2) {
			DatabaseTools.SQLErrorHandler(plugin, e2);
		}
		time = (System.nanoTime() - time) / 1000000;
		plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
		return sign;
	}
	
	/**
	 * Returns a PPSign from DB
	 * @param sign_loc
	 * @return PPSign
	 * @throws SQLException
	 */
	public PPSignStats getSignStatsByID(final int id) throws SQLException {
		long time = 0;
		plugin.getLoggerUtility().log("getting Sign!", LoggerUtility.Level.DEBUG);
		time = System.nanoTime();
		Statement st = null;
		String sql;
		ResultSet result;
		try {
			st = connector.getConnection().createStatement();
		} catch (SQLException e) {
			DatabaseTools.SQLErrorHandler(plugin, e);
		}
		sql = "SELECT * from PaypassageStats WHERE SignID=" + id + ";";
		result = st.executeQuery(sql);
		PPSignStats stats = new PPSignStats();
		try {
			while (result.next() == true) {
				stats.add(new PPTeleport(id, result.getLong("Time"), result.getString("Player"), result.getString("Owner"), result.getDouble("Price")));
			}
			connector.getConnection().commit();
			st.close();
			result.close();
		} catch (SQLException e2) {
			DatabaseTools.SQLErrorHandler(plugin, e2);
		}
		time = (System.nanoTime() - time) / 1000000;
		plugin.getLoggerUtility().log("Finished in " + time + " ms!", LoggerUtility.Level.DEBUG);
		return stats;
	}

}
