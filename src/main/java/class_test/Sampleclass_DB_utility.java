package class_test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public class Sampleclass_DB_utility {
	
	protected WebDriver driver;
	protected WebDriverWait wait;
	protected JavascriptExecutor js;
	protected int locationindx;
	protected String Webhislink="http://localhost:93/#/login/GAMC";
	protected String usr="seleniumuser";
	protected String pas="takecare";
	protected Faker rndname;
	protected Random rnd;
	protected LocalTime currentime;
	protected LocalTime futuretime;
	protected LocalTime pasttime;
	protected String consusrid;
	protected String conscode;
	protected String consname;
	protected String splname;
	protected String splcode;
	protected String splname1;
	protected String connect;
	protected String intervalTime2;
	protected String intervalTime;
	protected String Name;
	protected int age;
	protected String m1;
	protected String min_len;
	protected String nation_name;
	protected String nation_id;
	protected int len;
	protected String nation_code;
	protected int nat_cod;
	protected int patid;
	protected String PatID;
	protected String cuscode;
	protected String cusname;	
	protected String PM;
	protected String Pricename;
	protected int locId;
	public static String locName;
	public static String compName;
	public static String DBconnect;
	public static String link;
	protected String DBpath=System.getenv("path1");
	protected String querypath=System.getenv("path2");
	protected String loginpath=System.getenv("path3");
	
	public Sampleclass_DB_utility() {
       
    }

	
	@BeforeClass
	public void setup() throws FileNotFoundException {
		rndname=new Faker();
		rnd=new Random();
		currentime=LocalTime.now();
		futuretime=currentime.plusHours(1);
		pasttime=currentime.minusHours(1);
		
        DateTimeFormatter timformatter = DateTimeFormatter.ofPattern("h:mm a");
    	LocalTime roundedTime = currentime.truncatedTo(ChronoUnit.HOURS).plusMinutes((currentime.getMinute() / 10) * 10);
    	String formatroundtime = roundedTime.format(timformatter).substring(0, 5);
    	System.out.println("formattime"+formatroundtime);
    	String f1amOrPm = (currentime.getHour()<12)? "AM":"PM";
    	System.out.println("Rounded time: " + formatroundtime + " " + f1amOrPm);
    	String formatroundTime2 = roundedTime.plusMinutes(10).format(timformatter).substring(0, 5);
    	
    	LocalTime anothertime = roundedTime.plusMinutes(10);
    	String formatanothertime = anothertime.format(timformatter).substring(0, 5);
    	String f3amOrPmNext = (anothertime.getHour()<12)? "AM":"PM";
    	String formatanothertime2 = anothertime.plusMinutes(10).format(timformatter).substring(0, 5);

    	intervalTime = formatroundtime+" "+f1amOrPm+" - "+formatanothertime+" "+f3amOrPmNext;
    	intervalTime = intervalTime.replace("AM", " AM").replace("PM", " PM");
    	intervalTime= intervalTime.replaceAll("\\s+", " ").trim();
    	System.out.println("Time interval: " + intervalTime);

    	intervalTime2 = formatroundTime2+""+f1amOrPm+" - "+formatanothertime2 +" "+f3amOrPmNext;
    	intervalTime2 = intervalTime2.replace("AM", " AM").replace("PM", " PM");
    	intervalTime2 = intervalTime2.replaceAll("\\s+", " ").trim();
    	System.out.println("Time interval2: " + intervalTime2);
	}
	@DataProvider(name = "sqlQueries")
	public Object[][] provideSqlQueries() throws IOException {
	    Properties properties = new Properties();
	    
	    // Load Appntmnt_DB_Login.properties from classpath
	    try (InputStream input = getClass().getClassLoader().getResourceAsStream("Appntmnt_DB_Login.properties")) {
	        if (input == null) {
	            throw new FileNotFoundException("Sorry, unable to find Appntmnt_DB_Login.properties");
	        }
	        properties.load(input);
	    }
	    
	    String con = properties.getProperty("connect");
	    String usrd = properties.getProperty("userid");
	    String pass = properties.getProperty("pasword");
	    properties.clear();
	    
	    // Load Appntmnt_quer_sql.properties from classpath
	    try (InputStream input = getClass().getClassLoader().getResourceAsStream("Appntmnt_quer_sql.properties")) {
	        if (input == null) {
	            throw new FileNotFoundException("Sorry, unable to find Appntmnt_quer_sql.properties");
	        }
	        properties.load(input);
	    }
	    
	    String qy1 = properties.getProperty("q1");
	    String qy2 = properties.getProperty("q2");
	    String qy3 = properties.getProperty("q3");
	    String qy4 = properties.getProperty("q4");
	    
	    return new Object[][] {
	        {con, usrd, pass, qy1, qy2, qy3, qy4}
	    };
	}

	@DataProvider(name = "sqlQueries1")
	public Object[][] provideSqlQueries1() throws IOException {
	    Properties properties = new Properties();

	    // Load Appntmnt_Webhis_login.properties from classpath
	    try (InputStream input = getClass().getClassLoader().getResourceAsStream("Appntmnt_Webhis_login.properties")) {
	        if (input == null) {
	            throw new FileNotFoundException("Sorry, unable to find Appntmnt_Webhis_login.properties");
	        }
	        properties.load(input);
	    }
	    
	    String chrome = properties.getProperty("chromedriverpath");
	    String link = properties.getProperty("webhis_link");
	    String usr = properties.getProperty("userid");
	    String pas = properties.getProperty("password");
	    String emr_link = properties.getProperty("emr_link");
	    String emr_afi = properties.getProperty("emr_afiuser");
	    String emr_afipassword = properties.getProperty("emr_afipasswrd");
	    String emr_password = properties.getProperty("emr_passwrd");

	    return new Object[][] {
	        {chrome, link, usr, pas, emr_link, emr_afi, emr_afipassword, emr_password}
	    };
	}
	@Test(dataProvider="sqlQueries")
	public void DB_setup(String DBcon,String DBusr,String DBpas,String qu1,String qu2,String qu3,String qu4) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		System.out.println("Driver loaded");
		connect=DBcon;
		String user=DBusr;
		String password=DBpas;
		
		Connection con=DriverManager.getConnection(connect, user, password);
		if(con.isClosed()) {
					
		System.out.println("Database is not connected");
				}
		else {

		System.out.println("Database is connected successfully");
				}
		Statement st=con.createStatement();
		
		ResultSet set=st.executeQuery(qu1);
		List<String[]> resultlist=new ArrayList<>();
		while (set.next())
		{
			String[] row=new String[5];
			row[0]=set.getString(1);
			row[1]=set.getString(2);
			row[2]=set.getString(3);
			row[3]=set.getString(4);
			row[4]=set.getString(5);
			resultlist.add(row);
		}
		String[] selectvalue=resultlist.get(rnd.nextInt(resultlist.size()));
		consusrid=selectvalue[0];
		conscode=selectvalue[1];
		consname=selectvalue[2];
		splcode=selectvalue[3];
		splname=selectvalue[4];
		System.out.println("The cons usr id: "+consusrid);
		System.out.println("The cons name is: "+consname);
		System.out.println("The Spl name is: "+splname);
		System.out.println("The Spl code is: "+splcode);
		
		ResultSet set1 = st.executeQuery(qu2);
		List<String[]> resultlist1 = new ArrayList<>();

		ResultSetMetaData metaData = set1.getMetaData();
		int numColumns = metaData.getColumnCount();

		while (set1.next()) {
		    String[] row = new String[numColumns];
		    for (int i = 0; i < numColumns; i++) {
		        row[i] = set1.getString(i + 1);
		    }
		    resultlist1.add(row);
		}

		if (!resultlist1.isEmpty()) {
		    String[] selectvalue1 = resultlist1.get(rnd.nextInt(resultlist1.size())); 
		    
		    min_len = selectvalue1[0];
		    len = Integer.parseInt(min_len);
		    nation_id = selectvalue1[1];
		    nation_name = selectvalue1[2];

		    if (selectvalue1.length > 3) {
		        nation_code = selectvalue1[3];
		        nat_cod = Integer.parseInt(nation_code);
		        System.out.println("The nation code is: " + nat_cod);
		    }

		    System.out.println("The min_len is: " + len);
		    System.out.println("The Nation id is: " + nation_id);
		    System.out.println("The Nation name is: " + nation_name);
		}
		
		ResultSet set2=st.executeQuery(qu3);
		List<String[]> resultlist2=new ArrayList<>();
		while (set2.next())
		{
			String[] row=new String[4];
			row[0]=set2.getString(1);
			row[1]=set2.getString(2);
			row[2]=set2.getString(3);
			row[3]=set2.getString(4);
			resultlist2.add(row);
		}
		String[] selectvalue2=resultlist2.get(rnd.nextInt(resultlist2.size()));
		cuscode=selectvalue2[0];
		cusname=selectvalue2[1];
		PM=selectvalue2[2];
		Pricename=selectvalue2[3];
		System.out.println("The Customer code is: "+cuscode);
		System.out.println("The Customer name is: "+cusname);
		System.out.println("The PM code is: "+PM);
		System.out.println("The PM name is: "+Pricename);
		
		String locNam = null;
		String compNam = null;
		ResultSet set3=st.executeQuery(qu4);
		 if (set3.next()) {
             locId = set3.getInt(1);
            locNam = set3.getString(2);
            compNam = set3.getString(3);
             
             System.out.println("Loc_Id: " + locId);
             System.out.println("Loc_Name: " + locNam);
             System.out.println("Compname: " + compNam);
			
		}
		 locationindx=locId-1;
		 System.out.println("locationindex "+locationindx);
		
		 int startIndex = connect.indexOf("//");
	     int semicolonind = connect.indexOf(";", startIndex+1);
	     int endindex=connect.indexOf(";",semicolonind+1);

	        // Extract the desired substring
	     String DBconnec = connect.substring(startIndex + 2, endindex);
	     System.out.println("DBconnect: "+DBconnect);
	     
	     compName = compNam; 
	     locName = locNam; 
	     DBconnect = DBconnec;

	}
	
}
