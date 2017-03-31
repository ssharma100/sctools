package com.oracle.ofsc;
import org.apache.camel.spring.Main;

/**
 * Main launch class.  This binds to the Spring Main that used the camel-context.xml for
 * class scan for RouteBuilders.
 *
 */
public class OfscTool {

    public static void main(String[] args) throws Exception {
        Main.main(args);
    }
}
