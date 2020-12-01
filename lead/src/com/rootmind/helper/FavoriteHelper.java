package com.rootmind.helper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.FavoriteWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class FavoriteHelper extends Helper {

	
	public AbstractWrapper insertFavorite(UsersWrapper usersProfileWrapper, FavoriteWrapper favoriteWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		PreparedStatement pstmt = null;

		try {

				FavoriteWrapper favWrapper = (FavoriteWrapper)fetchFavorite(favoriteWrapper);

				con = getConnection();

				if (favWrapper.recordFound == false) 
				{


					sql = " INSERT INTO Favorite(FavoriteRefNo, FavoriteID, RiderRefNo, RiderID, Favorite, MakerID, MakerDateTime) "
							+ " Values (?,?,?,?,?,?,?)";

					System.out.println("sql " + sql);

					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, Utility.trim(favoriteWrapper.favoriteRefNo));
					pstmt.setString(2, Utility.trim(favoriteWrapper.favoriteID));
					pstmt.setString(3, Utility.trim(favoriteWrapper.riderRefNo));
					pstmt.setString(4, Utility.trim(favoriteWrapper.riderID));
					pstmt.setString(5, Utility.trim(favoriteWrapper.favorite));
					pstmt.setString(6, Utility.trim(favoriteWrapper.riderID));
					pstmt.setTimestamp(7, Utility.getCurrentTime()); // date time

					pstmt.executeUpdate();
					pstmt.close();
					

					favoriteWrapper.recordFound = true;

					dataArrayWrapper.favoriteWrapper = new FavoriteWrapper[1];
					dataArrayWrapper.favoriteWrapper[0] = favoriteWrapper;

					dataArrayWrapper.recordFound = true;

					System.out.println("Successfully inserted into insertFavorite");

				}
				else
				{
					sql = " UPDATE Favorite SET  Favorite=?,  ModifierID=?, ModifierDateTime=? "
							+ " WHERE FavoriteRefNo=? AND FavoriteID=? AND RiderRefNo=? AND RiderID=?";

					System.out.println("sql " + sql);

					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, Utility.trim(favoriteWrapper.favorite));
					pstmt.setString(2, Utility.trim(favoriteWrapper.riderID));
					pstmt.setTimestamp(3, Utility.getCurrentTime()); // date time
					pstmt.setString(4, Utility.trim(favoriteWrapper.favoriteRefNo));
					pstmt.setString(5, Utility.trim(favoriteWrapper.favoriteID));
					pstmt.setString(6, Utility.trim(favoriteWrapper.riderRefNo));
					pstmt.setString(7, Utility.trim(favoriteWrapper.riderID));

					pstmt.executeUpdate();
					pstmt.close();

					
					favoriteWrapper.recordFound = true;

					dataArrayWrapper.favoriteWrapper = new FavoriteWrapper[1];
					dataArrayWrapper.favoriteWrapper[0] = favoriteWrapper;

					dataArrayWrapper.recordFound = true;

					System.out.println("Successfully updated into insertFavorite");
					
				}
				
			

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new NamingException(ne.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}

	// -----------------End insertFavorite---------------------
	
	
	// -----------------Start fetchFavorite---------------------

		public AbstractWrapper fetchFavorite(FavoriteWrapper favoriteWrapper) throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			// DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();

			// Vector<Object> vector = new Vector<Object>();
			String sql = null;

			try {
				//PopoverHelper popoverHelper = new PopoverHelper();

				con = getConnection();

				sql = "SELECT FavoriteRefNo, FavoriteID, RiderRefNo, RiderID, Favorite, Rating "
						+ " FROM Favorite WHERE FavoriteRefNo=? AND FavoriteID=? AND RiderRefNo=? AND RiderID=?";

				PreparedStatement pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(favoriteWrapper.favoriteRefNo));
				pstmt.setString(2, Utility.trim(favoriteWrapper.favoriteID));
				pstmt.setString(3, Utility.trim(favoriteWrapper.riderRefNo));
				pstmt.setString(4, Utility.trim(favoriteWrapper.riderID));

				resultSet = pstmt.executeQuery();

				if (resultSet.next()) {

					favoriteWrapper = new FavoriteWrapper();

					favoriteWrapper.favoriteRefNo = Utility.trim(resultSet.getString("FavoriteRefNo"));
					favoriteWrapper.favoriteID = Utility.trim(resultSet.getString("FavoriteID"));
					favoriteWrapper.riderRefNo = Utility.trim(resultSet.getString("RiderRefNo"));
					favoriteWrapper.riderID = Utility.trim(resultSet.getString("RiderID"));
					favoriteWrapper.favorite = Utility.trim(resultSet.getString("Favorite"));
					System.out.println("rating  resultset " + resultSet.getFloat("Rating"));
					favoriteWrapper.rating = round(resultSet.getFloat("Rating"),1);
					

					favoriteWrapper.recordFound = true;

					System.out.println("fetchFavorite  successful");

				}

				if (resultSet != null)
					resultSet.close();
				pstmt.close();

			} catch (SQLException se) {

				se.printStackTrace();
				throw new SQLException(se.getSQLState() + " ; " + se.getMessage());

			} catch (NamingException ne) {

				ne.printStackTrace();
				throw new NamingException(ne.getMessage());
			} catch (Exception ex) {

				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}

			finally {
				try {
					releaseConnection(resultSet, con);
				} catch (SQLException se) {
					se.printStackTrace();
					throw new Exception(se.getSQLState() + " ; " + se.getMessage());
				}
			}

			return favoriteWrapper;
		}
		// -----------------End fetchDetails---------------------
		
		public static float round(float number, int decimalPlace) {
			BigDecimal bd = new BigDecimal(number);
			bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			return bd.floatValue();
		}
		
	//------start of update Avg rating in Driver Table
	public AbstractWrapper updateAvgRating(FavoriteWrapper favoriteWrapper)
				throws Exception {

			Connection con = null;
			ResultSet resultSet = null;

			//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql = null;

			PreparedStatement pstmt = null;

			try {

					con = getConnection();
					
					sql = "SELECT SUM(Rating)/COUNT(Rating) as AvgRating"
							+ " FROM Favorite WHERE FavoriteRefNo=? AND FavoriteID=?";

					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, Utility.trim(favoriteWrapper.favoriteRefNo));
					pstmt.setString(2, Utility.trim(favoriteWrapper.favoriteID));

					resultSet = pstmt.executeQuery();

					if (resultSet.next()) {

						favoriteWrapper.avgRating = round(resultSet.getFloat("AvgRating"),1);
						
						System.out.println("fetchFavorite  successful");

					}

					if (resultSet != null)
						resultSet.close();
					pstmt.close();

					
					pstmt = con.prepareStatement("UPDATE Rider SET Rating=?  "
							+ " WHERE RiderRefNo=? AND RiderID=?");

					pstmt.setFloat(1, favoriteWrapper.avgRating);
					pstmt.setString(2, Utility.trim(favoriteWrapper.favoriteRefNo));
					pstmt.setString(3, Utility.trim(favoriteWrapper.favoriteID));

					pstmt.executeUpdate();
					pstmt.close();
					
				

			} catch (SQLException se) {
				se.printStackTrace();
				throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
			} catch (NamingException ne) {
				ne.printStackTrace();
				throw new NamingException(ne.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			} finally {
				try {
					releaseConnection(resultSet, con);
				} catch (SQLException se) {
					se.printStackTrace();
					throw new Exception(se.getSQLState() + " ; " + se.getMessage());
				}
			}

			return favoriteWrapper;
		}

		// -----------------End updateRating---------------------
	
	//---------insert Rating
	public AbstractWrapper insertRating(UsersWrapper usersProfileWrapper, FavoriteWrapper favoriteWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		PreparedStatement pstmt = null;

		try {

				FavoriteWrapper favWrapper = (FavoriteWrapper)fetchFavorite(favoriteWrapper);

				con = getConnection();

				if (favWrapper.recordFound == false) 
				{


					sql = " INSERT INTO Favorite(FavoriteRefNo, FavoriteID, RiderRefNo, RiderID, Rating, MakerID, MakerDateTime) "
							+ " Values (?,?,?,?,?,?,?)";

					System.out.println("sql " + sql);

					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, Utility.trim(favoriteWrapper.favoriteRefNo));
					pstmt.setString(2, Utility.trim(favoriteWrapper.favoriteID));
					pstmt.setString(3, Utility.trim(favoriteWrapper.riderRefNo));
					pstmt.setString(4, Utility.trim(favoriteWrapper.riderID));
					pstmt.setFloat(5, favoriteWrapper.rating);
					pstmt.setString(6, Utility.trim(favoriteWrapper.riderID));
					pstmt.setTimestamp(7, Utility.getCurrentTime()); // date time

					pstmt.executeUpdate();
					pstmt.close();
					
					
					//update avg rating in Driver table;
					favoriteWrapper = (FavoriteWrapper)updateAvgRating(favoriteWrapper);

					favoriteWrapper.recordFound = true;

					dataArrayWrapper.favoriteWrapper = new FavoriteWrapper[1];
					dataArrayWrapper.favoriteWrapper[0] = favoriteWrapper;

					dataArrayWrapper.recordFound = true;

					System.out.println("Successfully inserted into insertRating");

				}
				else
				{
					sql = " UPDATE Favorite SET  Rating=?, ModifierID=?, ModifierDateTime=? "
							+ " WHERE FavoriteRefNo=? AND FavoriteID=? AND RiderRefNo=? AND RiderID=?";

					System.out.println("sql " + sql);

					pstmt = con.prepareStatement(sql);
					pstmt.setFloat(1, favoriteWrapper.rating);
					pstmt.setString(2, Utility.trim(favoriteWrapper.riderID));
					pstmt.setTimestamp(3, Utility.getCurrentTime()); // date time
					pstmt.setString(4, Utility.trim(favoriteWrapper.favoriteRefNo));
					pstmt.setString(5, Utility.trim(favoriteWrapper.favoriteID));
					pstmt.setString(6, Utility.trim(favoriteWrapper.riderRefNo));
					pstmt.setString(7, Utility.trim(favoriteWrapper.riderID));

					pstmt.executeUpdate();
					pstmt.close();

					//update avg rating in Driver table;
					favoriteWrapper = (FavoriteWrapper)updateAvgRating(favoriteWrapper);

					
					favoriteWrapper.recordFound = true;

					dataArrayWrapper.favoriteWrapper = new FavoriteWrapper[1];
					dataArrayWrapper.favoriteWrapper[0] = favoriteWrapper;

					dataArrayWrapper.recordFound = true;

					System.out.println("Successfully updated into insertRating");
					
				}
				
			

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new NamingException(ne.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}

	// -----------------End insertRating---------------------
}
