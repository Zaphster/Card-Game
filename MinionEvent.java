/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventbase;

import java.util.EventObject;

/**
 *
 * @author Cameron
 */

public class MinionEvent extends EventObject{
    public Minion minion;
    public MinionStatus status;
    public int damageToBeDealt;
    public int damageToBeTaken;
    public int healthToBeHealed;
    public int spaceMovingFrom;
    public int spaceMovingTo;
    public int cardImprintedOn;
    public int spaceSummonedTo;
    public int playerControlling;
    
    public MinionEvent(Object source, MinionStatus status) {
        super(source);
        this.minion = (Minion)source;
        this.status = status;
    }
    
}
