package com.flavorplus.support;

import android.util.Log;

import com.android.volley.Request;
import com.flavorplus.support.models.Message;
import com.flavorplus.support.models.Ticket;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketsRepository {

    public interface TicketsListListener{
        void onListChanged(ArrayList<Ticket> tickets);
        void onTokenChanged(String jwt);
    }

    public interface TicketListener{
        void onTicketChanged(Ticket ticket);
        void onTokenChanged(String jwt);
    }

    public void getTickets(String jwt, final TicketsListListener listener){
        String url = Server.APP_SERVER + "/tickets/getUserTickets";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getTickets", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013){
                        String newJwt = obj.getString("jwt");
                        listener.onTokenChanged(newJwt);
                        JSONArray ticketsJSON = obj.getJSONArray("result");
                        ArrayList<Ticket> tickets = new ArrayList<>();
                        for(int i = 0; i < ticketsJSON.length(); i++){
                            JSONObject ticketJSON = ticketsJSON.getJSONObject(i);
                            Ticket newTicket = new Ticket();
                            newTicket.setId(ticketJSON.getInt("id"));
                            newTicket.setTitle(ticketJSON.getString("thread_title"));
                            newTicket.setDate(ticketJSON.getString("submit_date"));
                            newTicket.setStatus((ticketJSON.getInt("current_status")));
                            tickets.add(newTicket);
                        }
                        listener.onListChanged(tickets);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createTicket(String jwt, String title, String message, final TicketsListListener listener) {
        String url = Server.APP_SERVER + "/tickets/new";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("title", title);
        params.put("message", message);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getTickets", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013){
                        String newJwt = obj.getString("jwt");
                        getTickets(newJwt, listener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getTicket(String jwt, final int id, final TicketListener listener){
        String url = Server.APP_SERVER + "/tickets/getTicketMessages";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("thread", id+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getTicket", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try {
                    if(obj.getInt("RES_CODE") == 10013){
                        String newJwt = obj.getString("jwt");
                        listener.onTokenChanged(newJwt);
                        Ticket newTicket = new Ticket();
                        JSONObject ticketJson = obj.getJSONObject("result");
                        newTicket.setId(id);
                        newTicket.setTitle(ticketJson.getJSONArray("thread").getJSONObject(0).getString("thread_title"));
                        newTicket.setStatus(ticketJson.getJSONArray("thread").getJSONObject(0).getInt("current_status"));

                        ArrayList<Message> messages = new ArrayList<>();

                        for(int i = 0; i < ticketJson.getJSONArray("messages").length(); i++){
                            JSONObject messageJson = ticketJson.getJSONArray("messages").getJSONObject(i);
                            Message message = new Message();
                            message.setUser(messageJson.getString("display_name"));
                            message.setDate(messageJson.getString("submit_date"));
                            message.setMessage(messageJson.getString("message"));
                            messages.add(message);
                        }

                        newTicket.setMessages(messages);

                        listener.onTicketChanged(newTicket);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void reply(String jwt, final int id, String message, final TicketListener listener){
        String url = Server.APP_SERVER + "/tickets/reply";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("thread", id+"");
        params.put("message", message);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getTickets", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013){
                        String newJwt = obj.getString("jwt");
                        getTicket(newJwt, id, listener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void closeTicket(String jwt, int id, int solved) {
        String url = Server.APP_SERVER + "/tickets/close";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("thread", id+"");
        params.put("wasSolved", solved+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "closeTicket", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
            }
        });
    }
}
