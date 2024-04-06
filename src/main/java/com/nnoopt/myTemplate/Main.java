package com.nnoopt.myTemplate;

import com.nnoopt.myTemplate.models.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Main {
    static RestTemplate restTemplate = new RestTemplate();
    static String url = "http://94.198.50.185:7081/api/users";
    static HttpHeaders headers;

    public static void main(String[] args) {

        // делаем запрос
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        System.out.println(responseEntity.getBody());


        // получаем куки
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        List<String> cookies = responseHeaders.get(HttpHeaders.SET_COOKIE);

        // получаем sessionId
        String sessionId = cookies.stream()
                .filter(cookie -> cookie.startsWith("JSESSIONID"))
                .map(cookie -> cookie.split(";")[0])
                .findFirst()
                .orElse(null);

        // создае хедер со своим sessionId
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionId);


        // добавляем пользователя
        User user = new User(3L, "James", "Brown", (byte) 26);
        HttpEntity<User> httpEntity1 = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange(url, HttpMethod.POST, httpEntity1, String.class);
        String str1 = responseEntity2.getBody();


        // изменяем
        User newUser = new User(3L, "James", "Shelby", (byte) 26);
        ResponseEntity<String> responseEntity3 = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(newUser, headers), String.class);
        String str2 = responseEntity3.getBody();


        // удаление
        HttpEntity<String> httpEntity2 = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity4 = restTemplate.exchange(url + "/" + 3, HttpMethod.DELETE, httpEntity2, String.class);
        String str3 = responseEntity4.getBody();


        String str = str1+str2+str3;
        System.out.println(str);
        System.out.println(str.length());
    }
}
