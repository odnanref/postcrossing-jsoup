package com.far.postcrossing.jsoup;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Auth a = new Auth("netcrash@gmail.com", "goblin");
        a.goAuth();
    }
}
