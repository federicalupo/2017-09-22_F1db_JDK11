package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.formulaone.model.Arco;
import it.polito.tdp.formulaone.model.Pilota;
import it.polito.tdp.formulaone.model.Race;
import it.polito.tdp.formulaone.model.Season;

public class FormulaOneDAO {

	public List<Season> getAllSeasons() {
		String sql = "SELECT year, url FROM seasons ORDER BY year";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Season> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Season(rs.getInt("year"), rs.getString("url")));
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Race> getVertici(Season stagione, Map<Integer, Race> idMap){
		String sql = "select  * " + 
				"from races " + 
				"where year=? "+
				"order by name";
		
		List<Race> vertici = new ArrayList<Race>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, stagione.getYear());
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Race r;
				
				if(rs.getTime("time")==null) {
					 r = new Race(rs.getInt("raceId"), Year.of(rs.getInt("year")), rs.getInt("round"),
							rs.getInt("circuitId"), rs.getString("name"), rs.getDate("date").toLocalDate(),
								null, rs.getString("url"));
					
				}else {
					 r = new Race(rs.getInt("raceId"), Year.of(rs.getInt("year")), rs.getInt("round"),
					rs.getInt("circuitId"), rs.getString("name"), rs.getDate("date").toLocalDate(),
						rs.getTime("time").toLocalTime(), rs.getString("url"));
				}
				
				idMap.put(r.getRaceId(), r);
				vertici.add(r);
						
						
			}
			conn.close();
			return vertici;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(Map<Integer, Race> idMap){
		String sql = "select r1.`raceId` id1, r2.raceId id2, count(distinct r1.driverid) as piloti " + 
				"from results r1, results r2 " + 
				"where r1.`raceId`<r2.`raceId` " + 
				"and r1.`statusId`= r2.statusId " + 
				"and r1.statusId=1 " + 
				"and r1.`driverId`=r2.driverId " + 
				"group by r1.`raceId`, r2.`raceId`";
		//controllo ci siano tra i vertici
		List<Arco> archi = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Race r1 = idMap.get(rs.getInt("id1"));
				Race r2 = idMap.get(rs.getInt("id2"));
				
				if(r1!=null && r2!=null) {
					Arco a = new Arco(r1,r2,rs.getInt("piloti"));
					archi.add(a);
				}
				
			}
			conn.close();
			return archi;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Pilota> piloti(Race r){
		String sql="select results.driverId, driverRef " + 
				"from results, drivers " + 
				"where raceId=? " + 
				"and results.`driverId` = drivers.`driverId` " + 
				"order by driverId ";
		List<Pilota> piloti = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r.getRaceId());
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				piloti.add(new Pilota(rs.getInt("driverId"), rs.getString("driverRef")));
			}
			conn.close();
			return piloti;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public Long getMillisecondi(Race gara, Pilota pilota, Integer nGiro) {
		String sql="select milliseconds " + 
				"from lapTimes " + 
				"where raceId=? " + 
				"and driverId=? " + 
				"and lap =? ";
		Long sec = null;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, gara.getRaceId());
			st.setInt(2, pilota.getDriverId());
			st.setInt(3, nGiro);
			
			ResultSet rs = st.executeQuery();
			
			if (rs.next()) {
				sec = Long.valueOf(rs.getInt("milliseconds")); 
			}
			
			conn.close();
			return sec;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}

