/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.io;

/**
 *
 * @author sperr
 */
public class Timer {
   public static double getTime() {
       return (double)System.nanoTime() / (double)1000000000L;
   } 
}
