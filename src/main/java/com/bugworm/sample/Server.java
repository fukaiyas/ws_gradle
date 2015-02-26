package com.bugworm.sample;

import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;

import java.io.File;

public class Server {
    public static void main(String... args)throws Exception{
        System.out.println("---------------Start------------------------");
        int port = 8080;
        GlassFishProperties prop = new GlassFishProperties();
        prop.setPort("http-listener", port);

        System.out.println("---------------GF Create------------------------");
        GlassFish gf = GlassFishRuntime.bootstrap().newGlassFish(prop);
        gf.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("--------------------------Shutdown-------------------");
            try{
                gf.stop();
                gf.dispose();
            }catch(Exception e){
                e.printStackTrace();
            }
        }));

        System.out.println("---------------Deploy------------------------");
        gf.getDeployer().deploy(new File("build/libs/websocket_sample.war"));
    }
}
