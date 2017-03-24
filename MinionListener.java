/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventbase;

import java.util.EventListener;

/**
 *
 * @author Cameron
 */
public interface MinionListener extends EventListener{
    public void handle(MinionEvent e);
}
