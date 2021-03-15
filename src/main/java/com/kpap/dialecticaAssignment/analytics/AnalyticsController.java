package com.kpap.dialecticaAssignment.analytics;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 *
 */
@RestController
@RequestMapping("api/analytics")
public class AnalyticsController {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /***
     * Request num of consultations created within a time period (based on created date and not consultation date)
     * @param fromDate yyyy-MM-dd
     * @param toDate yyyy-MM-dd
     * @param request @HttpServletRequest
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/consultationNum")
    public ResponseEntity<Object> consultationAnalytics(@RequestParam(name = "from",required = true) @DateTimeFormat(pattern="yyyy-MM-dd")  String fromDate, @RequestParam(name = "to",required = true) @DateTimeFormat(pattern="yyyy-MM-dd") String toDate, HttpServletRequest request) {
        try {
            HttpRequest restRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/consultation/consultationNum?from="+fromDate+"&to="+toDate))
                    .header("Content-Type", "application/json")
                    .header(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(restRequest, HttpResponse.BodyHandlers.ofString());
            nastyExceptionHandler(response);
            return new ResponseEntity (response.body(),HttpStatus.OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /***
     * Retrieve unique number of consultants that set up a consultation
     * @param request @HttpServletRequest
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/uniqueConsultants")
    public ResponseEntity<Object> uniqueConsultants(HttpServletRequest request) {
        try {
            HttpRequest restRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/consultation/uniqueConsultants"))
                    .header("Content-Type", "application/json")
                    .header(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(restRequest, HttpResponse.BodyHandlers.ofString());
            nastyExceptionHandler(response);
            return new ResponseEntity (response.body(),HttpStatus.OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



    /***
     * The average payable_amount in total OR per consultant (consultations that are set as bad_call should not be included)
     *  @param consultantId optional
     * @param request
     * @return
     */
    @GetMapping("/amountsAvg")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<Object> amountsAvg(@RequestParam(name = "consultantId",required = false) Long consultantId, HttpServletRequest request) {
        try {
            String extraUri="";
            if (consultantId!=null ) extraUri = "?consultantId="+consultantId;
            String uri = "http://localhost:8080/api/consultation/amountsAvg" + extraUri;
            HttpRequest restRequest = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Content-Type", "application/json")
                    .header(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(restRequest, HttpResponse.BodyHandlers.ofString());
            nastyExceptionHandler(response);
            return new ResponseEntity (response.body(),HttpStatus.OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /***
     *
     * @param consultantId
     * @param request
     * @return
     */
    @GetMapping("/amountsAvgPerUser")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<Object> amountsAvgPerUser(@RequestParam(name = "consultantId",required = false) Long consultantId, HttpServletRequest request) {
    	try {
            String extraUri="";
            if (consultantId!=null ) extraUri = "?consultantId="+consultantId;
            String uri = "http://localhost:8080/api/consultation/amountsAvgPerUser" + extraUri;
            HttpRequest restRequest = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Content-Type", "application/json")
                    .header(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(restRequest, HttpResponse.BodyHandlers.ofString());
            nastyExceptionHandler(response);
            return new ResponseEntity (response.body(),HttpStatus.OK);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_GATEWAY);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity (e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    
    private void nastyExceptionHandler(HttpResponse<String> response) throws Exception {
        try {
            //Nasty way to identify if response is number or a message
            Double.parseDouble(response.body());
        } catch (NumberFormatException e) {
            throw new Exception(e);
        }
    }
}
