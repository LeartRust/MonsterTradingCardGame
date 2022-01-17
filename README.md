Created by Leart Rustemi
-

This is a Card fighting simulator!
by starting the main you can start the server which then uses Threads to handle requests and send responses simultaneously.
For each request the ResponseHandler sorts out which action to perform.

Tables: Users, Cards, packages, trade_deals, current_battles, battle_log
* Users stores username, password, stats and coins
* cards stores name, element, type, damage
* package stores an id and a card_id (foreign key of a card)
* trade deals stores the id of a card that a user wants to trade and the requirements for a trade
* current_battle is a temporary store which stores 2 players that send a /battle request and after the battle gets deleted

Server uses port: 10001


Unique feature: winner of a battle gets losers best card and loser pays 5 coins to winner