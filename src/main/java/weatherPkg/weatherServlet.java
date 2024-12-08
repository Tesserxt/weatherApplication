package weatherPkg;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
		//String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" +city+ "&appid=" +apiKey;
		  
		HttpResponse<String> responseContent = null;
		try {
			URI uri = new URI(
	                "https",
	                "api.openweathermap.org",
	                "/data/2.5/weather",
	                "q=" + city + "&appid=" + apiKey,
	                null
	            );
			System.out.println("URI0: 	" + uri );	
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest reqst = HttpRequest.newBuilder()
										 .uri(uri)
										 .GET()
										 .build();
			responseContent = client.send(reqst, HttpResponse.BodyHandlers.ofString());
				
					
		} catch (Exception e) {
			e.printStackTrace();
		}


		
        
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.body(), JsonObject.class);

		long dateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimeStamp).toString();
		
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius =  (int) (temperatureKelvin - 273.15);
		
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature",temperatureCelsius);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("weatherData", responseContent);
		
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
		System.out.println(weatherCondition);
		System.out.println("City " + city);
		doGet(request, response);
	}

}
