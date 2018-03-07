import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
	private Broadcaster bc;
	private static String regexpattern = "(\\$GPRMC)\\,(\\d{6}\\.?\\d*)\\,([AV]{1})\\,(\\d*\\.?\\d*)\\,([NS]{1})\\,(\\d*\\.?\\d*)\\,([EW]{1})\\,([0-9]*\\.?[0-9]*)\\,([0-9]*\\.?[0-9]*)\\,([0-9]{6})\\,([0-9]*\\.?[0-9]*)\\,([EW]?)\\,([ADEMSN]{1}.*.[0-9A-F]{2})";
	private Pattern p = Pattern.compile(regexpattern);
	private Matcher m;
	private String imei;
	
	private LocalDateTime dt;
	private DateTimeFormatter dateparser = DateTimeFormatter.ofPattern("ddMMYY,HHmmss");
	 
	public Controller (String updateurl) {
		bc = new Broadcaster(updateurl);
	}
	
	public String processStreamResult(String gpsdata) {
		if (gpsdata.length() > 2) {
			try {
				if (gpsdata.substring(0,1).equalsIgnoreCase("$")) {
					gpsdata = gpsdata.substring(1,gpsdata.length()-1);
				}
				String[] dataparts = gpsdata.split("\\$");
				String nmeaData = "$" + dataparts[0];
				m = p.matcher(nmeaData);
				if (m.matches() == true) {
					dt = LocalDateTime.parse(m.group(10) + "," + m.group(2).substring(0, 5),dateparser);
					long unixtime = dt.atZone(ZoneId.systemDefault()).toEpochSecond();
					if (dataparts.length == 2) {
						imei = dataparts[1];
					} else {
						imei = "";
					}
					return triggerStorageProcess(nmeaData,unixtime,imei);
				} else {
					return "gps-data is not a valid GPRMC sentence";
				}
			} catch (Exception ex) {
				return "An error occured" + ex.toString();
			}
		} else {
			return "asfd";
		}
	}

	private String triggerStorageProcess(String nmeaData, long unixtime, String imei) {
	    return bc.broadcastLocation(nmeaData, imei, unixtime);
	}

}
