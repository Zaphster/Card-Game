/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventbase;

import java.util.HashMap;

/**
 *
 * @author Cameron
 */
public class Minion {
    private static Integer listenerKey = 0;
    public static MinionListener defaultListener = (MinionEvent event) -> {
        System.out.println(event.minion+ " - " + event.status);
    };
    
    public String name;
    public MinionStatus status;
    private HashMap<Integer, MinionListener> minionSummonedListeners;
    private HashMap<Integer, MinionListener> minionMovingListeners;
    private HashMap<Integer, MinionListener> minionAttackingListeners;
    private HashMap<Integer, MinionListener> minionDealingDamageListeners;
    private HashMap<Integer, MinionListener> minionReceivingDamageListeners;
    private HashMap<Integer, MinionListener> minionDiedListeners;
    private HashMap<Integer, MinionListener> minionHealingListeners;
    private HashMap<Integer, MinionListener> minionDefendingListeners;
    private HashMap<Integer, MinionListener> minionBeingSacrificedListeners;
    private HashMap<Integer, MinionListener> minionBeingImprintedListeners;
    private HashMap<MinionStatus, HashMap> statusConverter;
    private Boolean canTakeDamage;
    public int ogMaxHealth;
    public int maxHealth;
    public int currentHealth;
    public int ogMaxAttackRange;
    public int maxAttackRange;
    public int currentAttackRange;
    public int ogMaxDefendRange;
    public int maxDefendRange;
    public int currentDefendRange;
    public int ogMaxAttackDamage;
    public int maxAttackDamage;
    public int currentAttackDamage;
    public int ogMaxDefendDamage;
    public int maxDefendDamage;
    public int currentDefendDamage;
    public Boolean canDefend;
    public Boolean canAttack;
    
    public Minion(){
        statusConverter = new HashMap();
        minionSummonedListeners = new HashMap();
        minionMovingListeners = new HashMap();
        minionAttackingListeners = new HashMap();
        minionDealingDamageListeners = new HashMap();
        minionReceivingDamageListeners = new HashMap();
        minionDiedListeners = new HashMap();
        minionHealingListeners = new HashMap();
        minionDefendingListeners = new HashMap();
        minionBeingSacrificedListeners = new HashMap();
        minionBeingImprintedListeners = new HashMap();
        this.statusConverter.put(MinionStatus.ATTACKING, this.minionAttackingListeners);
        this.statusConverter.put(MinionStatus.BEINGIMPRINTED, this.minionBeingImprintedListeners);
        this.statusConverter.put(MinionStatus.BEINGSACRIFICED, this.minionBeingSacrificedListeners);
        this.statusConverter.put(MinionStatus.DEAD, this.minionDiedListeners);
        this.statusConverter.put(MinionStatus.DEALINGDAMAGE, this.minionDealingDamageListeners);
        this.statusConverter.put(MinionStatus.DEFENDING, this.minionDefendingListeners);
        this.statusConverter.put(MinionStatus.HEALING, this.minionHealingListeners);
        this.statusConverter.put(MinionStatus.MOVING, this.minionMovingListeners);
        this.statusConverter.put(MinionStatus.RECEIVINGDAMAGE, this.minionReceivingDamageListeners);
        this.statusConverter.put(MinionStatus.SUMMONED, this.minionSummonedListeners);
    }
    
    public Minion(String name){
        this();
        this.name = name;
        this.setOnAttack(defaultListener);
        this.setOnDealDamage(defaultListener);
        this.setOnDeath(defaultListener);
        this.setOnDefend(defaultListener);
        this.setOnHealed(defaultListener);
        this.setOnMove(defaultListener);
        this.setOnReceiveDamage(defaultListener);
        this.setOnSacrifice(defaultListener);
        this.setOnSummon(defaultListener);
        this.setOnUsedAsImprint(defaultListener);
        this.canAttack = true;
        this.canDefend = true;
        this.canTakeDamage = true;
        this.currentAttackRange = 1;
        this.currentDefendRange = 1;
    }
    
    @Override
    public String toString(){
        return name + ": " + currentAttackDamage + "/" + currentHealth;
    }
    
    public void setAttackDamage(int attack){
        setAttackDamage(attack, attack);
    }
    
    public void setAttackDamage(int attack, int defend){
        currentAttackDamage = attack;
        currentDefendDamage = defend;
    }
    
    public void setHealth(int health){
        currentHealth = health;
    }
    
    public int getAttackDamage(){
        return currentAttackDamage;
    }
    
    public int getDefenseDamage(){
        return currentDefendDamage;
    }
    
    public synchronized void attack(Minion defender){
        status = MinionStatus.ATTACKING;
        fireMinionEvent(this, status);
    }
    
    public synchronized void defend(Minion attacker){
        status = MinionStatus.DEFENDING;
        fireMinionEvent(this, status);
    }
    
    public static int checkDistance(Minion minion1, Minion minion2){
        int min = 1;
        int distance = min;
        return distance;
    }
    
    public static Integer nextListenerKey(){
        if(listenerKey == Integer.MAX_VALUE){
            listenerKey = 0;
        }
        Integer nextKey = listenerKey++;
        return nextKey;
    }
    
    public static void handleCombat(Minion attacker, Minion defender){
        int distance = checkDistance(attacker, defender);
        Boolean attackerRangeValid = distance <= attacker.currentAttackRange;
        Boolean defenderRangeValid = distance <= defender.currentDefendRange;
        if(attackerRangeValid){
            attacker.attack(defender);
            defender.defend(attacker);
            defender.takeDamage(attacker.getAttackDamage());
            if(defenderRangeValid){
                attacker.takeDamage(defender.getDefenseDamage());
            }
            defender.checkIfDead();
            attacker.checkIfDead();
        }
    }
    
    public synchronized void takeDamage(int damage){
        if(canTakeDamage){
            status = MinionStatus.RECEIVINGDAMAGE;
            fireMinionEvent(this, status);
            currentHealth -= damage;
        }
    }
    
    public void checkIfDead(){
        if(currentHealth <= 0){
            kill();
        }
    }
    
    public synchronized void kill(){
        status = MinionStatus.DEAD;
        fireMinionEvent(this, status);
    }
    
    private synchronized Integer setListener(HashMap map, MinionListener listener){
        Integer newKey = Minion.nextListenerKey();
//        System.out.println("Setting listener: " + listener + " on minion: " + this + " in hashmap: " + map);
        map.put(newKey, listener);
        return newKey;
    }
    
    public synchronized Integer setOnSummon(MinionListener listener){
        return setListener(minionSummonedListeners, listener);
    }
    
    public synchronized Integer setOnMove(MinionListener listener){
        return setListener(minionMovingListeners, listener);
    }
    
    public synchronized Integer setOnAttack(MinionListener listener){
        return setListener(minionAttackingListeners, listener);
    }
    
    public synchronized Integer setOnDealDamage(MinionListener listener){
        return setListener(minionDealingDamageListeners, listener);
    }
    
    public synchronized Integer setOnReceiveDamage(MinionListener listener){
        return setListener(minionReceivingDamageListeners, listener);
    }
    
    public synchronized Integer setOnDeath(MinionListener listener){
        return setListener(minionDiedListeners, listener);
    }
    
    public synchronized Integer setOnHealed(MinionListener listener){
        return setListener(minionHealingListeners, listener);
    }
    
    public synchronized Integer setOnDefend(MinionListener listener){
        return setListener(minionDefendingListeners, listener);
    }
    
    public synchronized Integer setOnSacrifice(MinionListener listener){
        return setListener(minionBeingSacrificedListeners, listener);
    }
    
    public synchronized Integer setOnUsedAsImprint(MinionListener listener){
        return setListener(minionBeingImprintedListeners, listener);
    }
    
    private synchronized void fireMinionEvent(Minion minion, MinionStatus status){
        MinionEvent event = new MinionEvent(minion, status);
        HashMap<Integer, MinionListener> listeners = statusConverter.get(status);
        listeners.forEach((Integer id, MinionListener listener) -> {
            listener.handle(event);
        });
        resetStatus();
    }
    
    private void resetStatus(){
        status = null;
    }
}
