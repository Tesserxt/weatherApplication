package weatherPkg;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Date;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import javax.net.ssl.HttpsURLConnection;
/**
 * Servlet implementation class weatherServlet
 */
@WebServlet("/weatherServlet")
public class weatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public weatherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//API_Setup
		String city = request.getParameter("city");
		String apiKey = "6f18b4020229383ecf0c6f8acbd4adbf";
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city + "&appid=" + apiKey;
		
		StringBuilder responseContent =  new StringBuilder();
		try {
		    var url = URI.create(apiUrl).toURL();
		    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    
		    try (InputStream inputStream = connection.getInputStream();
		         Scanner scanner = new Scanner(inputStream)) {
		        while (scanner.hasNext()) {
		            responseContent.append(scanner.nextLine());
		        }
		    }
		} catch (IOException e) {
		    System.err.println("An error occurred: " + e.getMessage());
		}

        System.out.println(responseContent);		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);

		long dateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimeStamp).toString();
		
		double tempratureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempratureCelsius =  (int) (tempratureKelvin - 273.15);
		
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		System.out.println(weatherCondition);
		System.out.println("City " + city);
		doGet(request, response);
	}

}
