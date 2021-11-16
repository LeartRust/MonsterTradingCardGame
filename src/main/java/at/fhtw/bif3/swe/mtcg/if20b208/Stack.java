package at.fhtw.bif3.swe.mtcg.if20b208;

import at.fhtw.bif3.swe.mtcg.if20b208.cards.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Stack {

/*    private Card monster = new MonsterCard();
    private Card spell = new SpellCard();*/
/*
    List<Card> stack = new ArrayList<>();

    public Stack(List<Card> stack){
        this.stack = stack;
    }
*/

    public static List<Card> fillStack() {
        List<Card> stack = new ArrayList<>();
        for(int i = 0; i<=9; i++){
            Random r = new Random();
            int low = 10;
            int high = 100;
            int damage = r.nextInt(high-low) + low;
            if(i % 2 == 0){
                MonsterCard newCard = new MonsterCard("", ElementType.values()[new Random().nextInt(ElementType.values().length)], damage, MonsterType.values()[new Random().nextInt(MonsterType.values().length)]);
                newCard.setName(newCard.getElement() +"-"+ newCard.getMonsterType()  );
                stack.add(newCard);
            }else {
                SpellCard newCard = new SpellCard("", ElementType.values()[new Random().nextInt(ElementType.values().length)], damage);
                newCard.setName(newCard.getElement()+"-Spell");
                stack.add(newCard);
            }


        }
        return stack;
    }

}

