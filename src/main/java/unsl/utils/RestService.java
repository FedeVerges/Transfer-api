package unsl.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import unsl.entities.Amount;
import unsl.entities.Balance;
import unsl.entities.User;

@Service
public class RestService {

    /**
     * @param url
     * @return
     * @throws Exception
     */
    public User getUser(String url) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        User user;

        try {
            user = restTemplate.getForObject(url, User.class);
        }  catch (Exception e){
            throw new Exception( buildMessageError(e));
        }

        return user;
    }

    /**
     * @param url
     * @return
     * @throws Exception
     */
    public Balance getBalance(String url) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Balance balance;

        try {
            balance = restTemplate.getForObject(url, Balance.class);
        }  catch (Exception e){
            throw new Exception( buildMessageError(e));
        }

        return balance;
    }

    /**
     * @param url
     * @throws Exception
     */
    public void putBalance(String url, Amount amount) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.put(url, amount);
        }  catch (Exception e){
            throw new Exception( buildMessageError(e));
        }

    }

    private String buildMessageError(Exception e) {
        String msg = e.getMessage();
        if (e instanceof HttpClientErrorException) {
            msg = ((HttpClientErrorException) e).getResponseBodyAsString();
        } else if (e instanceof HttpServerErrorException) {
            msg =  ((HttpServerErrorException) e).getResponseBodyAsString();
        }
        return msg;
    }

}

