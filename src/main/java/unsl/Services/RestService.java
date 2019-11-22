package unsl.Services;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import unsl.Entities.Account;

@Service
public class RestService {
    public Account getAccount(String url) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        Account account;

        try {
            account = restTemplate.getForObject(url, Account.class); // getForObject return an object of type Account
        }catch (Exception e){
            throw new Exception(buildMessageError(e));
        }
        return account;

    }


    public void updateAccount (String url, Account updatedAccount) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpEntity<Account> requestEntity = new HttpEntity<>(updatedAccount);
            restTemplate.put(url,requestEntity);
        }catch (Exception e){
            throw new Exception(buildMessageError(e));
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
