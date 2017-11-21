
package com.brooklyn.thejuggler;

import com.brooklyn.implementations.JugglerSetUpImpl;
import com.brooklyn.interfaces.JugglerSetUp;

public class JugglerMain {

    public static void main(String[] args) {
        
        System.out.println("Welcome to the Show.. THE JUGGLER!!");
        
        JugglerSetUp jugglerSetUp = new JugglerSetUpImpl();
        
        jugglerSetUp.startJuggling();
        
        
    }

}
