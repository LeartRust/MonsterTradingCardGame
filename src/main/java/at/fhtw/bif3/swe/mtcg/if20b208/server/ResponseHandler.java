package at.fhtw.bif3.swe.mtcg.if20b208.server;

import at.fhtw.bif3.swe.mtcg.if20b208.database.MTCGDaoDb;
import at.fhtw.bif3.swe.mtcg.if20b208.database.model.*;
import at.fhtw.bif3.swe.mtcg.if20b208.user.User;


import java.util.*;

public class ResponseHandler {

    private static String responseText="{}";
    private static String code = "200";

    public static Response handleRequest(Request request) {
        System.out.println("TEST");
        System.out.println("BODY: " + request.getBody());

        if(request.getHttpRequest() == HttpRequest.POST){
            MTCGDaoDb daoDb = new MTCGDaoDb();
            if (request.getPathname().equals("/users") ) {
                HashMap jsonMap = Helper.parseJson(request.getBody());
                daoDb.saveUser(new UserData(jsonMap.get("Username").toString(),jsonMap.get("Password").toString(),20, false,100,0,0,0));
            }else if(request.getPathname().equals("/sessions")){
                HashMap jsonMap = Helper.parseJson(request.getBody());
                Boolean asd = daoDb.logInOutUser(true, jsonMap.get("Username").toString(), jsonMap.get("Password").toString());
                System.out.println("TRUE OR FALSE: " + asd);
            }else if(request.getPathname().equals("/packages") && Helper.checkToken("admin", request.getToken())){
                if(daoDb.isLogedIn(Helper.nameFromToken(request.getToken()))) {
                    System.out.println("IS LOGED IN ");
                    //get cards from body and parse it into a list
                    List<PackageData> jsonMap = Helper.jsonToListForPackage(request.getBody());
                    //for each card in package create a card and a package record that references to that card
                    jsonMap.forEach(x -> daoDb.saveCard(Helper.getCardsFromPackage(x)));
                    jsonMap.forEach(x -> daoDb.addPackage(x.getId()));
                }

            }else if(request.getPathname().equals("/transactions/packages")){
                String username = Helper.nameFromToken(request.getToken());
                if(daoDb.isLogedIn(username)){
                    if (daoDb.getUserCoins(username) >= 5){
                        daoDb.acquirePackage(Helper.nameFromToken(request.getToken()));
                    }else {
                        responseText = "not enough coins";
                    }
                } else {
                    responseText = "not logged in";
                }
            }else if(request.getPathname().equals("/tradings")){
                String username = Helper.nameFromToken(request.getToken());
                if(request.getToken() != null){
                    if(daoDb.isLogedIn(username)){
                        HashMap jsonMap = Helper.parseJson(request.getBody());
                        if(daoDb.checkIfCardBelongsToUser(username, jsonMap.get("CardToTrade").toString())){
                            if(daoDb.createTradingDeal(username , jsonMap.get("Id").toString(), jsonMap.get("CardToTrade").toString(), jsonMap.get("Type").toString(), jsonMap.get("MinimumDamage").toString())){
                                responseText = "Trade Deal created";
                                code = "200";
                            }else {
                                responseText = "Trade Deal not created";
                                code = "400";
                            }
                        }else{
                            responseText = "Card doesnt belong to this user";
                            code = "400";
                        }

                    }else{
                        responseText = "Not logged in";
                        code = "400";
                    }
                }else {
                    responseText = "No token";
                    code = "400";
                }

            }else if (request.getPathname().contains("/tradings/")){
                String[] trade_id = request.getPathname().split("/");
                if(request.getToken() != null) {
                    String username = Helper.nameFromToken(request.getToken());
                    if(daoDb.isLogedIn(username)){
                        String card_id= request.getBody().replaceAll("^.|.$", "");
                        if (daoDb.checkIfCardBelongsToUser(username, card_id)) {
                            System.out.println("SPLITTED: " + card_id);
                            if(daoDb.tradeCards(username, trade_id[2], card_id)){
                                responseText = "Deal was successful";
                                code = "200";;
                            }else {
                                responseText = "Deal not found";
                                code = "404";;
                            }

                        }else {
                            responseText = "Wrong user or token";
                        }

                    }else {
                        responseText = "not logged in";
                    }
                }
            }else if(request.getPathname().equals("/battles")){
                if(request.getToken() != null) {
                    String username = Helper.nameFromToken(request.getToken());
                    if (daoDb.isLogedIn(username)) {
                        if(daoDb.getUserDeck(username).size() == 4){
                            String log = daoDb.battle(username);
                            responseText = log;
                            code = "200";;
                        }else {
                            responseText = "not enough cards in deck";
                            code = "400";;
                        }
                    }else {
                        responseText = "not logged in";
                        code = "400";;
                    }
                }else {
                    System.out.println("no token");
                    responseText = "No token";
                    code = "400";;
                }
            }

        }else if(request.getHttpRequest() == HttpRequest.GET){
            MTCGDaoDb daoDb = new MTCGDaoDb();
            if(request.getPathname().equals("/cards")){
                if(request.getToken() != null){
                    String username = Helper.nameFromToken(request.getToken());
                    if (daoDb.isLogedIn(username)){
                        responseText = Helper.writeListToJsonArray(daoDb.getUserCards(username));
                    }else {
                        responseText ="not logged in";
                    }
                }else {
                    System.out.println("no token");
                    responseText = "No token";
                    code = "400";;
                }
            }else if(request.getPathname().equals("/deck")){
                if(request.getToken() != null){
                    String username = Helper.nameFromToken(request.getToken());
                    if (daoDb.isLogedIn(username)){
                        responseText = Helper.writeListToJsonArray(daoDb.getUserDeck(username));
                    }else {
                        System.out.println("not logged in");
                    }
                }else {
                    System.out.println("no token");
                    responseText = "No token";
                    code = "200";;
                }
            }else if(request.getPathname().contains("/users/")){
                String[] name = request.getPathname().split("/");
                if(request.getToken() != null) {
                    String username = Helper.nameFromToken(request.getToken());
                    System.out.println("NAME::" + name[2] + "USERNAME::" + username);
                    if(daoDb.checkUserExistence(name[2]) && name[2].equals(username)){
                        if (daoDb.isLogedIn(username)) {
                            String[] profile = daoDb.getUserProfile(username);
                            responseText = "Name: " + profile[0] + " Bio: " + profile[1] + " Image: " + profile[2];
                            code = "200";;
                        }else {
                            responseText = "not logged in";
                        }
                    }else {
                        responseText = "Wrong user or token";
                    }
                }

            }else if(request.getPathname().equals("/stats")){
                if(request.getToken() != null){
                    String username = Helper.nameFromToken(request.getToken());
                    if (daoDb.isLogedIn(username)){
                        User user = daoDb.getUser(username);
                        responseText = user.getUsername() +" Coins: " + user.getCoins()+ " Elo points: " + user.getEloPoints() + " Games played: "+ user.getGamesPlayed() + " Wins: "+ user.getWins() + " losses: " + user.getLosses();                    }else {
                        System.out.println("not logged in");
                    }
                }else {
                    responseText = "No token";
                    code = "404";;
                }
            }else if (request.getPathname().equals("/score")){
                if(request.getToken() != null){
                    String username = Helper.nameFromToken(request.getToken());
                    if (daoDb.isLogedIn(username)){
                        List<UserScores> scores = daoDb.getScoreboard(username);
                        responseText = Helper.writeScoreListToJsonArray(scores);
                    }
                }else {
                    responseText = "No token";
                    code = "404";;
                }
            }else if(request.getPathname().equals("/tradings")){
                String username = Helper.nameFromToken(request.getToken());
                if(request.getToken() != null){
                    if(daoDb.isLogedIn(username)){
                        //TODO better json with no nulls
                        List<Trademarket> deals = daoDb.getTradingDeals();
                        responseText = Helper.writeTradeDealsListToJsonArray(deals);
                    }else{
                        responseText = "Not logged in";
                        code = "400";;
                    }
                }else {
                    responseText = "No token";
                    code = "400";;
                }

            }

        }else if(request.getHttpRequest() == HttpRequest.PUT){
            MTCGDaoDb daoDb = new MTCGDaoDb();
            if(request.getPathname().equals("/deck")){
                if(request.getToken() != null) {
                    if (daoDb.isLogedIn(Helper.nameFromToken(request.getToken()))) {
                        String[] cardIds = Helper.getCardIdsForDeck(request.getBody());
                        if (cardIds.length == 4) {
                            if (daoDb.createUserDeck(cardIds, Helper.nameFromToken(request.getToken()))) {
                                responseText = "Deck set";
                                code = "200";;
                            } else {
                                responseText = "Deck not set";
                                code = "400";;
                            }
                        } else {
                            responseText = "please pick exactly 4 cards";
                            code = "400";;
                        }
                    }
                }else{
                    responseText = "no token";
                    code = "400";;
                }
            }else if(request.getPathname().contains("/users/")){
                String[] name = request.getPathname().split("/");
                if(request.getToken() != null) {
                    String username = Helper.nameFromToken(request.getToken());
                    if(daoDb.checkUserExistence(name[2]) && name[2].equals(username)){

                        if (daoDb.isLogedIn(username)) {
                            HashMap jsonMap = Helper.parseJson(request.getBody());
                            if(daoDb.updateUserProfile(username , jsonMap.get("Name").toString(), jsonMap.get("Bio").toString(), jsonMap.get("Image").toString())){
                                responseText = "Profile updated";
                                code = "200";;
                            }else {
                                responseText = "Profile not found";
                            }

                        }else {
                            responseText = "not logged in";
                        }

                    }else {
                        responseText = "Wrong user or token";
                    }
                }

            }
        }else if(request.getHttpRequest() == HttpRequest.DELETE){
            MTCGDaoDb daoDb = new MTCGDaoDb();
            if (request.getPathname().contains("/tradings/")){
                String[] card_id = request.getPathname().split("/");
                if(request.getToken() != null) {
                    String username = Helper.nameFromToken(request.getToken());
                    if(daoDb.checkIfCardBelongsToUser(username,card_id[2])){
                        if (daoDb.isLogedIn(username)) {
                            if(daoDb.deleteTradingDeal(card_id[2])){
                                responseText = "Deal deleted";
                                code = "200";;
                            }else {
                                responseText = "Deal not found";
                                code = "404";;
                            }

                        }else {
                            responseText = "not logged in";
                        }

                    }else {
                        responseText = "Wrong user or token";
                    }
                }
            }
        }



        return new Response(
                code,
                responseText
                //"{ message: \"Success\" }"
        );
    }

}
